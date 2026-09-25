package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.grammar.GrammarData
import com.example.ui.AppScreen
import com.example.ui.GrammarViewModel
import com.example.ui.components.AppBottomNav
import com.example.ui.components.TopStatusBar
import com.example.ui.exploratory.ExplorationContainer
import com.example.ui.screens.CodexScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LessonDetailScreen
import com.example.ui.screens.MistakesScreen
import com.example.ui.screens.ModeSelectScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.SpeedDrillScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.training.TrainingPathScreen

class MainActivity : ComponentActivity() {

  private val viewModel: GrammarViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
      MyApplicationTheme(themeMode = themeMode) {
        FrenchGrammarQuestApp(
          viewModel = viewModel,
          themeMode = themeMode
        )
      }
    }
  }
}

@Composable
fun FrenchGrammarQuestApp(
  viewModel: GrammarViewModel,
  themeMode: com.example.ui.theme.ThemeMode = com.example.ui.theme.ThemeMode.DARK
) {
  val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
  val selectedExplorationRuleId by viewModel.selectedExplorationRuleId.collectAsStateWithLifecycle()
  val selectedTrainingRuleId by viewModel.selectedTrainingRuleId.collectAsStateWithLifecycle()
  val userStats by viewModel.userStats.collectAsStateWithLifecycle()
  val progressMap by viewModel.progressMap.collectAsStateWithLifecycle()
  val mistakes by viewModel.mistakes.collectAsStateWithLifecycle()
  val achievements by viewModel.achievements.collectAsStateWithLifecycle()
  val quizState by viewModel.quizState.collectAsStateWithLifecycle()
  val drillState by viewModel.drillState.collectAsStateWithLifecycle()
  val isSpeaking by viewModel.isSpeaking.collectAsStateWithLifecycle()
  val ruleTrainingProgressMap by viewModel.ruleTrainingProgressMap.collectAsStateWithLifecycle()
  val generationState by viewModel.generationState.collectAsStateWithLifecycle()
  val geminiApiKey by viewModel.geminiApiKey.collectAsStateWithLifecycle()
  val isPodcastPlaying by viewModel.isPodcastPlaying.collectAsStateWithLifecycle()
  val savedGenerations by viewModel.savedGenerations.collectAsStateWithLifecycle()
  val playingSavedAudioPath by viewModel.playingSavedAudioPath.collectAsStateWithLifecycle()

  // Handle system back navigation (no-op on the initial mode-select screen)
  BackHandler(enabled = currentScreen !is AppScreen.Exploration && currentScreen !is AppScreen.ModeSelect) {
    viewModel.navigateTo(AppScreen.Exploration)
  }

  when (val screen = currentScreen) {
    is AppScreen.ModeSelect -> {
      ModeSelectScreen(
        onSelectExploration = { viewModel.navigateTo(AppScreen.Exploration) },
        onSelectGamification = { viewModel.navigateTo(AppScreen.Training) }
      )
    }

    is AppScreen.LessonDetail -> {
      val isBookmarked = progressMap[screen.lessonId]?.isBookmarked == true
      val lesson = GrammarData.getLesson(screen.lessonId)
      LessonDetailScreen(
        lessonId = screen.lessonId,
        isBookmarked = isBookmarked,
        onToggleBookmark = {
          lesson?.let { l -> viewModel.toggleBookmark(l.id, l.categoryId) }
        },
        onStartQuiz = { lessonId -> viewModel.startQuiz(lessonId) },
        onBack = { viewModel.navigateTo(AppScreen.Exploration) },
        onSpeakFrench = { text -> viewModel.speakFrench(text) },
        onStopAudio = { viewModel.stopAudio() },
        isSpeaking = isSpeaking
      )
    }

    is AppScreen.Quiz -> {
      QuizScreen(
        state = quizState,
        onSelectOption = { optionIdx -> viewModel.selectQuizOption(optionIdx) },
        onSubmitAnswer = { viewModel.submitQuizAnswer() },
        onNextQuestion = { viewModel.nextQuizQuestion() },
        onExit = { viewModel.navigateTo(AppScreen.Exploration) },
        onSpeakFrench = { text -> viewModel.speakFrench(text) },
        onStopAudio = { viewModel.stopAudio() },
        isSpeaking = isSpeaking
      )
    }

    else -> {
      Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
          TopStatusBar(
            themeMode = themeMode,
            onCycleTheme = { viewModel.cycleThemeMode() }
          )
        },
        bottomBar = {
          AppBottomNav(
            currentScreen = currentScreen,
            onNavigate = { navScreen -> viewModel.navigateTo(navScreen) }
          )
        }
      ) { innerPadding ->
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
          when (screen) {
            is AppScreen.Exploration -> {
              ExplorationContainer(
                selectedRuleId = selectedExplorationRuleId,
                onSelectRule = { ruleId -> viewModel.selectExplorationRule(ruleId) },
                onStartDrillForRule = { ruleId -> viewModel.startTrainingForRule(ruleId) },
                onOpenIndex = { viewModel.navigateTo(AppScreen.Codex) },
                allRules = remember { viewModel.getAllStructuredRules() },
                generationState = generationState,
                geminiApiKey = geminiApiKey,
                onSaveGeminiApiKey = { key -> viewModel.setGeminiApiKey(key) },
                onGenerate = { level, ruleId, theme, format -> viewModel.generateContent(level, ruleId, theme, format) },
                onSpeakFrench = { text -> viewModel.speakFrench(text) },
                onStopAudio = { viewModel.stopAudio() },
                isSpeaking = isSpeaking,
                onSynthesizePodcastAudio = { script -> viewModel.synthesizePodcastAudio(script) },
                onPlayPodcastAudio = { path -> viewModel.playPodcastAudio(path) },
                onStopPodcastAudio = { viewModel.stopPodcastAudio() },
                isPodcastPlaying = isPodcastPlaying,
                onSaveGeneration = { viewModel.saveCurrentGeneration() },
                savedGenerations = savedGenerations,
                onPlaySavedAudio = { path -> viewModel.playSavedAudio(path) },
                onDeleteSavedGeneration = { id -> viewModel.deleteSavedGeneration(id) },
                playingSavedAudioPath = playingSavedAudioPath,
                onExportAudioToDevice = { path -> viewModel.exportAudioToDevice(path) }
              )
            }

            is AppScreen.Training -> {
              TrainingPathScreen(
                initialRuleId = selectedTrainingRuleId,
                onExploreRule = { ruleId -> viewModel.startExplorationForRule(ruleId) },
                onSessionComplete = { ruleId, isProductionMode, score, total ->
                  viewModel.recordTrainingSession(ruleId, isProductionMode, score, total)
                },
                onOpenIndex = { viewModel.navigateTo(AppScreen.Codex) }
              )
            }

            is AppScreen.Codex -> {
              CodexScreen(
                onExploreRule = { ruleId -> viewModel.startExplorationForRule(ruleId) },
                onTrainRule = { ruleId -> viewModel.startTrainingForRule(ruleId) },
                progressMap = ruleTrainingProgressMap
              )
            }

            // Legacy fallbacks
            is AppScreen.QuestMap -> {
              HomeScreen(
                stats = userStats,
                progressMap = progressMap,
                onStartLesson = { lessonId -> viewModel.startLesson(lessonId) },
                onStartQuiz = { lessonId -> viewModel.startQuiz(lessonId) }
              )
            }

            is AppScreen.SpeedDrill -> {
              SpeedDrillScreen(
                state = drillState,
                highScore = userStats.speedDrillHighScore,
                onStartDrill = { viewModel.startSpeedDrill() },
                onAnswerQuestion = { optionIdx -> viewModel.answerSpeedDrill(optionIdx) }
              )
            }

            is AppScreen.Mistakes -> {
              MistakesScreen(
                mistakes = mistakes,
                onResolveMistake = { mistakeId -> viewModel.resolveMistake(mistakeId) },
                onClearAll = { viewModel.clearAllMistakes() }
              )
            }

            else -> {}
          }
        }
      }
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  androidx.compose.material3.Text(text = "Hello $name!", modifier = modifier)
}
