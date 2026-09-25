package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.MistakeEntity
import com.example.ui.theme.AppThemeColors
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.FrenchBlue
import com.example.ui.theme.FrenchRed

@Composable
fun MistakesScreen(
  mistakes: List<MistakeEntity>,
  onResolveMistake: (Long) -> Unit,
  onClearAll: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .testTag("mistakes_screen")
  ) {
    // Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "SALLE DE RÉVISION",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = AppThemeColors.red,
          letterSpacing = 1.sp
        )
        Text(
          text = "${mistakes.size} point${if (mistakes.size > 1) "s" else ""} à revoir",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold
        )
      }

      if (mistakes.isNotEmpty()) {
        OutlinedButton(
          onClick = onClearAll,
          shape = RoundedCornerShape(20.dp),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Icon(Icons.Default.DeleteSweep, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Tout effacer", fontSize = 12.sp)
        }
      }
    }

    // Info pill about regaining hearts
    Surface(
      color = AppThemeColors.successContainer,
      shape = RoundedCornerShape(12.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
      Row(
        modifier = Modifier.padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(Icons.Default.Favorite, contentDescription = null, tint = AppThemeColors.red, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = "Comprenez vos erreurs et cliquez sur 'Résolu' pour regagner un cœur (+1 ❤️) gratuitement !",
          style = MaterialTheme.typography.bodySmall,
          color = AppThemeColors.onSuccessContainer
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    if (mistakes.isEmpty()) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(bottom = 90.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Box(
          modifier = Modifier
            .size(80.dp)
            .background(AppThemeColors.successContainer, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = AppThemeColors.success,
            modifier = Modifier.size(44.dp)
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "Aucune erreur enregistrée !",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = "Vous maîtrisez vos leçons sans accroc. Continuez les quêtes pour gagner plus d'XP !",
          style = MaterialTheme.typography.bodyMedium,
          textAlign = TextAlign.Center,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(horizontal = 32.dp)
        )
      }
    } else {
      LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(mistakes, key = { it.id }) { mistake ->
          MistakeCard(
            mistake = mistake,
            onResolve = { onResolveMistake(mistake.id) }
          )
        }
      }
    }
  }
}

@Composable
private fun MistakeCard(
  mistake: MistakeEntity,
  onResolve: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("mistake_card_${mistake.id}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Text(
        text = mistake.promptFr,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
      )

      if (mistake.promptEn.isNotEmpty()) {
        Text(
          text = mistake.promptEn,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(text = "Votre réponse :", style = MaterialTheme.typography.labelSmall, color = AppThemeColors.red)
          Text(
            text = mistake.userAnswer.ifEmpty { "(Aucune)" },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = AppThemeColors.red
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
          Text(text = "Bonne réponse :", style = MaterialTheme.typography.labelSmall, color = AppThemeColors.success)
          Text(
            text = mistake.correctAnswer,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = AppThemeColors.success
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = "💡 ${mistake.explanation}",
          style = MaterialTheme.typography.bodySmall,
          modifier = Modifier.padding(10.dp),
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      Button(
        onClick = onResolve,
        colors = ButtonDefaults.buttonColors(containerColor = AppThemeColors.success),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
          .align(Alignment.End)
          .testTag("resolve_mistake_btn_${mistake.id}")
      ) {
        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("Résolu (+1 ❤️)", fontWeight = FontWeight.Bold)
      }
    }
  }
}
