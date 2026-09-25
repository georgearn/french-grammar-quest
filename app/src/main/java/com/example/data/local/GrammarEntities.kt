package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_stats")
data class UserStatsEntity(
  @PrimaryKey val id: Int = 1,
  val xp: Int = 0,
  val level: Int = 1,
  val gems: Int = 30,
  val hearts: Int = 5,
  val maxHearts: Int = 5,
  val lastHeartRegenTimestamp: Long = System.currentTimeMillis(),
  val currentStreak: Int = 1,
  val lastActiveDate: String = "",
  val dailyGoalXp: Int = 50,
  val dailyEarnedXp: Int = 0,
  val lastDailyDate: String = "",
  val speedDrillHighScore: Int = 0,
  val totalQuestionsAnswered: Int = 0,
  val totalCorrectAnswers: Int = 0
)

@Entity(tableName = "lesson_progress")
data class LessonProgressEntity(
  @PrimaryKey val lessonId: String,
  val categoryId: String,
  val stars: Int = 0, // 0 to 3
  val completedTimes: Int = 0,
  val bestScore: Int = 0,
  val isBookmarked: Boolean = false,
  val lastCompletedTimestamp: Long = 0L
)

@Entity(tableName = "mistakes")
data class MistakeEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0L,
  val lessonId: String,
  val promptFr: String,
  val promptEn: String,
  val correctAnswer: String,
  val userAnswer: String,
  val explanation: String,
  val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "achievements")
data class AchievementEntity(
  @PrimaryKey val id: String,
  val title: String,
  val desc: String,
  val icon: String,
  val isUnlocked: Boolean = false,
  val unlockedTimestamp: Long? = null
)

/**
 * Gamification progress for a StructuredRule (data/engine/GrammarRuleRepository), tracked
 * from the Training path — kept separate from the legacy [LessonProgressEntity] which
 * tracks the older GrammarData quest-map lessons.
 */
@Entity(tableName = "rule_training_progress")
data class RuleTrainingProgressEntity(
  @PrimaryKey val ruleId: String,
  val bestProductionScore: Int = 0,
  val bestProductionTotal: Int = 0,
  val bestSpotErrorScore: Int = 0,
  val bestSpotErrorTotal: Int = 0,
  val totalXpEarned: Int = 0,
  val sessionsCompleted: Int = 0,
  val lastTrainedTimestamp: Long = 0L
)

/** A generated text/podcast script the user chose to keep (Génération tab "Enregistrer"). */
@Entity(tableName = "saved_generations")
data class SavedGenerationEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0L,
  val format: String, // GenerationFormat.name
  val level: String,
  val grammarPointTitle: String,
  val theme: String,
  val text: String,
  val audioPath: String? = null, // WAV file in filesDir, only for saved podcasts with synthesized audio
  val createdTimestamp: Long = System.currentTimeMillis()
)
