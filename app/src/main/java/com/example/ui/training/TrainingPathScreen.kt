package com.example.ui.training

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FindInPage
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.engine.GrammarRuleRepository
import com.example.data.engine.ProductionDrillItem
import com.example.data.engine.SpotErrorDrillItem
import com.example.data.engine.StructuredRule
import com.example.ui.theme.AppThemeColors

enum class DrillMode {
  PRODUCTION,   // Saisie active / transformation (Primary drill type)
  SPOT_ERROR    // Chasse à l'erreur en contexte (Secondary drill type)
}

@Composable
fun TrainingPathScreen(
  initialRuleId: String,
  onExploreRule: (String) -> Unit,
  onSessionComplete: (ruleId: String, isProductionMode: Boolean, score: Int, total: Int) -> Unit = { _, _, _, _ -> },
  onOpenIndex: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val allRules = remember { GrammarRuleRepository.getAllRules() }
  var selectedRuleId by remember { mutableStateOf(initialRuleId) }
  var drillMode by remember { mutableStateOf(DrillMode.PRODUCTION) }

  // Ensure valid rule
  val currentRule = remember(selectedRuleId, allRules) {
    GrammarRuleRepository.getRuleById(selectedRuleId) ?: allRules.firstOrNull() ?: allRules[0]
  }

  // Session state
  var currentQuestionIndex by remember(selectedRuleId, drillMode) { mutableIntStateOf(0) }
  var scoreCount by remember(selectedRuleId, drillMode) { mutableIntStateOf(0) }
  var isSessionFinished by remember(selectedRuleId, drillMode) { mutableStateOf(false) }

  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .verticalScroll(scrollState)
      .padding(16.dp)
      .testTag("training_screen")
  ) {
    // Top Mode Switcher: Production vs Spot Error
    TrainingModeTabHeader(
      activeMode = drillMode,
      onSelectMode = {
        drillMode = it
        currentQuestionIndex = 0
        scoreCount = 0
        isSessionFinished = false
      },
      onOpenIndex = onOpenIndex
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Free Topic Selector
    FreeTopicSelector(
      rules = allRules,
      selectedRuleId = currentRule.id,
      onSelectRule = {
        selectedRuleId = it
        currentQuestionIndex = 0
        scoreCount = 0
        isSessionFinished = false
      }
    )

    Spacer(modifier = Modifier.height(20.dp))

    if (isSessionFinished) {
      // Session Complete View
      val totalQuestions = if (drillMode == DrillMode.PRODUCTION) {
        currentRule.productionDrills.size
      } else {
        currentRule.spotErrorDrills.size
      }

      TrainingFinishedCard(
        score = scoreCount,
        total = totalQuestions,
        rule = currentRule,
        onRestart = {
          currentQuestionIndex = 0
          scoreCount = 0
          isSessionFinished = false
        },
        onExploreRule = { onExploreRule(currentRule.id) }
      )
    } else {
      // Active Drill Rendering
      if (drillMode == DrillMode.PRODUCTION) {
        val drills = currentRule.productionDrills
        val currentDrill = drills.getOrNull(currentQuestionIndex)

        if (currentDrill != null) {
          ActiveProductionDrillView(
            drill = currentDrill,
            currentIndex = currentQuestionIndex,
            totalCount = drills.size,
            onAnswerChecked = { isCorrect ->
              if (isCorrect) scoreCount++
            },
            onNext = {
              if (currentQuestionIndex + 1 < drills.size) {
                currentQuestionIndex++
              } else {
                isSessionFinished = true
                onSessionComplete(currentRule.id, true, scoreCount, drills.size)
              }
            }
          )
        } else {
          NoDrillsAvailableCard(rule = currentRule)
        }
      } else {
        // Spot-the-Error Mode
        val drills = currentRule.spotErrorDrills
        val currentDrill = drills.getOrNull(currentQuestionIndex)

        if (currentDrill != null) {
          ActiveSpotErrorDrillView(
            drill = currentDrill,
            currentIndex = currentQuestionIndex,
            totalCount = drills.size,
            onAnswerChecked = { isCorrect ->
              if (isCorrect) scoreCount++
            },
            onNext = {
              if (currentQuestionIndex + 1 < drills.size) {
                currentQuestionIndex++
              } else {
                isSessionFinished = true
                onSessionComplete(currentRule.id, false, scoreCount, drills.size)
              }
            }
          )
        } else {
          NoDrillsAvailableCard(rule = currentRule)
        }
      }
    }

    Spacer(modifier = Modifier.height(32.dp))
  }
}

@Composable
private fun TrainingModeTabHeader(
  activeMode: DrillMode,
  onSelectMode: (DrillMode) -> Unit,
  onOpenIndex: () -> Unit = {}
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = null,
            tint = AppThemeColors.primary,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Entraînement : Production & Détection",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
        }

        IconButton(onClick = onOpenIndex, modifier = Modifier.testTag("training_open_index")) {
          Icon(
            imageVector = Icons.Default.FindInPage,
            contentDescription = "Parcourir toutes les règles (Codex)",
            tint = AppThemeColors.primary
          )
        }
      }

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = "Choisissez librement votre mode d'exercice pour intérioriser la règle :",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(12.dp))

      TabRow(
        selectedTabIndex = if (activeMode == DrillMode.PRODUCTION) 0 else 1,
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        contentColor = AppThemeColors.primary,
        indicator = { tabPositions ->
          TabRowDefaults.SecondaryIndicator(
            Modifier.tabIndicatorOffset(tabPositions[if (activeMode == DrillMode.PRODUCTION) 0 else 1]),
            color = AppThemeColors.primary
          )
        }
      ) {
        Tab(
          selected = activeMode == DrillMode.PRODUCTION,
          onClick = { onSelectMode(DrillMode.PRODUCTION) },
          text = {
            Text(
              text = "✍️ Saisie Active (Production)",
              fontWeight = if (activeMode == DrillMode.PRODUCTION) FontWeight.Bold else FontWeight.Medium,
              fontSize = 13.5.sp
            )
          }
        )
        Tab(
          selected = activeMode == DrillMode.SPOT_ERROR,
          onClick = { onSelectMode(DrillMode.SPOT_ERROR) },
          text = {
            Text(
              text = "🔍 Chasse à l'Erreur",
              fontWeight = if (activeMode == DrillMode.SPOT_ERROR) FontWeight.Bold else FontWeight.Medium,
              fontSize = 13.5.sp
            )
          }
        )
      }
    }
  }
}

@Composable
private fun FreeTopicSelector(
  rules: List<StructuredRule>,
  selectedRuleId: String,
  onSelectRule: (String) -> Unit
) {
  val scrollState = rememberScrollState()

  Column {
    Text(
      text = "Sélection libre de la règle à travailler :",
      style = MaterialTheme.typography.labelMedium,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(6.dp))

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(scrollState),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      rules.forEach { rule ->
        val isSelected = rule.id == selectedRuleId
        FilterChip(
          selected = isSelected,
          onClick = { onSelectRule(rule.id) },
          label = {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = rule.level,
                fontWeight = FontWeight.Bold,
                fontSize = 10.5.sp,
                color = if (isSelected) Color.White else AppThemeColors.primary,
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(if (isSelected) Color.White.copy(alpha = 0.2f) else AppThemeColors.primaryContainer)
                  .padding(horizontal = 4.dp, vertical = 1.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = rule.titleFr.split(":").firstOrNull()?.trim() ?: rule.titleFr,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 12.5.sp
              )
            }
          },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = AppThemeColors.primary,
            selectedLabelColor = Color.White
          )
        )
      }
    }
  }
}

@Composable
private fun ActiveProductionDrillView(
  drill: ProductionDrillItem,
  currentIndex: Int,
  totalCount: Int,
  onAnswerChecked: (Boolean) -> Unit,
  onNext: () -> Unit
) {
  var typedAnswer by remember(drill.id) { mutableStateOf("") }
  var isSubmitted by remember(drill.id) { mutableStateOf(false) }
  var isCorrect by remember(drill.id) { mutableStateOf(false) }
  var showHint by remember(drill.id) { mutableStateOf(false) }

  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      // Step counter
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = AppThemeColors.primaryContainer
        ) {
          Text(
            text = "Exercice ${currentIndex + 1} / $totalCount",
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Bold,
            color = AppThemeColors.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          )
        }

        // Hint toggle button
        OutlinedButton(
          onClick = { showHint = !showHint },
          shape = RoundedCornerShape(20.dp),
          contentPadding = ButtonDefaults.ContentPadding
        ) {
          Icon(Icons.Default.HelpOutline, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = if (showHint) "Masquer l'indice" else "Indice", fontSize = 12.5.sp)
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Instruction
      Text(
        text = drill.instructionFr,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )

      Text(
        text = drill.instructionEn,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Base Prompt Sentence Box
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = drill.basePrompt,
          style = MaterialTheme.typography.bodyLarge,
          fontWeight = FontWeight.SemiBold,
          lineHeight = 24.sp,
          modifier = Modifier.padding(16.dp),
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      // Optional Hint display
      AnimatedVisibility(visible = showHint) {
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = AppThemeColors.goldContainer,
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Lightbulb,
              contentDescription = null,
              tint = AppThemeColors.onGoldContainer,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = drill.hint,
              fontSize = 12.5.sp,
              color = AppThemeColors.onGoldContainer
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Text Input Field
      OutlinedTextField(
        value = typedAnswer,
        onValueChange = { if (!isSubmitted) typedAnswer = it },
        label = { Text("Tapez votre réponse ici (conjugaison / transformation)") },
        placeholder = { Text("ex : ${drill.targetAnswer}") },
        singleLine = true,
        enabled = !isSubmitted,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("production_input_field"),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = AppThemeColors.primary,
          unfocusedBorderColor = MaterialTheme.colorScheme.outline,
          focusedTextColor = MaterialTheme.colorScheme.onSurface,
          unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
          disabledTextColor = MaterialTheme.colorScheme.onSurface,
          cursorColor = AppThemeColors.primary,
          focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
          unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
          focusedLabelColor = AppThemeColors.primary,
          unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
          onDone = {
            if (!isSubmitted && typedAnswer.isNotBlank()) {
              val cleanInput = typedAnswer.trim().lowercase()
              val cleanTarget = drill.targetAnswer.trim().lowercase()
              val isOk = cleanInput == cleanTarget || drill.acceptedAnswers.any { it.trim().lowercase() == cleanInput }
              isCorrect = isOk
              isSubmitted = true
              onAnswerChecked(isOk)
            }
          }
        )
      )

      Spacer(modifier = Modifier.height(8.dp))

      // French Accent Quick-Keys Toolbar
      if (!isSubmitted) {
        FrenchAccentToolbar(
          onInsertChar = { charToInsert ->
            typedAnswer += charToInsert
          }
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Action Button: Verify or Next
      if (!isSubmitted) {
        Button(
          onClick = {
            val cleanInput = typedAnswer.trim().lowercase()
            val cleanTarget = drill.targetAnswer.trim().lowercase()
            val isOk = cleanInput == cleanTarget || drill.acceptedAnswers.any { it.trim().lowercase() == cleanInput }
            isCorrect = isOk
            isSubmitted = true
            onAnswerChecked(isOk)
          },
          enabled = typedAnswer.isNotBlank(),
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("verify_answer_btn"),
          colors = ButtonDefaults.buttonColors(containerColor = AppThemeColors.primary),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text("Vérifier la réponse", fontWeight = FontWeight.Bold, fontSize = 15.5.sp)
        }
      } else {
        // Result Feedback with Smart Diff
        ProductionFeedbackCard(
          isCorrect = isCorrect,
          typedAnswer = typedAnswer,
          targetAnswer = drill.targetAnswer,
          explanation = drill.explanation
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = onNext,
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("next_question_btn"),
          colors = ButtonDefaults.buttonColors(
            containerColor = if (isCorrect) AppThemeColors.success else AppThemeColors.primary
          ),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text(
            text = if (currentIndex + 1 < totalCount) "Question suivante" else "Voir le bilan",
            fontWeight = FontWeight.Bold,
            fontSize = 15.5.sp
          )
        }
      }
    }
  }
}

@Composable
private fun FrenchAccentToolbar(onInsertChar: (String) -> Unit) {
  val accents = listOf("é", "è", "ê", "à", "â", "ç", "ù", "î", "ô", "œ", "«", "»")
  val scrollState = rememberScrollState()

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .horizontalScroll(scrollState),
    horizontalArrangement = Arrangement.spacedBy(6.dp)
  ) {
    accents.forEach { char ->
      Surface(
        onClick = { onInsertChar(char) },
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.size(38.dp)
      ) {
        Box(contentAlignment = Alignment.Center) {
          Text(
            text = char,
            fontSize = 16.5.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }
  }
}

@Composable
private fun ProductionFeedbackCard(
  isCorrect: Boolean,
  typedAnswer: String,
  targetAnswer: String,
  explanation: String
) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = if (isCorrect) AppThemeColors.successContainer else AppThemeColors.redContainer,
    border = BorderStroke(1.5.dp, if (isCorrect) AppThemeColors.success else AppThemeColors.red),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
          contentDescription = null,
          tint = if (isCorrect) AppThemeColors.onSuccessContainer else AppThemeColors.onRedContainer,
          modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = if (isCorrect) "Excellent ! Réponse exacte." else "Analyse de votre saisie :",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = if (isCorrect) AppThemeColors.onSuccessContainer else AppThemeColors.onRedContainer
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      if (!isCorrect) {
        Row {
          Text(text = "Votre saisie : ", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
          Text(text = typedAnswer, style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row {
          Text(text = "Attendu : ", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
          Text(
            text = targetAnswer,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = AppThemeColors.onRedContainer
          )
        }
        Spacer(modifier = Modifier.height(10.dp))
      }

      HorizontalDivider(color = (if (isCorrect) AppThemeColors.success else AppThemeColors.red).copy(alpha = 0.3f))
      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Explication : $explanation",
        style = MaterialTheme.typography.bodySmall,
        color = if (isCorrect) AppThemeColors.onSuccessContainer else AppThemeColors.onRedContainer,
        lineHeight = 18.sp
      )
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ActiveSpotErrorDrillView(
  drill: SpotErrorDrillItem,
  currentIndex: Int,
  totalCount: Int,
  onAnswerChecked: (Boolean) -> Unit,
  onNext: () -> Unit
) {
  var selectedTokenIdx by remember(drill.id) { mutableStateOf<Int?>(null) }
  var isSubmitted by remember(drill.id) { mutableStateOf(false) }
  var isCorrect by remember(drill.id) { mutableStateOf(false) }

  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = AppThemeColors.primaryContainer
        ) {
          Text(
            text = "Détection ${currentIndex + 1} / $totalCount",
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Bold,
            color = AppThemeColors.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.FindInPage, contentDescription = null, tint = AppThemeColors.primary, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Chasse à l'erreur", fontSize = 12.5.sp, color = AppThemeColors.primary, fontWeight = FontWeight.Bold)
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = drill.instructionFr,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
      )

      Text(
        text = "Touchez le mot ou groupe de mots qui enfreint la règle de grammaire :",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Clickable Word Tokens Flow
      FlowRow(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
          .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
          .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        drill.tokens.forEachIndexed { index, token ->
          val isSelected = selectedTokenIdx == index
          val isErrorTarget = index == drill.errorTokenIndex

          val tokenColor = when {
            isSubmitted && isErrorTarget -> AppThemeColors.successContainer
            isSubmitted && isSelected && !isErrorTarget -> AppThemeColors.redContainer
            isSelected -> AppThemeColors.primaryContainer
            else -> MaterialTheme.colorScheme.surface
          }

          val tokenBorder = when {
            isSubmitted && isErrorTarget -> AppThemeColors.success
            isSubmitted && isSelected && !isErrorTarget -> AppThemeColors.red
            isSelected -> AppThemeColors.primary
            else -> MaterialTheme.colorScheme.outlineVariant
          }

          Surface(
            onClick = {
              if (!isSubmitted) {
                selectedTokenIdx = if (isSelected) null else index
              }
            },
            shape = RoundedCornerShape(8.dp),
            color = tokenColor,
            border = BorderStroke(1.dp, tokenBorder),
            modifier = Modifier.padding(vertical = 2.dp)
          ) {
            Text(
              text = token,
              fontSize = 15.5.sp,
              fontWeight = if (isSelected || (isSubmitted && isErrorTarget)) FontWeight.Bold else FontWeight.Normal,
              color = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      if (!isSubmitted) {
        Button(
          onClick = {
            val isOk = selectedTokenIdx == drill.errorTokenIndex
            isCorrect = isOk
            isSubmitted = true
            onAnswerChecked(isOk)
          },
          enabled = selectedTokenIdx != null,
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
          colors = ButtonDefaults.buttonColors(containerColor = AppThemeColors.primary),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text("Valider la détection", fontWeight = FontWeight.Bold, fontSize = 15.5.sp)
        }
      } else {
        // Result Feedback for Spot the Error
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = if (isCorrect) AppThemeColors.successContainer else AppThemeColors.redContainer,
          border = BorderStroke(1.5.dp, if (isCorrect) AppThemeColors.success else AppThemeColors.red),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                contentDescription = null,
                tint = if (isCorrect) AppThemeColors.onSuccessContainer else AppThemeColors.onRedContainer,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = if (isCorrect) "Erreur bien identifiée !" else "Erreur manquée !",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = if (isCorrect) AppThemeColors.onSuccessContainer else AppThemeColors.onRedContainer
              )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = "Mot erroné : « ${drill.errorWord} »  ->  Correction : « ${drill.correction} »",
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.bodyMedium,
              color = if (isCorrect) AppThemeColors.onSuccessContainer else AppThemeColors.onRedContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = drill.ruleExplanation,
              style = MaterialTheme.typography.bodySmall,
              color = if (isCorrect) AppThemeColors.onSuccessContainer else AppThemeColors.onRedContainer,
              lineHeight = 18.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = onNext,
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = if (isCorrect) AppThemeColors.success else AppThemeColors.primary
          ),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text(
            text = if (currentIndex + 1 < totalCount) "Exercice suivant" else "Voir le bilan",
            fontWeight = FontWeight.Bold,
            fontSize = 15.5.sp
          )
        }
      }
    }
  }
}

@Composable
private fun TrainingFinishedCard(
  score: Int,
  total: Int,
  rule: StructuredRule,
  onRestart: () -> Unit,
  onExploreRule: () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
  ) {
    Column(
      modifier = Modifier.padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        modifier = Modifier
          .size(64.dp)
          .clip(CircleShape)
          .background(AppThemeColors.goldContainer),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.EmojiEvents,
          contentDescription = null,
          tint = AppThemeColors.onGoldContainer,
          modifier = Modifier.size(36.dp)
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = "Session d'entraînement terminée !",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "Score : $score / $total réponses correctes",
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.SemiBold,
        color = AppThemeColors.primary
      )

      Spacer(modifier = Modifier.height(20.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedButton(
          onClick = onRestart,
          modifier = Modifier
            .weight(1f)
            .height(48.dp),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Recommencer")
        }

        Button(
          onClick = onExploreRule,
          modifier = Modifier
            .weight(1f)
            .height(48.dp),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = AppThemeColors.primary)
        ) {
          Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Voir la règle")
        }
      }
    }
  }
}

@Composable
private fun NoDrillsAvailableCard(rule: StructuredRule) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
  ) {
    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
        text = "Aucun exercice disponible pour ce mode sur « ${rule.titleFr} ».",
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center
      )
    }
  }
}
