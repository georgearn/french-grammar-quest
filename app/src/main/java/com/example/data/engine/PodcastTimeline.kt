package com.example.data.engine

import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt

/** One spoken turn of a two-voice podcast script ("Camille : …"). */
data class DialogueTurn(val speaker: String, val text: String)

/** A word inside a turn: character range in [TimedTurn.text] and when it starts, in ms. */
data class TimedWord(val start: Int, val end: Int, val atMs: Long)

data class TimedTurn(
  val speaker: String,
  val text: String,
  val startMs: Long,
  val endMs: Long,
  val words: List<TimedWord>
)

/**
 * When each turn and word of a synthesized podcast is spoken. Gemini's TTS returns audio only,
 * with no timestamps, so this is an estimate: turn boundaries come from the pauses detected in
 * the audio, and words are spread over their turn in proportion to their length. Good enough to
 * follow along; it can be a fraction of a second early or late inside long turns.
 */
data class PodcastTimeline(val turns: List<TimedTurn>) {

  /** Turn and word being spoken at [positionMs]; the word is -1 in the pause before a turn. */
  fun locate(positionMs: Long): Pair<Int, Int>? {
    val turnIndex = turns.indexOfLast { it.startMs <= positionMs }
    if (turnIndex < 0) return null
    val turn = turns[turnIndex]
    if (positionMs > turn.endMs + 400) return turnIndex to -1
    return turnIndex to turn.words.indexOfLast { it.atMs <= positionMs }
  }

  fun toJson(): String = JSONObject().apply {
    put("v", 1)
    put("turns", JSONArray().apply {
      turns.forEach { turn ->
        put(JSONObject().apply {
          put("s", turn.speaker)
          put("t", turn.text)
          put("a", turn.startMs)
          put("b", turn.endMs)
          put("w", JSONArray().apply {
            turn.words.forEach { put(JSONArray().put(it.start).put(it.end).put(it.atMs)) }
          })
        })
      }
    })
  }.toString()

  companion object {
    fun fromJson(json: String): PodcastTimeline? = runCatching {
      val turns = JSONObject(json).getJSONArray("turns")
      PodcastTimeline(
        (0 until turns.length()).map { i ->
          val t = turns.getJSONObject(i)
          val words = t.getJSONArray("w")
          TimedTurn(
            speaker = t.getString("s"),
            text = t.getString("t"),
            startMs = t.getLong("a"),
            endMs = t.getLong("b"),
            words = (0 until words.length()).map { j ->
              val w = words.getJSONArray(j)
              TimedWord(w.getInt(0), w.getInt(1), w.getLong(2))
            }
          )
        }
      )
    }.getOrNull()
  }
}

object DialogueScript {

  private val SPEAKER_LINE = Regex("""^\s*[*_]*([A-ZÀ-Ý][\p{L}'-]{1,20})[*_]*\s*:\s*[*_]*\s*(.*)$""")
  private val MARKDOWN = Regex("""[*_#`]+""")

  /**
   * Speaker turns of a podcast script, stopping at the "---" line that starts the grammar notes.
   * Lines without a speaker label continue the previous turn. Only the two most frequent
   * speakers count as voices; anything else labelled (e.g. "Note :") joins the previous turn.
   */
  fun parse(script: String): List<DialogueTurn> {
    val body = script.lines().takeWhile { it.trim() != "---" }
    val labelled = body.mapNotNull { SPEAKER_LINE.find(it)?.groupValues?.get(1) }
    val voices = labelled.groupingBy { it }.eachCount().entries
      .sortedByDescending { it.value }
      .take(2)
      .map { it.key }
      .toSet()

    val turns = mutableListOf<DialogueTurn>()
    body.forEach { raw ->
      val line = raw.trim()
      if (line.isEmpty()) return@forEach
      val match = SPEAKER_LINE.find(line)
      val speaker = match?.groupValues?.get(1)
      val text = clean(if (match != null && speaker in voices) match.groupValues[2] else line)
      if (text.isEmpty()) return@forEach
      if (match != null && speaker in voices) {
        turns += DialogueTurn(speaker!!, text)
      } else if (turns.isNotEmpty()) {
        val last = turns.removeAt(turns.lastIndex)
        turns += last.copy(text = last.text + " " + text)
      }
    }
    return turns
  }

  private fun clean(text: String): String = text.replace(MARKDOWN, "").replace(Regex("\\s+"), " ").trim()

  /**
   * Groups turns into chunks of about [maxWords] words, one TTS request each. Shorter requests
   * finish well within the timeout, can be retried on their own, and give the alignment a fresh
   * start at every chunk boundary.
   */
  fun chunk(turns: List<DialogueTurn>, maxWords: Int = 180): List<List<DialogueTurn>> {
    val chunks = mutableListOf<MutableList<DialogueTurn>>()
    var words = 0
    turns.forEach { turn ->
      val count = turn.text.split(' ').size
      if (chunks.isEmpty() || (words + count > maxWords && chunks.last().isNotEmpty())) {
        chunks += mutableListOf<DialogueTurn>()
        words = 0
      }
      chunks.last() += turn
      words += count
    }
    return chunks
  }
}

object SpeechAlignment {

  private const val FRAME_MS = 20
  /** Shortest silence that can separate two turns. */
  private const val MIN_GAP_FRAMES = 8

  /**
   * Places [turns] (spoken in order in [pcm], 16-bit mono little-endian) on a timeline starting
   * at [offsetMs]: turn boundaries snap to the detected pauses closest to where the text length
   * says they should be, then words are spread over each turn.
   */
  fun align(pcm: ByteArray, sampleRate: Int, turns: List<DialogueTurn>, offsetMs: Long): List<TimedTurn> {
    if (turns.isEmpty()) return emptyList()
    val frameSamples = max(1, sampleRate * FRAME_MS / 1000)
    val frameCount = pcm.size / 2 / frameSamples
    if (frameCount == 0) return turns.map { TimedTurn(it.speaker, it.text, offsetMs, offsetMs, emptyList()) }

    val rms = DoubleArray(frameCount) { f ->
      var sum = 0.0
      for (s in 0 until frameSamples) {
        val i = (f * frameSamples + s) * 2
        val sample = ((pcm[i + 1].toInt() shl 8) or (pcm[i].toInt() and 0xFF)).toShort().toDouble()
        sum += sample * sample
      }
      sqrt(sum / frameSamples)
    }
    val loud = rms.sorted()[(frameCount * 0.9).toInt().coerceAtMost(frameCount - 1)]
    val threshold = max(250.0, loud * 0.08)
    val speaking = BooleanArray(frameCount) { rms[it] > threshold }
    val first = speaking.indexOfFirst { it }.takeIf { it >= 0 } ?: 0
    val last = speaking.indexOfLast { it }.takeIf { it >= 0 } ?: (frameCount - 1)

    // Pauses inside the speech: (first silent frame, first speaking frame after it).
    val gaps = mutableListOf<IntRange>()
    var runStart = -1
    for (f in first..last) {
      if (!speaking[f]) {
        if (runStart < 0) runStart = f
      } else if (runStart >= 0) {
        if (f - runStart >= MIN_GAP_FRAMES) gaps += runStart until f
        runStart = -1
      }
    }

    val weights = turns.map { weight(it.text).toDouble() }
    val totalWeight = weights.sum()
    val span = (last - first).coerceAtLeast(1)
    val longestGap = gaps.maxOfOrNull { it.last - it.first + 1 } ?: 1

    // Boundary j sits between turn j-1 and turn j.
    val starts = IntArray(turns.size)
    val ends = IntArray(turns.size)
    starts[0] = first
    ends[turns.lastIndex] = last + 1
    var cumulative = 0.0
    var searchFrom = first
    val available = gaps.toMutableList()
    for (j in 1 until turns.size) {
      cumulative += weights[j - 1]
      val expected = first + (cumulative / totalWeight * span).toInt()
      val best = available
        .filter { it.first >= searchFrom && abs(mid(it) - expected) < span * 0.25 }
        .minByOrNull { abs(mid(it) - expected).toDouble() / span - 0.5 * (it.last - it.first + 1) / longestGap }
      if (best != null) {
        ends[j - 1] = best.first
        starts[j] = best.last + 1
        available.remove(best)
        searchFrom = best.last + 1
      } else {
        ends[j - 1] = expected
        starts[j] = expected
        searchFrom = expected
      }
    }

    return turns.mapIndexed { i, turn ->
      val startMs = offsetMs + starts[i].toLong() * FRAME_MS
      val endMs = offsetMs + max(starts[i], ends[i]).toLong() * FRAME_MS
      TimedTurn(turn.speaker, turn.text, startMs, endMs, wordTimings(turn.text, startMs, endMs))
    }
  }

  /** Spreads the words of [text] over [startMs, endMs], longer words and punctuation taking longer. */
  fun wordTimings(text: String, startMs: Long, endMs: Long): List<TimedWord> {
    val words = Regex("""\S+""").findAll(text).toList()
    if (words.isEmpty()) return emptyList()
    val weights = words.map { weight(it.value) }
    val total = weights.sum().coerceAtLeast(1)
    val duration = (endMs - startMs).coerceAtLeast(0)
    var elapsed = 0L
    return words.mapIndexed { i, match ->
      val at = startMs + elapsed * duration / total
      elapsed += weights[i]
      TimedWord(match.range.first, match.range.last + 1, at)
    }
  }

  private fun mid(range: IntRange) = (range.first + range.last) / 2

  /** Rough speaking-time weight: letters, plus a pause after punctuation. */
  private fun weight(text: String): Int {
    var w = 0
    Regex("""\S+""").findAll(text).forEach { m ->
      val word = m.value
      w += word.count { it.isLetterOrDigit() }.coerceAtLeast(1) + 1
      when (word.last()) {
        ',', ';', ':' -> w += 3
        '.', '!', '?', '…' -> w += 6
      }
    }
    return w.coerceAtLeast(1)
  }
}
