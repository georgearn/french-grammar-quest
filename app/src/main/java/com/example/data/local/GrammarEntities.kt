package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Training progress for a StructuredRule (data/engine/GrammarRuleRepository), tracked from the Training path. */
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

/** A generated text/podcast script the user chose to keep (Create tab, "Enregistrer"). */
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

/** A rule the learner has opened or added to the grammar map; its presence means "on the map". */
@Entity(tableName = "rule_visit")
data class RuleVisitEntity(
  @PrimaryKey val ruleId: String,
  val firstSeen: Long = System.currentTimeMillis(),
  val lastSeen: Long = System.currentTimeMillis()
)

/** Last layout position of a node on the grammar map, so the map keeps its shape between visits. */
@Entity(tableName = "map_node_position")
data class MapNodePositionEntity(
  @PrimaryKey val ruleId: String,
  val x: Float,
  val y: Float
)
