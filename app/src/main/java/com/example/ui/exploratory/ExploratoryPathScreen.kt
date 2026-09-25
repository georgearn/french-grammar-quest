package com.example.ui.exploratory

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.engine.DecisionStep
import com.example.data.engine.FormulaToken
import com.example.data.engine.GrammarPillar
import com.example.data.engine.RegisterExample
import com.example.data.engine.RegisterType
import com.example.data.engine.RuleGraph
import com.example.data.engine.StructuredRule
import com.example.data.engine.TokenCategory
import com.example.data.engine.VisualStructure
import com.example.ui.GrammarViewModel
import com.example.ui.components.CollapsibleSection
import com.example.ui.components.EnglishHelpText
import com.example.ui.components.RulePickerSheet
import com.example.ui.components.RuleSelectorField
import com.example.ui.components.filterRules
import com.example.ui.components.ruleTitle
import com.example.ui.i18n.isEnglish
import com.example.ui.i18n.localized
import com.example.ui.theme.AppThemeColors

/**
 * "Comprendre": one rule at a time. The rule is picked from a compact field (or stepped through
 * with the arrows), its content is split into collapsible sections, and the next step — practising
 * the rule — stays visible at the bottom instead of at the end of a long scroll.
 */
@Composable
fun ExploratoryPathScreen(
  viewModel: GrammarViewModel,
  onTrainRule: (String) -> Unit,
  onCreateForRule: (String) -> Unit,
  onShowOnMap: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val ruleId by viewModel.exploreRuleId.collectAsStateWithLifecycle()
  val levelFilter by viewModel.levelFilter.collectAsStateWithLifecycle()
  val isSpeaking by viewModel.isSpeaking.collectAsStateWithLifecycle()
  val progressMap by viewModel.ruleTrainingProgressMap.collectAsStateWithLifecycle()
  val rule = viewModel.ruleById(ruleId)

  val sequence = remember(levelFilter) { filterRules(viewModel.rules, levelFilter) }
  val position = sequence.indexOfFirst { it.id == rule.id }
  val previousRule = if (position > 0) sequence[position - 1] else null
  val nextRule = when {
    position < 0 -> sequence.firstOrNull()
    else -> sequence.getOrNull(position + 1)
  }

  val contrastPartner = remember(rule) {
    rule.contrastGroupId?.let { groupId ->
      viewModel.rules.firstOrNull { it.id != rule.id && it.contrastGroupId == groupId }
    }
  }

  val mapExploredIds by viewModel.mapExploredIds.collectAsStateWithLifecycle()
  val relatedRules = remember(rule) {
    (RuleGraph.crossRefs(rule.id) + listOfNotNull(contrastPartner?.id))
      .distinct()
      .map(viewModel::ruleById)
  }

  // Every rule shown here joins the grammar map.
  LaunchedEffect(rule.id) { viewModel.markVisited(rule.id) }

  var showPicker by rememberSaveable { mutableStateOf(false) }
  var relatedExpanded by rememberSaveable { mutableStateOf(true) }
  var structureExpanded by rememberSaveable { mutableStateOf(true) }
  var examplesExpanded by rememberSaveable { mutableStateOf(false) }
  var passageExpanded by rememberSaveable { mutableStateOf(false) }

  // Back to the top when the rule changes, but not when the user merely comes back to this tab.
  val scrollState = rememberScrollState()
  var lastShownRuleId by rememberSaveable { mutableStateOf(rule.id) }
  LaunchedEffect(rule.id) {
    if (lastShownRuleId != rule.id) {
      scrollState.scrollTo(0)
      lastShownRuleId = rule.id
    }
  }

  Column(modifier = modifier.fillMaxSize().testTag("exploratory_screen")) {
    Column(
      modifier = Modifier
        .weight(1f)
        .verticalScroll(scrollState)
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      RuleNavigator(
        rule = rule,
        supportingText = positionText(position, sequence.size, levelFilter),
        onPrevious = previousRule?.let { { viewModel.selectExploreRule(it.id) } },
        onNext = nextRule?.let { { viewModel.selectExploreRule(it.id) } },
        onOpenPicker = { showPicker = true }
      )

      RuleOverviewCard(
        rule = rule,
        contrastPartner = contrastPartner,
        onJumpToContrastPartner = viewModel::selectExploreRule
      )

      CollapsibleSection(
        title = stringResource(R.string.explore_related),
        subtitle = stringResource(R.string.explore_related_subtitle),
        icon = Icons.Default.Hub,
        expanded = relatedExpanded,
        onToggle = { relatedExpanded = !relatedExpanded }
      ) {
        RelatedRulesContent(
          related = relatedRules,
          exploredIds = mapExploredIds,
          onOpenRule = viewModel::selectExploreRule,
          onShowOnMap = { onShowOnMap(rule.id) }
        )
      }

      CollapsibleSection(
        title = stringResource(R.string.section_structure),
        subtitle = rule.visualStructure.diagramTitle,
        icon = Icons.Default.AccountTree,
        expanded = structureExpanded,
        onToggle = { structureExpanded = !structureExpanded }
      ) {
        VisualStructureContent(structure = rule.visualStructure)
      }

      if (rule.registerExamples.isNotEmpty()) {
        CollapsibleSection(
          title = stringResource(R.string.section_examples),
          subtitle = stringResource(R.string.section_examples_subtitle),
          icon = Icons.Default.ChatBubbleOutline,
          expanded = examplesExpanded,
          onToggle = { examplesExpanded = !examplesExpanded }
        ) {
          RegisterExamplesContent(examples = rule.registerExamples, ruleId = rule.id)
        }
      }

      CollapsibleSection(
        title = stringResource(R.string.section_passage),
        subtitle = rule.contextualPassage.title,
        icon = Icons.AutoMirrored.Filled.MenuBook,
        expanded = passageExpanded,
        onToggle = { passageExpanded = !passageExpanded }
      ) {
        ContextualPassageContent(
          rule = rule,
          isSpeaking = isSpeaking,
          onSpeakFrench = viewModel::speakFrench,
          onStopAudio = viewModel::stopAudio
        )
      }
    }

    ExploreActionBar(
      onTrain = { onTrainRule(rule.id) },
      onCreate = { onCreateForRule(rule.id) }
    )
  }

  if (showPicker) {
    RulePickerSheet(
      rules = viewModel.rules,
      selectedRuleId = rule.id,
      levelFilter = levelFilter,
      onLevelFilterChange = viewModel::setLevelFilter,
      onSelectRule = viewModel::selectExploreRule,
      onDismiss = { showPicker = false },
      progressMap = progressMap
    )
  }
}

@Composable
fun positionText(position: Int, total: Int, levelFilter: String?): String? {
  if (position < 0 || total == 0) return levelFilter?.let { stringResource(R.string.rule_outside_filter, it) }
  val base = stringResource(R.string.rule_position, position + 1, total)
  return if (levelFilter != null) "$base · $levelFilter" else base
}

/** Previous / rule field / next. The arrows are disabled at the ends instead of wrapping around. */
@Composable
private fun RuleNavigator(
  rule: StructuredRule,
  supportingText: String?,
  onPrevious: (() -> Unit)?,
  onNext: (() -> Unit)?,
  onOpenPicker: () -> Unit
) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    IconButton(onClick = { onPrevious?.invoke() }, enabled = onPrevious != null) {
      Icon(Icons.Default.ChevronLeft, contentDescription = stringResource(R.string.rule_previous))
    }
    RuleSelectorField(
      rule = rule,
      supportingText = supportingText,
      onClick = onOpenPicker,
      modifier = Modifier.weight(1f)
    )
    IconButton(onClick = { onNext?.invoke() }, enabled = onNext != null) {
      Icon(Icons.Default.ChevronRight, contentDescription = stringResource(R.string.rule_next))
    }
  }
}

@Composable
private fun ExploreActionBar(onTrain: () -> Unit, onCreate: () -> Unit) {
  Surface(tonalElevation = 3.dp, shadowElevation = 6.dp) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Button(
        onClick = onTrain,
        modifier = Modifier
          .weight(1f)
          .heightIn(min = 52.dp)
          .testTag("go_to_production_drill_btn"),
        shape = RoundedCornerShape(14.dp)
      ) {
        Text(stringResource(R.string.explore_train_rule), fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(8.dp))
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
      }
      FilledTonalButton(
        onClick = onCreate,
        modifier = Modifier.heightIn(min = 52.dp),
        shape = RoundedCornerShape(14.dp)
      ) {
        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(6.dp))
        Text(stringResource(R.string.explore_create_text))
      }
    }
  }
}

/** Linked rules as chips (explored ones filled), plus the way to the grammar map. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RelatedRulesContent(
  related: List<StructuredRule>,
  exploredIds: Set<String>,
  onOpenRule: (String) -> Unit,
  onShowOnMap: () -> Unit
) {
  if (related.isEmpty()) {
    Text(
      text = stringResource(R.string.explore_no_related),
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  } else {
    FlowRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      related.forEach { other ->
        val explored = other.id in exploredIds
        FilterChip(
          selected = explored,
          onClick = { onOpenRule(other.id) },
          label = { Text("${other.level} · ${ruleTitle(other)}") },
          leadingIcon = if (explored) {
            { Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp)) }
          } else {
            null
          }
        )
      }
    }
  }
  Spacer(Modifier.height(8.dp))
  TextButton(onClick = onShowOnMap) {
    Icon(Icons.Default.Hub, contentDescription = null, modifier = Modifier.size(18.dp))
    Spacer(Modifier.width(6.dp))
    Text(stringResource(R.string.explore_show_on_map))
  }
}

@Composable
private fun pillarLabel(pillar: GrammarPillar): String? = when (pillar) {
  GrammarPillar.AGREEMENT_ENGINE -> stringResource(R.string.pillar_agreement)
  GrammarPillar.VERB_SYSTEM -> stringResource(R.string.pillar_verb)
  GrammarPillar.PRONOUN_HIERARCHY -> stringResource(R.string.pillar_pronoun)
  GrammarPillar.PREPOSITIONAL_REGIME -> stringResource(R.string.pillar_preposition)
  GrammarPillar.REGISTER_NEGATION -> stringResource(R.string.pillar_register)
  GrammarPillar.OTHER -> null
}

@Composable
private fun RuleOverviewCard(
  rule: StructuredRule,
  contrastPartner: StructuredRule?,
  onJumpToContrastPartner: (String) -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = rule.categoryName,
          style = MaterialTheme.typography.labelLarge,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        pillarLabel(rule.pillar)?.let { label ->
          Spacer(modifier = Modifier.width(8.dp))
          Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
            Text(
              text = label,
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = rule.titleFr,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      EnglishHelpText(rule.titleEn)

      Spacer(modifier = Modifier.height(12.dp))

      Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
      ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.MenuBook,
            contentDescription = null,
            tint = AppThemeColors.primary,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = localized(rule.summaryFr, rule.ruleExplanationEn.ifBlank { rule.summaryEn }),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }

      // Trigger-Action Pairing: surface the concrete trigger token instead of an abstract rule.
      rule.triggerToken?.let { trigger ->
        Spacer(modifier = Modifier.height(10.dp))
        Surface(shape = RoundedCornerShape(10.dp), color = AppThemeColors.flameContainer) {
          Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Bolt,
              contentDescription = null,
              tint = AppThemeColors.onFlameContainer,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "« $trigger » → ${localized(rule.triggerActionFr.orEmpty(), rule.triggerActionEn)}",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = AppThemeColors.onFlameContainer
              )
              Text(
                text = stringResource(R.string.trigger_hint),
                style = MaterialTheme.typography.bodySmall,
                color = AppThemeColors.onFlameContainer.copy(alpha = 0.85f)
              )
            }
          }
        }
      }

      // Mental Model Triads: point at the contrasting rule to learn as an opposing pair.
      contrastPartner?.let { partner ->
        Spacer(modifier = Modifier.height(10.dp))
        Surface(
          onClick = { onJumpToContrastPartner(partner.id) },
          shape = RoundedCornerShape(10.dp),
          color = AppThemeColors.goldContainer
        ) {
          Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.CompareArrows,
              contentDescription = null,
              tint = AppThemeColors.onGoldContainer,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
              val roleLabel = localized(rule.contrastRoleLabelFr.orEmpty(), rule.contrastRoleLabelEn)
              Text(
                text = roleLabel.takeIf { it.isNotBlank() }?.let { "$it — " }.orEmpty() +
                  stringResource(R.string.contrast_with, ruleTitle(partner)),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = AppThemeColors.onGoldContainer
              )
              Text(
                text = stringResource(R.string.contrast_hint),
                style = MaterialTheme.typography.bodySmall,
                color = AppThemeColors.onGoldContainer.copy(alpha = 0.85f)
              )
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = AppThemeColors.onGoldContainer)
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun VisualStructureContent(structure: VisualStructure) {
  var selectedTokenIndex by rememberSaveable(structure) { mutableStateOf<Int?>(null) }
  val selectedToken: FormulaToken? = selectedTokenIndex?.let { structure.formulaTokens.getOrNull(it) }

  Text(
    text = stringResource(R.string.structure_formula_hint),
    style = MaterialTheme.typography.bodySmall,
    color = MaterialTheme.colorScheme.onSurfaceVariant
  )

  Spacer(modifier = Modifier.height(12.dp))

  FlowRow(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    structure.formulaTokens.forEachIndexed { index, token ->
      val isSelected = selectedTokenIndex == index
      val (badgeBg, badgeFg) = when (token.category) {
        TokenCategory.SUBJECT -> Pair(AppThemeColors.primaryContainer, AppThemeColors.onPrimaryContainer)
        TokenCategory.TRIGGER -> Pair(AppThemeColors.flameContainer, AppThemeColors.onFlameContainer)
        TokenCategory.CONNECTOR -> Pair(AppThemeColors.goldContainer, AppThemeColors.onGoldContainer)
        TokenCategory.TARGET_VERB -> Pair(AppThemeColors.successContainer, AppThemeColors.onSuccessContainer)
        TokenCategory.OBJECT -> Pair(AppThemeColors.redContainer, AppThemeColors.onRedContainer)
        TokenCategory.MODIFIER -> Pair(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant)
      }

      Surface(
        onClick = { selectedTokenIndex = if (isSelected) null else index },
        shape = RoundedCornerShape(10.dp),
        color = badgeBg,
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) badgeFg else Color.Transparent),
        modifier = Modifier
          .heightIn(min = 48.dp)
          .animateContentSize()
          .semantics { contentDescription = "${token.text}, ${token.label}" }
      ) {
        Column(
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(text = token.text, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = badgeFg)
          Text(text = token.label, style = MaterialTheme.typography.labelSmall, color = badgeFg.copy(alpha = 0.85f))
        }
      }
    }
  }

  AnimatedVisibility(visible = selectedToken != null) {
    selectedToken?.let { token ->
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 12.dp),
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
      ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.HelpOutline,
            contentDescription = null,
            tint = AppThemeColors.primary,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(
              text = "${token.text} (${token.label})",
              style = MaterialTheme.typography.labelLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = localized(token.explanation, token.explanationEn),
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }
  }

  Spacer(modifier = Modifier.height(16.dp))
  HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
  Spacer(modifier = Modifier.height(12.dp))

  Text(
    text = structure.diagramTitle,
    style = MaterialTheme.typography.titleSmall,
    fontWeight = FontWeight.Bold,
    color = MaterialTheme.colorScheme.onSurface
  )
  Text(
    text = localized(structure.diagramDescription, structure.diagramDescriptionEn),
    style = MaterialTheme.typography.bodySmall,
    color = MaterialTheme.colorScheme.onSurfaceVariant
  )

  Spacer(modifier = Modifier.height(10.dp))

  structure.decisionSteps.forEach { step ->
    DecisionStepItem(step = step)
    Spacer(modifier = Modifier.height(8.dp))
  }

  structure.comparisonTableTitle?.let { tableTitle ->
    Spacer(modifier = Modifier.height(8.dp))
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    Spacer(modifier = Modifier.height(12.dp))

    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        imageVector = Icons.Default.TableChart,
        contentDescription = null,
        tint = AppThemeColors.primary,
        modifier = Modifier.size(18.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(text = tableTitle, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
    }

    Spacer(modifier = Modifier.height(10.dp))
    ComparisonTable(headers = structure.comparisonHeaders, rows = structure.comparisonRows)
  }
}

@Composable
private fun DecisionStepItem(step: DecisionStep) {
  Surface(
    shape = RoundedCornerShape(10.dp),
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(24.dp)
            .background(MaterialTheme.colorScheme.primary, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "${step.stepNumber}",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = step.question,
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 32.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Surface(shape = RoundedCornerShape(8.dp), color = AppThemeColors.successContainer, modifier = Modifier.weight(1f)) {
          Text(
            text = stringResource(R.string.decision_yes, step.ifYes),
            style = MaterialTheme.typography.bodySmall,
            color = AppThemeColors.onSuccessContainer,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
          )
        }
        Surface(shape = RoundedCornerShape(8.dp), color = AppThemeColors.flameContainer, modifier = Modifier.weight(1f)) {
          Text(
            text = stringResource(R.string.decision_no, step.ifNo),
            style = MaterialTheme.typography.bodySmall,
            color = AppThemeColors.onFlameContainer,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun ComparisonTable(headers: List<String>, rows: List<List<String>>) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
      .horizontalScroll(rememberScrollState())
  ) {
    Row(
      modifier = Modifier
        .background(AppThemeColors.primaryContainer)
        .padding(vertical = 8.dp)
    ) {
      headers.forEach { header ->
        Text(
          text = header,
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.Bold,
          color = AppThemeColors.onPrimaryContainer,
          modifier = Modifier
            .width(180.dp)
            .padding(horizontal = 8.dp)
        )
      }
    }

    rows.forEachIndexed { index, rowList ->
      Row(
        modifier = Modifier
          .background(
            if (index % 2 == 0) MaterialTheme.colorScheme.surface
            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
          )
          .padding(vertical = 8.dp)
      ) {
        rowList.forEach { cell ->
          Text(
            text = cell,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
              .width(180.dp)
              .padding(horizontal = 8.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun registerLabel(register: RegisterType): String = when (register) {
  RegisterType.COURANT -> stringResource(R.string.register_courant)
  RegisterType.SOUTENU -> stringResource(R.string.register_soutenu)
  RegisterType.FAMILIER -> stringResource(R.string.register_familier)
  RegisterType.PROFESSIONNEL -> stringResource(R.string.register_pro)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegisterExamplesContent(examples: List<RegisterExample>, ruleId: String) {
  var selectedTabIdx by rememberSaveable(ruleId) { mutableStateOf(0) }
  val safeIndex = selectedTabIdx.coerceIn(0, examples.lastIndex)
  val activeExample = examples[safeIndex]

  PrimaryScrollableTabRow(
    selectedTabIndex = safeIndex,
    edgePadding = 0.dp,
    containerColor = Color.Transparent
  ) {
    examples.forEachIndexed { index, ex ->
      Tab(
        selected = safeIndex == index,
        onClick = { selectedTabIdx = index },
        text = { Text(registerLabel(ex.register)) }
      )
    }
  }

  Spacer(modifier = Modifier.height(14.dp))

  Surface(
    shape = RoundedCornerShape(12.dp),
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
  ) {
    Column(modifier = Modifier.padding(14.dp).fillMaxWidth()) {
      Text(
        text = "« ${activeExample.frenchSentence} »",
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      EnglishHelpText(activeExample.englishSentence, Modifier.padding(top = 4.dp))

      Spacer(modifier = Modifier.height(10.dp))
      HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
      Spacer(modifier = Modifier.height(10.dp))

      Row(verticalAlignment = Alignment.Top) {
        Icon(
          imageVector = Icons.Default.CheckCircle,
          contentDescription = null,
          tint = AppThemeColors.success,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = stringResource(R.string.register_nuance, activeExample.contextNote),
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurface
        )
      }
    }
  }
}

@Composable
private fun ContextualPassageContent(
  rule: StructuredRule,
  isSpeaking: Boolean,
  onSpeakFrench: (String) -> Unit,
  onStopAudio: () -> Unit
) {
  val passage = rule.contextualPassage
  var selectedAnnotationId by rememberSaveable(rule.id) { mutableStateOf<String?>(null) }
  val activeAnnotation = passage.annotations.find { it.id == selectedAnnotationId }

  FilledTonalButton(onClick = { if (isSpeaking) onStopAudio() else onSpeakFrench(passage.fullTextFr) }) {
    Icon(
      imageVector = if (isSpeaking) Icons.Default.Pause else Icons.AutoMirrored.Filled.VolumeUp,
      contentDescription = null,
      modifier = Modifier.size(18.dp)
    )
    Spacer(modifier = Modifier.width(6.dp))
    Text(stringResource(if (isSpeaking) R.string.action_stop else R.string.action_listen))
  }

  Spacer(modifier = Modifier.height(12.dp))

  Surface(
    shape = RoundedCornerShape(12.dp),
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Text(
        text = passage.fullTextFr,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface
      )
      EnglishHelpText(passage.fullTextEn, Modifier.padding(top = 10.dp))
    }
  }

  if (passage.annotations.isNotEmpty()) {
    Spacer(modifier = Modifier.height(14.dp))
    Text(
      text = stringResource(R.string.passage_annotations_hint),
      style = MaterialTheme.typography.labelLarge,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(modifier = Modifier.height(8.dp))
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      passage.annotations.forEach { ann ->
        val isSelected = selectedAnnotationId == ann.id
        FilterChip(
          selected = isSelected,
          onClick = { selectedAnnotationId = if (isSelected) null else ann.id },
          label = { Text("« ${ann.targetPhrase} »") }
        )
      }
    }
  }

  AnimatedVisibility(visible = activeAnnotation != null) {
    activeAnnotation?.let { ann ->
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = AppThemeColors.primaryContainer.copy(alpha = 0.3f),
        border = BorderStroke(1.5.dp, AppThemeColors.primary),
        modifier = Modifier.padding(top = 12.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Lightbulb,
              contentDescription = null,
              tint = AppThemeColors.primary,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = stringResource(R.string.passage_analysis, ann.targetPhrase),
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = if (isEnglish) ann.explanationEn else ann.explanationFr,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
          )

          ann.commonPitfall?.let { pitfall ->
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Top) {
              Icon(
                imageVector = Icons.Default.WarningAmber,
                contentDescription = null,
                tint = AppThemeColors.red,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = stringResource(R.string.passage_pitfall, pitfall),
                style = MaterialTheme.typography.bodySmall,
                color = AppThemeColors.red,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }
      }
    }
  }
}
