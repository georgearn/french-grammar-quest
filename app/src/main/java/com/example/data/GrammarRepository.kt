package com.example.data

import com.example.data.local.GrammarDao
import com.example.data.local.MapNodePositionEntity
import com.example.data.local.RuleTrainingProgressEntity
import com.example.data.local.RuleVisitEntity
import com.example.data.local.SavedGenerationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class GrammarRepository(private val dao: GrammarDao) {

  val ruleTrainingProgress: Flow<List<RuleTrainingProgressEntity>> = dao.getAllRuleTrainingProgress()
  val savedGenerations: Flow<List<SavedGenerationEntity>> = dao.getAllSavedGenerations()
  val ruleVisits: Flow<List<RuleVisitEntity>> = dao.getAllRuleVisits()

  /** Puts [ruleId] on the grammar map (or refreshes its last-seen time). */
  suspend fun recordVisit(ruleId: String) {
    val now = System.currentTimeMillis()
    val existing = dao.getRuleVisit(ruleId)
    dao.upsertRuleVisit(existing?.copy(lastSeen = now) ?: RuleVisitEntity(ruleId, now, now))
  }

  /** Rules trained before the map existed count as explored. */
  suspend fun seedVisitsFromTraining() {
    dao.getAllRuleTrainingProgress().first()
      .filter { it.sessionsCompleted > 0 }
      .forEach { progress ->
        if (dao.getRuleVisit(progress.ruleId) == null) {
          dao.upsertRuleVisit(RuleVisitEntity(progress.ruleId, progress.lastTrainedTimestamp, progress.lastTrainedTimestamp))
        }
      }
  }

  suspend fun clearMap() {
    dao.clearRuleVisits()
    dao.clearMapNodePositions()
  }

  suspend fun mapPositions(): Map<String, Pair<Float, Float>> =
    dao.getMapNodePositions().associate { it.ruleId to (it.x to it.y) }

  suspend fun saveMapPositions(positions: Map<String, Pair<Float, Float>>) {
    dao.upsertMapNodePositions(positions.map { (id, p) -> MapNodePositionEntity(id, p.first, p.second) })
  }

  /** Records the result of a Training-path drill session against a StructuredRule. */
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
