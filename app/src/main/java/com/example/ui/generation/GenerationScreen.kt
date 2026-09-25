package com.example.ui.generation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Podcasts
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.engine.GenerationFormat
import com.example.data.engine.StructuredRule
import com.example.ui.theme.AppThemeColors

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
  val audioPath: String?
)

/**
 * Explicit, theme-aware text colors for every editable field on this screen. OutlinedTextField's
 * own defaults should already track the theme, but this screen showed up unreadable (dark text
 * regardless of background) so every field here pins its colors instead of trusting the default.
 */
@Composable
private fun readableOutlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
  focusedTextColor = MaterialTheme.colorScheme.onSurface,
  unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
  disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
  cursorColor = AppThemeColors.primary,
  focusedBorderColor = AppThemeColors.primary,
  unfocusedBorderColor = MaterialTheme.colorScheme.outline,
  focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
  unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
  focusedLabelColor = AppThemeColors.primary,
  unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerationScreen(
  rules: List<StructuredRule>,
  state: GenerationUiState,
  apiKey: String,
  onSaveApiKey: (String) -> Unit,
  onGenerate: (level: String, ruleId: String, theme: String, format: GenerationFormat) -> Unit,
  onSpeakFrench: (String) -> Unit,
  onStopAudio: () -> Unit,
  isSpeaking: Boolean,
  onSynthesizePodcastAudio: (String) -> Unit,
  onPlayPodcastAudio: (String) -> Unit,
  onStopPodcastAudio: () -> Unit,
  isPodcastPlaying: Boolean,
  onSaveGeneration: () -> Unit,
  savedGenerations: List<SavedGenerationUi> = emptyList(),
  onPlaySavedAudio: (String) -> Unit = {},
  onDeleteSavedGeneration: (Long) -> Unit = {},
  playingSavedAudioPath: String? = null,
  onExportAudioToDevice: (String) -> Unit = {},
  modifier: Modifier = Modifier
) {
  val levels = listOf("A1", "A2", "B1", "B2", "C1", "C2")
  var selectedLevel by remember { mutableStateOf("B1") }
  var selectedRule by remember { mutableStateOf(rules.firstOrNull()) }
  var theme by remember { mutableStateOf("") }
  var format by remember { mutableStateOf(GenerationFormat.TEXT) }
  var ruleMenuExpanded by remember { mutableStateOf(false) }

  var apiKeyDraft by remember(apiKey) { mutableStateOf(apiKey) }
  var apiKeyVisible by remember { mutableStateOf(false) }
  val fieldColors = readableOutlinedTextFieldColors()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .verticalScroll(rememberScrollState())
      .padding(16.dp)
  ) {
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
      Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = AppThemeColors.primary)
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            "Génération sur mesure (Gemini)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            "Choisis un niveau, une règle et un thème : Gemini écrit un texte ou un script de podcast (~5 min à l'oral) qui l'illustre.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // --- Your own Gemini API key ---
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(Icons.Default.Key, contentDescription = null, tint = AppThemeColors.primary, modifier = Modifier.height(16.dp))
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        "Ta clé API Google AI",
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
    }
    Spacer(modifier = Modifier.height(6.dp))
    Text(
      "Génère une clé gratuite sur aistudio.google.com/apikey. Elle reste stockée uniquement sur cet appareil.",
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
      value = apiKeyDraft,
      onValueChange = { apiKeyDraft = it },
      placeholder = { Text("AIza...") },
      singleLine = true,
      visualTransformation = if (apiKeyVisible) VisualTransformation.None else PasswordVisualTransformation(),
      trailingIcon = {
        IconButton(onClick = { apiKeyVisible = !apiKeyVisible }) {
          Icon(
            imageVector = if (apiKeyVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
            contentDescription = if (apiKeyVisible) "Masquer la clé" else "Afficher la clé"
          )
        }
      },
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(12.dp),
      colors = fieldColors
    )
    Spacer(modifier = Modifier.height(8.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      Button(
        onClick = { onSaveApiKey(apiKeyDraft) },
        enabled = apiKeyDraft.trim() != apiKey,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = AppThemeColors.primary)
      ) {
        Text("Enregistrer la clé", fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
      }
      if (apiKey.isNotBlank()) {
        TextButton(onClick = { apiKeyDraft = ""; onSaveApiKey("") }) {
          Text("Retirer", color = AppThemeColors.red)
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    Text(
      "Niveau CECRL",
      style = MaterialTheme.typography.labelLarge,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(modifier = Modifier.height(6.dp))
    Row(
      modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      levels.forEach { lvl ->
        FilterChip(
          selected = selectedLevel == lvl,
          onClick = { selectedLevel = lvl },
          label = { Text(lvl, color = if (selectedLevel == lvl) Color.White else MaterialTheme.colorScheme.onSurface) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = AppThemeColors.primary,
            selectedLabelColor = Color.White,
            containerColor = MaterialTheme.colorScheme.surface,
            labelColor = MaterialTheme.colorScheme.onSurface
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    Text(
      "Règle de grammaire ciblée",
      style = MaterialTheme.typography.labelLarge,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(modifier = Modifier.height(6.dp))
    ExposedDropdownMenuBox(
      expanded = ruleMenuExpanded,
      onExpandedChange = { ruleMenuExpanded = it }
    ) {
      OutlinedTextField(
        value = selectedRule?.titleFr ?: "Sélectionner une règle",
        onValueChange = {},
        readOnly = true,
        modifier = Modifier.fillMaxWidth().menuAnchor(),
        shape = RoundedCornerShape(12.dp),
        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = ruleMenuExpanded) },
        colors = fieldColors
      )
      ExposedDropdownMenu(
        expanded = ruleMenuExpanded,
        onDismissRequest = { ruleMenuExpanded = false }
      ) {
        rules.forEach { rule ->
          DropdownMenuItem(
            text = { Text("[${rule.level}] ${rule.titleFr}", color = MaterialTheme.colorScheme.onSurface) },
            onClick = {
              selectedRule = rule
              ruleMenuExpanded = false
            }
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    Text(
      "Thème libre",
      style = MaterialTheme.typography.labelLarge,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(modifier = Modifier.height(6.dp))
    OutlinedTextField(
      value = theme,
      onValueChange = { theme = it },
      placeholder = { Text("ex : le télétravail, la pollution lumineuse, un entretien d'embauche...") },
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(12.dp),
      colors = fieldColors
    )

    Spacer(modifier = Modifier.height(18.dp))

    Text(
      "Format",
      style = MaterialTheme.typography.labelLarge,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(modifier = Modifier.height(6.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      FilterChip(
        selected = format == GenerationFormat.TEXT,
        onClick = { format = GenerationFormat.TEXT },
        label = { Text("Texte court", color = if (format == GenerationFormat.TEXT) Color.White else MaterialTheme.colorScheme.onSurface) },
        colors = FilterChipDefaults.filterChipColors(
          selectedContainerColor = AppThemeColors.primary,
          containerColor = MaterialTheme.colorScheme.surface
        )
      )
      FilterChip(
        selected = format == GenerationFormat.PODCAST_SCRIPT,
        onClick = { format = GenerationFormat.PODCAST_SCRIPT },
        label = { Text("Podcast (script, ~5 min)", color = if (format == GenerationFormat.PODCAST_SCRIPT) Color.White else MaterialTheme.colorScheme.onSurface) },
        leadingIcon = { Icon(Icons.Default.Podcasts, contentDescription = null, modifier = Modifier.height(16.dp)) },
        colors = FilterChipDefaults.filterChipColors(
          selectedContainerColor = AppThemeColors.primary,
          containerColor = MaterialTheme.colorScheme.surface
        )
      )
    }

    Spacer(modifier = Modifier.height(20.dp))

    Button(
      onClick = {
        val rule = selectedRule ?: return@Button
        onGenerate(selectedLevel, rule.id, theme.ifBlank { "sujet libre" }, format)
      },
      enabled = !state.isLoading && selectedRule != null,
      modifier = Modifier.fillMaxWidth().height(52.dp),
      shape = RoundedCornerShape(14.dp),
      colors = ButtonDefaults.buttonColors(containerColor = AppThemeColors.primary)
    ) {
      if (state.isLoading) {
        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
        Spacer(modifier = Modifier.width(10.dp))
        Text("Génération en cours...", fontWeight = FontWeight.Bold, color = Color.White)
      } else {
        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Générer", fontWeight = FontWeight.Bold, color = Color.White)
      }
    }

    state.error?.let { err ->
      Spacer(modifier = Modifier.height(14.dp))
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = AppThemeColors.redContainer,
        border = BorderStroke(1.dp, AppThemeColors.red)
      ) {
        Text(
          text = "Erreur : $err",
          modifier = Modifier.padding(12.dp),
          color = AppThemeColors.onRedContainer,
          style = MaterialTheme.typography.bodySmall
        )
      }
    }

    state.resultText?.let { text ->
      Spacer(modifier = Modifier.height(18.dp))
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              "Contenu généré",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              if (state.format != GenerationFormat.PODCAST_SCRIPT) {
                Button(
                  onClick = { if (isSpeaking) onStopAudio() else onSpeakFrench(text) },
                  shape = RoundedCornerShape(20.dp),
                  colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSpeaking) AppThemeColors.redContainer else AppThemeColors.primaryContainer,
                    contentColor = if (isSpeaking) AppThemeColors.onRedContainer else AppThemeColors.onPrimaryContainer
                  )
                ) {
                  Icon(if (isSpeaking) Icons.Default.Pause else Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.height(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(if (isSpeaking) "Arrêter" else "Écouter", fontWeight = FontWeight.Bold)
                }
              }
              Button(
                onClick = onSaveGeneration,
                enabled = !state.isSaved,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                  containerColor = if (state.isSaved) MaterialTheme.colorScheme.surfaceVariant else AppThemeColors.primaryContainer,
                  contentColor = if (state.isSaved) MaterialTheme.colorScheme.onSurfaceVariant else AppThemeColors.onPrimaryContainer
                )
              ) {
                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.height(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (state.isSaved) "Enregistré" else "Enregistrer", fontWeight = FontWeight.Bold)
              }
            }
          }

          // Podcast format: real two-voice audio synthesized by Gemini's TTS models, instead of
          // the flat single-voice device TTS readback used for plain-text generations above.
          if (state.format == GenerationFormat.PODCAST_SCRIPT) {
            Spacer(modifier = Modifier.height(10.dp))
            when {
              state.isSynthesizingAudio -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = AppThemeColors.primary)
                  Spacer(modifier = Modifier.width(10.dp))
                  Text(
                    "Génération des voix en cours...",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
              state.podcastAudioPath != null -> {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                  Button(
                    onClick = {
                      if (isPodcastPlaying) onStopPodcastAudio() else onPlayPodcastAudio(state.podcastAudioPath)
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                      containerColor = if (isPodcastPlaying) AppThemeColors.redContainer else AppThemeColors.primaryContainer,
                      contentColor = if (isPodcastPlaying) AppThemeColors.onRedContainer else AppThemeColors.onPrimaryContainer
                    )
                  ) {
                    Icon(if (isPodcastPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.height(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isPodcastPlaying) "Arrêter" else "Écouter", fontWeight = FontWeight.Bold)
                  }
                  IconButton(onClick = { onExportAudioToDevice(state.podcastAudioPath) }) {
                    Icon(Icons.Default.Download, contentDescription = "Enregistrer dans Téléchargements", tint = AppThemeColors.primary)
                  }
                }
              }
              else -> {
                Button(
                  onClick = { onSynthesizePodcastAudio(text) },
                  shape = RoundedCornerShape(20.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = AppThemeColors.primaryContainer, contentColor = AppThemeColors.onPrimaryContainer)
                ) {
                  Icon(Icons.Default.Podcasts, contentDescription = null, modifier = Modifier.height(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Générer l'audio (voix naturelles)", fontWeight = FontWeight.Bold)
                }
              }
            }
            state.audioError?.let { audioErr ->
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                "Erreur audio : $audioErr",
                style = MaterialTheme.typography.bodySmall,
                color = AppThemeColors.red
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 22.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }

    if (savedGenerations.isNotEmpty()) {
      Spacer(modifier = Modifier.height(28.dp))
      Text(
        "Mes contenus enregistrés",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(8.dp))
      savedGenerations.forEach { saved ->
        var expanded by remember(saved.id) { mutableStateOf(false) }
        Card(
          modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
          Column(
            modifier = Modifier
              .padding(12.dp)
              .clickable { expanded = !expanded }
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  "[${saved.level}] ${saved.theme}",
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  saved.grammarPointTitle,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
              if (saved.audioPath != null) {
                IconButton(onClick = {
                  if (playingSavedAudioPath == saved.audioPath) onStopPodcastAudio() else onPlaySavedAudio(saved.audioPath)
                }) {
                  Icon(
                    if (playingSavedAudioPath == saved.audioPath) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = AppThemeColors.primary
                  )
                }
                IconButton(onClick = { onExportAudioToDevice(saved.audioPath) }) {
                  Icon(Icons.Default.Download, contentDescription = "Enregistrer dans Téléchargements", tint = AppThemeColors.primary)
                }
              }
              IconButton(onClick = { onDeleteSavedGeneration(saved.id) }) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = AppThemeColors.red)
              }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              saved.text,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurface,
              maxLines = if (expanded) Int.MAX_VALUE else 4
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              if (expanded) "Réduire ▲" else "Voir plus ▼",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = AppThemeColors.primary
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(32.dp))
  }
}
