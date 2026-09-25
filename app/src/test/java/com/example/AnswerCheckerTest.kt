package com.example

import com.example.data.engine.AnswerChecker
import com.example.data.engine.AnswerVerdict
import org.junit.Assert.assertEquals
import org.junit.Test

class AnswerCheckerTest {

  @Test
  fun `ignores case, extra spaces and final punctuation`() {
    assertEquals(AnswerVerdict.CORRECT, AnswerChecker.check("  Partions. ", listOf("partions")))
    assertEquals(AnswerVerdict.CORRECT, AnswerChecker.check("que nous   partions!", listOf("que nous partions")))
  }

  @Test
  fun `treats curly and straight apostrophes the same`() {
    assertEquals(AnswerVerdict.CORRECT, AnswerChecker.check("l’a vue", listOf("l'a vue")))
    assertEquals(AnswerVerdict.CORRECT, AnswerChecker.check("l' a vue", listOf("l’a vue")))
  }

  @Test
  fun `accepts any listed alternative`() {
    assertEquals(AnswerVerdict.CORRECT, AnswerChecker.check("allions", listOf("partions", "allions")))
  }

  @Test
  fun `flags accent-only mistakes separately`() {
    assertEquals(AnswerVerdict.ACCENT_MISTAKE, AnswerChecker.check("etes", listOf("êtes")))
  }

  @Test
  fun `rejects wrong or empty answers`() {
    assertEquals(AnswerVerdict.WRONG, AnswerChecker.check("partons", listOf("partions")))
    assertEquals(AnswerVerdict.WRONG, AnswerChecker.check("   ", listOf("partions")))
  }
}
