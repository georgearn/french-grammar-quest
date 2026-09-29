package com.example.data.engine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

enum class GenerationFormat {
  TEXT,           // Court texte de lecture
  PODCAST_SCRIPT  // Script de "podcast" à deux voix, lu ensuite via la synthèse vocale de l'appareil
}

data class GenerationRequest(
  val level: String,
  val grammarPointTitle: String,
  val theme: String,
  val format: GenerationFormat
)

/**
 * Calls the public Gemini Developer API (generativelanguage.googleapis.com) directly with the
 * user's own Google AI Studio API key — no Firebase project, no server-side key management.
 * The key is kept in local SharedPreferences only (see AppPreferences.geminiApiKey).
 *
 * Uses the streaming endpoint (server-sent events): text arrives as it is written, so a long
 * podcast script never sits silent long enough to hit a read timeout, and the screen can show
 * the text growing instead of a spinner. Overloaded or rate-limited calls are retried with
 * backoff as long as nothing has been received yet.
 */
class GeminiGenerationRepository {

  private val client = OkHttpClient.Builder()
    .connectTimeout(20, TimeUnit.SECONDS)
    // Time allowed between two chunks, not for the whole answer: the model "thinks" before the
    // first chunk, which can take a while for a 1,000-word script.
    .readTimeout(120, TimeUnit.SECONDS)
    .build()

  private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

  /**
   * Generates the text for [request], calling [onPartial] with the text received so far as it
   * streams in, and [onRetry] before each automatic retry. Failures are [GeminiException]s.
   */
  suspend fun generate(
    request: GenerationRequest,
    apiKey: String,
    onPartial: (String) -> Unit = {},
    onRetry: (attempt: Int, maxAttempts: Int) -> Unit = { _, _ -> }
  ): Result<String> = withContext(Dispatchers.IO) {
    runCatching {
      if (apiKey.isBlank()) throw GeminiException(GeminiErrorKind.INVALID_KEY)
      val body = requestBody(buildPrompt(request))
      GeminiHttp.withRetry(onRetry = onRetry) {
        streamOnce(body, apiKey, onPartial)
      }
    }.recoverCatching { throw if (it is kotlinx.coroutines.CancellationException) it else GeminiHttp.classify(it) }
  }

  private fun requestBody(prompt: String): String = JSONObject().apply {
    put(
      "contents",
      JSONArray().put(
        JSONObject().put(
          "parts",
          JSONArray().put(JSONObject().put("text", prompt))
        )
      )
    )
    put(
      "generationConfig",
      JSONObject().apply {
        put("temperature", 0.85)
        // gemini-3.6-flash is a reasoning model: "thought" tokens count against
        // maxOutputTokens before any visible text is produced. A low ceiling silently
        // truncates the response mid-sentence — this happened twice, once for plain text
        // and again for the longer podcast script (950-1100 words needs more headroom).
        // thinkingLevel "low" keeps most of the budget for actual output text.
        put("maxOutputTokens", 16384)
        put("thinkingConfig", JSONObject().put("thinkingLevel", "low"))
      }
    )
  }.toString()

  private suspend fun streamOnce(body: String, apiKey: String, onPartial: (String) -> Unit): String {
    val url = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL:streamGenerateContent?alt=sse&key=$apiKey"
    val call = client.newCall(
      Request.Builder().url(url).post(body.toRequestBody(jsonMediaType)).build()
    )
    // Cancelling the coroutine (new generation, screen closed) aborts the HTTP call too.
    val job = currentCoroutineContext()[Job]
    val handle = job?.invokeOnCompletion { call.cancel() }
    try {
      // IMPORTANT: execute() is blocking and must stay off the main thread (the caller is on
      // Dispatchers.IO); Android throws NetworkOnMainThreadException otherwise.
      call.execute().use { response ->
        if (!response.isSuccessful) throw GeminiHttp.errorFrom(response)
        val source = response.body?.source() ?: throw GeminiException(GeminiErrorKind.EMPTY)
        val text = StringBuilder()
        var blocked = false
        while (true) {
          val line = source.readUtf8Line() ?: break
          if (!line.startsWith("data:")) continue
          val chunk = runCatching { JSONObject(line.removePrefix("data:").trim()) }.getOrNull() ?: continue
          chunk.optJSONObject("error")?.let { throw GeminiHttp.errorFrom(it) }
          if (chunk.optJSONObject("promptFeedback")?.has("blockReason") == true) blocked = true
          val candidate = chunk.optJSONArray("candidates")?.optJSONObject(0) ?: continue
          if (candidate.optString("finishReason") in BLOCKED_REASONS) blocked = true
          val parts = candidate.optJSONObject("content")?.optJSONArray("parts") ?: continue
          var grew = false
          for (i in 0 until parts.length()) {
            val part = parts.getJSONObject(i)
            if (part.optBoolean("thought")) continue // reasoning summary, not the answer
            val piece = part.optString("text")
            if (piece.isNotEmpty()) {
              text.append(piece)
              grew = true
            }
          }
          if (grew) onPartial(text.toString())
        }
        val result = text.toString().trim()
        if (result.isEmpty()) {
          throw GeminiException(if (blocked) GeminiErrorKind.BLOCKED else GeminiErrorKind.EMPTY)
        }
        return result
      }
    } finally {
      handle?.dispose()
    }
  }

  private companion object {
    const val MODEL = "gemini-3.6-flash"
    val BLOCKED_REASONS = setOf("SAFETY", "PROHIBITED_CONTENT", "BLOCKLIST", "SPII", "RECITATION")
  }

  private fun buildPrompt(request: GenerationRequest): String {
    val (level, grammarPoint, theme) = Triple(request.level, request.grammarPointTitle, request.theme)

    return when (request.format) {
      GenerationFormat.TEXT -> """
        Tu es un rédacteur pédagogique de FLE (français langue étrangère).
        Écris un texte long et développé (550 à 700 mots, l'équivalent d'une page), en français, calibré pour le niveau CECRL $level.
        Thème imposé : "$theme".
        Contrainte grammaticale : le texte doit illustrer naturellement, à plusieurs reprises, la règle suivante : "$grammarPoint".
        Style : naturel, jamais artificiel ; adapte le vocabulaire au niveau $level.
        Structure de la réponse :
        1. Le texte en français.
        2. Une ligne "---".
        3. 3 à 5 puces indiquant où et comment la règle "$grammarPoint" apparaît dans le texte (citer la phrase exacte).
        Réponds uniquement avec ce contenu, sans introduction ni conclusion méta.
      """.trimIndent()

      GenerationFormat.PODCAST_SCRIPT -> """
        Tu es scénariste pour un podcast pédagogique de FLE (français langue étrangère).
        Écris le script d'un podcast à deux voix (Camille et Nadia) d'environ 950 à 1100 mots
        (soit environ 7 à 8 minutes à l'oral), en français, calibré pour le niveau CECRL $level.
        Thème imposé : "$theme".
        Contrainte grammaticale : les deux voix doivent employer plusieurs fois, de façon naturelle
        et variée, la règle suivante : "$grammarPoint".
        Format de sortie :
        - Indique le nom du locuteur avant chaque réplique (ex. "Camille :", "Nadia :").
        - Ton conversationnel, quelques hésitations naturelles, mais sans argot excessif si le niveau est bas.
        - Termine par une ligne "---" suivie de 3 puces expliquant où la règle grammaticale apparaît.
        Réponds uniquement avec ce script, sans introduction ni conclusion méta.
      """.trimIndent()
    }
  }
}
