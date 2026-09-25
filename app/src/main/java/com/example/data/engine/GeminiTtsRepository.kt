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
import java.io.IOException
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
    .readTimeout(300, TimeUnit.SECONDS) // a 7-8 min two-speaker script takes a while to synthesize
    .writeTimeout(60, TimeUnit.SECONDS)
    .callTimeout(340, TimeUnit.SECONDS)
    .build()

  private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

  companion object {
    private const val MODEL = "gemini-3.1-flash-tts-preview"
    private const val DEFAULT_SAMPLE_RATE_HZ = 24000
    private const val CHANNELS = 1
    private const val BITS_PER_SAMPLE = 16
  }

  /**
   * Synthesizes a two-speaker script (lines like "Camille : ..." / "Nadia : ...") into a WAV
   * byte array, ready to write to a file and play back with MediaPlayer.
   */
  suspend fun synthesizeDialogue(
    script: String,
    speaker1: String,
    speaker2: String,
    apiKey: String
  ): Result<ByteArray> = withContext(Dispatchers.IO) {
    runCatching {
      if (apiKey.isBlank()) {
        throw IllegalStateException("Aucune clé API Gemini renseignée.")
      }

      val url = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL:generateContent?key=$apiKey"

      val promptText =
        "Lis ce dialogue à voix haute de façon naturelle et expressive, avec deux voix bien " +
          "distinctes pour les deux locuteurs :\n\n$script"

      val body = JSONObject().apply {
        put(
          "contents",
          JSONArray().put(
            JSONObject().put(
              "parts",
              JSONArray().put(JSONObject().put("text", promptText))
            )
          )
        )
        put(
          "generationConfig",
          JSONObject().apply {
            put("responseModalities", JSONArray().put("AUDIO"))
            // Audio output tokens are far more expensive than text tokens (~dozens per second
            // of speech). Without an explicit high ceiling here, a multi-minute two-speaker
            // script gets silently cut short mid-audio even though the on-screen script text is
            // complete — this was the actual cause of "podcast" output feeling too short.
            put("maxOutputTokens", 32000)
            put(
              "speechConfig",
              JSONObject().put(
                "multiSpeakerVoiceConfig",
                JSONObject().put(
                  "speakerVoiceConfigs",
                  JSONArray()
                    .put(speakerVoiceConfig(speaker1, "Kore"))
                    .put(speakerVoiceConfig(speaker2, "Puck"))
                )
              )
            )
          }
        )
      }

      val httpRequest = Request.Builder()
        .url(url)
        .post(body.toString().toRequestBody(jsonMediaType))
        .build()

      val responseText = client.newCall(httpRequest).execute().use { response ->
        val raw = response.body?.string().orEmpty()
        if (!response.isSuccessful) {
          val apiMessage = runCatching {
            JSONObject(raw).optJSONObject("error")?.optString("message")
          }.getOrNull()
          throw IOException("Erreur Gemini TTS (${response.code}) : ${apiMessage ?: raw.take(200)}")
        }
        raw
      }

      val (pcmBytes, sampleRate) = extractAudio(responseText)
        ?: throw IllegalStateException("Réponse audio vide de Gemini.")

      pcmToWav(pcmBytes, sampleRate)
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
