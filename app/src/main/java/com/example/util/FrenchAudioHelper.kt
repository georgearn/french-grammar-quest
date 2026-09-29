package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap

/** The part of [text] the device voice is saying right now: characters [start] until [end]. */
data class SpokenRange(val text: String, val start: Int, val end: Int)

class FrenchAudioHelper(context: Context) : TextToSpeech.OnInitListener {

  private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
  private var isInitialized = false

  private val _isSpeaking = MutableStateFlow(false)
  val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

  private val _spokenRange = MutableStateFlow<SpokenRange?>(null)
  /** Word currently spoken, for highlighting; null when silent. */
  val spokenRange: StateFlow<SpokenRange?> = _spokenRange.asStateFlow()

  /** Utterance id to (full text, offset of this piece in the full text). */
  private val pieces = ConcurrentHashMap<String, Pair<String, Int>>()
  @Volatile private var lastUtteranceId: String? = null

  override fun onInit(status: Int) {
    if (status == TextToSpeech.SUCCESS) {
      val result = tts?.setLanguage(Locale.FRENCH)
      isInitialized = result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED
      if (isInitialized) {
        tts?.setSpeechRate(0.88f) // Slightly slower, clear pedagogical rate for language learners
        tts?.setPitch(1.0f)
        setupListener()
      }
    }
  }

  private fun setupListener() {
    tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
      override fun onStart(utteranceId: String?) {
        _isSpeaking.value = true
      }

      // Called by the engine just before it speaks each word (API 26+; Google's engine supports it).
      override fun onRangeStart(utteranceId: String?, start: Int, end: Int, frame: Int) {
        val (text, offset) = pieces[utteranceId] ?: return
        _spokenRange.value = SpokenRange(text, offset + start, offset + end)
      }

      override fun onDone(utteranceId: String?) {
        pieces.remove(utteranceId)
        if (utteranceId == lastUtteranceId) finished()
      }

      override fun onStop(utteranceId: String?, interrupted: Boolean) {
        pieces.remove(utteranceId)
        if (utteranceId == lastUtteranceId) finished()
      }

      @Deprecated("Deprecated in Java")
      override fun onError(utteranceId: String?) {
        pieces.remove(utteranceId)
        finished()
      }
    })
  }

  private fun finished() {
    _isSpeaking.value = false
    _spokenRange.value = null
  }

  /**
   * Speaks [text] in French. Long texts are split at sentence ends, because engines refuse input
   * longer than [TextToSpeech.getMaxSpeechInputLength] (about 4,000 characters), and a generated
   * page of text is right at that limit.
   */
  fun speak(text: String) {
    val engine = tts ?: return
    if (!isInitialized) return
    engine.stop()
    pieces.clear()

    // Symbols are blanked rather than removed, so character offsets reported while speaking
    // still point into the original text.
    val cleaned = blankOut(text)
    val maxLength = (TextToSpeech.getMaxSpeechInputLength() - 100).coerceAtLeast(500)
    val session = System.currentTimeMillis()
    var queued = 0
    splitAtSentences(cleaned, maxLength).forEach { (offset, piece) ->
      if (piece.isBlank()) return@forEach
      val id = "fr_${session}_$queued"
      pieces[id] = text to offset
      lastUtteranceId = id
      engine.speak(piece, if (queued == 0) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD, null, id)
      queued++
    }
  }

  fun stop() {
    tts?.stop()
    pieces.clear()
    finished()
  }

  fun shutdown() {
    tts?.stop()
    tts?.shutdown()
    tts = null
    finished()
  }

  private fun blankOut(text: String): String {
    val chars = text.toCharArray()
    fun blank(range: IntRange) = range.forEach { chars[it] = ' ' }
    Regex("""\[.*?]""").findAll(text).forEach { blank(it.range) }
    Regex("""[*#_~`•]""").findAll(text).forEach { blank(it.range) }
    Regex("""\.\.\.""").findAll(text).forEach { blank(it.range) }
    return String(chars)
  }

  /** Pieces of at most [maxLength] characters, cut after sentence ends, with their offsets. */
  private fun splitAtSentences(text: String, maxLength: Int): List<Pair<Int, String>> {
    val result = mutableListOf<Pair<Int, String>>()
    var start = 0
    while (start < text.length) {
      var end = (start + maxLength).coerceAtMost(text.length)
      if (end < text.length) {
        val window = text.substring(start, end)
        val cut = maxOf(window.lastIndexOf(". "), window.lastIndexOf("! "), window.lastIndexOf("? "), window.lastIndexOf('\n'))
        if (cut > maxLength / 2) end = start + cut + 1
      }
      result += start to text.substring(start, end)
      start = end
    }
    return result
  }
}
