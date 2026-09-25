package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.grammar.GrammarData
import com.example.data.local.LessonProgressEntity
import com.example.data.local.UserStatsEntity
import com.example.data.model.GrammarCategory
import com.example.data.model.GrammarLesson
import com.example.ui.theme.AppThemeColors
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.FrenchBlue
import com.example.ui.theme.FrenchBlueDark
import com.example.ui.theme.FrenchRed
import com.example.ui.theme.GoldStar

@Composable
fun HomeScreen(
  stats: UserStatsEntity,
  progressMap: Map<String, LessonProgressEntity>,
  onStartLesson: (String) -> Unit,
  onStartQuiz: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedCategoryId by remember { mutableStateOf(GrammarData.CATEGORIES.first().id) }
  var inspectingLesson by remember { mutableStateOf<GrammarLesson?>(null) }

  val activeCategory = GrammarData.getCategory(selectedCategoryId) ?: GrammarData.CATEGORIES.first()

  // Find next lesson to suggest
  val nextLesson = remember(progressMap) {
    GrammarData.getAllLessons().firstOrNull { lesson ->
      (progressMap[lesson.id]?.stars ?: 0) < 3
    } ?: GrammarData.getAllLessons().first()
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .testTag("home_screen"),
    contentPadding = PaddingValues(bottom = 24.dp)
  ) {
    // 1. Daily Goal & Quick Resume Banner
    item {
      DailyGoalCard(
        stats = stats,
        nextLesson = nextLesson,
        onStart = { onStartLesson(nextLesson.id) }
      )
    }

    // 2. World / Category Selector Chips
    item {
      CategorySelectorRow(
        categories = GrammarData.CATEGORIES,
        selectedId = selectedCategoryId,
        onSelect = { selectedCategoryId = it },
        progressMap = progressMap
      )
    }

    // 3. Category Banner Header
    item {
      CategoryHeaderBanner(category = activeCategory, progressMap = progressMap)
    }

    // 4. Quest Path Nodes
    itemsIndexed(activeCategory.lessons) { index, lesson ->
      val progress = progressMap[lesson.id]
      val stars = progress?.stars ?: 0
      val isCompleted = stars > 0
      // Node is unlocked if it's the first in list or previous is completed
      val isUnlocked = index == 0 || (progressMap[activeCategory.lessons[index - 1].id]?.stars ?: 0) > 0

      QuestNodeRow(
        lesson = lesson,
        index = index,
        stars = stars,
        isUnlocked = isUnlocked,
        onNodeClick = { inspectingLesson = lesson }
      )
    }
  }

  // Inspection Dialog
  inspectingLesson?.let { lesson ->
    val progress = progressMap[lesson.id]
    val stars = progress?.stars ?: 0
    val hasAudio = GrammarData.isAudioAvailableForLevel(lesson.level)

    AlertDialog(
      onDismissRequest = { inspectingLesson = null },
      title = {
        Column {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = AppThemeColors.primaryContainer
              ) {
                Text(
                  text = lesson.level,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                  fontWeight = FontWeight.Bold,
                  color = AppThemeColors.onPrimaryContainer,
                  fontSize = 12.sp
                )
              }
              if (hasAudio) {
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = AppThemeColors.successContainer
                ) {
                  Text(
                    text = "🎧 Audio A2+",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                    fontWeight = FontWeight.Bold,
                    color = AppThemeColors.onSuccessContainer,
                    fontSize = 11.sp
                  )
                }
              }
            }
            // Stars
            Row {
              repeat(3) { starIdx ->
                Icon(
                  imageVector = Icons.Default.Star,
                  contentDescription = null,
                  tint = if (starIdx < stars) AppThemeColors.gold else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                  modifier = Modifier.size(20.dp)
                )
              }
            }
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = lesson.frTitle,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = lesson.enSub,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      },
      text = {
        Column {
          Text(
            text = lesson.desc,
            style = MaterialTheme.typography.bodyMedium
          )
          Spacer(modifier = Modifier.height(10.dp))
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(10.dp)) {
              Text(
                text = "📖 1. Fiche de grammaire ${if (hasAudio) "(avec écoute audio 🎧)" else ""}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = AppThemeColors.primary
              )
              Spacer(modifier = Modifier.height(3.dp))
              Text(
                text = "🎯 2. Série de 5 exercices interactifs pour valider",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val id = lesson.id
            inspectingLesson = null
            onStartLesson(id)
          },
          colors = ButtonDefaults.buttonColors(containerColor = AppThemeColors.primary),
          modifier = Modifier.testTag("start_quest_study_btn")
        ) {
          Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Comprendre la règle puis s'exercer")
        }
      },
      dismissButton = {
        TextButton(
          onClick = {
            val id = lesson.id
            inspectingLesson = null
            onStartQuiz(id)
          },
          modifier = Modifier.testTag("direct_quiz_btn")
        ) {
          Text("Exercices directs")
        }
      }
    )
  }
}

@Composable
private fun DailyGoalCard(
  stats: UserStatsEntity,
  nextLesson: GrammarLesson,
  onStart: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.primary
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
  ) {
    Column(
      modifier = Modifier.padding(18.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "OBJECTIF DU JOUR",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White.copy(alpha = 0.85f),
            letterSpacing = 1.sp
          )
          Text(
            text = "${stats.dailyEarnedXp} / ${stats.dailyGoalXp} XP",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
          )
        }
        if (stats.dailyEarnedXp >= stats.dailyGoalXp) {
          Surface(
            shape = CircleShape,
            color = AppThemeColors.success,
            modifier = Modifier.size(36.dp)
          ) {
            Icon(
              Icons.Default.Check,
              contentDescription = "Objectif atteint",
              tint = Color.White,
              modifier = Modifier.padding(8.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      val progress = (stats.dailyEarnedXp.toFloat() / stats.dailyGoalXp.toFloat()).coerceIn(0f, 1f)
      LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
          .fillMaxWidth()
          .height(8.dp)
          .clip(RoundedCornerShape(4.dp)),
        color = AppThemeColors.gold,
        trackColor = Color.White.copy(alpha = 0.25f),
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Next lesson suggestion
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
          .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "CONTINUER L'AVENTURE",
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFFFDE68A),
            fontWeight = FontWeight.Bold
          )
          Text(
            text = nextLesson.frTitle,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = nextLesson.enSub,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.85f)
          )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Button(
          onClick = onStart,
          colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = FrenchBlueDark
          ),
          shape = RoundedCornerShape(20.dp),
          contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
        ) {
          Text("Étudier & Jouer", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
private fun CategorySelectorRow(
  categories: List<GrammarCategory>,
  selectedId: String,
  onSelect: (String) -> Unit,
  progressMap: Map<String, LessonProgressEntity>
) {
  LazyRow(
    contentPadding = PaddingValues(horizontal = 16.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    modifier = Modifier.padding(bottom = 12.dp)
  ) {
    items(categories) { category ->
      val isSelected = category.id == selectedId
      val totalStars = category.lessons.sumOf { progressMap[it.id]?.stars ?: 0 }
      val maxStars = category.lessons.size * 3

      FilterChip(
        selected = isSelected,
        onClick = { onSelect(category.id) },
        label = {
          Text(
            text = "${category.number}. ${category.frLabel.take(16)}… ($totalStars/$maxStars⭐)",
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
          )
        },
        colors = FilterChipDefaults.filterChipColors(
          selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
          selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
          containerColor = MaterialTheme.colorScheme.surface,
          labelColor = MaterialTheme.colorScheme.onSurface
        )
      )
    }
  }
}

@Composable
private fun CategoryHeaderBanner(
  category: GrammarCategory,
  progressMap: Map<String, LessonProgressEntity>
) {
  val totalStars = category.lessons.sumOf { progressMap[it.id]?.stars ?: 0 }
  val maxStars = category.lessons.size * 3
  val mastery = if (maxStars > 0) (totalStars.toFloat() / maxStars.toFloat()) * 100 else 0f

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant
    ),
    shape = RoundedCornerShape(12.dp)
  ) {
    Row(
      modifier = Modifier.padding(14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = AppThemeColors.primary
          ) {
            Text(
              text = category.levelRange,
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = category.label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = category.desc,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
          text = "${mastery.toInt()}%",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.ExtraBold,
          color = AppThemeColors.primary
        )
        Text(
          text = "Maîtrise",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}

@Composable
private fun QuestNodeRow(
  lesson: GrammarLesson,
  index: Int,
  stars: Int,
  isUnlocked: Boolean,
  onNodeClick: () -> Unit
) {
  // Stepped zigzag layout (alternates left, center, right)
  val step = index % 3
  val horizontalOffset = when (step) {
    0 -> (-40).dp
    1 -> 0.dp
    else -> 40.dp
  }

  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = 1.08f,
    animationSpec = infiniteRepeatable(
      animation = tween(900, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "scale"
  )

  val isNextActive = isUnlocked && stars < 3

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 12.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .offset(x = horizontalOffset)
        .clickable(enabled = isUnlocked, onClick = onNodeClick)
    ) {
      // Glow/Pulse background for active node
      if (isNextActive) {
        Box(
          modifier = Modifier
            .size(76.dp)
            .scale(pulseScale)
            .background(AppThemeColors.primary.copy(alpha = 0.22f), CircleShape)
        )
      }

      // Main Circle Node
      val nodeColor = when {
        !isUnlocked -> MaterialTheme.colorScheme.surfaceVariant
        stars == 3 -> AppThemeColors.success
        stars > 0 -> AppThemeColors.primary
        else -> AppThemeColors.primary
      }

      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .size(60.dp)
          .shadow(6.dp, CircleShape)
          .background(
            brush = Brush.verticalGradient(
              colors = listOf(
                nodeColor,
                if (isUnlocked) nodeColor.copy(alpha = 0.85f) else Color.Gray
              )
            ),
            shape = CircleShape
          )
          .border(
            width = if (isNextActive) 3.dp else 1.5.dp,
            color = if (isNextActive) AppThemeColors.gold else Color.White.copy(alpha = 0.8f),
            shape = CircleShape
          )
      ) {
        if (!isUnlocked) {
          Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Verrouillé",
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.size(24.dp)
          )
        } else if (stars == 3) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Complété",
            tint = Color.White,
            modifier = Modifier.size(28.dp)
          )
        } else {
          Text(
            text = (index + 1).toString(),
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp
          )
        }
      }
    }

    // Stars below node
    Row(
      modifier = Modifier
        .offset(x = horizontalOffset)
        .padding(top = 4.dp),
      horizontalArrangement = Arrangement.Center
    ) {
      repeat(3) { starIdx ->
        Icon(
          imageVector = Icons.Default.Star,
          contentDescription = null,
          tint = if (starIdx < stars) AppThemeColors.gold else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
          modifier = Modifier.size(16.dp)
        )
      }
    }

    // Title label
    Text(
      text = lesson.frTitle,
      style = MaterialTheme.typography.labelMedium,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .offset(x = horizontalOffset)
        .padding(horizontal = 24.dp, vertical = 2.dp)
        .width(180.dp),
      maxLines = 2,
      color = if (isUnlocked) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
    )
  }
}
