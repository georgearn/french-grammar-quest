package com.example.ui.training

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.engine.AnswerVerdict
import com.example.data.engine.ProductionDrillItem
import com.example.data.engine.SpotErrorDrillItem
import com.example.data.engine.StructuredRule
import com.example.data.local.RuleTrainingProgressEntity
import com.example.data.prefs.DrillMode
import com.example.ui.DrillFeedback
import com.example.ui.GrammarViewModel
import com.example.ui.TrainingSession
import com.example.ui.components.RulePickerSheet
import com.example.ui.components.RuleSelectorField
import com.example.ui.i18n.localized
import com.example.ui.theme.AppThemeColors

/**
 * "S'entraîner": one compact header (rule + drill type), then the exercise with its action button
 * pinned at the bottom, above the keyboard. The whole session lives in the ViewModel, so switching
 * tabs or rotating the phone never loses progress.
 */
@Composable
fun TrainingPathScreen(
  viewModel: GrammarViewModel,
  onExploreRule: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val session by viewModel.training.collectAsStateWithLifecycle()
  val levelFilter by viewModel.levelFilter.collectAsStateWithLifecycle()
  val progressMap by viewModel.ruleTrainingProgressMap.collectAsStateWithLifecycle()
  val rule = viewModel.ruleById(session.ruleId)
  var showPicker by rememberSaveable { mutableStateOf(false) }

  val itemId = session.itemIds.getOrNull(session.index)
  val productionItem = if (session.mode == DrillMode.PRODUCTION) rule.productionDrills.firstOrNull { it.id == itemId } else null
  val spotItem = if (session.mode == DrillMode.SPOT_ERROR) rule.spotErrorDrills.firstOrNull { it.id == itemId } else null

  // Per-item input, reset for every new item and every new run.
  val itemKey = "${session.sessionId}-$itemId"
  var input by rememberSaveable(itemKey, stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
  var selectedToken by rememberSaveable(itemKey) { mutableStateOf<Int?>(null) }

  val submit: () -> Unit = {
    when {
      productionItem != null && input.text.isNotBlank() -> viewModel.submitProductionAnswer(input.text)
      spotItem != null -> selectedToken?.let(viewModel::submitSpotError)
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .imePadding()
      .testTag("training_screen")
  ) {
    Column(
      modifier = Modifier
        .weight(1f)
        .verticalScroll(rememberScrollState())
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      RuleSelectorField(
        rule = rule,
        supportingText = bestScoreText(progressMap[rule.id], session.mode),
        onClick = { showPicker = true },
        modifier = Modifier.fillMaxWidth()
      )

      DrillModeSelector(mode = session.mode, onSelect = viewModel::setDrillMode)

      when {
        session.itemIds.isEmpty() -> NoDrillsCard(
          onSwitchMode = {
            viewModel.setDrillMode(if (session.mode == DrillMode.PRODUCTION) DrillMode.SPOT_ERROR else DrillMode.PRODUCTION)
          },
          onPickRule = { showPicker = true }
        )

        session.isFinished -> SessionSummary(
          session = session,
          rule = rule,
          onReviewMistakes = viewModel::reviewMistakes,
          onRestart = viewModel::restartTraining,
          onExploreRule = { onExploreRule(rule.id) },
          onNextRule = viewModel.nextRuleId(rule.id)?.let { nextId -> { viewModel.selectTrainingRule(nextId) } }
        )

        else -> {
          SessionProgress(session)
          if (productionItem != null) {
            ProductionDrill(
              drill = productionItem,
              input = input,
              onInputChange = { if (session.feedback == null) input = it },
              feedback = session.feedback,
              onSubmit = submit
            )
          }
          if (spotItem != null) {
            SpotErrorDrill(
              drill = spotItem,
              selectedToken = selectedToken,
              onSelectToken = { if (session.feedback == null) selectedToken = it },
              feedback = session.feedback
            )
          }
        }
      }
    }

    if (!session.isFinished && session.itemIds.isNotEmpty()) {
      DrillActionBar(
        session = session,
        canSubmit = (productionItem != null && input.text.isNotBlank()) || (spotItem != null && selectedToken != null),
        onSubmit = submit,
        onNext = viewModel::nextDrill
      )
    }
  }

  if (showPicker) {
    RulePickerSheet(
      rules = viewModel.rules,
      selectedRuleId = rule.id,
      levelFilter = levelFilter,
      onLevelFilterChange = viewModel::setLevelFilter,
      onSelectRule = viewModel::selectTrainingRule,
      onDismiss = { showPicker = false },
      progressMap = progressMap
    )
  }
}

@Composable
private fun bestScoreText(progress: RuleTrainingProgressEntity?, mode: DrillMode): String {
  val (score, total) = when (mode) {
    DrillMode.PRODUCTION -> (progress?.bestProductionScore ?: 0) to (progress?.bestProductionTotal ?: 0)
    DrillMode.SPOT_ERROR -> (progress?.bestSpotErrorScore ?: 0) to (progress?.bestSpotErrorTotal ?: 0)
  }
  return if (total > 0) stringResource(R.string.train_best_score, score, total)
  else stringResource(R.string.train_not_trained)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DrillModeSelector(mode: DrillMode, onSelect: (DrillMode) -> Unit) {
  val options = listOf(
    DrillMode.PRODUCTION to R.string.drill_production,
    DrillMode.SPOT_ERROR to R.string.drill_spot_error
  )
  SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
    options.forEachIndexed { index, (option, label) ->
      SegmentedButton(
        selected = mode == option,
        onClick = { onSelect(option) },
        shape = SegmentedButtonDefaults.itemShape(index, options.size)
      ) { Text(stringResource(label)) }
    }
  }
}

@Composable
private fun SessionProgress(session: TrainingSession) {
  val done = session.index + if (session.feedback != null) 1 else 0
  Column {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Text(
        text = stringResource(R.string.train_item_position, session.index + 1, session.itemIds.size),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.weight(1f)
      )
      if (session.isReview) {
        Surface(shape = RoundedCornerShape(8.dp), color = AppThemeColors.goldContainer) {
          Text(
            stringResource(R.string.train_review_badge),
            style = MaterialTheme.typography.labelMedium,
            color = AppThemeColors.onGoldContainer,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
          )
        }
        Spacer(Modifier.width(8.dp))
      }
      Text(
        text = stringResource(R.string.train_score_so_far, session.score),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
    Spacer(Modifier.height(6.dp))
    LinearProgressIndicator(
      progress = { done.toFloat() / session.itemIds.size.coerceAtLeast(1) },
      modifier = Modifier.fillMaxWidth()
    )
  }
}

@Composable
private fun DrillCard(content: @Composable () -> Unit) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
  ) {
    Column(modifier = Modifier.padding(18.dp)) { content() }
  }
}

@Composable
private fun ProductionDrill(
  drill: ProductionDrillItem,
  input: TextFieldValue,
  onInputChange: (TextFieldValue) -> Unit,
  feedback: DrillFeedback?,
  onSubmit: () -> Unit
) {
  var showHint by rememberSaveable(drill.id) { mutableStateOf(false) }
  val submitted = feedback != null

  DrillCard {
    Text(
      text = localized(drill.instructionFr, drill.instructionEn),
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(Modifier.height(10.dp))
    Text(
      text = drill.basePrompt,
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold
    )

    if (!submitted) {
      TextButton(onClick = { showHint = !showHint }, modifier = Modifier.padding(top = 4.dp)) {
        Icon(Icons.Default.Lightbulb, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(6.dp))
        Text(stringResource(if (showHint) R.string.train_hide_hint else R.string.train_show_hint))
      }
      AnimatedVisibility(visible = showHint) {
        Surface(shape = RoundedCornerShape(10.dp), color = AppThemeColors.goldContainer) {
          Text(
            text = drill.hint,
            style = MaterialTheme.typography.bodyMedium,
            color = AppThemeColors.onGoldContainer,
            modifier = Modifier.padding(12.dp)
          )
        }
      }
    }

    Spacer(Modifier.height(12.dp))

    OutlinedTextField(
      value = input,
      onValueChange = onInputChange,
      label = { Text(stringResource(R.string.train_answer_label)) },
      singleLine = true,
      enabled = !submitted,
      keyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.None,
        autoCorrectEnabled = false,
        imeAction = ImeAction.Done
      ),
      keyboardActions = KeyboardActions(onDone = { onSubmit() }),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("production_input_field"),
      shape = RoundedCornerShape(12.dp)
    )

    if (!submitted) {
      Spacer(Modifier.height(8.dp))
      FrenchAccentToolbar(onInsert = { char -> onInputChange(input.insertAtCursor(char)) })
    }

    if (feedback != null) {
      Spacer(Modifier.height(14.dp))
      ProductionFeedback(feedback = feedback, expected = drill.targetAnswer, explanation = drill.explanation)
    }
  }
}

/** Inserts [text] at the cursor (replacing any selection) instead of appending at the end. */
private fun TextFieldValue.insertAtCursor(text: String): TextFieldValue {
  val start = selection.min
  val end = selection.max
  val newText = this.text.replaceRange(start, end, text)
  return copy(text = newText, selection = TextRange(start + text.length))
}

private val FRENCH_ACCENTS = listOf("é", "è", "ê", "ë", "à", "â", "ç", "î", "ï", "ô", "ù", "û", "œ", "’")

@Composable
private fun FrenchAccentToolbar(onInsert: (String) -> Unit) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.spacedBy(6.dp)
  ) {
    FRENCH_ACCENTS.forEach { char ->
      Surface(
        onClick = { onInsert(char) },
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
          .size(48.dp)
          .semantics { contentDescription = char }
      ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
          Text(char, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
private fun ProductionFeedback(feedback: DrillFeedback, expected: String, explanation: String) {
  val (icon, title, container, onContainer) = verdictStyle(feedback.verdict)
  Surface(shape = RoundedCornerShape(12.dp), color = container, border = BorderStroke(1.dp, onContainer.copy(alpha = 0.4f))) {
    Column(modifier = Modifier.padding(14.dp).fillMaxWidth()) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = onContainer)
        Spacer(Modifier.width(8.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = onContainer)
      }
      if (feedback.verdict != AnswerVerdict.CORRECT) {
        Spacer(Modifier.height(8.dp))
        LabeledValue(stringResource(R.string.train_your_answer), feedback.given, onContainer)
        LabeledValue(stringResource(R.string.train_expected), expected, onContainer)
      }
      Spacer(Modifier.height(8.dp))
      Text(explanation, style = MaterialTheme.typography.bodyMedium, color = onContainer)
    }
  }
}

@Composable
private fun LabeledValue(label: String, value: String, color: Color) {
  Row {
    Text("$label ", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = color)
    Text(value, style = MaterialTheme.typography.bodyMedium, fontFamily = FontFamily.Monospace, color = color)
  }
}

private data class VerdictStyle(val icon: ImageVector, val title: String, val container: Color, val onContainer: Color)

@Composable
private fun verdictStyle(verdict: AnswerVerdict): VerdictStyle = when (verdict) {
  AnswerVerdict.CORRECT -> VerdictStyle(
    Icons.Default.CheckCircle, stringResource(R.string.train_correct),
    AppThemeColors.successContainer, AppThemeColors.onSuccessContainer
  )
  AnswerVerdict.ACCENT_MISTAKE -> VerdictStyle(
    Icons.Default.WarningAmber, stringResource(R.string.train_accent_mistake),
    AppThemeColors.goldContainer, AppThemeColors.onGoldContainer
  )
  AnswerVerdict.WRONG -> VerdictStyle(
    Icons.Default.Cancel, stringResource(R.string.train_wrong),
    AppThemeColors.redContainer, AppThemeColors.onRedContainer
  )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SpotErrorDrill(
  drill: SpotErrorDrillItem,
  selectedToken: Int?,
  onSelectToken: (Int?) -> Unit,
  feedback: DrillFeedback?
) {
  val submitted = feedback != null
  val correctLabel = stringResource(R.string.train_token_error)
  val wrongLabel = stringResource(R.string.train_token_your_pick)

  DrillCard {
    Text(
      text = drill.instructionFr,
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(Modifier.height(4.dp))
    Text(
      text = stringResource(R.string.train_spot_hint),
      style = MaterialTheme.typography.labelLarge,
      fontWeight = FontWeight.Bold
    )
    Spacer(Modifier.height(12.dp))

    FlowRow(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(6.dp),
      verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      drill.tokens.forEachIndexed { index, token ->
        val isSelected = selectedToken == index
        val isErrorTarget = index == drill.errorTokenIndex
        val showAsError = submitted && isErrorTarget
        val showAsWrongPick = submitted && isSelected && !isErrorTarget

        val container = when {
          showAsError -> AppThemeColors.successContainer
          showAsWrongPick -> AppThemeColors.redContainer
          isSelected -> AppThemeColors.primaryContainer
          else -> MaterialTheme.colorScheme.surface
        }
        val border = when {
          showAsError -> AppThemeColors.success
          showAsWrongPick -> AppThemeColors.red
          isSelected -> AppThemeColors.primary
          else -> MaterialTheme.colorScheme.outline
        }

        Surface(
          onClick = { onSelectToken(if (isSelected) null else index) },
          enabled = !submitted,
          shape = RoundedCornerShape(10.dp),
          color = container,
          border = BorderStroke(if (isSelected || showAsError) 2.dp else 1.dp, border),
          modifier = Modifier
            .heightIn(min = 48.dp)
            .semantics {
              when {
                showAsError -> stateDescription = correctLabel
                showAsWrongPick -> stateDescription = wrongLabel
              }
            }
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            if (showAsError) {
              Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AppThemeColors.success, modifier = Modifier.size(16.dp))
              Spacer(Modifier.width(4.dp))
            } else if (showAsWrongPick) {
              Icon(Icons.Default.Cancel, contentDescription = null, tint = AppThemeColors.red, modifier = Modifier.size(16.dp))
              Spacer(Modifier.width(4.dp))
            }
            Text(
              text = token,
              style = MaterialTheme.typography.bodyLarge,
              fontWeight = if (isSelected || showAsError) FontWeight.Bold else FontWeight.Normal
            )
          }
        }
      }
    }

    if (feedback != null) {
      Spacer(Modifier.height(14.dp))
      val (icon, title, container, onContainer) = verdictStyle(feedback.verdict)
      Surface(shape = RoundedCornerShape(12.dp), color = container) {
        Column(modifier = Modifier.padding(14.dp).fillMaxWidth()) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = onContainer)
            Spacer(Modifier.width(8.dp))
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = onContainer)
          }
          Spacer(Modifier.height(8.dp))
          Text(
            stringResource(R.string.train_spot_correction, drill.errorWord, drill.correction),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = onContainer
          )
          Spacer(Modifier.height(4.dp))
          Text(drill.ruleExplanation, style = MaterialTheme.typography.bodyMedium, color = onContainer)
        }
      }
    }
  }
}

@Composable
private fun DrillActionBar(
  session: TrainingSession,
  canSubmit: Boolean,
  onSubmit: () -> Unit,
  onNext: () -> Unit
) {
  Surface(tonalElevation = 3.dp, shadowElevation = 6.dp) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp)) {
      if (session.feedback == null) {
        Button(
          onClick = onSubmit,
          enabled = canSubmit,
          modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp)
            .testTag("verify_answer_btn"),
          shape = RoundedCornerShape(14.dp)
        ) {
          Text(stringResource(R.string.train_check), fontWeight = FontWeight.Bold)
        }
      } else {
        val isLast = session.index + 1 >= session.itemIds.size
        Button(
          onClick = onNext,
          modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp)
            .testTag("next_question_btn"),
          shape = RoundedCornerShape(14.dp)
        ) {
          Text(stringResource(if (isLast) R.string.train_see_summary else R.string.train_next), fontWeight = FontWeight.Bold)
          Spacer(Modifier.width(8.dp))
          Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
        }
      }
    }
  }
}

@Composable
private fun SessionSummary(
  session: TrainingSession,
  rule: StructuredRule,
  onReviewMistakes: () -> Unit,
  onRestart: () -> Unit,
  onExploreRule: () -> Unit,
  onNextRule: (() -> Unit)?
) {
  val total = session.itemIds.size
  val ratio = if (total == 0) 0f else session.score.toFloat() / total
  val message = when {
    ratio >= 1f -> R.string.summary_perfect
    ratio >= 0.7f -> R.string.summary_good
    else -> R.string.summary_keep_going
  }

  DrillCard {
    Text(
      stringResource(if (session.isReview) R.string.summary_review_title else R.string.summary_title),
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold
    )
    Spacer(Modifier.height(8.dp))
    Text(
      text = "${session.score} / $total",
      style = MaterialTheme.typography.displaySmall,
      fontWeight = FontWeight.ExtraBold,
      color = if (ratio >= 0.7f) AppThemeColors.success else MaterialTheme.colorScheme.primary
    )
    Text(stringResource(message), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

    val mistakes = session.mistakes
    if (mistakes.isNotEmpty()) {
      Spacer(Modifier.height(16.dp))
      Text(stringResource(R.string.summary_mistakes), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
      Spacer(Modifier.height(6.dp))
      mistakes.forEach { result ->
        val (prompt, expected) = when (session.mode) {
          DrillMode.PRODUCTION -> rule.productionDrills.firstOrNull { it.id == result.itemId }
            ?.let { it.basePrompt to it.targetAnswer }
          DrillMode.SPOT_ERROR -> rule.spotErrorDrills.firstOrNull { it.id == result.itemId }
            ?.let { it.passage to "${it.errorWord} → ${it.correction}" }
        } ?: return@forEach
        HorizontalDivider(Modifier.padding(vertical = 6.dp))
        Text(prompt, style = MaterialTheme.typography.bodyMedium)
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Cancel, contentDescription = null, tint = AppThemeColors.red, modifier = Modifier.size(16.dp))
          Spacer(Modifier.width(4.dp))
          Text(result.given.ifBlank { "—" }, style = MaterialTheme.typography.bodySmall, color = AppThemeColors.red)
          Spacer(Modifier.width(12.dp))
          Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AppThemeColors.success, modifier = Modifier.size(16.dp))
          Spacer(Modifier.width(4.dp))
          Text(expected, style = MaterialTheme.typography.bodySmall, color = AppThemeColors.success, fontWeight = FontWeight.Bold)
        }
      }
    }

    Spacer(Modifier.height(18.dp))
    if (mistakes.isNotEmpty()) {
      Button(onClick = onReviewMistakes, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
        Icon(Icons.Default.Replay, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(stringResource(R.string.summary_review_mistakes, mistakes.size))
      }
      Spacer(Modifier.height(8.dp))
    }
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      OutlinedButton(onClick = onRestart, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) {
        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(6.dp))
        Text(stringResource(R.string.summary_restart))
      }
      OutlinedButton(onClick = onExploreRule, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) {
        Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(6.dp))
        Text(stringResource(R.string.summary_see_rule))
      }
    }
    if (onNextRule != null) {
      Spacer(Modifier.height(8.dp))
      TextButton(onClick = onNextRule, modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.summary_next_rule))
        Spacer(Modifier.width(6.dp))
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
      }
    }
  }
}

@Composable
private fun NoDrillsCard(onSwitchMode: () -> Unit, onPickRule: () -> Unit) {
  DrillCard {
    Text(stringResource(R.string.train_no_drills), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(12.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      OutlinedButton(onClick = onSwitchMode) { Text(stringResource(R.string.train_switch_mode)) }
      OutlinedButton(onClick = onPickRule) { Text(stringResource(R.string.rule_change)) }
    }
  }
}
