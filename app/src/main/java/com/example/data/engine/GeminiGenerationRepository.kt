package com.example.data.engine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
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
 * The user pastes their key in Settings; it's kept in local SharedPreferences only
 * (see GrammarViewModel.setGeminiApiKey) and sent as a query param on each request, exactly as
 * Google's own REST docs describe.
 *
 * NOTE: there is no bundled cloud text-to-speech/audio generation here. "Podcast" means a
 * generated dialogue script targeting ~5 minutes of spoken content; playback reuses the
 * device's own French TTS voice (see FrenchAudioHelper), the same one already used to read
 * contextual passages in Exploration mode.
 */
class GeminiGenerationRepository {

  private val client = OkHttpClient.Builder()
    .connectTimeout(20, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .build()

  private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

  suspend fun generate(request: GenerationRequest, apiKey: String): Result<String> = withContext(Dispatchers.IO) {
    runCatching {
      if (apiKey.isBlank()) {
        throw IllegalStateException("Aucune clé API Gemini renseignée. Ajoute la tienne dans les Réglages.")
      }

      val prompt = buildPrompt(request)
      val model = "gemini-3.6-flash"
      val url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey"

      val body = JSONObject().apply {
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
      }

      val httpRequest = Request.Builder()
        .url(url)
        .post(body.toString().toRequestBody(jsonMediaType))
        .build()

      // IMPORTANT: this is a blocking OkHttp call (execute(), not enqueue()). It must run off
      // the main thread — Android throws NetworkOnMainThreadException otherwise, and that
      // exception's message is null, which used to surface to the user as a bare "unknown
      // error" with no clue what actually went wrong. Hence withContext(Dispatchers.IO) above.
      val responseText = client.newCall(httpRequest).execute().use { response ->
        val raw = response.body?.string().orEmpty()
        if (!response.isSuccessful) {
          val apiMessage = runCatching {
            JSONObject(raw).optJSONObject("error")?.optString("message")
          }.getOrNull()
          throw IOException("Erreur Gemini (${response.code}) : ${apiMessage ?: raw.take(200)}")
        }
        raw
      }

      parseGeneratedText(responseText)
        ?: throw IllegalStateException("Réponse vide de Gemini.")
    }
  }

  private fun parseGeneratedText(raw: String): String? {
    val json = JSONObject(raw)
    val candidates = json.optJSONArray("candidates") ?: return null
    if (candidates.length() == 0) return null
    val content = candidates.getJSONObject(0).optJSONObject("content") ?: return null
    val parts = content.optJSONArray("parts") ?: return null
    val builder = StringBuilder()
    for (i in 0 until parts.length()) {
      builder.append(parts.getJSONObject(i).optString("text"))
    }
    return builder.toString().trim().takeUnless { it.isEmpty() }
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
