package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.grammar.GrammarData
import com.example.ui.theme.AppThemeColors
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.FrenchBlue
import com.example.ui.theme.GoldStar

@Composable
fun LessonDetailScreen(
  lessonId: String,
  isBookmarked: Boolean,
  onToggleBookmark: () -> Unit,
  onStartQuiz: (String) -> Unit,
  onBack: () -> Unit,
  onSpeakFrench: (String) -> Unit,
  onStopAudio: () -> Unit,
  isSpeaking: Boolean,
  modifier: Modifier = Modifier
) {
  val lesson = GrammarData.getLesson(lessonId)

  if (lesson == null) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Text("Leçon introuvable.")
    }
    return
  }

  val hasAudio = GrammarData.isAudioAvailableForLevel(lesson.level)

  Scaffold(
    modifier = modifier.fillMaxSize(),
    contentWindowInsets = WindowInsets.safeDrawing,
    topBar = {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          IconButton(onClick = onBack, modifier = Modifier.testTag("detail_back_btn")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = AppThemeColors.primaryContainer
            ) {
              Text(
                text = "NIVEAU ${lesson.level}",
                color = AppThemeColors.onPrimaryContainer,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
              )
            }

            if (hasAudio) {
              Spacer(modifier = Modifier.width(8.dp))
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = AppThemeColors.successContainer
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = Icons.Default.Headphones,
                    contentDescription = null,
                    tint = AppThemeColors.onSuccessContainer,
                    modifier = Modifier.size(14.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "Audio",
                    color = AppThemeColors.onSuccessContainer,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                  )
                }
              }
            }
          }

          IconButton(onClick = onToggleBookmark, modifier = Modifier.testTag("detail_bookmark_btn")) {
            Icon(
              imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
              contentDescription = "Signet",
              tint = if (isBookmarked) AppThemeColors.gold else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    },
    bottomBar = {
      Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier
            .navigationBarsPadding()
            .padding(16.dp)
        ) {
          Button(
            onClick = {
              onStopAudio()
              onStartQuiz(lesson.id)
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(54.dp)
              .testTag("detail_start_quiz_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = AppThemeColors.primary),
            shape = RoundedCornerShape(14.dp)
          ) {
            Text(
              text = "Passer aux exercices (5 questions)",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(20.dp))
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Étape 2 : Validez votre apprentissage et remportez jusqu'à 3 étoiles ⭐",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally)
          )
        }
      }
    }
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
      contentPadding = PaddingValues(18.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Step 1 Header & Title
      item {
        Column {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = AppThemeColors.primaryContainer,
            modifier = Modifier.padding(bottom = 6.dp)
          ) {
            Text(
              text = "ÉTAPE 1 SUR 2 : COMPRENDRE LA RÈGLE",
              color = AppThemeColors.onPrimaryContainer,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
              letterSpacing = 1.sp,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
          }
          Text(
            text = lesson.frTitle,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = lesson.enSub,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      // Audio Banner for complex topics (>= A2)
      if (hasAudio) {
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = AppThemeColors.primaryContainer.copy(alpha = 0.5f)),
            modifier = Modifier.border(1.dp, AppThemeColors.primary.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(42.dp)
                  .background(AppThemeColors.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Headphones,
                  contentDescription = null,
                  tint = AppThemeColors.onPrimaryContainer,
                  modifier = Modifier.size(24.dp)
                )
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "Écoute audio native (Niveau ${lesson.level})",
                  style = MaterialTheme.typography.labelMedium,
                  fontWeight = FontWeight.Bold,
                  color = AppThemeColors.onPrimaryContainer
                )
                Text(
                  text = "Appuyez sur les haut-parleurs 🔊 pour écouter la prononciation et affûter votre oreille !",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }

      // Rule Card
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Lightbulb, contentDescription = null, tint = AppThemeColors.primary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "RÈGLE DE GRAMMAIRE",
                  style = MaterialTheme.typography.labelMedium,
                  fontWeight = FontWeight.Bold,
                  color = AppThemeColors.primary,
                  letterSpacing = 1.sp
                )
              }

              if (hasAudio) {
                FilledTonalButton(
                  onClick = {
                    if (isSpeaking) onStopAudio() else onSpeakFrench(lesson.ruleFr)
                  },
                  contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                  shape = RoundedCornerShape(16.dp),
                  modifier = Modifier.testTag("listen_rule_btn")
                ) {
                  Icon(
                    imageVector = if (isSpeaking) Icons.Default.Stop else Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = "Écouter la règle",
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(if (isSpeaking) "Arrêter" else "Écouter", fontSize = 12.sp)
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = lesson.ruleFr,
              style = MaterialTheme.typography.bodyLarge,
              fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = lesson.ruleEn,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      // Table if present
      if (lesson.tableRows.isNotEmpty()) {
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              lesson.tableTitle?.let {
                Text(
                  text = it,
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold,
                  color = AppThemeColors.primary,
                  modifier = Modifier.padding(bottom = 8.dp)
                )
              }

              // Headers
              if (lesson.tableHeaders.isNotEmpty()) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .background(AppThemeColors.primaryContainer, RoundedCornerShape(6.dp))
                    .padding(8.dp),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  lesson.tableHeaders.forEach { header ->
                    Text(
                      text = header,
                      style = MaterialTheme.typography.labelMedium,
                      fontWeight = FontWeight.Bold,
                      color = AppThemeColors.onPrimaryContainer,
                      modifier = Modifier.weight(1f)
                    )
                  }
                }
              }

              // Rows
              lesson.tableRows.forEachIndexed { idx, row ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .background(if (idx % 2 == 1) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else Color.Transparent)
                    .padding(8.dp),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(row.col1, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                  Text(row.col2, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                  row.col3?.let {
                    Text(it, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                  }
                  row.col4?.let {
                    Text(it, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                  }
                }
              }
            }
          }
        }
      }

      // Examples with Audio Listen buttons
      if (lesson.examples.isNotEmpty()) {
        item {
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Exemples concrets",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
              if (hasAudio) {
                Text(
                  text = "🔊 Touchez pour écouter",
                  style = MaterialTheme.typography.labelSmall,
                  color = AppThemeColors.primary,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            lesson.examples.forEachIndexed { exIndex, ex ->
              Card(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
              ) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Column(modifier = Modifier.weight(1f)) {
                    Text(
                      text = ex.fr,
                      style = MaterialTheme.typography.bodyLarge,
                      fontWeight = FontWeight.Bold,
                      color = AppThemeColors.primary
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                      text = ex.en,
                      style = MaterialTheme.typography.bodyMedium,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }

                  if (hasAudio) {
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                      onClick = { onSpeakFrench(ex.fr) },
                      modifier = Modifier
                        .size(40.dp)
                        .background(AppThemeColors.primaryContainer, CircleShape)
                        .testTag("play_example_audio_$exIndex")
                    ) {
                      Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Écouter l'exemple",
                        tint = AppThemeColors.onPrimaryContainer,
                        modifier = Modifier.size(20.dp)
                      )
                    }
                  }
                }
              }
            }
          }
        }
      }

      // Attention / Traps Note
      lesson.note?.let { noteText ->
        item {
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = AppThemeColors.flameContainer),
            modifier = Modifier.border(1.dp, AppThemeColors.flame.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
          ) {
            Row(modifier = Modifier.padding(14.dp)) {
              Icon(Icons.Default.Warning, contentDescription = null, tint = AppThemeColors.flame, modifier = Modifier.size(22.dp))
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "ATTENTION / PIÈGE FRÉQUENT",
                  style = MaterialTheme.typography.labelMedium,
                  fontWeight = FontWeight.Bold,
                  color = AppThemeColors.onFlameContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = noteText,
                  style = MaterialTheme.typography.bodyMedium,
                  color = AppThemeColors.onFlameContainer
                )
              }
            }
          }
        }
      }
    }
  }
}
