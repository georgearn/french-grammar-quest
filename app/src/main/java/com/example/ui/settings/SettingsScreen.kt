package com.example.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.prefs.StartMode
import com.example.ui.GrammarViewModel
import com.example.ui.theme.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: GrammarViewModel, modifier: Modifier = Modifier) {
  val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
  val startMode by viewModel.startMode.collectAsStateWithLifecycle()
  val apiKey by viewModel.geminiApiKey.collectAsStateWithLifecycle()

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    SettingsHeader(stringResource(R.string.settings_theme))
    val themes = listOf(
      ThemeMode.SYSTEM to R.string.theme_system,
      ThemeMode.LIGHT to R.string.theme_light,
      ThemeMode.DARK to R.string.theme_dark
    )
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
      themes.forEachIndexed { index, (mode, label) ->
        SegmentedButton(
          selected = themeMode == mode,
          onClick = { viewModel.setThemeMode(mode) },
          shape = SegmentedButtonDefaults.itemShape(index, themes.size)
        ) { Text(stringResource(label)) }
      }
    }

    HorizontalDivider(Modifier.padding(vertical = 8.dp))

    SettingsHeader(stringResource(R.string.settings_start_mode))
    Text(
      stringResource(R.string.settings_start_mode_desc),
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    val modes = listOf(StartMode.EXPLORE to R.string.nav_explore, StartMode.TRAIN to R.string.nav_train)
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
      modes.forEachIndexed { index, (mode, label) ->
        SegmentedButton(
          selected = startMode == mode,
          onClick = { viewModel.setStartMode(mode) },
          shape = SegmentedButtonDefaults.itemShape(index, modes.size)
        ) { Text(stringResource(label)) }
      }
    }

    HorizontalDivider(Modifier.padding(vertical = 8.dp))

    SettingsHeader(stringResource(R.string.settings_api_key))
    ApiKeyEditor(apiKey = apiKey, onSave = viewModel::setGeminiApiKey)
  }
}

@Composable
private fun SettingsHeader(text: String) {
  Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
}

@Composable
private fun ApiKeyEditor(apiKey: String, onSave: (String) -> Unit) {
  var draft by rememberSaveable(apiKey) { mutableStateOf(apiKey) }
  var visible by rememberSaveable { mutableStateOf(false) }
  val uriHandler = LocalUriHandler.current

  Text(
    stringResource(R.string.settings_api_key_desc),
    style = MaterialTheme.typography.bodySmall,
    color = MaterialTheme.colorScheme.onSurfaceVariant
  )
  TextButton(onClick = { uriHandler.openUri("https://aistudio.google.com/apikey") }) {
    Text(stringResource(R.string.settings_api_key_get))
  }
  OutlinedTextField(
    value = draft,
    onValueChange = { draft = it },
    label = { Text(stringResource(R.string.settings_api_key)) },
    placeholder = { Text("AIza…") },
    singleLine = true,
    visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, autoCorrectEnabled = false),
    trailingIcon = {
      IconButton(onClick = { visible = !visible }) {
        Icon(
          if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
          contentDescription = stringResource(if (visible) R.string.api_key_hide else R.string.api_key_show)
        )
      }
    },
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp)
  )
  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
    Button(onClick = { onSave(draft) }, enabled = draft.trim() != apiKey) {
      Text(stringResource(R.string.action_save))
    }
    if (apiKey.isNotBlank()) {
      TextButton(onClick = { draft = ""; onSave("") }) {
        Text(stringResource(R.string.action_remove), color = MaterialTheme.colorScheme.error)
      }
      Spacer(Modifier.weight(1f))
      Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
      Text(stringResource(R.string.settings_api_key_saved), style = MaterialTheme.typography.labelMedium)
    }
  }
  Spacer(Modifier.height(24.dp))
}
