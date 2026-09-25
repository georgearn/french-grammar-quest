package com.example.ui.exploratory

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WarningAmber
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
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.engine.DecisionStep
import com.example.data.engine.FormulaToken
import com.example.data.engine.GrammarRuleRepository
import com.example.data.engine.InlineAnnotation
import com.example.data.engine.RegisterExample
import com.example.data.engine.RegisterType
import com.example.data.engine.StructuredRule
import com.example.data.engine.TokenCategory
import com.example.data.engine.VisualStructure
import com.example.ui.theme.AppThemeColors

@Composable
fun ExploratoryPathScreen(
  selectedRuleId: String,
  onSelectRule: (String) -> Unit,
  onStartDrillForRule: (String) -> Unit,
  onSpeakFrench: (String) -> Unit,
  onStopAudio: () -> Unit,
  isSpeaking: Boolean,
  onOpenIndex: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val allRules = remember { GrammarRuleRepository.getAllRules() }
  var selectedLevel by remember { mutableStateOf("TOUS") }
  val filteredRules = remember(selectedLevel, allRules) {
    if (selectedLevel == "TOUS") allRules else allRules.filter { it.level.equals(selectedLevel, ignoreCase = true) }
  }

  // Keep the displayed rule in sync with the level filter: if the currently shown rule
  // doesn't belong to the newly selected level, jump to the first rule that does, rather
  // than silently keeping the old rule on screen while the filter says otherwise.
  LaunchedEffect(selectedLevel, filteredRules) {
    if (filteredRules.isNotEmpty() && filteredRules.none { it.id == selectedRuleId }) {
      onSelectRule(filteredRules.first().id)
    }
  }

  val activeRule = remember(selectedRuleId, allRules) {
    GrammarRuleRepository.getRuleById(selectedRuleId) ?: allRules.firstOrNull() ?: allRules[0]
  }

  val activeIndexInFiltered = remember(activeRule, filteredRules) {
    filteredRules.indexOfFirst { it.id == activeRule.id }.let { if (it < 0) 0 else it }
  }

  // On-demand "explain in English" trigger. Off by default (French-first exploration);
  // the learner flips it whenever the French explanation doesn't land for a given rule.
  var showEnglish by remember { mutableStateOf(false) }

  val contrastPartner = remember(activeRule, allRules) {
    activeRule.contrastGroupId?.let { groupId ->
      allRules.firstOrNull { it.id != activeRule.id && it.contrastGroupId == groupId }
    }
  }

  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .verticalScroll(scrollState)
      .padding(16.dp)
      .testTag("exploratory_screen")
  ) {
    // Header Banner
    ExploratoryHeaderBanner(
      onOpenIndex = onOpenIndex,
      showEnglish = showEnglish,
      onToggleEnglish = { showEnglish = !showEnglish }
    )

    Spacer(modifier = Modifier.height(16.dp))

    // CEFR Level Filter Row
    LevelFilterChipsRow(
      selectedLevel = selectedLevel,
      onSelectLevel = { selectedLevel = it }
    )

    Spacer(modifier = Modifier.height(12.dp))

    // Rules Horizontal Selector
    RuleSelectorChips(
      rules = filteredRules,
      activeRuleId = activeRule.id,
      onSelectRule = onSelectRule
    )

    Spacer(modifier = Modifier.height(14.dp))

    if (filteredRules.isEmpty()) {
      // The level filter matches no rule at all: show that plainly instead of leaving
      // the previous rule's content on screen looking as if it still applied.
      NoRulesForLevelCard(level = selectedLevel, onShowAll = { selectedLevel = "TOUS" })
      Spacer(modifier = Modifier.height(36.dp))
      return@Column
    }

    // Sequential "one rule at a time" navigation: Prev / position / Next
    RuleSequenceNavRow(
      currentIndex = activeIndexInFiltered,
      total = filteredRules.size,
      onPrevious = {
        if (filteredRules.isNotEmpty()) {
          val prevIndex = (activeIndexInFiltered - 1 + filteredRules.size) % filteredRules.size
          onSelectRule(filteredRules[prevIndex].id)
        }
      },
      onNext = {
        if (filteredRules.isNotEmpty()) {
          val nextIndex = (activeIndexInFiltered + 1) % filteredRules.size
          onSelectRule(filteredRules[nextIndex].id)
        }
      }
    )

    Spacer(modifier = Modifier.height(20.dp))

    // Rule Title & Overview Card (title/summary, pillar tag, trigger + contrast callouts)
    RuleOverviewCard(
      rule = activeRule,
      showEnglish = showEnglish,
      contrastPartner = contrastPartner,
      onJumpToContrastPartner = { partnerId -> onSelectRule(partnerId) }
    )

    Spacer(modifier = Modifier.height(20.dp))

    // 1. VISUAL STRUCTURAL EXPLANATION (Diagrams, Formula, Decision Tree, Tables)
    VisualStructureSection(structure = activeRule.visualStructure, showEnglish = showEnglish)

    Spacer(modifier = Modifier.height(24.dp))

    // 2. DIVERSIFIED EXAMPLES BY REGISTER (Courant, Soutenu, Familier, Professionnel)
    RegisterExamplesSection(examples = activeRule.registerExamples)

    Spacer(modifier = Modifier.height(24.dp))

    // 3. CONTEXTUAL PASSAGE WITH INLINE ANNOTATIONS & NATIVE AUDIO
    ContextualPassageSection(
      rule = activeRule,
      onSpeakFrench = onSpeakFrench,
      onStopAudio = onStopAudio,
      isSpeaking = isSpeaking,
      showEnglish = showEnglish
    )

    Spacer(modifier = Modifier.height(32.dp))

    // Bottom Action CTA: Transition directly to the Training/Production Drill path
    Button(
      onClick = { onStartDrillForRule(activeRule.id) },
      modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .testTag("go_to_production_drill_btn"),
      colors = ButtonDefaults.buttonColors(
        containerColor = AppThemeColors.primary,
        contentColor = Color.White
      ),
      shape = RoundedCornerShape(14.dp),
      elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
    ) {
      Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(20.dp))
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "S'entraîner sur cette règle (Mode Production)",
        fontSize = 16.5.sp,
        fontWeight = FontWeight.Bold
      )
    }

    Spacer(modifier = Modifier.height(36.dp))
  }
}

@Composable
private fun ExploratoryHeaderBanner(
  onOpenIndex: () -> Unit,
  showEnglish: Boolean,
  onToggleEnglish: () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
  ) {
    Row(
      modifier = Modifier.padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(AppThemeColors.primaryContainer),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Lightbulb,
          contentDescription = null,
          tint = AppThemeColors.onPrimaryContainer,
          modifier = Modifier.size(24.dp)
        )
      }

      Spacer(modifier = Modifier.width(14.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = "Exploration & Compréhension",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "Visualisez les structures, comparez les registres et écoutez les règles en contexte.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      // On-demand English explanation trigger: "I might not get the rule itself sometimes."
      Surface(
        onClick = onToggleEnglish,
        shape = RoundedCornerShape(20.dp),
        color = if (showEnglish) AppThemeColors.primary else MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, if (showEnglish) AppThemeColors.primary else MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.testTag("toggle_english_explanations")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Language,
            contentDescription = "Basculer les explications en anglais",
            tint = if (showEnglish) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "EN",
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Bold,
            color = if (showEnglish) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.width(4.dp))

      IconButton(onClick = onOpenIndex) {
        Icon(
          imageVector = Icons.Default.MenuBook,
          contentDescription = "Aller à l'index (Codex)",
          tint = AppThemeColors.primary
        )
      }
    }
  }
}

@Composable
private fun RuleSequenceNavRow(
  currentIndex: Int,
  total: Int,
  onPrevious: () -> Unit,
  onNext: () -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    OutlinedButton(onClick = onPrevious, enabled = total > 1, shape = RoundedCornerShape(20.dp)) {
      Text("◀ Précédente", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
    }

    Text(
      text = if (total > 0) "Règle ${currentIndex + 1} / $total" else "—",
      style = MaterialTheme.typography.labelMedium,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    OutlinedButton(onClick = onNext, enabled = total > 1, shape = RoundedCornerShape(20.dp)) {
      Text("Suivante ▶", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
    }
  }
}

@Composable
private fun NoRulesForLevelCard(level: String, onShowAll: () -> Unit) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(
        imageVector = Icons.Default.MenuBook,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.size(28.dp)
      )
      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = "Aucune règle disponible pour le niveau $level pour le moment.",
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(14.dp))
      OutlinedButton(onClick = onShowAll, shape = RoundedCornerShape(20.dp)) {
        Text("Voir tous les niveaux", fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
      }
    }
  }
}

@Composable
private fun LevelFilterChipsRow(
  selectedLevel: String,
  onSelectLevel: (String) -> Unit
) {
  val levels = listOf("TOUS", "A1", "A2", "B1", "B2", "C1", "C2")
  val scrollState = rememberScrollState()

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .horizontalScroll(scrollState),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    levels.forEach { lvl ->
      val isSelected = selectedLevel.equals(lvl, ignoreCase = true)
      FilterChip(
        selected = isSelected,
        onClick = { onSelectLevel(lvl) },
        label = {
          Text(
            text = lvl,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 12.5.sp
          )
        },
        colors = FilterChipDefaults.filterChipColors(
          selectedContainerColor = AppThemeColors.primary,
          selectedLabelColor = Color.White,
          containerColor = MaterialTheme.colorScheme.surface,
          labelColor = MaterialTheme.colorScheme.onSurface
        )
      )
    }
  }
}

@Composable
private fun RuleSelectorChips(
  rules: List<StructuredRule>,
  activeRuleId: String,
  onSelectRule: (String) -> Unit
) {
  val scrollState = rememberScrollState()

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .horizontalScroll(scrollState),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    rules.forEach { rule ->
      val isActive = rule.id == activeRuleId
      Surface(
        onClick = { onSelectRule(rule.id) },
        shape = RoundedCornerShape(12.dp),
        color = if (isActive) AppThemeColors.primaryContainer else MaterialTheme.colorScheme.surface,
        border = BorderStroke(
          width = if (isActive) 1.5.dp else 1.dp,
          color = if (isActive) AppThemeColors.primary else MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = Modifier.height(40.dp)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = rule.level,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (isActive) AppThemeColors.onPrimaryContainer else AppThemeColors.primary,
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(if (isActive) Color.White.copy(alpha = 0.7f) else AppThemeColors.primaryContainer)
              .padding(horizontal = 6.dp, vertical = 2.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = rule.titleFr.split(":").firstOrNull()?.trim() ?: rule.titleFr,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
            color = if (isActive) AppThemeColors.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }
  }
}

private fun pillarLabel(pillar: com.example.data.engine.GrammarPillar, en: Boolean): String? =
  when (pillar) {
    com.example.data.engine.GrammarPillar.AGREEMENT_ENGINE -> if (en) "Agreement Engine" else "Moteur d'accord"
    com.example.data.engine.GrammarPillar.VERB_SYSTEM -> if (en) "Verb System" else "Système verbal"
    com.example.data.engine.GrammarPillar.PRONOUN_HIERARCHY -> if (en) "Pronoun Hierarchy" else "Hiérarchie des pronoms"
    com.example.data.engine.GrammarPillar.PREPOSITIONAL_REGIME -> if (en) "Prepositional Regime" else "Régime prépositionnel"
    com.example.data.engine.GrammarPillar.REGISTER_NEGATION -> if (en) "Register & Negation" else "Registre & négation"
    com.example.data.engine.GrammarPillar.OTHER -> null
  }

@Composable
private fun RuleOverviewCard(
  rule: StructuredRule,
  showEnglish: Boolean,
  contrastPartner: StructuredRule?,
  onJumpToContrastPartner: (String) -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(AppThemeColors.primaryContainer)
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Text(
            text = "NIVEAU ${rule.level}",
            fontSize = 11.5.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AppThemeColors.onPrimaryContainer
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = rule.categoryName,
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        pillarLabel(rule.pillar, showEnglish)?.let { label ->
          Spacer(modifier = Modifier.width(8.dp))
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant)
              .padding(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Text(
              text = label.uppercase(),
              fontSize = 10.5.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = rule.titleFr,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )

      Text(
        text = rule.titleEn,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(12.dp))

      Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.Top
        ) {
          Icon(
            imageVector = Icons.Default.MenuBook,
            contentDescription = null,
            tint = AppThemeColors.primary,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          val summaryText = if (showEnglish) {
            rule.ruleExplanationEn.ifBlank { rule.summaryEn }
          } else {
            rule.summaryFr
          }
          Text(
            text = summaryText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 18.sp
          )
        }
      }

      // Trigger-Action Pairing: surface the concrete trigger token instead of an abstract rule.
      rule.triggerToken?.let { trigger ->
        Spacer(modifier = Modifier.height(10.dp))
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = AppThemeColors.flameContainer
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Bolt,
              contentDescription = null,
              tint = AppThemeColors.onFlameContainer,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "« $trigger » → ${if (showEnglish) (rule.triggerActionEn ?: rule.triggerActionFr ?: "") else (rule.triggerActionFr ?: "")}",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = AppThemeColors.onFlameContainer
              )
              Text(
                text = if (showEnglish) "Trigger, not theory: memorize the phrase, not the abstract rule." else "Un déclencheur automatique, pas une théorie à retenir.",
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
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.CompareArrows,
              contentDescription = null,
              tint = AppThemeColors.onGoldContainer,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
              val roleLabel = if (showEnglish) rule.contrastRoleLabelEn else rule.contrastRoleLabelFr
              Text(
                text = roleLabel?.let { "$it — " }.orEmpty() +
                  (if (showEnglish) "Contrast with: " else "À contraster avec : ") + partner.titleFr,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = AppThemeColors.onGoldContainer
              )
              Text(
                text = if (showEnglish) "Learn the condition, not a conjugation table." else "Mémorisez la condition, pas un tableau de conjugaison.",
                style = MaterialTheme.typography.bodySmall,
                color = AppThemeColors.onGoldContainer.copy(alpha = 0.85f)
              )
            }
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun VisualStructureSection(structure: VisualStructure, showEnglish: Boolean) {
  var selectedToken by remember { mutableStateOf<FormulaToken?>(null) }

  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      // Header
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.AccountTree,
          contentDescription = null,
          tint = AppThemeColors.primary,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Structure Visuelle & Schéma",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "Formule morphologique de la phrase (touchez un bloc pour inspecter son rôle) :",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Formula Tokens Flow
      FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        structure.formulaTokens.forEach { token ->
          val isSelected = selectedToken?.text == token.text
          val (badgeBg, badgeFg) = when (token.category) {
            TokenCategory.SUBJECT -> Pair(AppThemeColors.primaryContainer, AppThemeColors.onPrimaryContainer)
            TokenCategory.TRIGGER -> Pair(AppThemeColors.flameContainer, AppThemeColors.onFlameContainer)
            TokenCategory.CONNECTOR -> Pair(AppThemeColors.goldContainer, AppThemeColors.onGoldContainer)
            TokenCategory.TARGET_VERB -> Pair(AppThemeColors.successContainer, AppThemeColors.onSuccessContainer)
            TokenCategory.OBJECT -> Pair(AppThemeColors.redContainer, AppThemeColors.onRedContainer)
            TokenCategory.MODIFIER -> Pair(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant)
          }

          Surface(
            onClick = { selectedToken = if (isSelected) null else token },
            shape = RoundedCornerShape(10.dp),
            color = badgeBg,
            border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) badgeFg else Color.Transparent),
            modifier = Modifier.animateContentSize()
          ) {
            Column(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = token.text,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = badgeFg
              )
              Text(
                text = token.label,
                fontSize = 10.5.sp,
                color = badgeFg.copy(alpha = 0.85f)
              )
            }
          }
        }
      }

      // Selected Token Detailed Inspector Card
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
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.Top
            ) {
              Icon(
                imageVector = Icons.Default.HelpOutline,
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
                  text = if (showEnglish) (token.explanationEn ?: token.explanation) else token.explanation,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))
      HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
      Spacer(modifier = Modifier.height(14.dp))

      // Decision Flow Steps
      Text(
        text = structure.diagramTitle,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = if (showEnglish) (structure.diagramDescriptionEn ?: structure.diagramDescription) else structure.diagramDescription,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(10.dp))

      structure.decisionSteps.forEach { step ->
        DecisionStepItem(step = step)
        Spacer(modifier = Modifier.height(8.dp))
      }

      // Contrast / Comparison Table if available
      structure.comparisonTableTitle?.let { tableTitle ->
        Spacer(modifier = Modifier.height(14.dp))
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
          Text(
            text = tableTitle,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
          )
        }

        Spacer(modifier = Modifier.height(10.dp))
        ComparisonTable(headers = structure.comparisonHeaders, rows = structure.comparisonRows)
      }
    }
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
            .size(22.dp)
            .clip(CircleShape)
            .background(AppThemeColors.primary),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "${step.stepNumber}",
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = step.question,
          style = MaterialTheme.typography.bodySmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 30.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // OUI
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = AppThemeColors.successContainer,
          modifier = Modifier.weight(1f)
        ) {
          Text(
            text = "OUI: ${step.ifYes}",
            fontSize = 11.5.sp,
            color = AppThemeColors.onSuccessContainer,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
          )
        }
        // NON
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = AppThemeColors.flameContainer,
          modifier = Modifier.weight(1f)
        ) {
          Text(
            text = "NON: ${step.ifNo}",
            fontSize = 11.5.sp,
            color = AppThemeColors.onFlameContainer,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun ComparisonTable(
  headers: List<String>,
  rows: List<List<String>>
) {
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
      .horizontalScroll(scrollState)
  ) {
    // Header row
    Row(
      modifier = Modifier
        .background(AppThemeColors.primaryContainer)
        .padding(vertical = 8.dp)
    ) {
      headers.forEach { header ->
        Text(
          text = header,
          fontSize = 11.5.sp,
          fontWeight = FontWeight.Bold,
          color = AppThemeColors.onPrimaryContainer,
          modifier = Modifier
            .width(180.dp)
            .padding(horizontal = 8.dp)
        )
      }
    }

    // Data rows
    rows.forEachIndexed { index, rowList ->
      Row(
        modifier = Modifier
          .background(if (index % 2 == 0) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
          .padding(vertical = 8.dp)
      ) {
        rowList.forEach { cell ->
          Text(
            text = cell,
            fontSize = 11.5.sp,
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
private fun RegisterExamplesSection(examples: List<RegisterExample>) {
  var selectedTabIdx by remember { mutableStateOf(0) }
  val activeExample = examples.getOrNull(selectedTabIdx) ?: examples.firstOrNull()

  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.ChatBubbleOutline,
          contentDescription = null,
          tint = AppThemeColors.primary,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Exemples par Registres de Langue",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = "Découvrez comment la règle s'adapte selon le contexte d'énonciation :",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Register Tabs
      val tabTitles = examples.map { ex ->
        when (ex.register) {
          RegisterType.COURANT -> "Courant"
          RegisterType.SOUTENU -> "Soutenu"
          RegisterType.FAMILIER -> "Familier"
          RegisterType.PROFESSIONNEL -> "Pro"
        }
      }

      ScrollableTabRow(
        selectedTabIndex = selectedTabIdx,
        edgePadding = 0.dp,
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        contentColor = AppThemeColors.primary,
        indicator = { tabPositions ->
          TabRowDefaults.SecondaryIndicator(
            Modifier.tabIndicatorOffset(tabPositions[selectedTabIdx]),
            color = AppThemeColors.primary
          )
        }
      ) {
        tabTitles.forEachIndexed { index, title ->
          Tab(
            selected = selectedTabIdx == index,
            onClick = { selectedTabIdx = index },
            text = {
              Text(
                text = title,
                fontWeight = if (selectedTabIdx == index) FontWeight.Bold else FontWeight.Medium,
                fontSize = 12.5.sp
              )
            }
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Register Example Content Card
      activeExample?.let { ex ->
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            // French Sentence
            Text(
              text = "« ${ex.frenchSentence} »",
              style = MaterialTheme.typography.bodyLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            // English Translation
            Text(
              text = ex.englishSentence,
              style = MaterialTheme.typography.bodyMedium,
              fontStyle = FontStyle.Italic,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(10.dp))

            // Nuance Note
            Row(verticalAlignment = Alignment.Top) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = AppThemeColors.success,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Nuance de registre : ${ex.contextNote}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun ContextualPassageSection(
  rule: StructuredRule,
  onSpeakFrench: (String) -> Unit,
  onStopAudio: () -> Unit,
  isSpeaking: Boolean,
  showEnglish: Boolean
) {
  val passage = rule.contextualPassage
  var selectedAnnotationId by remember { mutableStateOf<String?>(null) }
  val activeAnnotation = passage.annotations.find { it.id == selectedAnnotationId }

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
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Texte en Contexte & Écoute",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = passage.title,
            style = MaterialTheme.typography.labelMedium,
            color = AppThemeColors.primary
          )
        }

        // Native French TTS Listen Button
        Button(
          onClick = {
            if (isSpeaking) onStopAudio() else onSpeakFrench(passage.fullTextFr)
          },
          shape = RoundedCornerShape(20.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = if (isSpeaking) AppThemeColors.redContainer else AppThemeColors.primaryContainer,
            contentColor = if (isSpeaking) AppThemeColors.onRedContainer else AppThemeColors.onPrimaryContainer
          ),
          contentPadding = ButtonDefaults.ContentPadding
        ) {
          Icon(
            imageVector = if (isSpeaking) Icons.Default.Pause else Icons.Default.VolumeUp,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (isSpeaking) "Arrêter" else "Écouter",
            fontWeight = FontWeight.Bold,
            fontSize = 12.5.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Reading text container with clickable annotations
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = passage.fullTextFr,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = passage.fullTextEn,
            style = MaterialTheme.typography.bodySmall,
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Annotation selector chips
      Text(
        text = "Points grammaticaux annotés dans ce texte (touchez pour analyser) :",
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
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
            onClick = {
              selectedAnnotationId = if (isSelected) null else ann.id
            },
            label = {
              Text(
                text = "« ${ann.targetPhrase} »",
                fontSize = 12.5.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
              )
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = AppThemeColors.primary,
              selectedLabelColor = Color.White
            )
          )
        }
      }

      // Active Annotation Analysis Pop-out
      AnimatedVisibility(visible = activeAnnotation != null) {
        activeAnnotation?.let { ann ->
          Spacer(modifier = Modifier.height(12.dp))
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = AppThemeColors.primaryContainer.copy(alpha = 0.3f),
            border = BorderStroke(1.5.dp, AppThemeColors.primary)
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
                  text = "Analyse : « ${ann.targetPhrase} »",
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }

              Spacer(modifier = Modifier.height(6.dp))

              Text(
                text = if (showEnglish) ann.explanationEn else ann.explanationFr,
                style = MaterialTheme.typography.bodySmall,
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
                    text = "Piège à éviter : $pitfall",
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
  }
}
