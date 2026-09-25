package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.SpeedDrillState
import com.example.ui.theme.AppThemeColors
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.FrenchBlue
import com.example.ui.theme.GoldStar

@Composable
fun SpeedDrillScreen(
  state: SpeedDrillState,
  highScore: Int,
  onStartDrill: () -> Unit,
  onAnswerQuestion: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  if (!state.isActive && !state.isFinished) {
    // Start Screen
    SpeedDrillIntroView(
      highScore = highScore,
      onStart = onStartDrill,
      modifier = modifier
    )
    return
  }

  if (state.isFinished) {
    // Result Screen
    SpeedDrillFinishedView(
      score = state.score,
      maxCombo = state.maxCombo,
      highScore = highScore,
      onPlayAgain = onStartDrill,
      modifier = modifier
    )
    return
  }

  // Active Time Attack Gameplay
  val currentQ = state.questions.getOrNull(state.currentIndex)

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(20.dp)
      .testTag("speed_drill_screen")
  ) {
    // Timer & Score Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Timer Pill
      val isLowTime = state.secondsRemaining <= 10
      val timerBg = if (isLowTime) AppThemeColors.redContainer else AppThemeColors.primaryContainer
      val timerFg = if (isLowTime) AppThemeColors.onRedContainer else AppThemeColors.onPrimaryContainer

      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clip(RoundedCornerShape(20.dp))
          .background(timerBg)
          .padding(horizontal = 14.dp, vertical = 8.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Timer,
          contentDescription = null,
          tint = timerFg,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "${state.secondsRemaining}s",
          fontWeight = FontWeight.ExtraBold,
          style = MaterialTheme.typography.titleMedium,
          color = timerFg
        )
      }

      // Score counter
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clip(RoundedCornerShape(20.dp))
          .background(AppThemeColors.goldContainer)
          .padding(horizontal = 14.dp, vertical = 8.dp)
      ) {
        Icon(Icons.Default.Bolt, contentDescription = null, tint = AppThemeColors.onGoldContainer, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "${state.score} pts",
          fontWeight = FontWeight.Bold,
          style = MaterialTheme.typography.titleMedium,
          color = AppThemeColors.onGoldContainer
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Time Progress Bar
    val isLowTime = state.secondsRemaining <= 10
    val timeProgress = (state.secondsRemaining / 60f).coerceIn(0f, 1f)
    LinearProgressIndicator(
      progress = { timeProgress },
      modifier = Modifier
        .fillMaxWidth()
        .height(6.dp)
        .clip(RoundedCornerShape(3.dp)),
      color = if (isLowTime) AppThemeColors.red else AppThemeColors.primary,
      trackColor = MaterialTheme.colorScheme.surfaceVariant
    )

    Spacer(modifier = Modifier.height(20.dp))

    // Combo streak badge
    if (state.combo > 1) {
      Row(
        modifier = Modifier
          .align(Alignment.CenterHorizontally)
          .clip(RoundedCornerShape(12.dp))
          .background(AppThemeColors.flameContainer)
          .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = AppThemeColors.onFlameContainer, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "Combo x${state.combo} !",
          fontWeight = FontWeight.Bold,
          color = AppThemeColors.onFlameContainer,
          fontSize = 13.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Question Card
    if (currentQ != null) {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
          verticalArrangement = Arrangement.Center
        ) {
          Text(
            text = currentQ.promptFr,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = currentQ.promptEn,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(30.dp))

          // Fast Options
          currentQ.options.forEachIndexed { index, option ->
            Button(
              onClick = { onAnswerQuestion(index) },
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
                .height(52.dp)
                .testTag("speed_option_$index"),
              colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurface
              ),
              shape = RoundedCornerShape(12.dp)
            ) {
              Text(
                text = option,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun SpeedDrillIntroView(
  highScore: Int,
  onStart: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Box(
      modifier = Modifier
        .size(90.dp)
        .clip(CircleShape)
        .background(
          brush = Brush.radialGradient(listOf(GoldStar, FlameOrange))
        ),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.Bolt,
        contentDescription = null,
        tint = Color.White,
        modifier = Modifier.size(54.dp)
      )
    }

    Spacer(modifier = Modifier.height(20.dp))

    Text(
      text = "DÉFI ÉCLAIR (60s)",
      style = MaterialTheme.typography.headlineMedium,
      fontWeight = FontWeight.ExtraBold,
      color = MaterialTheme.colorScheme.onBackground
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = "Testez vos réflexes grammaticaux ! Répondez à un maximum de questions de grammaire en 60 secondes chrono.",
      style = MaterialTheme.typography.bodyMedium,
      textAlign = TextAlign.Center,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.padding(horizontal = 16.dp)
    )

    Spacer(modifier = Modifier.height(24.dp))

    Card(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
      modifier = Modifier.fillMaxWidth(0.8f)
    ) {
      Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = AppThemeColors.gold, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(text = "Meilleur Score", style = MaterialTheme.typography.labelMedium)
          Text(
            text = "$highScore points",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = AppThemeColors.gold
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(36.dp))

    Button(
      onClick = onStart,
      modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .testTag("start_drill_btn"),
      colors = ButtonDefaults.buttonColors(containerColor = AppThemeColors.primary),
      shape = RoundedCornerShape(14.dp)
    ) {
      Text("Lancer le chrono (60s)", fontSize = 17.sp, fontWeight = FontWeight.Bold)
    }
  }
}

@Composable
private fun SpeedDrillFinishedView(
  score: Int,
  maxCombo: Int,
  highScore: Int,
  onPlayAgain: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isNewHigh = score > highScore

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Text(
      text = if (isNewHigh) "🔥 NOUVEAU RECORD !" else "⚡ TEMPS ÉCOULÉ !",
      style = MaterialTheme.typography.headlineMedium,
      fontWeight = FontWeight.ExtraBold,
      color = if (isNewHigh) AppThemeColors.flame else AppThemeColors.primary
    )

    Spacer(modifier = Modifier.height(10.dp))

    Text(
      text = "Score final : $score points",
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(24.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceEvenly
    ) {
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppThemeColors.primaryContainer),
        modifier = Modifier.width(130.dp)
      ) {
        Column(
          modifier = Modifier.padding(14.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text("XP GAGNÉS", style = MaterialTheme.typography.labelSmall, color = AppThemeColors.onPrimaryContainer)
          Text("+${score * 5}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = AppThemeColors.onPrimaryContainer)
        }
      }

      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppThemeColors.goldContainer),
        modifier = Modifier.width(130.dp)
      ) {
        Column(
          modifier = Modifier.padding(14.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text("GEMMES", style = MaterialTheme.typography.labelSmall, color = AppThemeColors.onGoldContainer)
          Text("+${score / 3} 💎", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = AppThemeColors.onGoldContainer)
        }
      }
    }

    Spacer(modifier = Modifier.height(36.dp))

    Button(
      onClick = onPlayAgain,
      modifier = Modifier
        .fillMaxWidth()
        .height(54.dp)
        .testTag("play_again_drill_btn"),
      colors = ButtonDefaults.buttonColors(containerColor = AppThemeColors.primary),
      shape = RoundedCornerShape(14.dp)
    ) {
      Text("Rejouer le Défi Éclair", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
  }
}
