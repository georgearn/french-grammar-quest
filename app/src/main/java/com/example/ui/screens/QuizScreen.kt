package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.grammar.GrammarData
import com.example.ui.QuizSessionState
import com.example.ui.theme.AppThemeColors
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.EmeraldSuccessLight
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.FrenchBlue
import com.example.ui.theme.FrenchRed
import com.example.ui.theme.FrenchRedLight
import com.example.ui.theme.GoldStar

@Composable
fun QuizScreen(
  state: QuizSessionState,
  onSelectOption: (Int) -> Unit,
  onSubmitAnswer: () -> Unit,
  onNextQuestion: () -> Unit,
  onExit: () -> Unit,
  onSpeakFrench: (String) -> Unit,
  onStopAudio: () -> Unit,
  isSpeaking: Boolean,
  modifier: Modifier = Modifier
) {
  if (state.isFinished) {
    QuizFinishedView(
      state = state,
      onContinue = onExit,
      modifier = modifier
    )
    return
  }

  var showRuleModal by remember { mutableStateOf(false) }
  val currentQuestion = state.questions.getOrNull(state.currentIndex)
  val lessonLevel = state.lesson?.level ?: "A1"
  val hasAudio = GrammarData.isAudioAvailableForLevel(lessonLevel)

  Scaffold(
    modifier = modifier.fillMaxSize(),
    contentWindowInsets = WindowInsets.safeDrawing,
    topBar = {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            IconButton(onClick = onExit, modifier = Modifier.testTag("exit_quiz_btn")) {
              Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quitter")
            }

            // Progress bar
            val totalQ = state.questions.size
            val progress = if (totalQ > 0) (state.currentIndex.toFloat() / totalQ.toFloat()) else 0f
            LinearProgressIndicator(
              progress = { progress },
              modifier = Modifier
                .weight(1f)
                .height(10.dp)
                .padding(horizontal = 10.dp)
                .clip(RoundedCornerShape(5.dp)),
              color = AppThemeColors.primary,
              trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            // Hearts remaining
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(end = 4.dp)
            ) {
              Icon(
                Icons.Default.Favorite,
                contentDescription = "Vies",
                tint = AppThemeColors.red,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(3.dp))
              Text(
                text = "${state.heartsLeft}",
                fontWeight = FontWeight.Bold,
                color = AppThemeColors.red,
                style = MaterialTheme.typography.titleMedium
              )
            }

            // Review grammar piece button
            IconButton(
              onClick = { showRuleModal = true },
              modifier = Modifier.testTag("quiz_review_rule_btn")
            ) {
              Icon(
                Icons.Default.MenuBook,
                contentDescription = "Voir la règle",
                tint = AppThemeColors.primary
              )
            }
          }

          // Combo indicator
          if (state.combo > 1) {
            Row(
              modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(12.dp))
                .background(AppThemeColors.flameContainer)
                .padding(horizontal = 10.dp, vertical = 3.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = AppThemeColors.flame, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Combo x${state.combo} 🔥 +${state.combo * 3} XP",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = AppThemeColors.onFlameContainer
              )
            }
          }
        }
      }
    },
    bottomBar = {
      // Bottom Sheet Feedback & Action Button
      val bottomBg = when {
        !state.isAnswered -> MaterialTheme.colorScheme.surface
        state.isCorrect -> AppThemeColors.successContainer
        else -> AppThemeColors.redContainer
      }
      Surface(
        modifier = Modifier.fillMaxWidth(),
        color = bottomBg,
        tonalElevation = 8.dp
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(16.dp)
        ) {
          if (state.isAnswered) {
            val statusColor = if (state.isCorrect) AppThemeColors.success else AppThemeColors.red
            val textStatusColor = if (state.isCorrect) AppThemeColors.onSuccessContainer else AppThemeColors.onRedContainer
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = if (state.isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                  contentDescription = null,
                  tint = statusColor,
                  modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                  text = if (state.isCorrect) "Excellent ! +10 XP" else "Oups !",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = textStatusColor
                )
              }

              if (hasAudio) {
                IconButton(
                  onClick = {
                    currentQuestion?.let { q ->
                      val textToSpeak = "${q.promptFr}. ${q.options.getOrNull(q.correctIndex) ?: ""}"
                      onSpeakFrench(textToSpeak)
                    }
                  }
                ) {
                  Icon(
                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = "Écouter la correction",
                    tint = statusColor
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(6.dp))

            currentQuestion?.let { q ->
              Text(
                text = q.explanation,
                style = MaterialTheme.typography.bodyMedium,
                color = textStatusColor,
                modifier = Modifier.padding(vertical = 4.dp)
              )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
              onClick = onNextQuestion,
              modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("next_question_btn"),
              colors = ButtonDefaults.buttonColors(
                containerColor = statusColor,
                contentColor = Color.White
              ),
              shape = RoundedCornerShape(12.dp)
            ) {
              Text(
                text = "Continuer",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
              )
            }
          } else {
            Button(
              onClick = onSubmitAnswer,
              enabled = state.selectedOption != null,
              modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("submit_quiz_answer_btn"),
              colors = ButtonDefaults.buttonColors(
                containerColor = AppThemeColors.primary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
              ),
              shape = RoundedCornerShape(12.dp)
            ) {
              Text(
                text = "Vérifier",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 20.dp)
        .verticalScroll(rememberScrollState()),
      verticalArrangement = Arrangement.Center
    ) {
      if (currentQuestion != null) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Question Level Tag
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = AppThemeColors.primaryContainer
          ) {
            Text(
              text = "Question ${state.currentIndex + 1} / ${state.questions.size}",
              color = AppThemeColors.onPrimaryContainer,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }

          // Audio listen button for topics >= A2
          if (hasAudio) {
            FilledTonalButton(
              onClick = { onSpeakFrench(currentQuestion.promptFr) },
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
              shape = RoundedCornerShape(16.dp),
              modifier = Modifier.testTag("quiz_audio_btn")
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = "Écouter",
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text("Écouter (A2+)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Prompt
        Text(
          text = currentQuestion.promptFr,
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          lineHeight = 28.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = currentQuestion.promptEn,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(26.dp))

        // Options
        currentQuestion.options.forEachIndexed { index, optionText ->
          val isSelected = state.selectedOption == index
          val isCorrectOption = index == currentQuestion.correctIndex

          val cardBorderColor = when {
            state.isAnswered && isCorrectOption -> AppThemeColors.success
            state.isAnswered && isSelected && !state.isCorrect -> AppThemeColors.red
            isSelected -> AppThemeColors.primary
            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
          }

          val cardBackground = when {
            state.isAnswered && isCorrectOption -> AppThemeColors.successContainer.copy(alpha = 0.45f)
            state.isAnswered && isSelected && !state.isCorrect -> AppThemeColors.redContainer.copy(alpha = 0.45f)
            isSelected -> AppThemeColors.primaryContainer.copy(alpha = 0.45f)
            else -> MaterialTheme.colorScheme.surface
          }

          val badgeBg = when {
            state.isAnswered && isCorrectOption -> AppThemeColors.success
            state.isAnswered && isSelected && !state.isCorrect -> AppThemeColors.red
            isSelected -> AppThemeColors.primary
            else -> MaterialTheme.colorScheme.surfaceVariant
          }

          val badgeTextColor = when {
            isSelected || (state.isAnswered && (isCorrectOption || isSelected)) -> Color.White
            else -> MaterialTheme.colorScheme.onSurfaceVariant
          }

          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 6.dp)
              .clip(RoundedCornerShape(14.dp))
              .border(
                width = if (isSelected || (state.isAnswered && isCorrectOption)) 2.5.dp else 1.dp,
                color = cardBorderColor,
                shape = RoundedCornerShape(14.dp)
              )
              .clickable(enabled = !state.isAnswered) { onSelectOption(index) }
              .testTag("quiz_option_$index"),
            colors = CardDefaults.cardColors(containerColor = cardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 0.dp)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(28.dp)
                  .clip(CircleShape)
                  .background(badgeBg),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = ('A' + index).toString(),
                  color = badgeTextColor,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp
                )
              }

              Spacer(modifier = Modifier.width(14.dp))

              Text(
                text = optionText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
              )

              if (hasAudio) {
                IconButton(
                  onClick = { onSpeakFrench(optionText) },
                  modifier = Modifier.size(32.dp)
                ) {
                  Icon(
                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = "Écouter l'option",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                  )
                }
              }
            }
          }
        }
      }
    }
  }

  // Quick Grammar Review Modal
  if (showRuleModal && state.lesson != null) {
    val lesson = state.lesson
    AlertDialog(
      onDismissRequest = { showRuleModal = false },
      title = {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.fillMaxWidth()
        ) {
          Column {
            Text(
              text = lesson.frTitle,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Fiche récapitulative • ${lesson.level}",
              style = MaterialTheme.typography.bodySmall,
              color = AppThemeColors.primary
            )
          }
          if (hasAudio) {
            IconButton(
              onClick = { onSpeakFrench(lesson.ruleFr) },
              modifier = Modifier.background(AppThemeColors.primaryContainer, CircleShape)
            ) {
              Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Écouter", tint = AppThemeColors.onPrimaryContainer)
            }
          }
        }
      },
      text = {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
        ) {
          Text(
            text = lesson.ruleFr,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = lesson.ruleEn,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          if (lesson.examples.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "Exemple clé :",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = AppThemeColors.primary
            )
            val firstEx = lesson.examples.first()
            Text(
              text = "• ${firstEx.fr} (${firstEx.en})",
              style = MaterialTheme.typography.bodySmall
            )
          }

          lesson.note?.let { note ->
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = AppThemeColors.flameContainer,
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = "⚠️ $note",
                style = MaterialTheme.typography.bodySmall,
                color = AppThemeColors.onFlameContainer,
                modifier = Modifier.padding(8.dp)
              )
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = { showRuleModal = false },
          colors = ButtonDefaults.buttonColors(containerColor = AppThemeColors.primary)
        ) {
          Text("Reprendre l'exercice")
        }
      }
    )
  }
}

@Composable
private fun QuizFinishedView(
  state: QuizSessionState,
  onContinue: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .statusBarsPadding()
      .navigationBarsPadding()
      .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Text(
      text = when (state.starsEarned) {
        3 -> "🏆 Défi Accompli !"
        2 -> "🎉 Très Bon Travail !"
        1 -> "👍 Défi Réussi !"
        else -> "💪 Continuez de Vous Entraîner !"
      },
      style = MaterialTheme.typography.headlineMedium,
      fontWeight = FontWeight.ExtraBold,
      color = AppThemeColors.primary,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = "${state.score} / ${state.questions.size} bonnes réponses",
      style = MaterialTheme.typography.titleMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(24.dp))

    // Stars Display
    Row(
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(16.dp)
    ) {
      repeat(3) { starIndex ->
        val isEarned = starIndex < state.starsEarned
        Icon(
          imageVector = Icons.Default.Star,
          contentDescription = null,
          tint = if (isEarned) AppThemeColors.gold else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
          modifier = Modifier
            .size(if (starIndex == 1) 64.dp else 48.dp)
            .padding(4.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Reward Cards
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceEvenly
    ) {
      RewardMetricCard(
        label = "XP GAGNÉS",
        value = "+${state.xpEarned}",
        containerColor = AppThemeColors.primaryContainer,
        contentColor = AppThemeColors.onPrimaryContainer
      )
      RewardMetricCard(
        label = "MAX COMBO",
        value = "${state.maxCombo} 🔥",
        containerColor = AppThemeColors.flameContainer,
        contentColor = AppThemeColors.onFlameContainer
      )
    }

    Spacer(modifier = Modifier.height(40.dp))

    Button(
      onClick = onContinue,
      modifier = Modifier
        .fillMaxWidth()
        .height(54.dp)
        .testTag("finish_continue_btn"),
      colors = ButtonDefaults.buttonColors(containerColor = AppThemeColors.primary),
      shape = RoundedCornerShape(14.dp)
    ) {
      Text("Continuer la quête", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
  }
}

@Composable
private fun RewardMetricCard(
  label: String,
  value: String,
  containerColor: Color,
  contentColor: Color
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = containerColor),
    modifier = Modifier.width(130.dp)
  ) {
    Column(
      modifier = Modifier.padding(14.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = label,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = contentColor
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = value,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.ExtraBold,
        color = contentColor
      )
    }
  }
}
