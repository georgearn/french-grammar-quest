package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GrammarDao {

  // User Stats
  @Query("SELECT * FROM user_stats WHERE id = 1 LIMIT 1")
  fun getUserStats(): Flow<UserStatsEntity?>

  @Query("SELECT * FROM user_stats WHERE id = 1 LIMIT 1")
  suspend fun getUserStatsDirect(): UserStatsEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdateUserStats(stats: UserStatsEntity)

  // Lesson Progress
  @Query("SELECT * FROM lesson_progress")
  fun getAllLessonProgress(): Flow<List<LessonProgressEntity>>

  @Query("SELECT * FROM lesson_progress WHERE lessonId = :lessonId LIMIT 1")
  suspend fun getLessonProgress(lessonId: String): LessonProgressEntity?

  @Query("SELECT * FROM lesson_progress WHERE isBookmarked = 1")
  fun getBookmarkedLessons(): Flow<List<LessonProgressEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveLessonProgress(progress: LessonProgressEntity)

  @Query("UPDATE lesson_progress SET isBookmarked = :bookmarked WHERE lessonId = :lessonId")
  suspend fun setBookmark(lessonId: String, bookmarked: Boolean)

  // Mistakes
  @Query("SELECT * FROM mistakes ORDER BY timestamp DESC LIMIT 50")
  fun getAllMistakes(): Flow<List<MistakeEntity>>

  @Query("SELECT COUNT(*) FROM mistakes")
  fun getMistakesCount(): Flow<Int>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMistake(mistake: MistakeEntity)

  @Query("DELETE FROM mistakes WHERE id = :id")
  suspend fun deleteMistake(id: Long)

  @Query("DELETE FROM mistakes WHERE lessonId = :lessonId")
  suspend fun clearMistakesForLesson(lessonId: String)

  @Query("DELETE FROM mistakes")
  suspend fun clearAllMistakes()

  // Achievements
  @Query("SELECT * FROM achievements")
  fun getAllAchievements(): Flow<List<AchievementEntity>>

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertAchievements(achievements: List<AchievementEntity>)

  @Update
  suspend fun updateAchievement(achievement: AchievementEntity)

  // Rule training progress (gamification path, keyed by StructuredRule id)
  @Query("SELECT * FROM rule_training_progress")
  fun getAllRuleTrainingProgress(): Flow<List<RuleTrainingProgressEntity>>

  @Query("SELECT * FROM rule_training_progress WHERE ruleId = :ruleId LIMIT 1")
  suspend fun getRuleTrainingProgress(ruleId: String): RuleTrainingProgressEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun upsertRuleTrainingProgress(progress: RuleTrainingProgressEntity)

  // Saved generations (Génération tab)
  @Query("SELECT * FROM saved_generations ORDER BY createdTimestamp DESC")
  fun getAllSavedGenerations(): Flow<List<SavedGenerationEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSavedGeneration(generation: SavedGenerationEntity): Long

  @Query("DELETE FROM saved_generations WHERE id = :id")
  suspend fun deleteSavedGeneration(id: Long)
}
