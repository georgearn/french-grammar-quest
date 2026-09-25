package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen

@Composable
fun AppBottomNav(
  currentScreen: AppScreen,
  onNavigate: (AppScreen) -> Unit,
  modifier: Modifier = Modifier
) {
  val navItemColors = NavigationBarItemDefaults.colors(
    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
    selectedTextColor = MaterialTheme.colorScheme.primary,
    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
  )

  NavigationBar(
    modifier = modifier.testTag("app_bottom_nav"),
    windowInsets = WindowInsets.navigationBars,
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 6.dp
  ) {
    // 1. Exploratory Path: "Comprendre"
    val isExploration = currentScreen is AppScreen.Exploration
    NavigationBarItem(
      selected = isExploration,
      onClick = { onNavigate(AppScreen.Exploration) },
      icon = {
        Icon(
          imageVector = if (isExploration) Icons.Filled.Lightbulb else Icons.Outlined.Lightbulb,
          contentDescription = "Exploration & Compréhension",
          modifier = Modifier.size(24.dp)
        )
      },
      label = {
        Text(
          text = "Comprendre",
          fontSize = 11.5.sp,
          fontWeight = if (isExploration) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = navItemColors,
      modifier = Modifier.testTag("nav_exploration")
    )

    // 2. Training / Game Path: "Produire"
    val isTraining = currentScreen is AppScreen.Training
    NavigationBarItem(
      selected = isTraining,
      onClick = { onNavigate(AppScreen.Training) },
      icon = {
        Icon(
          imageVector = if (isTraining) Icons.Filled.Edit else Icons.Outlined.Edit,
          contentDescription = "Entraînement & Drills",
          modifier = Modifier.size(24.dp)
        )
      },
      label = {
        Text(
          text = "Produire",
          fontSize = 11.5.sp,
          fontWeight = if (isTraining) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = navItemColors,
      modifier = Modifier.testTag("nav_training")
    )

    // Codex is no longer its own bottom-nav destination: the rule index/search it
    // provides is now reached from inside "Produire" (Training), since the app's two
    // real modes are Exploration (understand) and Training (practice + progress).
  }
}
