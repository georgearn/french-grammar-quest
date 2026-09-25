package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
  entities = [
    RuleTrainingProgressEntity::class,
    SavedGenerationEntity::class
  ],
  version = 4,
  exportSchema = false
)
abstract class GrammarDatabase : RoomDatabase() {
  abstract fun grammarDao(): GrammarDao

  companion object {
    @Volatile
    private var INSTANCE: GrammarDatabase? = null

    /**
     * v4 drops the tables of the removed quest-map/quiz/speed-drill flow. An explicit migration
     * (rather than the destructive fallback) keeps the user's training progress and saved generations.
     */
    private val MIGRATION_3_4 = object : Migration(3, 4) {
      override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS user_stats")
        db.execSQL("DROP TABLE IF EXISTS lesson_progress")
        db.execSQL("DROP TABLE IF EXISTS mistakes")
        db.execSQL("DROP TABLE IF EXISTS achievements")
      }
    }

    fun getDatabase(context: Context): GrammarDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          GrammarDatabase::class.java,
          "french_grammar_quest.db"
        )
          .addMigrations(MIGRATION_3_4)
          .fallbackToDestructiveMigration()
          .build()
        INSTANCE = instance
        instance
      }
    }
  }
}
