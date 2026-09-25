package com.example.ui

import android.app.Application
import android.content.ContentValues
import android.content.res.Configuration
import android.media.MediaPlayer
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.data.GrammarRepository
import com.example.data.engine.AnswerChecker
import com.example.data.engine.AnswerVerdict
import com.example.data.engine.ForceLayout
import com.example.data.engine.GrammarMapBuilder
import com.example.data.engine.MapGraph
import com.example.data.engine.GeminiGenerationRepository
import com.example.data.engine.GeminiTtsRepository
import com.example.data.engine.GenerationFormat
import com.example.data.engine.GenerationRequest
import com.example.data.engine.GrammarRuleRepository
import com.example.data.engine.StructuredRule
import com.example.data.local.GrammarDatabase
import com.example.data.local.RuleTrainingProgressEntity
import com.example.data.prefs.AppPreferences
import com.example.data.prefs.DrillMode
import com.example.data.prefs.StartMode
import com.example.ui.generation.GenerationForm
import com.example.ui.generation.GenerationUiState
import com.example.ui.generation.SavedGenerationUi
import com.example.ui.i18n.AppLanguage
import com.example.ui.navigation.Routes
import com.example.ui.theme.ThemeMode
import com.example.util.FrenchAudioHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/** Result of one drill item within a training session. */
data class DrillResult(
  val itemId: String,
  val correct: Boolean,
  /** What the learner typed (production) or the token they picked (spot-the-error). */
  val given: String
)

/** Feedback shown after the current drill item was submitted. */
data class DrillFeedback(
  val verdict: AnswerVerdict,
  val given: String,
  val selectedTokenIndex: Int? = null
)

/**
 * A training session lives in the ViewModel so it survives tab switches and rotation.
 * [itemIds] is the ordered set of drill items for this run: all items of the rule, or only the
 * missed ones when [isReview] is true.
 */
data class TrainingSession(
  val ruleId: String,
  val mode: DrillMode,
  val itemIds: List<String>,
  val index: Int = 0,
  val feedback: DrillFeedback? = null,
  val results: List<DrillResult> = emptyList(),
  val isFinished: Boolean = false,
  val isReview: Boolean = false,
  /** Distinguishes runs over the same items, so per-item UI state (typed text) resets on restart. */
  val sessionId: Long = System.nanoTime()
) {
  val score: Int get() = results.count { it.correct }
  val mistakes: List<DrillResult> get() = results.filterNot { it.correct }
}

class GrammarViewModel(application: Application) : AndroidViewModel(application) {

  private val prefs = AppPreferences(application)
  private val repository = GrammarRepository(GrammarDatabase.getDatabase(application).grammarDao())
  private val generationRepository = GeminiGenerationRepository()
  private val ttsRepository = GeminiTtsRepository()
  private val audioHelper = FrenchAudioHelper(application)
  private var podcastPlayer: MediaPlayer? = null

  /** All rules in curriculum order (A1 → C2, keeping the authoring order inside a level). */
  val rules: List<StructuredRule> = GrammarRuleRepository.getAllRules().sortedBy { it.level }

  private val defaultRuleId = rules.first().id

  private fun validRuleId(id: String?): String =
    id?.takeIf { candidate -> rules.any { it.id == candidate } } ?: defaultRuleId

  fun ruleById(id: String): StructuredRule = rules.firstOrNull { it.id == id } ?: rules.first()

  // --- APP SETTINGS ---

  /** Where the app opens; computed once so the NavHost start destination never changes mid-session. */
  val startRoute: String = when {
    !prefs.onboardingDone -> Routes.ONBOARDING
    prefs.startMode == StartMode.TRAIN -> Routes.TRAIN
    else -> Routes.EXPLORE
  }

  private val _themeMode = MutableStateFlow(prefs.themeMode)
  val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

  fun setThemeMode(mode: ThemeMode) {
    _themeMode.value = mode
    prefs.themeMode = mode
  }

  private val _startMode = MutableStateFlow(prefs.startMode)
  val startMode: StateFlow<StartMode> = _startMode.asStateFlow()

  fun setStartMode(mode: StartMode) {
    _startMode.value = mode
    prefs.startMode = mode
  }

  fun completeOnboarding(mode: StartMode) {
    setStartMode(mode)
    prefs.onboardingDone = true
  }

  /** On-demand English help. Not persisted: every launch starts in French. */
  private val _language = MutableStateFlow(AppLanguage.FR)
  val language: StateFlow<AppLanguage> = _language.asStateFlow()

  fun toggleLanguage() {
    _language.update { if (it == AppLanguage.FR) AppLanguage.EN else AppLanguage.FR }
  }

  // User-supplied Google AI Studio (Gemini Developer API) key — kept only in local
  // SharedPreferences on this device, never bundled with the app or sent anywhere but
  // Google's own generativelanguage.googleapis.com endpoint.
  private val _geminiApiKey = MutableStateFlow(prefs.geminiApiKey)
  val geminiApiKey: StateFlow<String> = _geminiApiKey.asStateFlow()

  fun setGeminiApiKey(key: String) {
    val trimmed = key.trim()
    _geminiApiKey.value = trimmed
    prefs.geminiApiKey = trimmed
  }

  // --- RULE SELECTION (shared by Explore, Train, Create and Index) ---

  private val _levelFilter = MutableStateFlow(prefs.levelFilter)
  /** CEFR level the rule lists are filtered on; null means all levels. */
  val levelFilter: StateFlow<String?> = _levelFilter.asStateFlow()

  fun setLevelFilter(level: String?) {
    _levelFilter.value = level
    prefs.levelFilter = level
  }

  private val _exploreRuleId = MutableStateFlow(validRuleId(prefs.exploreRuleId))
  val exploreRuleId: StateFlow<String> = _exploreRuleId.asStateFlow()

  fun selectExploreRule(ruleId: String) {
    val id = validRuleId(ruleId)
    _exploreRuleId.value = id
    prefs.exploreRuleId = id
    if (_generationForm.value.followsExplore) {
      _generationForm.update { it.copy(ruleId = id, level = ruleById(id).level) }
    }
  }

  val ruleTrainingProgressMap: StateFlow<Map<String, RuleTrainingProgressEntity>> =
    repository.ruleTrainingProgress
      .map { list -> list.associateBy { it.ruleId } }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyMap()
      )

  // --- TRAINING ---

  private val _training = MutableStateFlow(
    newSession(validRuleId(prefs.trainRuleId), prefs.drillMode)
  )
  val training: StateFlow<TrainingSession> = _training.asStateFlow()

  private fun drillItemIds(rule: StructuredRule, mode: DrillMode): List<String> = when (mode) {
    DrillMode.PRODUCTION -> rule.productionDrills.map { it.id }
    DrillMode.SPOT_ERROR -> rule.spotErrorDrills.map { it.id }
  }

  private fun newSession(ruleId: String, mode: DrillMode): TrainingSession =
    TrainingSession(ruleId = ruleId, mode = mode, itemIds = drillItemIds(ruleById(ruleId), mode))

  /** Opens Training on [ruleId]; an unfinished session on the same rule is kept, not restarted. */
  fun selectTrainingRule(ruleId: String) {
    val id = validRuleId(ruleId)
    prefs.trainRuleId = id
    val current = _training.value
    if (current.ruleId == id && !current.isFinished) return
    _training.value = newSession(id, current.mode)
  }

  fun setDrillMode(mode: DrillMode) {
    prefs.drillMode = mode
    val current = _training.value
    if (current.mode == mode) return
    _training.value = newSession(current.ruleId, mode)
  }

  fun submitProductionAnswer(input: String) {
    val session = _training.value
    if (session.feedback != null || session.isFinished) return
    val itemId = session.itemIds.getOrNull(session.index) ?: return
    val drill = ruleById(session.ruleId).productionDrills.firstOrNull { it.id == itemId } ?: return
    val verdict = AnswerChecker.check(input, listOf(drill.targetAnswer) + drill.acceptedAnswers)
    _training.value = session.copy(
      feedback = DrillFeedback(verdict = verdict, given = input.trim()),
      results = session.results + DrillResult(itemId, verdict == AnswerVerdict.CORRECT, input.trim())
    )
  }

  fun submitSpotError(tokenIndex: Int) {
    val session = _training.value
    if (session.feedback != null || session.isFinished) return
    val itemId = session.itemIds.getOrNull(session.index) ?: return
    val drill = ruleById(session.ruleId).spotErrorDrills.firstOrNull { it.id == itemId } ?: return
    val correct = tokenIndex == drill.errorTokenIndex
    val given = drill.tokens.getOrElse(tokenIndex) { "" }
    _training.value = session.copy(
      feedback = DrillFeedback(
        verdict = if (correct) AnswerVerdict.CORRECT else AnswerVerdict.WRONG,
        given = given,
        selectedTokenIndex = tokenIndex
      ),
      results = session.results + DrillResult(itemId, correct, given)
    )
  }

  fun nextDrill() {
    val session = _training.value
    if (session.feedback == null) return
    if (session.index + 1 < session.itemIds.size) {
      _training.value = session.copy(index = session.index + 1, feedback = null)
    } else {
      _training.value = session.copy(feedback = null, isFinished = true)
      // A review run only replays missed items; recording it would overwrite the real best score.
      if (!session.isReview) {
        viewModelScope.launch {
          repository.recordRuleTrainingResult(
            ruleId = session.ruleId,
            isProductionMode = session.mode == DrillMode.PRODUCTION,
            score = session.score,
            total = session.itemIds.size
          )
        }
      }
    }
  }

  fun restartTraining() {
    val session = _training.value
    _training.value = newSession(session.ruleId, session.mode)
  }

  /** Replays only the items missed in the session that just finished. */
  fun reviewMistakes() {
    val session = _training.value
    val missed = session.mistakes.map { it.itemId }.distinct()
    if (missed.isEmpty()) return
    _training.value = TrainingSession(
      ruleId = session.ruleId,
      mode = session.mode,
      itemIds = missed,
      isReview = true
    )
  }

  /** The rule after [ruleId] in curriculum order, or null at the end. */
  fun nextRuleId(ruleId: String): String? {
    val index = rules.indexOfFirst { it.id == ruleId }
    return rules.getOrNull(index + 1)?.id
  }

  // --- GENERATION ---

  private val _generationForm = MutableStateFlow(
    GenerationForm(ruleId = _exploreRuleId.value, level = ruleById(_exploreRuleId.value).level)
  )
  val generationForm: StateFlow<GenerationForm> = _generationForm.asStateFlow()

  fun updateGenerationForm(transform: (GenerationForm) -> GenerationForm) {
    _generationForm.update(transform)
  }

  /** Picking a rule inside Create detaches the form from the rule open in Explore. */
  fun selectGenerationRule(ruleId: String) {
    val id = validRuleId(ruleId)
    _generationForm.update { it.copy(ruleId = id, level = ruleById(id).level, followsExplore = false) }
  }

  /** "Create a text on this rule" from Explore. */
  fun prepareGenerationForRule(ruleId: String) {
    val id = validRuleId(ruleId)
    _generationForm.update { it.copy(ruleId = id, level = ruleById(id).level, followsExplore = true) }
  }

  private val _generationState = MutableStateFlow(GenerationUiState())
  val generationState: StateFlow<GenerationUiState> = _generationState.asStateFlow()

  val savedGenerations: StateFlow<List<SavedGenerationUi>> = repository.savedGenerations
    .map { list ->
      list.map {
        SavedGenerationUi(
          id = it.id,
          format = runCatching { GenerationFormat.valueOf(it.format) }.getOrDefault(GenerationFormat.TEXT),
          level = it.level,
          grammarPointTitle = it.grammarPointTitle,
          theme = it.theme,
          text = it.text,
          audioPath = it.audioPath,
          createdTimestamp = it.createdTimestamp
        )
      }
    }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  fun generateContent() {
    val form = _generationForm.value
    val rule = ruleById(form.ruleId)
    val apiKey = _geminiApiKey.value
    if (apiKey.isBlank()) {
      _generationState.value = GenerationUiState(error = string(R.string.gen_error_no_key))
      return
    }
    val theme = form.theme.trim().ifBlank { string(R.string.gen_default_theme) }
    stopPodcastAudio()
    _generationState.value = GenerationUiState(
      isLoading = true,
      format = form.format,
      level = form.level,
      grammarPointTitle = rule.titleFr,
      theme = theme
    )
    viewModelScope.launch {
      generationRepository.generate(
        GenerationRequest(
          level = form.level,
          grammarPointTitle = rule.titleFr,
          theme = theme,
          format = form.format
        ),
        apiKey = apiKey
      ).onSuccess { text ->
        _generationState.update { it.copy(isLoading = false, resultText = text, isSaved = false) }
      }.onFailure { err ->
        _generationState.update { it.copy(isLoading = false, error = errorMessage(err)) }
      }
    }
  }

  fun dismissGeneration() {
    stopPodcastAudio()
    _generationState.value = GenerationUiState()
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
        withContext(Dispatchers.IO) {
          runCatching {
            val cacheFile = File(cachePath)
            val destFile = File(getApplication<Application>().filesDir, cacheFile.name)
            cacheFile.copyTo(destFile, overwrite = true)
            destFile.absolutePath
          }.getOrNull()
        }
      }
      repository.saveGeneration(
        format = format.name,
        level = level,
        grammarPointTitle = grammarPointTitle,
        theme = theme,
        text = text,
        audioPath = persistedAudioPath
      )
      _generationState.update { it.copy(isSaved = true) }
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
          if (!sourceFile.exists()) throw IllegalStateException(string(R.string.export_error_missing))
          val resolver = context.contentResolver
          val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, sourceFile.name)
            put(MediaStore.Downloads.MIME_TYPE, "audio/wav")
            put(MediaStore.Downloads.RELATIVE_PATH, "Download/FrenchGrammarQuest")
          }
          val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
            ?: throw IllegalStateException(string(R.string.export_error_create))
          resolver.openOutputStream(uri)?.use { out ->
            sourceFile.inputStream().use { it.copyTo(out) }
          } ?: throw IllegalStateException(string(R.string.export_error_write))
        }
      }
      val message = result.fold(
        onSuccess = { string(R.string.export_success) },
        onFailure = { err -> string(R.string.export_error, errorMessage(err)) }
      )
      Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
  }

  private val _isPodcastPlaying = MutableStateFlow(false)
  val isPodcastPlaying: StateFlow<Boolean> = _isPodcastPlaying.asStateFlow()

  // Path of the saved-generation audio currently playing (distinct from the just-generated
  // podcast's own audio, which uses podcastAudioPath/isPodcastPlaying above).
  private val _playingSavedAudioPath = MutableStateFlow<String?>(null)
  val playingSavedAudioPath: StateFlow<String?> = _playingSavedAudioPath.asStateFlow()

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
      _generationState.update {
        it.copy(isSynthesizingAudio = false, audioError = string(R.string.gen_error_no_key))
      }
      return
    }
    stopPodcastAudio()
    _generationState.update {
      it.copy(isSynthesizingAudio = true, audioError = null, podcastAudioPath = null)
    }
    viewModelScope.launch {
      ttsRepository.synthesizeDialogue(
        script = script,
        speaker1 = "Camille",
        speaker2 = "Nadia",
        apiKey = apiKey
      ).onSuccess { wavBytes ->
        val file = withContext(Dispatchers.IO) {
          File(getApplication<Application>().cacheDir, "podcast_${System.currentTimeMillis()}.wav")
            .also { it.writeBytes(wavBytes) }
        }
        _generationState.update {
          it.copy(isSynthesizingAudio = false, podcastAudioPath = file.absolutePath)
        }
      }.onFailure { err ->
        _generationState.update { it.copy(isSynthesizingAudio = false, audioError = errorMessage(err)) }
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

  // --- GRAMMAR MAP ---

  private val _mapFocus = MutableStateFlow(prefs.mapFocusRuleId)
  val mapFocus: StateFlow<String?> = _mapFocus.asStateFlow()

  private val ruleIds = rules.map { it.id }.toSet()

  /** Rules on the map (opened in Comprendre, trained, or added from the map). */
  val mapExploredIds: StateFlow<Set<String>> = repository.ruleVisits
    .map { visits -> visits.map { it.ruleId }.filter { it in ruleIds }.toSet() }
    .stateIn(viewModelScope, SharingStarted.Eagerly, emptySet())

  private val masteredIds = repository.ruleTrainingProgress.map { list ->
    list.filter { progress ->
      val production = if (progress.bestProductionTotal > 0) progress.bestProductionScore.toFloat() / progress.bestProductionTotal else 0f
      val spotting = if (progress.bestSpotErrorTotal > 0) progress.bestSpotErrorScore.toFloat() / progress.bestSpotErrorTotal else 0f
      maxOf(production, spotting) >= GrammarMapBuilder.MASTERY_RATIO
    }.map { it.ruleId }.toSet()
  }

  /** Null until the first computation, so the screen can tell "loading" from "empty". */
  val mapGraph: StateFlow<MapGraph?> = combine(repository.ruleVisits, masteredIds, _mapFocus) { visits, mastered, focus ->
    GrammarMapBuilder.build(rules, visits.map { it.ruleId }.toSet(), mastered, focus)
  }
    .flowOn(Dispatchers.Default)
    .stateIn(viewModelScope, SharingStarted.Eagerly, null)

  private val positionCache = mutableMapOf<String, Pair<Float, Float>>()
  private val _mapPositions = MutableStateFlow<Map<String, Pair<Float, Float>>>(emptyMap())
  /** Node positions in dp around (0, 0), for the nodes of the current [mapGraph]. */
  val mapPositions: StateFlow<Map<String, Pair<Float, Float>>> = _mapPositions.asStateFlow()

  init {
    viewModelScope.launch {
      if (!prefs.mapSeeded) {
        repository.seedVisitsFromTraining()
        prefs.mapSeeded = true
      }
      positionCache.putAll(repository.mapPositions())
      mapGraph.filterNotNull().collectLatest { graph ->
        val ids = graph.nodes.map { it.ruleId }
        val known = positionCache.filterKeys { it in ids }
        val laidOut = withContext(Dispatchers.Default) {
          ForceLayout.layout(ids, graph.edges.map { it.from to it.to }, known)
        }
        positionCache.putAll(laidOut)
        _mapPositions.value = laidOut
        if (laidOut.keys.any { it !in known }) repository.saveMapPositions(laidOut)
      }
    }
  }

  /** Called whenever a rule is shown in Comprendre: it joins the map and becomes the focus. */
  fun markVisited(ruleId: String) {
    if (ruleId !in ruleIds) return
    viewModelScope.launch { repository.recordVisit(ruleId) }
    setMapFocus(ruleId)
  }

  /** Adds a rule to the map without leaving it (ghost node or picker). */
  fun addToMap(ruleId: String) = markVisited(ruleId)

  fun setMapFocus(ruleId: String?) {
    _mapFocus.value = ruleId
    prefs.mapFocusRuleId = ruleId
  }

  /** The learner dragged a node: keep it where they put it. */
  fun moveMapNode(ruleId: String, x: Float, y: Float) {
    positionCache[ruleId] = x to y
    _mapPositions.update { it + (ruleId to (x to y)) }
    viewModelScope.launch { repository.saveMapPositions(mapOf(ruleId to (x to y))) }
  }

  fun resetMap() {
    viewModelScope.launch {
      repository.clearMap()
      positionCache.clear()
      _mapPositions.value = emptyMap()
    }
    setMapFocus(null)
  }

  /** A few rules not on the map yet, closest to where the learner currently is. */
  fun mapSuggestions(onMap: Set<String>, count: Int = 3): List<StructuredRule> {
    val targetLevel = _levelFilter.value ?: _mapFocus.value?.let { ruleById(it).level }
    val candidates = rules.filter { it.id !in onMap }
    val sameLevel = candidates.filter { targetLevel == null || it.level == targetLevel }
    return (sameLevel + candidates).distinct().take(count)
  }

  // --- AUDIO LISTENING (FRENCH TTS) ---

  val isSpeaking: StateFlow<Boolean> = audioHelper.isSpeaking

  fun speakFrench(text: String) {
    audioHelper.speak(text)
  }

  fun stopAudio() {
    audioHelper.stop()
  }

  // --- HELPERS ---

  /** Resolves a string in the language currently shown, so ViewModel messages follow the EN toggle. */
  private fun string(@StringRes id: Int, vararg args: Any): String {
    val app = getApplication<Application>()
    val config = Configuration(app.resources.configuration).apply { setLocale(_language.value.locale) }
    return app.createConfigurationContext(config).resources.getString(id, *args)
  }

  // Some exceptions (e.g. Android's NetworkOnMainThreadException) carry a null message; fall back
  // to the exception's class name so there's always something actionable on screen.
  private fun errorMessage(err: Throwable): String =
    err.message?.takeIf { it.isNotBlank() }
      ?: string(R.string.error_unexpected, err::class.simpleName ?: "?")

  override fun onCleared() {
    super.onCleared()
    audioHelper.shutdown()
    stopPodcastAudio()
  }
}
