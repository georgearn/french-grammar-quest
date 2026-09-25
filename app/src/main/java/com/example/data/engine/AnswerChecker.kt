package com.example.data.engine

import java.text.Normalizer
import java.util.Locale

/** Outcome of checking a typed answer against the expected ones. */
enum class AnswerVerdict {
  CORRECT,
  /** Right letters, wrong or missing accents: still counted wrong, but the feedback says why. */
  ACCENT_MISTAKE,
  WRONG
}

/**
 * Compares a typed production-drill answer with the accepted answers, tolerating what a learner
 * should not be penalised for (case, curly vs straight apostrophes, extra spaces, final
 * punctuation) while keeping accents significant, since they are part of French spelling.
 */
object AnswerChecker {

  fun check(input: String, accepted: List<String>): AnswerVerdict {
    val normalizedInput = normalize(input)
    if (normalizedInput.isEmpty()) return AnswerVerdict.WRONG
    val normalizedAccepted = accepted.map(::normalize).filter { it.isNotEmpty() }
    if (normalizedInput in normalizedAccepted) return AnswerVerdict.CORRECT
    val inputWithoutAccents = stripAccents(normalizedInput)
    if (normalizedAccepted.any { stripAccents(it) == inputWithoutAccents }) return AnswerVerdict.ACCENT_MISTAKE
    return AnswerVerdict.WRONG
  }

  fun normalize(raw: String): String =
    Normalizer.normalize(raw, Normalizer.Form.NFC)
      .replace('’', '\'')
      .replace('‘', '\'')
      .replace('ʼ', '\'')
      .replace(' ', ' ')
      .replace(Regex("\\s*'\\s*"), "'")
      .replace(Regex("\\s+"), " ")
      .trim()
      .trimEnd('.', '!', '?', ';', ',', '…')
      .trim()
      .lowercase(Locale.FRENCH)

  private fun stripAccents(text: String): String =
    Normalizer.normalize(text, Normalizer.Form.NFD).replace(Regex("\\p{Mn}+"), "")
}
