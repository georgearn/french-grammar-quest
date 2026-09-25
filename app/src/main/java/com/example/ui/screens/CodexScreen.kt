package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.engine.GrammarRuleRepository
import com.example.data.engine.StructuredRule
import com.example.data.local.RuleTrainingProgressEntity
import com.example.ui.theme.AppThemeColors

@Composable
fun CodexScreen(
  onExploreRule: (String) -> Unit,
  onTrainRule: (String) -> Unit,
  progressMap: Map<String, RuleTrainingProgressEntity> = emptyMap(),
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedLevel by remember { mutableStateOf("TOUS") }

  val allRules = remember { GrammarRuleRepository.getAllRules() }

  val filteredRules = remember(searchQuery, selectedLevel, allRules) {
    allRules.filter { rule ->
      val matchesSearch = searchQuery.isBlank() ||
        rule.titleFr.contains(searchQuery, ignoreCase = true) ||
        rule.titleEn.contains(searchQuery, ignoreCase = true) ||
        rule.summaryFr.contains(searchQuery, ignoreCase = true) ||
        rule.categoryName.contains(searchQuery, ignoreCase = true)

      val matchesLevel = selectedLevel.equals("TOUS", ignoreCase = true) ||
        rule.level.equals(selectedLevel, ignoreCase = true)

      matchesSearch && matchesLevel
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .testTag("codex_screen")
  ) {
    // Search Field
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .testTag("codex_search_input"),
      placeholder = { Text("Rechercher une règle (ex: subjonctif, accord, pronom...)") },
      leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
      trailingIcon = {
        if (searchQuery.isNotEmpty()) {
          IconButton(onClick = { searchQuery = "" }) {
            Icon(Icons.Default.Clear, contentDescription = "Effacer")
          }
        }
      },
      singleLine = true,
      shape = RoundedCornerShape(14.dp),
      colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        focusedBorderColor = AppThemeColors.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        cursorColor = AppThemeColors.primary,
        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
      )
    )

    // Level Filter Chips
    LazyRow(
      contentPadding = PaddingValues(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.padding(bottom = 8.dp)
    ) {
      val levels = listOf("TOUS", "A1", "A2", "B1", "B2", "C1", "C2")
      items(levels) { level ->
        FilterChip(
          selected = selectedLevel.equals(level, ignoreCase = true),
          onClick = { selectedLevel = level },
          label = { Text(level) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
            containerColor = MaterialTheme.colorScheme.surface,
            labelColor = MaterialTheme.colorScheme.onSurface
          )
        )
      }
    }

    // Rules list
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 90.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      if (filteredRules.isEmpty()) {
        item {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "Aucune règle ne correspond à votre recherche.",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      } else {
        items(filteredRules) { rule ->
          CodexStructuredRuleCard(
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
private fun CodexStructuredRuleCard(
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
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Level Badge
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = AppThemeColors.primaryContainer,
          modifier = Modifier.size(46.dp)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Text(
              text = rule.level,
              color = AppThemeColors.onPrimaryContainer,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 14.5.sp
            )
          }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = rule.titleFr,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = rule.titleEn,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = rule.summaryFr,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurface,
        lineHeight = 18.sp
      )

      if (progress != null && progress.sessionsCompleted > 0) {
        Spacer(modifier = Modifier.height(10.dp))
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = AppThemeColors.successContainer
        ) {
          Text(
            text = "🏆 Meilleur score : ${progress.bestProductionScore}/${progress.bestProductionTotal} production" +
              " · ${progress.bestSpotErrorScore}/${progress.bestSpotErrorTotal} détection" +
              " · ${progress.sessionsCompleted} session(s)",
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Bold,
            color = AppThemeColors.onSuccessContainer
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Dual Action Buttons per the TOR: Exploration vs Production
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedButton(
          onClick = onExplore,
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(10.dp)
        ) {
          Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Explorer", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
        }

        Button(
          onClick = onTrain,
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = AppThemeColors.primary)
        ) {
          Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("S'entraîner", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
