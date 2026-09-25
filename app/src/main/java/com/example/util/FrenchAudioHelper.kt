package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class FrenchAudioHelper(context: Context) : TextToSpeech.OnInitListener {

  private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
  private var isInitialized = false

  private val _isSpeaking = MutableStateFlow(false)
  val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

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

      override fun onDone(utteranceId: String?) {
        _isSpeaking.value = false
      }

      @Deprecated("Deprecated in Java")
      override fun onError(utteranceId: String?) {
        _isSpeaking.value = false
      }
    })
  }

  fun speak(text: String) {
    if (!isInitialized) return
    tts?.stop()
    // Clean string for speech (remove markdown symbols, slashes, brackets)
    val cleanedText = text
      .replace(Regex("\\[.*?\\]"), "")
      .replace(Regex("[*#_~`•]"), " ")
      .replace("...", " ")
      .trim()

    if (cleanedText.isNotEmpty()) {
      val utteranceId = "fr_audio_${System.currentTimeMillis()}"
      tts?.speak(cleanedText, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }
  }

  fun stop() {
    tts?.stop()
    _isSpeaking.value = false
  }

  fun shutdown() {
    tts?.stop()
    tts?.shutdown()
    tts = null
    _isSpeaking.value = false
  }
}
