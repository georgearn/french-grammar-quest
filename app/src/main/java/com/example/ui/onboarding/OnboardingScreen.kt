package com.example.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.prefs.StartMode

/**
 * Shown once, on first launch: the learner picks the mode the app opens on. The choice is stored
 * and can be changed later in Settings; both modes stay one tap away in the bottom bar.
 */
@Composable
fun OnboardingScreen(onChooseMode: (StartMode) -> Unit) {
  var selected by rememberSaveable { mutableStateOf(StartMode.EXPLORE) }

  Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .safeDrawingPadding()
        .verticalScroll(rememberScrollState())
        .padding(24.dp)
        .testTag("onboarding_screen"),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(Modifier.height(24.dp))
      Box(
        modifier = Modifier
          .size(72.dp)
          .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          Icons.Default.AutoStories,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onPrimaryContainer,
          modifier = Modifier.size(36.dp)
        )
      }
      Spacer(Modifier.height(20.dp))
      Text(
        text = stringResource(R.string.onboarding_title),
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.ExtraBold,
        textAlign = TextAlign.Center
      )
      Spacer(Modifier.height(8.dp))
      Text(
        text = stringResource(R.string.onboarding_subtitle),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center
      )
      Spacer(Modifier.height(28.dp))

      Column(
        modifier = Modifier.selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        ModeOption(
          icon = Icons.Default.Lightbulb,
          title = stringResource(R.string.mode_explore_title),
          description = stringResource(R.string.mode_explore_desc),
          selected = selected == StartMode.EXPLORE,
          onSelect = { selected = StartMode.EXPLORE }
        )
        ModeOption(
          icon = Icons.Default.Edit,
          title = stringResource(R.string.mode_train_title),
          description = stringResource(R.string.mode_train_desc),
          selected = selected == StartMode.TRAIN,
          onSelect = { selected = StartMode.TRAIN }
        )
      }

      Spacer(Modifier.height(20.dp))
      Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
        Icon(
          Icons.Default.Translate,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(
          text = stringResource(R.string.onboarding_english_tip),
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
      Spacer(Modifier.height(28.dp))
      Button(
        onClick = { onChooseMode(selected) },
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("onboarding_start")
      ) {
        Text(stringResource(R.string.onboarding_start), fontWeight = FontWeight.Bold)
      }
      Spacer(Modifier.height(8.dp))
      Text(
        text = stringResource(R.string.onboarding_change_later),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center
      )
    }
  }
}

@Composable
private fun ModeOption(
  icon: ImageVector,
  title: String,
  description: String,
  selected: Boolean,
  onSelect: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(18.dp),
    color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
    border = BorderStroke(
      if (selected) 2.dp else 1.dp,
      if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
    ),
    modifier = Modifier
      .fillMaxWidth()
      .selectable(selected = selected, onClick = onSelect, role = Role.RadioButton)
  ) {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
      Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
      Spacer(Modifier.width(14.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(
          description,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
      RadioButton(selected = selected, onClick = null)
    }
  }
}
