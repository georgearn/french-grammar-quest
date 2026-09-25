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

  // Grammar map
  @Query("SELECT * FROM rule_visit")
  fun getAllRuleVisits(): Flow<List<RuleVisitEntity>>

  @Query("SELECT * FROM rule_visit WHERE ruleId = :ruleId LIMIT 1")
  suspend fun getRuleVisit(ruleId: String): RuleVisitEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun upsertRuleVisit(visit: RuleVisitEntity)

  @Query("DELETE FROM rule_visit")
  suspend fun clearRuleVisits()

  @Query("SELECT * FROM map_node_position")
  suspend fun getMapNodePositions(): List<MapNodePositionEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun upsertMapNodePositions(positions: List<MapNodePositionEntity>)

  @Query("DELETE FROM map_node_position")
  suspend fun clearMapNodePositions()
}
