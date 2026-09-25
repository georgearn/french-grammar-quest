package com.example.data.engine

/**
 * Cross-references between rules (the "see also" links of the grammar map), undirected.
 *
 * Imported from the Toile de Grammaire prototype's lesson data, keeping only links whose both
 * ends exist as a [StructuredRule]. Same-category and contrast-pair links are derived at runtime
 * and are not listed here.
 */
object RuleGraph {

  private val LINKS: List<Pair<String, String>> = listOf(
    "accord-participe-passe" to "passe-compose",
    "accord-sujet-verbe" to "chiffres-proportion",
    "accord-sujet-verbe" to "pronom-on",
    "but" to "cause",
    "cause" to "connecteurs-argumentation",
    "cause" to "consequence",
    "chiffres-proportion" to "nombres-heure-date",
    "conditionnel" to "exprimer-volonte-souhait",
    "connecteurs-argumentation" to "consequence",
    "connecteurs-argumentation" to "opposition-concession",
    "discours-rapporte" to "indicateurs-temps",
    "discours-rapporte" to "plus-que-parfait",
    "exprimer-volonte-souhait" to "necessite-obligation",
    "futur-anterieur" to "plus-que-parfait",
    "futur-proche-passe-recent" to "futur-simple",
    "futur-proche-passe-recent" to "imparfait",
    "genre-noms" to "genre-professions",
    "imparfait" to "passe-compose",
    "imperatif" to "pronoms-complements",
    "indicateurs-temps" to "nombres-heure-date",
    "pronom-on" to "pronoms-toniques",
    "pronoms-complements" to "pronoms-toniques"
  )

  private val neighbours: Map<String, Set<String>> = buildMap<String, MutableSet<String>> {
    LINKS.forEach { (a, b) ->
      getOrPut(a) { mutableSetOf() }.add(b)
      getOrPut(b) { mutableSetOf() }.add(a)
    }
  }

  /** Rules cross-referenced by [ruleId], in either direction. */
  fun crossRefs(ruleId: String): Set<String> = neighbours[ruleId].orEmpty()

  /** Every cross-reference as an unordered pair. */
  val links: List<Pair<String, String>> get() = LINKS

  /**
   * Groups the rule catalogue's category ids, which use a few spellings for the same family
   * ("cat-pronom"/"cat-pronoms", "cat-verbe"/"cat-verbes"/"cat-mode").
   */
  fun family(categoryId: String): String = when (categoryId) {
    "cat-pronom" -> "cat-pronoms"
    "cat-verbe", "cat-mode" -> "cat-verbes"
    else -> categoryId
  }
}
