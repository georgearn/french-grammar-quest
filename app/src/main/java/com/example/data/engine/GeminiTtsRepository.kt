package com.example.data.engine

import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import java.util.concurrent.atomic.AtomicInteger
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.TimeUnit

/**
 * Calls the Gemini API's dedicated text-to-speech models (generateContent with
 * responseModalities = ["AUDIO"]) to synthesize a two-speaker podcast script into real spoken
 * audio, instead of re-reading the script with the device's built-in TextToSpeech engine — which
 * is what made generated "podcasts" sound flat and robotic, since that engine has no idea two
 * different speakers exist and just reads the whole script, speaker labels and all, in one voice.
 *
 * Same API key as GeminiGenerationRepository — kept in local SharedPreferences only, never
 * bundled with the app or sent anywhere but Google's own generativelanguage.googleapis.com.
 *
 * NOTE on model naming: exactly like the main generation model, Gemini's TTS model names are
 * versioned and Google periodically retires older ones for new API keys (see
 * GeminiGenerationRepository's gemini-2.5-flash -> gemini-3.6-flash history — this is the same
 * kind of rotation, just for the *-tts family instead of the plain generation family). If this
 * ever 404s with a "model no longer available" error, swap MODEL below for whatever exact model
 * name the error message specifies, the same way the main generator's model string was fixed.
 */
class GeminiTtsRepository {

  private val client = OkHttpClient.Builder()
    .connectTimeout(20, TimeUnit.SECONDS)
    // Each request now carries one chunk (~180 words, about a minute of speech), not the whole
    // 7-8 minute script, so it fits comfortably in these limits.
    .readTimeout(150, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS)
    .callTimeout(180, TimeUnit.SECONDS)
    .build()

  private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

  companion object {
    private const val MODEL = "gemini-3.1-flash-tts-preview"
    private const val DEFAULT_SAMPLE_RATE_HZ = 24000
    private const val CHANNELS = 1
    private const val BITS_PER_SAMPLE = 16
    private const val PARALLEL_REQUESTS = 2
    private const val GAP_BETWEEN_CHUNKS_MS = 300
    private val VOICES = listOf("Kore", "Puck")
  }

  /** A synthesized podcast: the WAV file content and when each turn and word is spoken. */
  class PodcastAudio(val wav: ByteArray, val timeline: PodcastTimeline)

  /**
   * Synthesizes a two-speaker script (lines like "Camille : ..." / "Nadia : ...") into a WAV
   * with a word timeline. The script is split into chunks of a few turns, synthesized two at a
   * time, each retried on its own if Gemini is overloaded, then joined back together.
   * [onProgress] reports finished chunks; failures are [GeminiException]s.
   */
  suspend fun synthesizePodcast(
    script: String,
    apiKey: String,
    onProgress: (done: Int, total: Int) -> Unit = { _, _ -> },
    onRetry: (attempt: Int, maxAttempts: Int) -> Unit = { _, _ -> }
  ): Result<PodcastAudio> = withContext(Dispatchers.IO) {
    runCatching {
      if (apiKey.isBlank()) throw GeminiException(GeminiErrorKind.INVALID_KEY)
      val turns = DialogueScript.parse(script)
      if (turns.isEmpty()) throw GeminiException(GeminiErrorKind.EMPTY, detail = "No dialogue lines found")
      val speakers = turns.map { it.speaker }.distinct().take(2)
      val chunks = DialogueScript.chunk(turns)
      val done = AtomicInteger(0)
      onProgress(0, chunks.size)

      val gate = Semaphore(PARALLEL_REQUESTS)
      val audio = coroutineScope {
        chunks.map { chunk ->
          async {
            gate.withPermit {
              GeminiHttp.withRetry(maxAttempts = 4, onRetry = onRetry) {
                requestChunk(chunk, speakers, apiKey)
              }.also { onProgress(done.incrementAndGet(), chunks.size) }
            }
          }
        }.awaitAll()
      }

      val sampleRate = audio.first().second
      val gap = ByteArray(sampleRate * GAP_BETWEEN_CHUNKS_MS / 1000 * 2)
      val pcm = java.io.ByteArrayOutputStream()
      val timed = mutableListOf<TimedTurn>()
      audio.forEachIndexed { i, (chunkPcm, rate) ->
        val offsetMs = pcm.size().toLong() * 1000 / (rate * 2)
        timed += SpeechAlignment.align(chunkPcm, rate, chunks[i], offsetMs)
        pcm.write(chunkPcm)
        if (i < audio.lastIndex) pcm.write(gap)
      }
      PodcastAudio(pcmToWav(pcm.toByteArray(), sampleRate), PodcastTimeline(timed))
    }.recoverCatching { throw if (it is kotlinx.coroutines.CancellationException) it else GeminiHttp.classify(it) }
  }

  /** One TTS request for a few turns; returns raw PCM and its sample rate. */
  private suspend fun requestChunk(chunk: List<DialogueTurn>, speakers: List<String>, apiKey: String): Pair<ByteArray, Int> {
    val url = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL:generateContent?key=$apiKey"
    val dialogue = chunk.joinToString("\n") { "${it.speaker}: ${it.text}" }
    val promptText =
      "Lis ce dialogue à voix haute de façon naturelle et expressive, avec deux voix bien " +
        "distinctes pour les deux locuteurs :\n\n$dialogue"

    val voiceConfigs = JSONArray()
    // Multi-speaker TTS needs exactly two voices, even if this chunk only has one speaker.
    val names = (speakers + listOf("Camille", "Nadia")).distinct().take(2)
    names.forEachIndexed { i, name -> voiceConfigs.put(speakerVoiceConfig(name, VOICES[i])) }

    val body = JSONObject().apply {
      put(
        "contents",
        JSONArray().put(JSONObject().put("parts", JSONArray().put(JSONObject().put("text", promptText))))
      )
      put(
        "generationConfig",
        JSONObject().apply {
          put("responseModalities", JSONArray().put("AUDIO"))
          // Audio output tokens are far more expensive than text tokens (~dozens per second of
          // speech). Without a high enough ceiling the audio gets cut short mid-chunk.
          put("maxOutputTokens", 16000)
          put(
            "speechConfig",
            JSONObject().put("multiSpeakerVoiceConfig", JSONObject().put("speakerVoiceConfigs", voiceConfigs))
          )
        }
      )
    }

    val call = client.newCall(
      Request.Builder().url(url).post(body.toString().toRequestBody(jsonMediaType)).build()
    )
    val handle = currentCoroutineContext()[Job]?.invokeOnCompletion { call.cancel() }
    try {
      val responseText = call.execute().use { response ->
        if (!response.isSuccessful) throw GeminiHttp.errorFrom(response)
        response.body?.string().orEmpty()
      }
      return extractAudio(responseText) ?: throw GeminiException(GeminiErrorKind.EMPTY)
    } finally {
      handle?.dispose()
    }
  }

  private fun speakerVoiceConfig(speaker: String, voiceName: String): JSONObject =
    JSONObject().apply {
      put("speaker", speaker)
      put(
        "voiceConfig",
        JSONObject().put(
          "prebuiltVoiceConfig",
          JSONObject().put("voiceName", voiceName)
        )
      )
    }

  /** Returns raw PCM bytes plus the sample rate parsed from the response's mimeType, if present. */
  private fun extractAudio(raw: String): Pair<ByteArray, Int>? {
    val json = JSONObject(raw)
    val candidates = json.optJSONArray("candidates") ?: return null
    if (candidates.length() == 0) return null
    val content = candidates.getJSONObject(0).optJSONObject("content") ?: return null
    val parts = content.optJSONArray("parts") ?: return null
    for (i in 0 until parts.length()) {
      val inlineData = parts.getJSONObject(i).optJSONObject("inlineData") ?: continue
      val base64Data = inlineData.optString("data").takeIf { it.isNotBlank() } ?: continue
      val mimeType = inlineData.optString("mimeType", "")
      val rateMatch = Regex("rate=(\\d+)").find(mimeType)
      val sampleRate = rateMatch?.groupValues?.get(1)?.toIntOrNull() ?: DEFAULT_SAMPLE_RATE_HZ
      val pcm = Base64.decode(base64Data, Base64.DEFAULT)
      return pcm to sampleRate
    }
    return null
  }

  /** Wraps raw 16-bit PCM in a minimal 44-byte WAV header so MediaPlayer can play it directly. */
  private fun pcmToWav(pcm: ByteArray, sampleRate: Int): ByteArray {
    val byteRate = sampleRate * CHANNELS * BITS_PER_SAMPLE / 8
    val blockAlign = CHANNELS * BITS_PER_SAMPLE / 8
    val dataSize = pcm.size
    val header = ByteBuffer.allocate(44).order(ByteOrder.LITTLE_ENDIAN).apply {
      put("RIFF".toByteArray())
      putInt(36 + dataSize)
      put("WAVE".toByteArray())
      put("fmt ".toByteArray())
      putInt(16) // PCM fmt chunk size
      putShort(1) // audio format = PCM
      putShort(CHANNELS.toShort())
      putInt(sampleRate)
      putInt(byteRate)
      putShort(blockAlign.toShort())
      putShort(BITS_PER_SAMPLE.toShort())
      put("data".toByteArray())
      putInt(dataSize)
    }.array()
    return header + pcm
  }
}
