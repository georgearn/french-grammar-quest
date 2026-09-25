package com.example.data.model

enum class QuestionType {
  MULTIPLE_CHOICE,
  GENDER_PICKER,
  MOOD_PICKER,
  AGREEMENT,
  FILL_BLANK
}

data class GrammarExample(
  val fr: String,
  val en: String,
  val highlight: String? = null
)

data class GrammarTableRow(
  val col1: String,
  val col2: String,
  val col3: String? = null,
  val col4: String? = null
)

data class QuizQuestion(
  val id: String,
  val lessonId: String,
  val promptFr: String,
  val promptEn: String,
  val options: List<String>,
  val correctIndex: Int,
  val explanation: String,
  val type: QuestionType = QuestionType.MULTIPLE_CHOICE
)

data class GrammarLesson(
  val id: String,
  val categoryId: String,
  val level: String, // "A1", "A2", "B1", "B2", "C1", "C2"
  val frTitle: String,
  val enSub: String,
  val desc: String,
  val ruleFr: String,
  val ruleEn: String,
  val examples: List<GrammarExample>,
  val note: String? = null,
  val tableTitle: String? = null,
  val tableHeaders: List<String> = emptyList(),
  val tableRows: List<GrammarTableRow> = emptyList(),
  val seeAlsoIds: List<String> = emptyList(),
  val questions: List<QuizQuestion> = emptyList()
)

data class GrammarCategory(
  val id: String,
  val number: Int,
  val label: String,
  val frLabel: String,
  val desc: String,
  val levelRange: String,
  val iconName: String,
  val colorHex: Long,
  val lessons: List<GrammarLesson>
)

data class UserLevelInfo(
  val level: Int,
  val titleFr: String,
  val titleEn: String,
  val minXp: Int,
  val maxXp: Int,
  val badgeIcon: String
)
