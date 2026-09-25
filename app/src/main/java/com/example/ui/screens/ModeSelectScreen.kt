package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AppThemeColors

/**
 * First screen shown at launch: the user picks how they want to learn today.
 * Both modes stay reachable afterwards from the bottom navigation, so this is
 * a starting suggestion, not a lock-in.
 */
@Composable
fun ModeSelectScreen(
  onSelectExploration: () -> Unit,
  onSelectGamification: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(24.dp),
    verticalArrangement = Arrangement.Center
  ) {
    Text(
      text = "Comment veux-tu apprendre aujourd'hui ?",
      style = MaterialTheme.typography.headlineSmall,
      fontWeight = FontWeight.ExtraBold,
      color = MaterialTheme.colorScheme.onSurface,
      textAlign = TextAlign.Center,
      modifier = Modifier.fillMaxWidth()
    )
    Text(
      text = "Tu pourras changer de mode à tout moment depuis la barre du bas.",
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      textAlign = TextAlign.Center,
      modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 32.dp)
    )

    ModeCard(
      icon = Icons.Default.Lightbulb,
      title = "Exploration",
      subtitle = "Comprendre les règles, voir des exemples par registre, écouter des textes, générer du contenu sur mesure.",
      buttonLabel = "Explorer & comprendre",
      onClick = onSelectExploration
    )

    androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = 20.dp))

    ModeCard(
      icon = Icons.Default.Edit,
      title = "Gamification",
      subtitle = "S'entraîner par drills chronométrés, suivre son score et sa progression règle par règle dans le Codex.",
      buttonLabel = "S'entraîner & progresser",
      onClick = onSelectGamification
    )
  }
}

@Composable
private fun ModeCard(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  subtitle: String,
  buttonLabel: String,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
  ) {
    Column(
      modifier = Modifier.padding(22.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      androidx.compose.foundation.layout.Box(
        modifier = Modifier
          .size(56.dp)
          .clip(CircleShape)
          .background(AppThemeColors.primaryContainer),
        contentAlignment = Alignment.Center
      ) {
        Icon(icon, contentDescription = null, tint = AppThemeColors.onPrimaryContainer, modifier = Modifier.size(28.dp))
      }

      androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = 12.dp))

      Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

      androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = 4.dp))

      Text(
        text = subtitle,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center
      )

      androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = 16.dp))

      Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = AppThemeColors.primary, contentColor = Color.White)
      ) {
        Text(buttonLabel, fontWeight = FontWeight.Bold)
      }
    }
  }
}
