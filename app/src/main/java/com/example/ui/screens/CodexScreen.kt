package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.engine.StructuredRule
import com.example.data.local.RuleTrainingProgressEntity
import com.example.ui.GrammarViewModel
import com.example.ui.components.EnglishHelpText
import com.example.ui.components.LevelBadge
import com.example.ui.components.LevelFilterRow
import com.example.ui.components.RuleSearchField
import com.example.ui.components.filterRules
import com.example.ui.i18n.localized
import com.example.ui.theme.AppThemeColors

/** "Index": search every rule, see training progress, jump to explaining or practising it. */
@Composable
fun CodexScreen(
  viewModel: GrammarViewModel,
  onExploreRule: (String) -> Unit,
  onTrainRule: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val levelFilter by viewModel.levelFilter.collectAsStateWithLifecycle()
  val progressMap by viewModel.ruleTrainingProgressMap.collectAsStateWithLifecycle()
  var query by rememberSaveable { mutableStateOf("") }
  val visibleRules = remember(levelFilter, query) { filterRules(viewModel.rules, levelFilter, query) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .imePadding()
      .testTag("codex_screen")
  ) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
      RuleSearchField(query = query, onQueryChange = { query = it })
      Spacer(Modifier.height(8.dp))
      LevelFilterRow(selected = levelFilter, onSelect = viewModel::setLevelFilter)
    }

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 24.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      item {
        Text(
          text = stringResource(R.string.index_count, visibleRules.size),
          style = MaterialTheme.typography.labelLarge,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
      if (visibleRules.isEmpty()) {
        item {
          Text(
            text = stringResource(R.string.rule_picker_empty),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 32.dp)
          )
        }
      } else {
        items(visibleRules, key = { it.id }) { rule ->
          RuleCard(
            rule = rule,
            progress = progressMap[rule.id],
            onExplore = { onExploreRule(rule.id) },
            onTrain = { onTrainRule(rule.id) }
          )
        }
      }
    }
  }
}

@Composable
private fun RuleCard(
  rule: StructuredRule,
  progress: RuleTrainingProgressEntity?,
  onExplore: () -> Unit,
  onTrain: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("codex_card_${rule.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        LevelBadge(rule.level)
        Spacer(Modifier.width(10.dp))
        Text(
          text = rule.categoryName,
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
      Spacer(Modifier.height(8.dp))
      Text(rule.titleFr, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
      EnglishHelpText(rule.titleEn)
      Spacer(Modifier.height(6.dp))
      Text(
        text = localized(rule.summaryFr, rule.summaryEn),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 3,
        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
      )

      if (progress != null && progress.sessionsCompleted > 0) {
        Spacer(Modifier.height(10.dp))
        Surface(shape = RoundedCornerShape(8.dp), color = AppThemeColors.successContainer) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              Icons.Default.EmojiEvents,
              contentDescription = null,
              tint = AppThemeColors.onSuccessContainer,
              modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
              text = stringResource(
                R.string.index_progress,
                progress.bestProductionScore, progress.bestProductionTotal,
                progress.bestSpotErrorScore, progress.bestSpotErrorTotal
              ),
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = AppThemeColors.onSuccessContainer
            )
          }
        }
      }

      Spacer(Modifier.height(12.dp))
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedButton(onClick = onExplore, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) {
          Icon(Icons.Default.Lightbulb, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(Modifier.width(6.dp))
          Text(stringResource(R.string.nav_explore))
        }
        Button(onClick = onTrain, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) {
          Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(Modifier.width(6.dp))
          Text(stringResource(R.string.nav_train))
        }
      }
    }
  }
}
