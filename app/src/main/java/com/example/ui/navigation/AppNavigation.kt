package com.example.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Hub
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.R
import com.example.data.prefs.StartMode
import com.example.ui.GrammarViewModel
import com.example.ui.exploratory.ExploratoryPathScreen
import com.example.ui.generation.GenerationScreen
import com.example.ui.i18n.isEnglish
import com.example.ui.onboarding.OnboardingScreen
import com.example.ui.map.MapScreen
import com.example.ui.settings.SettingsScreen
import com.example.ui.training.TrainingPathScreen

object Routes {
  const val ONBOARDING = "onboarding"
  const val EXPLORE = "explore"
  const val TRAIN = "train"
  const val CREATE = "create"
  const val MAP = "map"
  const val SETTINGS = "settings"
}

/** Bottom-bar destinations. Each keeps its own state when the user switches tabs. */
enum class TopLevelDestination(
  val route: String,
  @StringRes val labelRes: Int,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector
) {
  EXPLORE(Routes.EXPLORE, R.string.nav_explore, Icons.Filled.Lightbulb, Icons.Outlined.Lightbulb),
  TRAIN(Routes.TRAIN, R.string.nav_train, Icons.Filled.Edit, Icons.Outlined.Edit),
  CREATE(Routes.CREATE, R.string.nav_create, Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome),
  MAP(Routes.MAP, R.string.nav_map, Icons.Filled.Hub, Icons.Outlined.Hub)
}

/**
 * App root. The mode question is asked once (onboarding); after that the app always opens on
 * the chosen mode, and every section is one tap away in the bottom bar.
 */
@Composable
fun FrenchGrammarQuestApp(viewModel: GrammarViewModel) {
  var showOnboarding by rememberSaveable { mutableStateOf(viewModel.startRoute == Routes.ONBOARDING) }

  if (showOnboarding) {
    OnboardingScreen(
      onChooseMode = { mode ->
        viewModel.completeOnboarding(mode)
        showOnboarding = false
      }
    )
  } else {
    MainScaffold(viewModel)
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScaffold(viewModel: GrammarViewModel) {
  val navController = rememberNavController()
  val backStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = backStackEntry?.destination?.route
  val currentTab = TopLevelDestination.entries.firstOrNull { it.route == currentRoute }

  // Fixed for the whole session: the tab the user chose to start on.
  val startRoute = rememberSaveable {
    if (viewModel.startMode.value == StartMode.TRAIN) Routes.TRAIN else Routes.EXPLORE
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = currentTab?.let { stringResource(it.labelRes) }
              ?: if (currentRoute == Routes.SETTINGS) stringResource(R.string.settings_title) else "",
            style = MaterialTheme.typography.titleLarge
          )
        },
        navigationIcon = {
          if (currentTab == null && currentRoute != null) {
            IconButton(onClick = { navController.popBackStack() }) {
              Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.action_back))
            }
          }
        },
        actions = {
          LanguageToggle(isOn = isEnglish, onToggle = viewModel::toggleLanguage)
          if (currentRoute != Routes.SETTINGS) {
            IconButton(onClick = { navController.navigate(Routes.SETTINGS) { launchSingleTop = true } }) {
              Icon(Icons.Filled.Settings, contentDescription = stringResource(R.string.settings_title))
            }
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
      )
    },
    bottomBar = {
      if (currentTab != null) {
        NavigationBar(modifier = Modifier.testTag("app_bottom_nav")) {
          TopLevelDestination.entries.forEach { destination ->
            val selected = destination == currentTab
            NavigationBarItem(
              selected = selected,
              onClick = { navController.navigateToTab(destination) },
              icon = {
                Icon(
                  imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                  contentDescription = null
                )
              },
              label = { Text(stringResource(destination.labelRes)) },
              modifier = Modifier.testTag("nav_${destination.route}")
            )
          }
        }
      }
    }
  ) { innerPadding ->
    NavHost(
      navController = navController,
      startDestination = startRoute,
      // Consuming the scaffold padding lets screens use imePadding() without double-counting the bottom bar.
      modifier = Modifier
        .padding(innerPadding)
        .consumeWindowInsets(innerPadding),
      enterTransition = { fadeIn() },
      exitTransition = { fadeOut() }
    ) {
      composable(Routes.EXPLORE) {
        ExploratoryPathScreen(
          viewModel = viewModel,
          onTrainRule = { ruleId ->
            viewModel.selectTrainingRule(ruleId)
            navController.navigateToTab(TopLevelDestination.TRAIN)
          },
          onCreateForRule = { ruleId ->
            viewModel.prepareGenerationForRule(ruleId)
            navController.navigateToTab(TopLevelDestination.CREATE)
          },
          onShowOnMap = { ruleId ->
            viewModel.setMapFocus(ruleId)
            navController.navigateToTab(TopLevelDestination.MAP)
          }
        )
      }
      composable(Routes.TRAIN) {
        TrainingPathScreen(
          viewModel = viewModel,
          onExploreRule = { ruleId ->
            viewModel.selectExploreRule(ruleId)
            navController.navigateToTab(TopLevelDestination.EXPLORE)
          }
        )
      }
      composable(Routes.CREATE) {
        GenerationScreen(
          viewModel = viewModel,
          onOpenSettings = { navController.navigate(Routes.SETTINGS) { launchSingleTop = true } }
        )
      }
      composable(Routes.MAP) {
        MapScreen(
          viewModel = viewModel,
          onExploreRule = { ruleId ->
            viewModel.selectExploreRule(ruleId)
            navController.navigateToTab(TopLevelDestination.EXPLORE)
          },
          onTrainRule = { ruleId ->
            viewModel.selectTrainingRule(ruleId)
            navController.navigateToTab(TopLevelDestination.TRAIN)
          }
        )
      }
      composable(Routes.SETTINGS) {
        SettingsScreen(viewModel = viewModel)
      }
    }
  }
}

/**
 * Standard bottom-bar navigation: one copy of each tab, tab state saved and restored, and Back
 * from any tab returns to the start tab before leaving the app.
 */
private fun NavHostController.navigateToTab(destination: TopLevelDestination) {
  navigate(destination.route) {
    popUpTo(graph.findStartDestination().id) { saveState = true }
    launchSingleTop = true
    restoreState = true
  }
}

@Composable
private fun LanguageToggle(isOn: Boolean, onToggle: () -> Unit) {
  FilterChip(
    selected = isOn,
    onClick = onToggle,
    label = { Text("EN") },
    leadingIcon = {
      Icon(
        Icons.Filled.Translate,
        contentDescription = stringResource(if (isOn) R.string.language_back_to_french else R.string.language_show_english),
        modifier = Modifier.size(FilterChipDefaults.IconSize)
      )
    },
    modifier = Modifier
      .padding(end = 4.dp)
      .testTag("language_toggle")
  )
}
