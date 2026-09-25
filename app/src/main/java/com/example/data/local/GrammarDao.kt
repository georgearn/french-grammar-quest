package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GrammarDao {

  // Rule training progress (keyed by StructuredRule id)
  @Query("SELECT * FROM rule_training_progress")
  fun getAllRuleTrainingProgress(): Flow<List<RuleTrainingProgressEntity>>

  @Query("SELECT * FROM rule_training_progress WHERE ruleId = :ruleId LIMIT 1")
  suspend fun getRuleTrainingProgress(ruleId: String): RuleTrainingProgressEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun upsertRuleTrainingProgress(progress: RuleTrainingProgressEntity)

  // Saved generations (Create tab)
  @Query("SELECT * FROM saved_generations ORDER BY createdTimestamp DESC")
  fun getAllSavedGenerations(): Flow<List<SavedGenerationEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSavedGeneration(generation: SavedGenerationEntity): Long

  @Query("DELETE FROM saved_generations WHERE id = :id")
  suspend fun deleteSavedGeneration(id: Long)
}
