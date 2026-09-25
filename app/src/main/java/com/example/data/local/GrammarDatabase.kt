package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
  entities = [
    UserStatsEntity::class,
    LessonProgressEntity::class,
    MistakeEntity::class,
    AchievementEntity::class,
    RuleTrainingProgressEntity::class,
    SavedGenerationEntity::class
  ],
  version = 3,
  exportSchema = false
)
abstract class GrammarDatabase : RoomDatabase() {
  abstract fun grammarDao(): GrammarDao

  companion object {
    @Volatile
    private var INSTANCE: GrammarDatabase? = null

    fun getDatabase(context: Context): GrammarDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          GrammarDatabase::class.java,
          "french_grammar_quest.db"
        ).fallbackToDestructiveMigration().build()
        INSTANCE = instance
        instance
      }
    }
  }
}
