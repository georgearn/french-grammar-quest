package com.example.data.engine

/**
 * The five structural pillars a rule belongs to (Accord, Verb System, Pronoun
 * Hierarchy, Prepositional Regimes, Register & Negation). Purely additive
 * metadata used to group/filter rules by underlying mechanism rather than by
 * CEFR level alone. Defaults to OTHER so existing rule data keeps compiling
 * without being re-tagged immediately.
 */
enum class GrammarPillar {
  AGREEMENT_ENGINE,        // Accord: genre/nombre cascade (déterminant, nom, adjectif, participe passé)
  VERB_SYSTEM,             // Modes, temps, radicaux réguliers/irréguliers
  PRONOUN_HIERARCHY,       // Ordre strict des pronoms clitiques
  PREPOSITIONAL_REGIME,    // Verbe + préposition fixe avant infinitif/objet
  REGISTER_NEGATION,       // Formel/informel, chute du "ne", inversion vs est-ce que
  OTHER
}

/**
 * Token semantic categories for visual structural diagrams.
 */
enum class TokenCategory {
  SUBJECT,      // e.g. "Sujet (Il, Nous, etc.)"
  TRIGGER,      // e.g. "Verbe de volonté / émotion (vouloir, falloir)"
  CONNECTOR,    // e.g. "que / qui / dont"
  TARGET_VERB,  // e.g. "Verbe au subjonctif / participe passé"
  OBJECT,       // e.g. "COD / COI (le, la, lui, en, y)"
  MODIFIER      // e.g. "Adverbe / négation"
}

data class FormulaToken(
  val text: String,
  val label: String,
  val category: TokenCategory,
  val explanation: String = "",
  /** Same explanation in English, shown when the on-demand EN toggle is active. Optional. */
  val explanationEn: String? = null
)

data class DecisionStep(
  val stepNumber: Int,
  val question: String,
  val ifYes: String,
  val ifNo: String
)

data class VisualStructure(
  val formulaTokens: List<FormulaToken>,
  val diagramTitle: String,
  val diagramDescription: String,
  val decisionSteps: List<DecisionStep> = emptyList(),
  val comparisonTableTitle: String? = null,
  val comparisonHeaders: List<String> = emptyList(),
  val comparisonRows: List<List<String>> = emptyList(),
  /** English version of diagramDescription, shown when the EN toggle is active. Optional. */
  val diagramDescriptionEn: String? = null
)

enum class RegisterType {
  COURANT,        // Langue standard / quotidienne
  SOUTENU,        // Style littéraire / académique / formel
  FAMILIER,       // Oral quotidien / informel
  PROFESSIONNEL   // Contexte d'entreprise / administratif
}

data class RegisterExample(
  val register: RegisterType,
  val frenchSentence: String,
  val highlightedSegment: String,
  val englishSentence: String,
  val contextNote: String
)

data class InlineAnnotation(
  val id: String,
  val targetPhrase: String,
  val explanationFr: String,
  val explanationEn: String,
  val whyItApplies: String,
  val commonPitfall: String? = null
)

data class ContextualPassage(
  val title: String,
  val fullTextFr: String,
  val fullTextEn: String,
  val annotations: List<InlineAnnotation>
)

data class ProductionDrillItem(
  val id: String,
  val instructionFr: String,
  val instructionEn: String,
  val basePrompt: String,         // e.g. "Il faut que nous _____ (partir) avant la nuit."
  val targetAnswer: String,       // e.g. "partions"
  val acceptedAnswers: List<String> = emptyList(),
  val hint: String,
  val explanation: String
)

data class SpotErrorDrillItem(
  val id: String,
  val instructionFr: String,
  val passage: String,
  val tokens: List<String>,       // Passage split into clickable tokens
  val errorTokenIndex: Int,       // Which index in tokens contains the error
  val errorWord: String,
  val correction: String,
  val ruleExplanation: String
)

data class StructuredRule(
  val id: String,
  val categoryId: String,
  val categoryName: String,
  val level: String, // "A1", "A2", "B1", "B2", "C1", "C2"
  val titleFr: String,
  val titleEn: String,
  val summaryFr: String,
  val summaryEn: String,
  val visualStructure: VisualStructure,
  val registerExamples: List<RegisterExample>,
  val contextualPassage: ContextualPassage,
  val productionDrills: List<ProductionDrillItem>,
  val spotErrorDrills: List<SpotErrorDrillItem>,
  // --- Additive pedagogical metadata (all optional, default to "not tagged yet") ---
  /** Which of the five structural pillars this rule mainly belongs to. */
  val pillar: GrammarPillar = GrammarPillar.OTHER,
  /**
   * A longer, plain-English walkthrough of the rule for the on-demand "Explain in English"
   * trigger. Falls back to [summaryEn] in the UI when left blank.
   */
  val ruleExplanationEn: String = "",
  /**
   * Trigger-Action Pairing: a concrete phrase/token that automatically calls this rule
   * (e.g. "Il faut que" -> subjonctif), rather than an abstract description.
   */
  val triggerToken: String? = null,
  val triggerActionFr: String? = null,
  val triggerActionEn: String? = null,
  /**
   * Mental Model Triads: rules sharing the same [contrastGroupId] are meant to be learned
   * as a contrastive pair/triad (e.g. passé composé vs. imparfait) rather than in isolation.
   * [contrastRoleLabelFr]/[contrastRoleLabelEn] name this rule's role within that group
   * (e.g. "Ce qui s'est passé" / "What happened").
   */
  val contrastGroupId: String? = null,
  val contrastRoleLabelFr: String? = null,
  val contrastRoleLabelEn: String? = null
)
