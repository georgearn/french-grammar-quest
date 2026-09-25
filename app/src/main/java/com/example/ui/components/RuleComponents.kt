package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.engine.StructuredRule
import com.example.data.local.RuleTrainingProgressEntity
import com.example.ui.i18n.isEnglish
import com.example.ui.i18n.localized

val CEFR_LEVELS = listOf("A1", "A2", "B1", "B2", "C1", "C2")

/** A rule's display title in the language currently shown. */
@Composable
fun ruleTitle(rule: StructuredRule): String = localized(rule.titleFr, rule.titleEn)

/** Rules matching an optional level filter and a free-text query (French or English fields). */
fun filterRules(rules: List<StructuredRule>, level: String?, query: String = ""): List<StructuredRule> {
  val q = query.trim()
  return rules.filter { rule ->
    (level == null || rule.level.equals(level, ignoreCase = true)) &&
      (q.isEmpty() ||
        rule.titleFr.contains(q, ignoreCase = true) ||
        rule.titleEn.contains(q, ignoreCase = true) ||
        rule.summaryFr.contains(q, ignoreCase = true) ||
        rule.categoryName.contains(q, ignoreCase = true))
  }
}

@Composable
fun LevelBadge(level: String, modifier: Modifier = Modifier) {
  Surface(
    shape = RoundedCornerShape(8.dp),
    color = MaterialTheme.colorScheme.primaryContainer,
    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    modifier = modifier.semantics { contentDescription = "CECRL $level" }
  ) {
    Text(
      text = level,
      style = MaterialTheme.typography.labelMedium,
      fontWeight = FontWeight.ExtraBold,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )
  }
}

/** Level chips: "All" plus each CEFR level. [selected] null means all levels. */
@Composable
fun LevelFilterRow(
  selected: String?,
  onSelect: (String?) -> Unit,
  modifier: Modifier = Modifier,
  levels: List<String> = CEFR_LEVELS
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    FilterChip(
      selected = selected == null,
      onClick = { onSelect(null) },
      label = { Text(stringResource(R.string.level_all)) }
    )
    levels.forEach { level ->
      FilterChip(
        selected = selected == level,
        onClick = { onSelect(if (selected == level) null else level) },
        label = { Text(level) }
      )
    }
  }
}

/**
 * Compact field showing the current rule. Tapping it opens [RulePickerSheet]; this replaces the
 * old level-chips + rule-chips rows that pushed the content down on every screen.
 */
@Composable
fun RuleSelectorField(
  rule: StructuredRule,
  supportingText: String?,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val changeLabel = stringResource(R.string.rule_change)
  OutlinedCard(
    onClick = onClick,
    shape = RoundedCornerShape(14.dp),
    modifier = modifier.semantics { stateDescription = changeLabel }
  ) {
    Row(
      modifier = Modifier
        .heightIn(min = 56.dp)
        .padding(horizontal = 12.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      LevelBadge(rule.level)
      Spacer(Modifier.width(10.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = ruleTitle(rule),
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )
        if (supportingText != null) {
          Text(
            text = supportingText,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
      Icon(Icons.Default.ExpandMore, contentDescription = null)
    }
  }
}

/** Searchable, level-filterable rule list in a bottom sheet. Shared by every screen that picks a rule. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RulePickerSheet(
  rules: List<StructuredRule>,
  selectedRuleId: String?,
  levelFilter: String?,
  onLevelFilterChange: (String?) -> Unit,
  onSelectRule: (String) -> Unit,
  onDismiss: () -> Unit,
  progressMap: Map<String, RuleTrainingProgressEntity> = emptyMap(),
  title: String = stringResource(R.string.rule_picker_title),
  /** When set (grammar map), rules not on the map come first and the others are dimmed. */
  onMapIds: Set<String>? = null,
  /** Shown above the list while the search is empty. */
  suggestions: List<StructuredRule> = emptyList()
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  var query by rememberSaveable { mutableStateOf("") }
  val visibleRules = remember(rules, levelFilter, query, onMapIds) {
    val filtered = filterRules(rules, levelFilter, query)
    if (onMapIds == null) filtered else filtered.sortedBy { it.id in onMapIds }
  }
  val pick: (String) -> Unit = { id ->
    onSelectRule(id)
    onDismiss()
  }

  ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
      )
      Spacer(Modifier.size(12.dp))
      RuleSearchField(query = query, onQueryChange = { query = it })
      Spacer(Modifier.size(8.dp))
      LevelFilterRow(selected = levelFilter, onSelect = onLevelFilterChange)
      Spacer(Modifier.size(8.dp))
      if (query.isBlank() && suggestions.isNotEmpty()) {
        Text(
          text = stringResource(R.string.map_suggestions),
          style = MaterialTheme.typography.labelLarge,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.size(4.dp))
        Row(
          modifier = Modifier.horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          suggestions.forEach { rule ->
            androidx.compose.material3.SuggestionChip(
              onClick = { pick(rule.id) },
              label = { Text("${rule.level} · ${ruleTitle(rule)}", maxLines = 1, overflow = TextOverflow.Ellipsis) }
            )
          }
        }
        Spacer(Modifier.size(8.dp))
      }
    }
    HorizontalDivider()
    if (visibleRules.isEmpty()) {
      Text(
        text = stringResource(R.string.rule_picker_empty),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(24.dp)
      )
    } else {
      LazyColumn(contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 24.dp)) {
        items(visibleRules, key = { it.id }) { rule ->
          val selected = rule.id == selectedRuleId
          val trained = (progressMap[rule.id]?.sessionsCompleted ?: 0) > 0
          val onMap = onMapIds != null && rule.id in onMapIds
          ListItem(
            headlineContent = {
              Text(
                ruleTitle(rule),
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
              )
            },
            supportingContent = { Text(rule.categoryName) },
            leadingContent = { LevelBadge(rule.level) },
            trailingContent = {
              when {
                selected -> Icon(
                  Icons.Default.CheckCircle,
                  contentDescription = stringResource(R.string.rule_current),
                  tint = MaterialTheme.colorScheme.primary
                )
                onMap -> Text(
                  stringResource(R.string.map_already_on_map),
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                trained -> Text(
                  stringResource(R.string.rule_trained),
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            },
            colors = ListItemDefaults.colors(
              containerColor = if (selected) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
              else MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
              .alpha(if (onMap) 0.55f else 1f)
              .clickable(role = Role.Button) { pick(rule.id) }
          )
        }
      }
    }
  }
}

@Composable
fun RuleSearchField(query: String, onQueryChange: (String) -> Unit, modifier: Modifier = Modifier) {
  OutlinedTextField(
    value = query,
    onValueChange = onQueryChange,
    modifier = modifier.fillMaxWidth(),
    placeholder = { Text(stringResource(R.string.rule_search_hint)) },
    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
    trailingIcon = {
      if (query.isNotEmpty()) {
        IconButton(onClick = { onQueryChange("") }) {
          Icon(Icons.Default.Clear, contentDescription = stringResource(R.string.action_clear))
        }
      }
    },
    singleLine = true,
    shape = RoundedCornerShape(14.dp)
  )
}

/**
 * Card with a tappable header that shows or hides its content. Expansion survives tab switches
 * and rotation through [rememberSaveable], keyed by the caller.
 */
@Composable
fun CollapsibleSection(
  title: String,
  icon: ImageVector,
  expanded: Boolean,
  onToggle: () -> Unit,
  modifier: Modifier = Modifier,
  subtitle: String? = null,
  content: @Composable () -> Unit
) {
  val rotation by animateFloatAsState(if (expanded) 180f else 0f, label = "chevron")
  val stateText = stringResource(if (expanded) R.string.section_expanded else R.string.section_collapsed)
  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clickable(role = Role.Button, onClick = onToggle)
        .semantics { stateDescription = stateText }
        .heightIn(min = 56.dp)
        .padding(horizontal = 16.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
      ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(20.dp))
      }
      Spacer(Modifier.width(12.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        if (subtitle != null) {
          Text(
            subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }
      Icon(Icons.Default.ExpandMore, contentDescription = null, modifier = Modifier.rotate(rotation))
    }
    AnimatedVisibility(visible = expanded) {
      Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
        content()
      }
    }
  }
}

/** Shows [text] only when English help is on; used for translations under French content. */
@Composable
fun EnglishHelpText(text: String?, modifier: Modifier = Modifier) {
  if (isEnglish && !text.isNullOrBlank()) {
    Text(
      text = text,
      style = MaterialTheme.typography.bodySmall,
      fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = modifier
    )
  }
}
