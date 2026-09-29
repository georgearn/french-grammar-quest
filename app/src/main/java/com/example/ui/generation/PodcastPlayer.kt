package com.example.ui.generation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.engine.PodcastTimeline
import com.example.util.SpokenRange

/** What the podcast player is doing. [path] is null when nothing is loaded. */
data class PlaybackState(
  val path: String? = null,
  val isPlaying: Boolean = false,
  val positionMs: Long = 0,
  val durationMs: Long = 0
)

/** Play/pause, elapsed time and a seek bar for the podcast at [path]. */
@Composable
fun PodcastPlayerControls(
  path: String,
  playback: PlaybackState,
  onToggle: (String) -> Unit,
  onSeek: (String, Long) -> Unit,
  modifier: Modifier = Modifier
) {
  val isThis = playback.path == path
  val playing = isThis && playback.isPlaying
  val duration = if (isThis) playback.durationMs else 0L
  var dragging by remember { mutableStateOf<Float?>(null) }
  val position = if (isThis) playback.positionMs else 0L
  val fraction = dragging ?: if (duration > 0) (position.toFloat() / duration).coerceIn(0f, 1f) else 0f

  Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
    FilledTonalIconButton(onClick = { onToggle(path) }) {
      Icon(
        if (playing) Icons.Default.Pause else Icons.Default.PlayArrow,
        contentDescription = stringResource(if (playing) R.string.action_pause else R.string.action_listen)
      )
    }
    Spacer(Modifier.width(8.dp))
    Column(modifier = Modifier.weight(1f)) {
      Slider(
        value = fraction,
        onValueChange = { dragging = it },
        onValueChangeFinished = {
          dragging?.let { if (duration > 0) onSeek(path, (it * duration).toLong()) }
          dragging = null
        },
        enabled = isThis && duration > 0
      )
      Text(
        text = if (duration > 0) "${formatTime((fraction * duration).toLong())} / ${formatTime(duration)}" else "",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

private fun formatTime(ms: Long): String {
  val seconds = ms / 1000
  return "%d:%02d".format(seconds / 60, seconds % 60)
}

/**
 * The script as spoken turns, following the audio: the current turn is tinted, the current word
 * highlighted, and the view keeps the current turn on screen while playing. Tapping a turn plays
 * from there. [positionMs] is null when this podcast is not the one playing.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PodcastTranscript(
  timeline: PodcastTimeline,
  positionMs: Long?,
  isPlaying: Boolean,
  onSeek: (Long) -> Unit,
  modifier: Modifier = Modifier
) {
  val current = positionMs?.let { timeline.locate(it) }
  val speakerColor = MaterialTheme.colorScheme.primary
  val wordHighlight = MaterialTheme.colorScheme.primary.copy(alpha = 0.28f)
  val turnTint = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)

  Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
    timeline.turns.forEachIndexed { index, turn ->
      val isCurrent = current?.first == index
      val requester = remember { BringIntoViewRequester() }
      if (isCurrent && isPlaying) {
        LaunchedEffect(index) { requester.bringIntoView() }
      }
      val wordIndex = if (isCurrent) current!!.second else -1
      val text = buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = speakerColor)) { append("${turn.speaker} : ") }
        val offset = length
        append(turn.text)
        turn.words.getOrNull(wordIndex)?.let { word ->
          addStyle(SpanStyle(background = wordHighlight), offset + word.start, offset + word.end)
        }
      }
      Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
          .fillMaxWidth()
          .bringIntoViewRequester(requester)
          .background(if (isCurrent) turnTint else Color.Transparent, RoundedCornerShape(8.dp))
          .clickable(role = Role.Button) { onSeek(turn.startMs) }
          .padding(horizontal = 6.dp, vertical = 4.dp)
      )
    }
  }
}

/** [text] with the word the device voice is saying highlighted, when [range] belongs to it. */
@Composable
fun highlightSpoken(text: String, range: SpokenRange?): AnnotatedString {
  val highlight = MaterialTheme.colorScheme.primary.copy(alpha = 0.28f)
  if (range == null || range.text != text || range.end > text.length || range.start >= range.end) {
    return AnnotatedString(text)
  }
  return buildAnnotatedString {
    append(text)
    addStyle(SpanStyle(background = highlight), range.start, range.end)
  }
}

/** Retry notice and chunk progress while podcast audio is being created. */
@Composable
fun AudioProgressText(progress: Pair<Int, Int>?, status: String?) {
  val text = status ?: progress?.let { (done, total) -> stringResource(R.string.gen_audio_progress, done, total) }
    ?: stringResource(R.string.gen_audio_generating)
  Text(text, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
}
