package com.example.ui.exploratory

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.data.engine.GenerationFormat
import com.example.data.engine.StructuredRule
import com.example.ui.generation.GenerationScreen
import com.example.ui.generation.GenerationUiState
import com.example.ui.generation.SavedGenerationUi
import com.example.ui.theme.AppThemeColors

/**
 * Hosts the two sub-modes of "Exploration": the rule-by-rule comprehension path
 * (understand one rule at a time, with examples, then move to the next) and the
 * Gemini-powered generation tab (text / podcast script on demand).
 */
@Composable
fun ExplorationContainer(
  selectedRuleId: String,
  onSelectRule: (String) -> Unit,
  onStartDrillForRule: (String) -> Unit,
  onOpenIndex: () -> Unit,
  allRules: List<StructuredRule>,
  generationState: GenerationUiState,
  geminiApiKey: String,
  onSaveGeminiApiKey: (String) -> Unit,
  onGenerate: (level: String, ruleId: String, theme: String, format: GenerationFormat) -> Unit,
  onSpeakFrench: (String) -> Unit,
  onStopAudio: () -> Unit,
  isSpeaking: Boolean,
  onSynthesizePodcastAudio: (String) -> Unit,
  onPlayPodcastAudio: (String) -> Unit,
  onStopPodcastAudio: () -> Unit,
  isPodcastPlaying: Boolean,
  onSaveGeneration: () -> Unit,
  savedGenerations: List<SavedGenerationUi>,
  onPlaySavedAudio: (String) -> Unit,
  onDeleteSavedGeneration: (Long) -> Unit,
  playingSavedAudioPath: String?,
  onExportAudioToDevice: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var tabIndex by rememberSaveable { mutableIntStateOf(0) }

  Column(modifier = modifier.fillMaxSize()) {
    TabRow(
      selectedTabIndex = tabIndex,
      containerColor = MaterialTheme.colorScheme.surface,
      contentColor = AppThemeColors.primary
    ) {
      Tab(
        selected = tabIndex == 0,
        onClick = { tabIndex = 0 },
        text = { Text("Comprendre", fontWeight = if (tabIndex == 0) FontWeight.Bold else FontWeight.Normal) },
        icon = { Icon(Icons.Default.MenuBook, contentDescription = null) }
      )
      Tab(
        selected = tabIndex == 1,
        onClick = { tabIndex = 1 },
        text = { Text("Génération", fontWeight = if (tabIndex == 1) FontWeight.Bold else FontWeight.Normal) },
        icon = { Icon(Icons.Default.AutoAwesome, contentDescription = null) }
      )
    }

    val contentModifier = Modifier.weight(1f)
    when (tabIndex) {
      0 -> ExploratoryPathScreen(
        selectedRuleId = selectedRuleId,
        onSelectRule = onSelectRule,
        onStartDrillForRule = onStartDrillForRule,
        onSpeakFrench = onSpeakFrench,
        onStopAudio = onStopAudio,
        isSpeaking = isSpeaking,
        onOpenIndex = onOpenIndex,
        modifier = contentModifier
      )
      else -> GenerationScreen(
        rules = allRules,
        state = generationState,
        apiKey = geminiApiKey,
        onSaveApiKey = onSaveGeminiApiKey,
        onGenerate = onGenerate,
        onSpeakFrench = onSpeakFrench,
        onStopAudio = onStopAudio,
        isSpeaking = isSpeaking,
        onSynthesizePodcastAudio = onSynthesizePodcastAudio,
        onPlayPodcastAudio = onPlayPodcastAudio,
        onStopPodcastAudio = onStopPodcastAudio,
        isPodcastPlaying = isPodcastPlaying,
        onSaveGeneration = onSaveGeneration,
        savedGenerations = savedGenerations,
        onPlaySavedAudio = onPlaySavedAudio,
        onDeleteSavedGeneration = onDeleteSavedGeneration,
        playingSavedAudioPath = playingSavedAudioPath,
        onExportAudioToDevice = onExportAudioToDevice,
        modifier = contentModifier
      )
    }
  }
}
