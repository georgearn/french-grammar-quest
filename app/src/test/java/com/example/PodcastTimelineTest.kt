package com.example

import com.example.data.engine.DialogueScript
import com.example.data.engine.DialogueTurn
import com.example.data.engine.GeminiErrorKind
import com.example.data.engine.GeminiException
import com.example.data.engine.GeminiHttp
import com.example.data.engine.PodcastTimeline
import com.example.data.engine.SpeechAlignment
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin

class PodcastTimelineTest {

  @Test
  fun `parses speaker turns, strips markdown and ignores the notes`() {
    val script = """
      **Camille :** Bonjour Nadia !
      Nadia : Salut Camille, il faut que tu
      écoutes ça.
      Camille: D'accord.
      ---
      - Note : le subjonctif apparaît ici.
    """.trimIndent()
    val turns = DialogueScript.parse(script)
    assertEquals(
      listOf(
        DialogueTurn("Camille", "Bonjour Nadia !"),
        DialogueTurn("Nadia", "Salut Camille, il faut que tu écoutes ça."),
        DialogueTurn("Camille", "D'accord.")
      ),
      turns
    )
  }

  @Test
  fun `chunks keep turns whole and stay near the word limit`() {
    val turns = (1..20).map { DialogueTurn(if (it % 2 == 0) "A" else "B", List(30) { "mot" }.joinToString(" ")) }
    val chunks = DialogueScript.chunk(turns, maxWords = 100)
    assertEquals(turns, chunks.flatten())
    assertTrue(chunks.all { chunk -> chunk.sumOf { it.text.split(' ').size } <= 100 })
  }

  @Test
  fun `turn boundaries snap to pauses in the audio`() {
    val rate = 24_000
    // Three turns of speech (1.0 s, 2.0 s, 1.0 s) separated by 400 ms pauses.
    val pcm = tone(rate, 1000) + silence(rate, 400) + tone(rate, 2000) + silence(rate, 400) + tone(rate, 1000)
    val turns = listOf(
      DialogueTurn("A", "Un deux trois quatre."),
      DialogueTurn("B", "Cinq six sept huit neuf dix onze douze."),
      DialogueTurn("A", "Treize quatorze.")
    )
    val timed = SpeechAlignment.align(pcm, rate, turns, offsetMs = 5_000)
    assertEquals(3, timed.size)
    assertNear(5_000, timed[0].startMs)
    assertNear(5_000 + 1_400, timed[1].startMs)
    assertNear(5_000 + 3_800, timed[2].startMs)
    timed.forEach { turn ->
      assertTrue(turn.words.zipWithNext().all { (a, b) -> a.atMs <= b.atMs })
      assertTrue(turn.words.all { it.atMs in turn.startMs..turn.endMs })
    }
    val timeline = PodcastTimeline(timed)
    assertEquals(1 to 0, timeline.locate(timed[1].startMs))
  }

  @Test
  fun `retries overloaded calls and gives up on a bad key`() = runTest {
    var calls = 0
    val result = GeminiHttp.withRetry(maxAttempts = 3) {
      calls++
      if (calls < 3) throw GeminiException(GeminiErrorKind.OVERLOADED, 503)
      "ok"
    }
    assertEquals("ok", result)
    assertEquals(3, calls)

    calls = 0
    try {
      GeminiHttp.withRetry(maxAttempts = 3) {
        calls++
        throw GeminiException(GeminiErrorKind.INVALID_KEY, 400)
      }
      fail("expected an exception")
    } catch (e: GeminiException) {
      assertEquals(GeminiErrorKind.INVALID_KEY, e.kind)
      assertEquals(1, calls)
    }
  }

  private fun assertNear(expected: Long, actual: Long, tolerance: Long = 60) =
    assertTrue("expected ~$expected, was $actual", abs(expected - actual) <= tolerance)

  private fun tone(rate: Int, ms: Int): ByteArray {
    val samples = rate * ms / 1000
    val out = ByteArray(samples * 2)
    for (i in 0 until samples) {
      val v = (8000 * sin(2 * PI * 220 * i / rate)).toInt()
      out[2 * i] = (v and 0xFF).toByte()
      out[2 * i + 1] = ((v shr 8) and 0xFF).toByte()
    }
    return out
  }

  private fun silence(rate: Int, ms: Int) = ByteArray(rate * ms / 1000 * 2)
}
