package com.example.ui.generation

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Podcasts
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.engine.GenerationFormat
import com.example.ui.GrammarViewModel
import com.example.ui.components.CEFR_LEVELS
import com.example.ui.components.RulePickerSheet
import com.example.ui.components.RuleSelectorField
import com.example.ui.theme.AppThemeColors
import java.text.DateFormat
import java.util.Date

/** What the learner is about to generate. Lives in the ViewModel so it survives tab switches. */
data class GenerationForm(
  val ruleId: String,
  val level: String,
  val theme: String = "",
  val format: GenerationFormat = GenerationFormat.TEXT,
  /** True while the rule simply mirrors the one open in "Comprendre". */
  val followsExplore: Boolean = true
)

data class GenerationUiState(
  val isLoading: Boolean = false,
  val resultText: String? = null,
  val error: String? = null,
  val format: GenerationFormat? = null,
  val isSynthesizingAudio: Boolean = false,
  val podcastAudioPath: String? = null,
  val audioError: String? = null,
  val level: String? = null,
  val grammarPointTitle: String? = null,
  val theme: String? = null,
  val isSaved: Boolean = false
)

data class SavedGenerationUi(
  val id: Long,
  val format: GenerationFormat,
  val level: String,
  val grammarPointTitle: String,
  val theme: String,
  val text: String,
  val audioPath: String?,
  val createdTimestamp: Long = 0L
)

/**
 * "Créer": a short form (rule, level, theme, format), one Generate button, the result right below
 * with its actions (listen, save, copy, share, regenerate), then the learner's library.
 * The Gemini key lives in Settings; this screen only points there when it is missing.
 */
@Composable
fun GenerationScreen(
  viewModel: GrammarViewModel,
  onOpenSettings: () -> Unit,
  modifier: Modifier = Modifier
) {
  val form by viewModel.generationForm.collectAsStateWithLifecycle()
  val state by viewModel.generationState.collectAsStateWithLifecycle()
  val apiKey by viewModel.geminiApiKey.collectAsStateWithLifecycle()
  val saved by viewModel.savedGenerations.collectAsStateWithLifecycle()
  val levelFilter by viewModel.levelFilter.collectAsStateWithLifecycle()
  val isSpeaking by viewModel.isSpeaking.collectAsStateWithLifecycle()
  val isPodcastPlaying by viewModel.isPodcastPlaying.collectAsStateWithLifecycle()
  val playingSavedAudioPath by viewModel.playingSavedAudioPath.collectAsStateWithLifecycle()
  val rule = viewModel.ruleById(form.ruleId)
  var showPicker by rememberSaveable { mutableStateOf(false) }
  // The text field owns its text (synchronous updates keep the cursor stable); the ViewModel gets a copy.
  var theme by rememberSaveable { mutableStateOf(form.theme) }
  val setTheme: (String) -> Unit = { value ->
    theme = value
    viewModel.updateGenerationForm { it.copy(theme = value) }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .imePadding()
      .verticalScroll(rememberScrollState())
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    HeroHeader()

    if (apiKey.isBlank()) {
      MissingKeyCard(onOpenSettings = onOpenSettings)
    }

    // --- Form ---
    FormCard {
      FieldLabel(stringResource(R.string.gen_rule))
      RuleSelectorField(
        rule = rule,
        supportingText = rule.categoryName,
        onClick = { showPicker = true },
        modifier = Modifier.fillMaxWidth()
      )

      FieldLabel(stringResource(R.string.gen_level))
      Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        CEFR_LEVELS.forEach { level ->
          FilterChip(
            selected = form.level == level,
            onClick = { viewModel.updateGenerationForm { it.copy(level = level) } },
            label = { Text(level) }
          )
        }
      }

      FieldLabel(stringResource(R.string.gen_theme))
      OutlinedTextField(
        value = theme,
        onValueChange = setTheme,
        placeholder = { Text(stringResource(R.string.gen_theme_hint)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Done),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
      )
      ThemeSuggestions(onPick = setTheme)

      FieldLabel(stringResource(R.string.gen_format))
      FormatSelector(format = form.format, onSelect = { f -> viewModel.updateGenerationForm { it.copy(format = f) } })

      Spacer(Modifier.height(4.dp))
      if (apiKey.isBlank()) {
        OutlinedButton(
          onClick = onOpenSettings,
          modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
          shape = RoundedCornerShape(14.dp)
        ) {
          Icon(Icons.Default.Key, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(Modifier.width(8.dp))
          Text(stringResource(R.string.gen_add_key))
        }
      } else {
        Button(
          onClick = viewModel::generateContent,
          enabled = !state.isLoading,
          modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp)
            .testTag("generate_btn"),
          shape = RoundedCornerShape(14.dp)
        ) {
          if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
            Spacer(Modifier.width(10.dp))
            Text(stringResource(R.string.gen_generating), fontWeight = FontWeight.Bold)
          } else {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.gen_generate), fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    // --- Result ---
    when {
      state.isLoading -> LoadingCard(state.format ?: form.format)
      state.error != null -> ErrorCard(message = state.error.orEmpty(), onRetry = viewModel::generateContent, onDismiss = viewModel::dismissGeneration)
      state.resultText != null -> ResultCard(
        state = state,
        text = state.resultText.orEmpty(),
        isSpeaking = isSpeaking,
        isPodcastPlaying = isPodcastPlaying,
        onSpeak = viewModel::speakFrench,
        onStopSpeaking = viewModel::stopAudio,
        onSave = viewModel::saveCurrentGeneration,
        onRegenerate = viewModel::generateContent,
        onClose = viewModel::dismissGeneration,
        onSynthesizeAudio = viewModel::synthesizePodcastAudio,
        onPlayAudio = viewModel::playPodcastAudio,
        onStopAudio = viewModel::stopPodcastAudio,
        onExportAudio = viewModel::exportAudioToDevice
      )
    }

    // --- Library ---
    Library(
      items = saved,
      playingAudioPath = playingSavedAudioPath,
      onPlayAudio = viewModel::playSavedAudio,
      onStopAudio = viewModel::stopPodcastAudio,
      onExportAudio = viewModel::exportAudioToDevice,
      onDelete = viewModel::deleteSavedGeneration
    )

    Spacer(Modifier.height(16.dp))
  }

  if (showPicker) {
    RulePickerSheet(
      rules = viewModel.rules,
      selectedRuleId = rule.id,
      levelFilter = levelFilter,
      onLevelFilterChange = viewModel::setLevelFilter,
      onSelectRule = viewModel::selectGenerationRule,
      onDismiss = { showPicker = false }
    )
  }
}

@Composable
private fun HeroHeader() {
  val gradient = Brush.linearGradient(
    listOf(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.secondaryContainer)
  )
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .background(gradient, RoundedCornerShape(20.dp))
      .padding(20.dp)
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Box(
        modifier = Modifier
          .size(48.dp)
          .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f), CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
      }
      Spacer(Modifier.width(14.dp))
      Column {
        Text(
          stringResource(R.string.gen_hero_title),
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
          stringResource(R.string.gen_hero_subtitle),
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
        )
      }
    }
  }
}

@Composable
private fun MissingKeyCard(onOpenSettings: () -> Unit) {
  Surface(
    onClick = onOpenSettings,
    shape = RoundedCornerShape(16.dp),
    color = AppThemeColors.goldContainer
  ) {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
      Icon(Icons.Default.Key, contentDescription = null, tint = AppThemeColors.onGoldContainer)
      Spacer(Modifier.width(12.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(
          stringResource(R.string.gen_missing_key_title),
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = AppThemeColors.onGoldContainer
        )
        Text(
          stringResource(R.string.gen_missing_key_desc),
          style = MaterialTheme.typography.bodySmall,
          color = AppThemeColors.onGoldContainer
        )
      }
    }
  }
}

@Composable
private fun FormCard(content: @Composable () -> Unit) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
  ) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { content() }
  }
}

@Composable
private fun FieldLabel(text: String) {
  Text(
    text,
    style = MaterialTheme.typography.labelLarge,
    fontWeight = FontWeight.Bold,
    color = MaterialTheme.colorScheme.onSurfaceVariant,
    modifier = Modifier.padding(top = 4.dp)
  )
}

@Composable
private fun ThemeSuggestions(onPick: (String) -> Unit) {
  val suggestions = listOf(
    R.string.gen_theme_travel, R.string.gen_theme_work, R.string.gen_theme_food,
    R.string.gen_theme_city, R.string.gen_theme_nature, R.string.gen_theme_tech, R.string.gen_theme_family
  ).map { stringResource(it) }
  // Themes are sent to Gemini in French, whatever language the interface shows.
  val frenchValues = listOf("un voyage", "le travail", "la cuisine", "la vie en ville", "la nature", "la technologie", "la famille")
  Row(
    modifier = Modifier.horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    suggestions.forEachIndexed { index, label ->
      AssistChip(onClick = { onPick(frenchValues[index]) }, label = { Text(label) })
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormatSelector(format: GenerationFormat, onSelect: (GenerationFormat) -> Unit) {
  val options = listOf(
    Triple(GenerationFormat.TEXT, R.string.gen_format_text, Icons.AutoMirrored.Filled.Article),
    Triple(GenerationFormat.PODCAST_SCRIPT, R.string.gen_format_podcast, Icons.Default.Podcasts)
  )
  SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
    options.forEachIndexed { index, (option, label, icon) ->
      SegmentedButton(
        selected = format == option,
        onClick = { onSelect(option) },
        shape = SegmentedButtonDefaults.itemShape(index, options.size),
        icon = { SegmentedButtonDefaults.Icon(active = format == option) { Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp)) } }
      ) { Text(stringResource(label)) }
    }
  }
  Text(
    stringResource(if (format == GenerationFormat.TEXT) R.string.gen_format_text_desc else R.string.gen_format_podcast_desc),
    style = MaterialTheme.typography.bodySmall,
    color = MaterialTheme.colorScheme.onSurfaceVariant
  )
}

@Composable
private fun LoadingCard(format: GenerationFormat) {
  FormCard {
    Row(verticalAlignment = Alignment.CenterVertically) {
      CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 3.dp)
      Spacer(Modifier.width(14.dp))
      Column {
        Text(stringResource(R.string.gen_loading_title), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Text(
          stringResource(if (format == GenerationFormat.TEXT) R.string.gen_loading_text else R.string.gen_loading_podcast),
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}

@Composable
private fun ErrorCard(message: String, onRetry: () -> Unit, onDismiss: () -> Unit) {
  Surface(shape = RoundedCornerShape(16.dp), color = AppThemeColors.redContainer) {
    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = AppThemeColors.onRedContainer)
        Spacer(Modifier.width(10.dp))
        Text(
          stringResource(R.string.gen_error_title),
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = AppThemeColors.onRedContainer,
          modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onDismiss) {
          Icon(Icons.Default.Close, contentDescription = stringResource(R.string.action_close), tint = AppThemeColors.onRedContainer)
        }
      }
      Text(message, style = MaterialTheme.typography.bodySmall, color = AppThemeColors.onRedContainer)
      TextButton(onClick = onRetry) {
        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(6.dp))
        Text(stringResource(R.string.action_retry))
      }
    }
  }
}

@Composable
private fun ResultCard(
  state: GenerationUiState,
  text: String,
  isSpeaking: Boolean,
  isPodcastPlaying: Boolean,
  onSpeak: (String) -> Unit,
  onStopSpeaking: () -> Unit,
  onSave: () -> Unit,
  onRegenerate: () -> Unit,
  onClose: () -> Unit,
  onSynthesizeAudio: (String) -> Unit,
  onPlayAudio: (String) -> Unit,
  onStopAudio: () -> Unit,
  onExportAudio: (String) -> Unit
) {
  val isPodcast = state.format == GenerationFormat.PODCAST_SCRIPT
  val (body, notes) = remember(text) { splitNotes(text) }

  Card(
    modifier = Modifier.fillMaxWidth().testTag("generation_result"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          if (isPodcast) Icons.Default.Podcasts else Icons.AutoMirrored.Filled.Article,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(
            stringResource(if (isPodcast) R.string.gen_result_podcast else R.string.gen_result_text),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Text(
            listOfNotNull(state.level, state.grammarPointTitle, state.theme).joinToString(" · "),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
          )
        }
        IconButton(onClick = onClose) {
          Icon(Icons.Default.Close, contentDescription = stringResource(R.string.action_close))
        }
      }

      Spacer(Modifier.height(10.dp))
      ResultActions(
        text = text,
        isSaved = state.isSaved,
        listenButton = {
          if (!isPodcast) {
            FilledTonalButton(onClick = { if (isSpeaking) onStopSpeaking() else onSpeak(body) }) {
              Icon(if (isSpeaking) Icons.Default.Pause else Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(Modifier.width(6.dp))
              Text(stringResource(if (isSpeaking) R.string.action_stop else R.string.action_listen))
            }
          }
        },
        onSave = onSave,
        onRegenerate = onRegenerate
      )

      if (isPodcast) {
        Spacer(Modifier.height(10.dp))
        PodcastAudioRow(
          state = state,
          script = text,
          isPlaying = isPodcastPlaying,
          onSynthesize = onSynthesizeAudio,
          onPlay = onPlayAudio,
          onStop = onStopAudio,
          onExport = onExportAudio
        )
      }

      HorizontalDivider(Modifier.padding(vertical = 14.dp))
      Text(
        text = if (isPodcast) podcastScript(body) else AnnotatedString(body),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface
      )

      if (notes.isNotBlank()) {
        Spacer(Modifier.height(16.dp))
        Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
          Column(modifier = Modifier.padding(14.dp).fillMaxWidth()) {
            Text(stringResource(R.string.gen_rule_in_text), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text(notes, style = MaterialTheme.typography.bodyMedium)
          }
        }
      }
    }
  }
}

@Composable
private fun ResultActions(
  text: String,
  isSaved: Boolean,
  listenButton: @Composable () -> Unit,
  onSave: () -> Unit,
  onRegenerate: () -> Unit
) {
  val clipboard = LocalClipboardManager.current
  val context = LocalContext.current
  val shareTitle = stringResource(R.string.action_share)
  Row(
    modifier = Modifier.horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.spacedBy(4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    listenButton()
    ActionIcon(
      icon = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
      label = stringResource(if (isSaved) R.string.gen_saved else R.string.action_save),
      enabled = !isSaved,
      onClick = onSave
    )
    ActionIcon(Icons.Default.ContentCopy, stringResource(R.string.action_copy)) {
      clipboard.setText(AnnotatedString(text))
    }
    ActionIcon(Icons.Default.Share, shareTitle) {
      val send = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
      }
      context.startActivity(Intent.createChooser(send, shareTitle))
    }
    ActionIcon(Icons.Default.Refresh, stringResource(R.string.gen_regenerate), onClick = onRegenerate)
  }
}

@Composable
private fun ActionIcon(icon: ImageVector, label: String, enabled: Boolean = true, onClick: () -> Unit) {
  IconButton(onClick = onClick, enabled = enabled) {
    Icon(icon, contentDescription = label)
  }
}

@Composable
private fun PodcastAudioRow(
  state: GenerationUiState,
  script: String,
  isPlaying: Boolean,
  onSynthesize: (String) -> Unit,
  onPlay: (String) -> Unit,
  onStop: () -> Unit,
  onExport: (String) -> Unit
) {
  val audioPath = state.podcastAudioPath
  when {
    state.isSynthesizingAudio -> Row(verticalAlignment = Alignment.CenterVertically) {
      CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
      Spacer(Modifier.width(10.dp))
      Text(stringResource(R.string.gen_audio_generating), style = MaterialTheme.typography.bodySmall)
    }
    audioPath != null -> Row(verticalAlignment = Alignment.CenterVertically) {
      FilledTonalButton(onClick = { if (isPlaying) onStop() else onPlay(audioPath) }) {
        Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(6.dp))
        Text(stringResource(if (isPlaying) R.string.action_stop else R.string.action_listen))
      }
      ActionIcon(Icons.Default.Download, stringResource(R.string.gen_export_audio)) { onExport(audioPath) }
    }
    else -> FilledTonalButton(onClick = { onSynthesize(script) }) {
      Icon(Icons.Default.Podcasts, contentDescription = null, modifier = Modifier.size(18.dp))
      Spacer(Modifier.width(6.dp))
      Text(stringResource(R.string.gen_audio_generate))
    }
  }
  state.audioError?.let { err ->
    Text(
      stringResource(R.string.gen_audio_error, err),
      style = MaterialTheme.typography.bodySmall,
      color = AppThemeColors.red,
      modifier = Modifier.padding(top = 6.dp)
    )
  }
}

@Composable
private fun Library(
  items: List<SavedGenerationUi>,
  playingAudioPath: String?,
  onPlayAudio: (String) -> Unit,
  onStopAudio: () -> Unit,
  onExportAudio: (String) -> Unit,
  onDelete: (Long) -> Unit
) {
  var pendingDelete by rememberSaveable { mutableStateOf<Long?>(null) }

  Text(
    stringResource(R.string.gen_library_title, items.size),
    style = MaterialTheme.typography.titleMedium,
    fontWeight = FontWeight.Bold
  )
  if (items.isEmpty()) {
    Text(
      stringResource(R.string.gen_library_empty),
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    return
  }

  val dateFormat = remember { DateFormat.getDateInstance(DateFormat.MEDIUM) }
  items.forEach { item ->
    var expanded by rememberSaveable(item.id) { mutableStateOf(false) }
    Card(
      modifier = Modifier.fillMaxWidth().animateContentSize(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
      Column(
        modifier = Modifier
          .clickable { expanded = !expanded }
          .padding(14.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            if (item.format == GenerationFormat.PODCAST_SCRIPT) Icons.Default.Podcasts else Icons.AutoMirrored.Filled.Article,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
          )
          Spacer(Modifier.width(10.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              "${item.level} · ${item.theme}",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            Text(
              listOfNotNull(
                item.grammarPointTitle,
                item.createdTimestamp.takeIf { it > 0 }?.let { dateFormat.format(Date(it)) }
              ).joinToString(" · "),
              style = MaterialTheme.typography.labelMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
          if (item.audioPath != null) {
            val playing = playingAudioPath == item.audioPath
            ActionIcon(
              if (playing) Icons.Default.Pause else Icons.Default.PlayArrow,
              stringResource(if (playing) R.string.action_stop else R.string.action_listen)
            ) { if (playing) onStopAudio() else onPlayAudio(item.audioPath) }
          }
          ActionIcon(Icons.Default.Delete, stringResource(R.string.action_delete)) { pendingDelete = item.id }
        }
        Spacer(Modifier.height(8.dp))
        Text(
          item.text,
          style = MaterialTheme.typography.bodyMedium,
          maxLines = if (expanded) Int.MAX_VALUE else 3,
          overflow = TextOverflow.Ellipsis
        )
        AnimatedVisibility(visible = expanded) {
          Column {
            Spacer(Modifier.height(8.dp))
            ResultActionsForSaved(item = item, onExportAudio = onExportAudio)
          }
        }
        Text(
          stringResource(if (expanded) R.string.action_show_less else R.string.action_show_more),
          style = MaterialTheme.typography.labelLarge,
          color = MaterialTheme.colorScheme.primary,
          modifier = Modifier.padding(top = 6.dp)
        )
      }
    }
  }

  pendingDelete?.let { id ->
    AlertDialog(
      onDismissRequest = { pendingDelete = null },
      title = { Text(stringResource(R.string.gen_delete_title)) },
      text = { Text(stringResource(R.string.gen_delete_message)) },
      confirmButton = {
        TextButton(onClick = { onDelete(id); pendingDelete = null }) {
          Text(stringResource(R.string.action_delete), color = MaterialTheme.colorScheme.error)
        }
      },
      dismissButton = {
        TextButton(onClick = { pendingDelete = null }) { Text(stringResource(R.string.action_cancel)) }
      }
    )
  }
}

@Composable
private fun ResultActionsForSaved(item: SavedGenerationUi, onExportAudio: (String) -> Unit) {
  val clipboard = LocalClipboardManager.current
  val context = LocalContext.current
  val shareTitle = stringResource(R.string.action_share)
  Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
    ActionIcon(Icons.Default.ContentCopy, stringResource(R.string.action_copy)) {
      clipboard.setText(AnnotatedString(item.text))
    }
    ActionIcon(Icons.Default.Share, shareTitle) {
      val send = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, item.text)
      }
      context.startActivity(Intent.createChooser(send, shareTitle))
    }
    item.audioPath?.let { path ->
      ActionIcon(Icons.Default.Download, stringResource(R.string.gen_export_audio)) { onExportAudio(path) }
    }
  }
}

/** Splits Gemini's answer at its "---" line: the text itself, then the notes on where the rule appears. */
private fun splitNotes(text: String): Pair<String, String> {
  val lines = text.lines()
  val separator = lines.indexOfLast { it.trim() == "---" }
  if (separator < 0) return text.trim() to ""
  return lines.take(separator).joinToString("\n").trim() to lines.drop(separator + 1).joinToString("\n").trim()
}

/** Bolds the speaker names ("Camille :", "Nadia :") at the start of podcast lines. */
@Composable
private fun podcastScript(script: String): AnnotatedString {
  val speakerColor = MaterialTheme.colorScheme.primary
  return remember(script, speakerColor) {
    val speaker = Regex("^\\s*\\**([A-ZÀ-Ý][\\p{L}-]+)\\**\\s*:")
    buildAnnotatedString {
      script.lines().forEachIndexed { index, line ->
        if (index > 0) append("\n")
        val match = speaker.find(line)
        if (match != null) {
          withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = speakerColor)) {
            append("${match.groupValues[1]} :")
          }
          append(line.substring(match.range.last + 1))
        } else {
          append(line)
        }
      }
    }
  }
}
