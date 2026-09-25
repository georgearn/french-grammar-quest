package com.example.data

import com.example.data.grammar.GrammarData
import com.example.data.local.AchievementEntity
import com.example.data.local.GrammarDao
import com.example.data.local.LessonProgressEntity
import com.example.data.local.MistakeEntity
import com.example.data.local.RuleTrainingProgressEntity
import com.example.data.local.SavedGenerationEntity
import com.example.data.local.UserStatsEntity
import com.example.data.model.GrammarCategory
import com.example.data.model.GrammarLesson
import com.example.data.model.QuizQuestion
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GrammarRepository(private val dao: GrammarDao) {

  val userStats: Flow<UserStatsEntity?> = dao.getUserStats()
  val lessonProgressList: Flow<List<LessonProgressEntity>> = dao.getAllLessonProgress()
  val bookmarkedLessons: Flow<List<LessonProgressEntity>> = dao.getBookmarkedLessons()
  val mistakes: Flow<List<MistakeEntity>> = dao.getAllMistakes()
  val mistakesCount: Flow<Int> = dao.getMistakesCount()
  val achievements: Flow<List<AchievementEntity>> = dao.getAllAchievements()
  val ruleTrainingProgress: Flow<List<RuleTrainingProgressEntity>> = dao.getAllRuleTrainingProgress()
  val savedGenerations: Flow<List<SavedGenerationEntity>> = dao.getAllSavedGenerations()

  private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE)

  suspend fun initDefaultDataIfNeeded() {
    val stats = dao.getUserStatsDirect()
    val today = dateFormat.format(Date())
    if (stats == null) {
      dao.insertOrUpdateUserStats(
        UserStatsEntity(
          id = 1,
          xp = 0,
          level = 1,
          gems = 40,
          hearts = 5,
          maxHearts = 5,
          lastHeartRegenTimestamp = System.currentTimeMillis(),
          currentStreak = 1,
          lastActiveDate = today,
          dailyGoalXp = 50,
          dailyEarnedXp = 0,
          lastDailyDate = today
        )
      )
    } else {
      // Check day rollover for streak and daily goal
      var updatedStreak = stats.currentStreak
      var updatedDailyXp = stats.dailyEarnedXp
      if (stats.lastDailyDate != today) {
        updatedDailyXp = 0
      }
      if (stats.lastActiveDate != today && stats.lastActiveDate.isNotEmpty()) {
        try {
          val lastDate = dateFormat.parse(stats.lastActiveDate)
          val diffDays = (Date().time - (lastDate?.time ?: Date().time)) / (1000 * 60 * 60 * 24)
          if (diffDays == 1L) {
            updatedStreak += 1
          } else if (diffDays > 1L) {
            updatedStreak = 1
          }
        } catch (_: Exception) {
          updatedStreak = 1
        }
      }

      // Check heart regeneration (1 heart every 30 minutes, max 5)
      val timePassed = System.currentTimeMillis() - stats.lastHeartRegenTimestamp
      val heartsToAdd = (timePassed / (30 * 60 * 1000)).toInt()
      val newHearts = minOf(stats.maxHearts, stats.hearts + heartsToAdd)

      dao.insertOrUpdateUserStats(
        stats.copy(
          currentStreak = updatedStreak,
          dailyEarnedXp = updatedDailyXp,
          lastActiveDate = today,
          lastDailyDate = today,
          hearts = newHearts,
          lastHeartRegenTimestamp = if (heartsToAdd > 0) System.currentTimeMillis() else stats.lastHeartRegenTimestamp
        )
      )
    }

    // Initialize Default Achievements
    val initialAchievements = listOf(
      AchievementEntity("first_step", "Premier Pas", "Terminez votre premier défi de grammaire.", "🎯", false),
      AchievementEntity("streak_3", "En Flammes", "Atteignez une série de 3 jours d'apprentissage.", "🔥", false),
      AchievementEntity("gender_master", "Maître du Genre", "Maîtrisez les pièges du masculin et féminin.", "⚖️", false),
      AchievementEntity("subjunctive_hero", "Le Dompteur de Subjonctif", "Réussissez le défi du subjonctif sans faute.", "🔮", false),
      AchievementEntity("speed_demon", "Éclair Bleu", "Scorez 10+ dans le Défi Éclair (60s).", "⚡", false),
      AchievementEntity("perfectionist", "Le Sans-Faute", "Obtenez 3 étoiles sur 3 leçons différentes.", "🌟", false)
    )
    dao.insertAchievements(initialAchievements)
  }

  suspend fun addXp(earnedXp: Int, questionsAnswered: Int = 0, correctAnswers: Int = 0) {
    val stats = dao.getUserStatsDirect() ?: return
    val newXp = stats.xp + earnedXp
    val newDailyXp = stats.dailyEarnedXp + earnedXp
    val newLevel = GrammarData.getLevelInfo(newXp).level
    val gemsEarned = if (newLevel > stats.level) 15 else 0

    dao.insertOrUpdateUserStats(
      stats.copy(
        xp = newXp,
        level = newLevel,
        gems = stats.gems + gemsEarned,
        dailyEarnedXp = newDailyXp,
        totalQuestionsAnswered = stats.totalQuestionsAnswered + questionsAnswered,
        totalCorrectAnswers = stats.totalCorrectAnswers + correctAnswers
      )
    )

    // Check achievement for first step
    if (newXp > 0) {
      unlockAchievement("first_step")
    }
  }

  suspend fun loseHeart(): Boolean {
    val stats = dao.getUserStatsDirect() ?: return false
    val newHearts = maxOf(0, stats.hearts - 1)
    dao.insertOrUpdateUserStats(stats.copy(hearts = newHearts))
    return newHearts > 0
  }

  suspend fun refillHeartsWithGems(): Boolean {
    val stats = dao.getUserStatsDirect() ?: return false
    val cost = 20
    if (stats.gems >= cost && stats.hearts < stats.maxHearts) {
      dao.insertOrUpdateUserStats(
        stats.copy(
          gems = stats.gems - cost,
          hearts = stats.maxHearts,
          lastHeartRegenTimestamp = System.currentTimeMillis()
        )
      )
      return true
    }
    return false
  }

  suspend fun awardFreeHeart() {
    val stats = dao.getUserStatsDirect() ?: return
    if (stats.hearts < stats.maxHearts) {
      dao.insertOrUpdateUserStats(
        stats.copy(hearts = stats.hearts + 1)
      )
    }
  }

  suspend fun recordLessonCompleted(lessonId: String, categoryId: String, stars: Int, score: Int) {
    val existing = dao.getLessonProgress(lessonId)
    val bestScore = maxOf(existing?.bestScore ?: 0, score)
    val bestStars = maxOf(existing?.stars ?: 0, stars)
    val completedTimes = (existing?.completedTimes ?: 0) + 1

    dao.saveLessonProgress(
      LessonProgressEntity(
        lessonId = lessonId,
        categoryId = categoryId,
        stars = bestStars,
        completedTimes = completedTimes,
        bestScore = bestScore,
        isBookmarked = existing?.isBookmarked ?: false,
        lastCompletedTimestamp = System.currentTimeMillis()
      )
    )

    if (lessonId == "genre-noms" && bestStars >= 2) {
      unlockAchievement("gender_master")
    }
    if (lessonId == "subjonctif" && bestStars >= 3) {
      unlockAchievement("subjunctive_hero")
    }
  }

  suspend fun recordSpeedDrillResult(score: Int) {
    val stats = dao.getUserStatsDirect() ?: return
    val newHighScore = maxOf(stats.speedDrillHighScore, score)
    val bonusGems = score / 3
    val earnedXp = score * 5

    dao.insertOrUpdateUserStats(
      stats.copy(
        speedDrillHighScore = newHighScore,
        gems = stats.gems + bonusGems,
        xp = stats.xp + earnedXp,
        dailyEarnedXp = stats.dailyEarnedXp + earnedXp
      )
    )

    if (score >= 10) {
      unlockAchievement("speed_demon")
    }
  }

  suspend fun toggleBookmark(lessonId: String, categoryId: String) {
    val existing = dao.getLessonProgress(lessonId)
    if (existing == null) {
      dao.saveLessonProgress(
        LessonProgressEntity(
          lessonId = lessonId,
          categoryId = categoryId,
          stars = 0,
          completedTimes = 0,
          bestScore = 0,
          isBookmarked = true
        )
      )
    } else {
      dao.setBookmark(lessonId, !existing.isBookmarked)
    }
  }

  suspend fun recordMistake(question: QuizQuestion, userAnswer: String) {
    dao.insertMistake(
      MistakeEntity(
        lessonId = question.lessonId,
        promptFr = question.promptFr,
        promptEn = question.promptEn,
        correctAnswer = question.options[question.correctIndex],
        userAnswer = userAnswer,
        explanation = question.explanation
      )
    )
  }

  suspend fun deleteMistake(id: Long) {
    dao.deleteMistake(id)
    awardFreeHeart()
  }

  suspend fun clearAllMistakes() {
    dao.clearAllMistakes()
  }

  private suspend fun unlockAchievement(id: String) {
    dao.updateAchievement(
      AchievementEntity(
        id = id,
        title = "",
        desc = "",
        icon = "",
        isUnlocked = true,
        unlockedTimestamp = System.currentTimeMillis()
      )
    )
  }

  fun getCategories(): List<GrammarCategory> = GrammarData.CATEGORIES
  fun getLesson(id: String): GrammarLesson? = GrammarData.getLesson(id)

  /**
   * Records the result of a Training-path drill session against a StructuredRule and
   * feeds the earned XP into the same global economy used by the legacy quiz flow, so
   * the Gamification path (Training + Codex) has one consistent progress source.
   */
  suspend fun recordRuleTrainingResult(
    ruleId: String,
    isProductionMode: Boolean,
    score: Int,
    total: Int
  ) {
    if (total <= 0) return
    val existing = dao.getRuleTrainingProgress(ruleId)
    val earnedXp = score * 8

    val updated = if (existing == null) {
      RuleTrainingProgressEntity(
        ruleId = ruleId,
        bestProductionScore = if (isProductionMode) score else 0,
        bestProductionTotal = if (isProductionMode) total else 0,
        bestSpotErrorScore = if (!isProductionMode) score else 0,
        bestSpotErrorTotal = if (!isProductionMode) total else 0,
        totalXpEarned = earnedXp,
        sessionsCompleted = 1,
        lastTrainedTimestamp = System.currentTimeMillis()
      )
    } else {
      existing.copy(
        bestProductionScore = if (isProductionMode) maxOf(existing.bestProductionScore, score) else existing.bestProductionScore,
        bestProductionTotal = if (isProductionMode) total else existing.bestProductionTotal,
        bestSpotErrorScore = if (!isProductionMode) maxOf(existing.bestSpotErrorScore, score) else existing.bestSpotErrorScore,
        bestSpotErrorTotal = if (!isProductionMode) total else existing.bestSpotErrorTotal,
        totalXpEarned = existing.totalXpEarned + earnedXp,
        sessionsCompleted = existing.sessionsCompleted + 1,
        lastTrainedTimestamp = System.currentTimeMillis()
      )
    }

    dao.upsertRuleTrainingProgress(updated)
    addXp(earnedXp, questionsAnswered = total, correctAnswers = score)

    if (updated.bestProductionScore == updated.bestProductionTotal && updated.bestProductionTotal > 0) {
      unlockAchievement("perfectionist")
    }
  }

  suspend fun saveGeneration(
    format: String,
    level: String,
    grammarPointTitle: String,
    theme: String,
    text: String,
    audioPath: String? = null
  ): Long = dao.insertSavedGeneration(
    SavedGenerationEntity(
      format = format,
      level = level,
      grammarPointTitle = grammarPointTitle,
      theme = theme,
      text = text,
      audioPath = audioPath
    )
  )

  suspend fun deleteSavedGeneration(id: Long) {
    dao.deleteSavedGeneration(id)
  }
}
