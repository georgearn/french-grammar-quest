package com.example.ui

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.media.MediaPlayer
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GrammarRepository
import com.example.data.engine.GenerationFormat
import com.example.data.engine.GeminiGenerationRepository
import com.example.data.engine.GeminiTtsRepository
import com.example.data.engine.GrammarRuleRepository
import com.example.data.grammar.GrammarData
import com.example.data.local.AchievementEntity
import com.example.data.local.GrammarDatabase
import com.example.data.local.LessonProgressEntity
import com.example.data.local.MistakeEntity
import com.example.data.local.RuleTrainingProgressEntity
import com.example.data.local.SavedGenerationEntity
import com.example.data.local.UserStatsEntity
import com.example.data.model.GrammarCategory
import com.example.data.model.GrammarLesson
import com.example.data.model.QuizQuestion
import com.example.ui.generation.GenerationUiState
import com.example.ui.generation.SavedGenerationUi
import com.example.ui.theme.ThemeMode
import com.example.util.FrenchAudioHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

sealed interface AppScreen {
  data object ModeSelect : AppScreen
  data object Exploration : AppScreen
  data object Training : AppScreen
  data object Codex : AppScreen
  data object QuestMap : AppScreen
  data object SpeedDrill : AppScreen
  data object Mistakes : AppScreen
  data class LessonDetail(val lessonId: String) : AppScreen
  data class Quiz(val lessonId: String) : AppScreen
}

data class QuizSessionState(
  val lesson: GrammarLesson? = null,
  val questions: List<QuizQuestion> = emptyList(),
  val currentIndex: Int = 0,
  val selectedOption: Int? = null,
  val isAnswered: Boolean = false,
  val isCorrect: Boolean = false,
  val score: Int = 0,
  val combo: Int = 0,
  val maxCombo: Int = 0,
  val isFinished: Boolean = false,
  val xpEarned: Int = 0,
  val starsEarned: Int = 0,
  val heartsLeft: Int = 5
)

data class SpeedDrillState(
  val isActive: Boolean = false,
  val isFinished: Boolean = false,
  val secondsRemaining: Int = 60,
  val questions: List<QuizQuestion> = emptyList(),
  val currentIndex: Int = 0,
  val score: Int = 0,
  val combo: Int = 0,
  val maxCombo: Int = 0,
  val feedbackColor: Long? = null // For instant flash feedback
)

class GrammarViewModel(application: Application) : AndroidViewModel(application) {

  private val repository: GrammarRepository
  private val generationRepository = GeminiGenerationRepository()
  private val ttsRepository = GeminiTtsRepository()
  private val audioHelper = FrenchAudioHelper(application)
  private var podcastPlayer: MediaPlayer? = null

  private val _isPodcastPlaying = MutableStateFlow(false)
  val isPodcastPlaying: StateFlow<Boolean> = _isPodcastPlaying.asStateFlow()

  // Path of the saved-generation audio currently playing (distinct from the just-generated
  // podcast's own audio, which uses podcastAudioPath/isPodcastPlaying above).
  private val _playingSavedAudioPath = MutableStateFlow<String?>(null)
  val playingSavedAudioPath: StateFlow<String?> = _playingSavedAudioPath.asStateFlow()
  private val themePrefs = application.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
  private val apiKeyPrefs = application.getSharedPreferences("gemini_api_prefs", Context.MODE_PRIVATE)

  val isSpeaking: StateFlow<Boolean> = audioHelper.isSpeaking

  private val _themeMode = MutableStateFlow(
    runCatching {
      val saved = themePrefs.getString("saved_theme_mode", ThemeMode.DARK.name) ?: ThemeMode.DARK.name
      ThemeMode.valueOf(saved)
    }.getOrDefault(ThemeMode.DARK)
  )
  val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

  fun setThemeMode(mode: ThemeMode) {
    _themeMode.value = mode
    themePrefs.edit().putString("saved_theme_mode", mode.name).apply()
  }

  fun cycleThemeMode() {
    val next = when (_themeMode.value) {
      ThemeMode.LIGHT -> ThemeMode.DARK
      ThemeMode.DARK -> ThemeMode.LIGHT
    }
    setThemeMode(next)
  }

  init {
    val db = GrammarDatabase.getDatabase(application)
    repository = GrammarRepository(db.grammarDao())
    viewModelScope.launch {
      repository.initDefaultDataIfNeeded()
    }
  }

  val userStats: StateFlow<UserStatsEntity> = repository.userStats
    .map { it ?: UserStatsEntity() }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = UserStatsEntity()
    )

  val progressMap: StateFlow<Map<String, LessonProgressEntity>> = repository.lessonProgressList
    .map { list -> list.associateBy { it.lessonId } }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyMap()
    )

  val mistakes: StateFlow<List<MistakeEntity>> = repository.mistakes
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  val achievements: StateFlow<List<AchievementEntity>> = repository.achievements
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  val ruleTrainingProgressMap: StateFlow<Map<String, RuleTrainingProgressEntity>> =
    repository.ruleTrainingProgress
      .map { list -> list.associateBy { it.ruleId } }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyMap()
      )

  private val _generationState = MutableStateFlow(GenerationUiState())
  val generationState: StateFlow<GenerationUiState> = _generationState.asStateFlow()

  val savedGenerations: StateFlow<List<SavedGenerationUi>> = repository.savedGenerations
    .map { list ->
      list.map {
        SavedGenerationUi(
          id = it.id,
          format = GenerationFormat.valueOf(it.format),
          level = it.level,
          grammarPointTitle = it.grammarPointTitle,
          theme = it.theme,
          text = it.text,
          audioPath = it.audioPath
        )
      }
    }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  // User-supplied Google AI Studio (Gemini Developer API) key — kept only in local
  // SharedPreferences on this device, never bundled with the app or sent anywhere but
  // Google's own generativelanguage.googleapis.com endpoint.
  private val _geminiApiKey = MutableStateFlow(apiKeyPrefs.getString("gemini_api_key", "") ?: "")
  val geminiApiKey: StateFlow<String> = _geminiApiKey.asStateFlow()

  fun setGeminiApiKey(key: String) {
    val trimmed = key.trim()
    _geminiApiKey.value = trimmed
    apiKeyPrefs.edit().putString("gemini_api_key", trimmed).apply()
  }

  private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.ModeSelect)
  val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

  private val _selectedExplorationRuleId = MutableStateFlow("subjonctif-present")
  val selectedExplorationRuleId: StateFlow<String> = _selectedExplorationRuleId.asStateFlow()

  private val _selectedTrainingRuleId = MutableStateFlow("subjonctif-present")
  val selectedTrainingRuleId: StateFlow<String> = _selectedTrainingRuleId.asStateFlow()

  private val _quizState = MutableStateFlow(QuizSessionState())
  val quizState: StateFlow<QuizSessionState> = _quizState.asStateFlow()

  private val _drillState = MutableStateFlow(SpeedDrillState())
  val drillState: StateFlow<SpeedDrillState> = _drillState.asStateFlow()

  private var drillTimerJob: Job? = null

  fun selectExplorationRule(ruleId: String) {
    _selectedExplorationRuleId.value = ruleId
  }

  fun startTrainingForRule(ruleId: String) {
    _selectedTrainingRuleId.value = ruleId
    _currentScreen.value = AppScreen.Training
  }

  fun startExplorationForRule(ruleId: String) {
    _selectedExplorationRuleId.value = ruleId
    _currentScreen.value = AppScreen.Exploration
  }

  fun getAllStructuredRules() = GrammarRuleRepository.getAllRules()

  fun generateContent(level: String, ruleId: String, theme: String, format: GenerationFormat) {
    val rule = GrammarRuleRepository.getRuleById(ruleId) ?: return
    val apiKey = _geminiApiKey.value
    if (apiKey.isBlank()) {
      _generationState.value = GenerationUiState(
        isLoading = false,
        error = "Ajoute ta clé API Gemini ci-dessus avant de générer du contenu."
      )
      return
    }
    stopPodcastAudio()
    _generationState.value = GenerationUiState(
      isLoading = true,
      format = format,
      level = level,
      grammarPointTitle = rule.titleFr,
      theme = theme
    )
    viewModelScope.launch {
      generationRepository.generate(
        com.example.data.engine.GenerationRequest(
          level = level,
          grammarPointTitle = rule.titleFr,
          theme = theme,
          format = format
        ),
        apiKey = apiKey
      ).onSuccess { text ->
        _generationState.value = _generationState.value.copy(isLoading = false, resultText = text, isSaved = false)
      }.onFailure { err ->
        // Some exceptions (e.g. Android's NetworkOnMainThreadException) carry a null message,
        // which used to surface as a bare, unhelpful "Erreur inconnue". Fall back to the
        // exception's class name so there's always something actionable on screen.
        val message = err.message?.takeIf { it.isNotBlank() }
          ?: "Erreur inattendue (${err::class.simpleName ?: "inconnue"})"
        _generationState.value = _generationState.value.copy(isLoading = false, error = message)
      }
    }
  }

  /**
   * Persists the currently-displayed generation (text or podcast script + its synthesized audio,
   * if any) to the local database, moving podcast audio out of cacheDir (which the OS can wipe at
   * any time) into filesDir so a saved podcast's audio survives.
   */
  fun saveCurrentGeneration() {
    val state = _generationState.value
    val text = state.resultText ?: return
    val format = state.format ?: return
    val level = state.level ?: return
    val grammarPointTitle = state.grammarPointTitle ?: return
    val theme = state.theme ?: ""
    if (state.isSaved) return

    viewModelScope.launch {
      val persistedAudioPath = state.podcastAudioPath?.let { cachePath ->
        runCatching {
          val cacheFile = File(cachePath)
          val destFile = File(getApplication<Application>().filesDir, cacheFile.name)
          cacheFile.copyTo(destFile, overwrite = true)
          destFile.absolutePath
        }.getOrNull()
      }
      repository.saveGeneration(
        format = format.name,
        level = level,
        grammarPointTitle = grammarPointTitle,
        theme = theme,
        text = text,
        audioPath = persistedAudioPath
      )
      _generationState.value = _generationState.value.copy(isSaved = true)
    }
  }

  fun deleteSavedGeneration(id: Long) {
    viewModelScope.launch {
      repository.deleteSavedGeneration(id)
    }
  }

  /**
   * Copies a podcast WAV (from cacheDir or the app's private filesDir) into the device's public
   * Téléchargements/FrenchGrammarQuest folder via MediaStore, so the user can find, share, or move
   * it outside the app — neither cacheDir nor filesDir are visible to the user or other apps.
   * minSdk is 31, so this always goes through scoped storage (MediaStore.Downloads); no runtime
   * storage permission is needed for that.
   */
  fun exportAudioToDevice(sourcePath: String) {
    val context = getApplication<Application>()
    viewModelScope.launch {
      val result = withContext(Dispatchers.IO) {
        runCatching {
          val sourceFile = File(sourcePath)
          if (!sourceFile.exists()) throw IllegalStateException("Fichier audio introuvable.")
          val resolver = context.contentResolver
          val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, sourceFile.name)
            put(MediaStore.Downloads.MIME_TYPE, "audio/wav")
            put(MediaStore.Downloads.RELATIVE_PATH, "Download/FrenchGrammarQuest")
          }
          val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
            ?: throw IllegalStateException("Impossible de créer le fichier dans Téléchargements.")
          resolver.openOutputStream(uri)?.use { out ->
            sourceFile.inputStream().use { it.copyTo(out) }
          } ?: throw IllegalStateException("Impossible d'écrire le fichier.")
        }
      }
      val message = result.fold(
        onSuccess = { "Podcast enregistré dans Téléchargements/FrenchGrammarQuest" },
        onFailure = { err -> "Erreur lors de l'export : ${err.message ?: "inconnue"}" }
      )
      Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
  }

  /** Plays back audio belonging to a saved generation (separate from the just-generated podcast). */
  fun playSavedAudio(path: String) {
    stopPodcastAudio()
    runCatching {
      podcastPlayer = MediaPlayer().apply {
        setDataSource(path)
        setOnCompletionListener { _playingSavedAudioPath.value = null }
        prepare()
        start()
      }
      _playingSavedAudioPath.value = path
    }
  }

  /**
   * Synthesizes a podcast script into real spoken audio via Gemini's TTS models (two distinct
   * voices), replacing the flat single-voice device TTS readback. Writes the result as a WAV
   * file into the app's cache dir and exposes its path for playback.
   */
  fun synthesizePodcastAudio(script: String) {
    val apiKey = _geminiApiKey.value
    if (apiKey.isBlank()) {
      _generationState.value = _generationState.value.copy(
        isSynthesizingAudio = false,
        audioError = "Ajoute ta clé API Gemini ci-dessus avant de générer l'audio."
      )
      return
    }
    stopPodcastAudio()
    _generationState.value = _generationState.value.copy(
      isSynthesizingAudio = true,
      audioError = null,
      podcastAudioPath = null
    )
    viewModelScope.launch {
      ttsRepository.synthesizeDialogue(
        script = script,
        speaker1 = "Camille",
        speaker2 = "Nadia",
        apiKey = apiKey
      ).onSuccess { wavBytes ->
        val file = File(getApplication<Application>().cacheDir, "podcast_${System.currentTimeMillis()}.wav")
        file.writeBytes(wavBytes)
        _generationState.value = _generationState.value.copy(
          isSynthesizingAudio = false,
          podcastAudioPath = file.absolutePath
        )
      }.onFailure { err ->
        val message = err.message?.takeIf { it.isNotBlank() }
          ?: "Erreur inattendue (${err::class.simpleName ?: "inconnue"})"
        _generationState.value = _generationState.value.copy(isSynthesizingAudio = false, audioError = message)
      }
    }
  }

  fun playPodcastAudio(path: String) {
    stopPodcastAudio()
    runCatching {
      podcastPlayer = MediaPlayer().apply {
        setDataSource(path)
        setOnCompletionListener { _isPodcastPlaying.value = false }
        prepare()
        start()
      }
      _isPodcastPlaying.value = true
    }
  }

  fun stopPodcastAudio() {
    podcastPlayer?.let {
      runCatching { if (it.isPlaying) it.stop() }
      it.release()
    }
    podcastPlayer = null
    _isPodcastPlaying.value = false
    _playingSavedAudioPath.value = null
  }

  /** Called when a Training-path drill session (production or spot-error) finishes. */
  fun recordTrainingSession(ruleId: String, isProductionMode: Boolean, score: Int, total: Int) {
    viewModelScope.launch {
      repository.recordRuleTrainingResult(ruleId, isProductionMode, score, total)
    }
  }

  fun navigateTo(screen: AppScreen) {
    if (_drillState.value.isActive && screen != AppScreen.SpeedDrill) {
      drillTimerJob?.cancel()
      _drillState.value = SpeedDrillState()
    }
    _currentScreen.value = screen
  }

  // --- LESSON & QUIZ FLOW ---
  fun startLesson(lessonId: String) {
    _currentScreen.value = AppScreen.LessonDetail(lessonId)
  }

  fun startQuiz(lessonId: String) {
    val lesson = GrammarData.getLesson(lessonId) ?: return
    val currentHearts = userStats.value.hearts
    if (currentHearts <= 0) {
      // User needs hearts!
      return
    }

    _quizState.value = QuizSessionState(
      lesson = lesson,
      questions = lesson.questions.shuffled(),
      currentIndex = 0,
      selectedOption = null,
      isAnswered = false,
      isCorrect = false,
      score = 0,
      combo = 0,
      maxCombo = 0,
      isFinished = false,
      xpEarned = 0,
      starsEarned = 0,
      heartsLeft = currentHearts
    )
    _currentScreen.value = AppScreen.Quiz(lessonId)
  }

  fun selectQuizOption(index: Int) {
    if (_quizState.value.isAnswered || _quizState.value.isFinished) return
    _quizState.value = _quizState.value.copy(selectedOption = index)
  }

  fun submitQuizAnswer() {
    val state = _quizState.value
    val selected = state.selectedOption ?: return
    if (state.isAnswered || state.isFinished) return

    val currentQ = state.questions.getOrNull(state.currentIndex) ?: return
    val correct = selected == currentQ.correctIndex

    val newCombo = if (correct) state.combo + 1 else 0
    val newMaxCombo = maxOf(state.maxCombo, newCombo)
    val newScore = if (correct) state.score + 1 else state.score
    var updatedHearts = state.heartsLeft

    if (!correct) {
      updatedHearts = maxOf(0, state.heartsLeft - 1)
      viewModelScope.launch {
        repository.loseHeart()
        repository.recordMistake(currentQ, currentQ.options.getOrElse(selected) { "" })
      }
    }

    _quizState.value = state.copy(
      isAnswered = true,
      isCorrect = correct,
      score = newScore,
      combo = newCombo,
      maxCombo = newMaxCombo,
      heartsLeft = updatedHearts
    )
  }

  fun nextQuizQuestion() {
    val state = _quizState.value
    val nextIndex = state.currentIndex + 1

    if (nextIndex >= state.questions.size || state.heartsLeft <= 0) {
      // Finish Quiz Stage
      val totalQuestions = state.questions.size
      val percentage = if (totalQuestions > 0) (state.score.toDouble() / totalQuestions) * 100 else 0.0
      val stars = when {
        percentage >= 100.0 -> 3
        percentage >= 70.0 -> 2
        percentage >= 40.0 -> 1
        else -> 0
      }

      val xp = (state.score * 10) + (state.maxCombo * 3) + (stars * 15)

      _quizState.value = state.copy(
        isFinished = true,
        isAnswered = false,
        starsEarned = stars,
        xpEarned = xp
      )

      viewModelScope.launch {
        state.lesson?.let { l ->
          repository.recordLessonCompleted(l.id, l.categoryId, stars, state.score)
        }
        repository.addXp(xp, questionsAnswered = totalQuestions, correctAnswers = state.score)
      }
    } else {
      _quizState.value = state.copy(
        currentIndex = nextIndex,
        selectedOption = null,
        isAnswered = false,
        isCorrect = false
      )
    }
  }

  // --- SPEED DRILL FLOW ---
  fun startSpeedDrill() {
    drillTimerJob?.cancel()
    val questions = GrammarData.getSpeedDrillQuestions(20)
    _drillState.value = SpeedDrillState(
      isActive = true,
      isFinished = false,
      secondsRemaining = 60,
      questions = questions,
      currentIndex = 0,
      score = 0,
      combo = 0,
      maxCombo = 0
    )
    _currentScreen.value = AppScreen.SpeedDrill

    drillTimerJob = viewModelScope.launch {
      while (_drillState.value.secondsRemaining > 0 && _drillState.value.isActive) {
        delay(1000)
        val remaining = _drillState.value.secondsRemaining - 1
        _drillState.value = _drillState.value.copy(secondsRemaining = remaining)
        if (remaining <= 0) {
          finishSpeedDrill()
        }
      }
    }
  }

  fun answerSpeedDrill(optionIndex: Int) {
    val state = _drillState.value
    if (!state.isActive || state.isFinished) return

    val currentQ = state.questions.getOrNull(state.currentIndex) ?: return
    val isCorrect = optionIndex == currentQ.correctIndex

    val newCombo = if (isCorrect) state.combo + 1 else 0
    val newScore = if (isCorrect) state.score + 1 else state.score
    val newMaxCombo = maxOf(state.maxCombo, newCombo)
    val nextIndex = state.currentIndex + 1

    _drillState.value = state.copy(
      score = newScore,
      combo = newCombo,
      maxCombo = newMaxCombo,
      currentIndex = nextIndex,
      feedbackColor = if (isCorrect) 0xFF10B981 else 0xFFEF4444
    )

    if (nextIndex >= state.questions.size) {
      finishSpeedDrill()
    }
  }

  private fun finishSpeedDrill() {
    drillTimerJob?.cancel()
    val finalScore = _drillState.value.score
    _drillState.value = _drillState.value.copy(isActive = false, isFinished = true)

    viewModelScope.launch {
      repository.recordSpeedDrillResult(finalScore)
    }
  }

  // --- USER PROGRESS ACTIONS ---
  fun refillHeartsWithGems() {
    viewModelScope.launch {
      repository.refillHeartsWithGems()
    }
  }

  fun toggleBookmark(lessonId: String, categoryId: String) {
    viewModelScope.launch {
      repository.toggleBookmark(lessonId, categoryId)
    }
  }

  fun resolveMistake(mistakeId: Long) {
    viewModelScope.launch {
      repository.deleteMistake(mistakeId)
    }
  }

  fun clearAllMistakes() {
    viewModelScope.launch {
      repository.clearAllMistakes()
    }
  }

  // --- AUDIO LISTENING (FRENCH TTS) ---
  fun speakFrench(text: String) {
    audioHelper.speak(text)
  }

  fun stopAudio() {
    audioHelper.stop()
  }

  override fun onCleared() {
    super.onCleared()
    audioHelper.shutdown()
    stopPodcastAudio()
  }
}
