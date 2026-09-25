package com.example.data.grammar

import com.example.data.model.GrammarCategory
import com.example.data.model.GrammarExample
import com.example.data.model.GrammarLesson
import com.example.data.model.GrammarTableRow
import com.example.data.model.QuestionType
import com.example.data.model.QuizQuestion
import com.example.data.model.UserLevelInfo

object GrammarData {

  val LEVEL_TITLES = listOf(
    UserLevelInfo(1, "Initié", "Initiate", 0, 100, "🌱"),
    UserLevelInfo(2, "Apprenti", "Apprentice", 100, 250, "📖"),
    UserLevelInfo(3, "Explorateur", "Explorer", 250, 450, "🧭"),
    UserLevelInfo(4, "Voyageur", "Traveler", 450, 700, "🎒"),
    UserLevelInfo(5, "Lettré", "Scholar", 700, 1000, "📜"),
    UserLevelInfo(6, "Érudit", "Learned", 1000, 1400, "✒️"),
    UserLevelInfo(7, "Connaisseur", "Connoisseur", 1400, 1900, "🍷"),
    UserLevelInfo(8, "Grammairien", "Grammarian", 1900, 2500, "🛡️"),
    UserLevelInfo(9, "Virtuose", "Virtuoso", 2500, 3200, "⚡"),
    UserLevelInfo(10, "Maître de la Langue", "Master of the Tongue", 3200, 4000, "👑"),
    UserLevelInfo(11, "Académicien", "Immortal (Académie)", 4000, 99999, "🏆")
  )

  fun getLevelInfo(xp: Int): UserLevelInfo {
    return LEVEL_TITLES.lastOrNull { xp >= it.minXp } ?: LEVEL_TITLES.first()
  }

  fun isAudioAvailableForLevel(level: String): Boolean {
    val clean = level.uppercase().trim()
    return clean in listOf("A2", "B1", "B2", "C1", "C2")
  }

  val CATEGORIES: List<GrammarCategory> = listOf(
    // 1. LE GROUPE NOMINAL
    GrammarCategory(
      id = "cat-nom",
      number = 1,
      label = "1. Le groupe nominal",
      frLabel = "Le groupe nominal",
      desc = "Genre, articles, adjectifs, possessifs, démonstratifs — les fondations du français.",
      levelRange = "A1–B2",
      iconName = "shield",
      colorHex = 0xFF2563EB,
      lessons = listOf(
        GrammarLesson(
          id = "genre-noms",
          categoryId = "cat-nom",
          level = "A1",
          frTitle = "Le genre des noms",
          enSub = "Gender of nouns & common endings",
          desc = "Chaque nom est soit masculin soit féminin. Les terminaisons sont des indices fiables dans la majorité des cas.",
          ruleFr = "En français, chaque nom a un genre : masculin ou féminin. Il n'y a pas de neutre. Les terminaisons comme -age, -isme, -ment sont typiquement masculines, tandis que -tion, -ade, -ette, -ure sont féminines.",
          ruleEn = "Every French noun is masculine or feminine — there is no neuter. While endings like -age, -isme, -ment signal masculine, endings like -tion, -ade, -ette, -ure are feminine.",
          examples = listOf(
            GrammarExample("Le voyage, le tourisme, le monument", "Masculine nouns ending in -age, -isme, -ment"),
            GrammarExample("La station, la salade, la bicyclette, la voiture", "Feminine nouns ending in -tion, -ade, -ette, -ure"),
            GrammarExample("Le problème, le musée, le silence", "Masculine despite ending in -e (famous traps!)")
          ),
          note = "Piège classique : la plage, la page, la cage et l'image sont féminines malgré la terminaison -age !",
          tableTitle = "Terminaisons fréquentes",
          tableHeaders = listOf("Masculin", "Exemple", "Féminin", "Exemple"),
          tableRows = listOf(
            GrammarTableRow("-age", "le voyage", "-ade", "la salade"),
            GrammarTableRow("-isme", "le tourisme", "-tion / -sion", "la nation"),
            GrammarTableRow("-ment", "le logement", "-ette", "la baguette"),
            GrammarTableRow("-eau", "le bureau", "-ure", "la voiture"),
            GrammarTableRow("-oir", "le miroir", "-té / -tié", "la beauté")
          ),
          questions = listOf(
            QuizQuestion(
              id = "gn_1",
              lessonId = "genre-noms",
              promptFr = "Quel est le genre du mot « problème » ?",
              promptEn = "What is the gender of the word 'problème'?",
              options = listOf("Masculin (le problème)", "Féminin (la problème)"),
              correctIndex = 0,
              explanation = "« Problème » vient du grec et fait partie des noms masculins en -e très courants (le problème, le système, le programme).",
              type = QuestionType.GENDER_PICKER
            ),
            QuizQuestion(
              id = "gn_2",
              lessonId = "genre-noms",
              promptFr = "Quel mot est une exception féminine à la règle des mots en « -age » ?",
              promptEn = "Which word is a feminine exception to the -age rule?",
              options = listOf("Le fromage", "La plage", "Le voyage", "Le garage"),
              correctIndex = 1,
              explanation = "La plage, la page, la cage et l'image sont féminines malgré leur terminaison en -age.",
              type = QuestionType.MULTIPLE_CHOICE
            ),
            QuizQuestion(
              id = "gn_3",
              lessonId = "genre-noms",
              promptFr = "Le mot « solution » est...",
              promptEn = "The word 'solution' is...",
              options = listOf("Féminin (la solution)", "Masculin (le solution)"),
              correctIndex = 0,
              explanation = "La terminaison -tion/-sion est l'une des plus fiables pour le féminin (la solution, la nation, la télévision).",
              type = QuestionType.GENDER_PICKER
            ),
            QuizQuestion(
              id = "gn_4",
              lessonId = "genre-noms",
              promptFr = "Choisissez l'article correct : « ___ musée du Louvre ».",
              promptEn = "Choose the correct article: '___ Louvre museum'.",
              options = listOf("Le", "La"),
              correctIndex = 0,
              explanation = "« Musée » est masculin (le musée, le lycée, le trophée) bien qu'il finisse par -ée.",
              type = QuestionType.FILL_BLANK
            ),
            QuizQuestion(
              id = "gn_5",
              lessonId = "genre-noms",
              promptFr = "Quel est le genre du mot « eau » ?",
              promptEn = "What is the gender of the word 'eau' (water)?",
              options = listOf("Féminin (l'eau / une eau)", "Masculin (un eau)"),
              correctIndex = 0,
              explanation = "Bien que les mots en -eau soient généralement masculins (le bureau, le chapeau), « l'eau » et « la peau » sont féminins !",
              type = QuestionType.GENDER_PICKER
            )
          )
        ),
        GrammarLesson(
          id = "articles",
          categoryId = "cat-nom",
          level = "A1/A2",
          frTitle = "Les articles et la négation",
          enSub = "Definite, indefinite, partitive & the negation rule",
          desc = "À la forme négative, du / de la / des / un / une deviennent « de » (d'). Exception après être.",
          ruleFr = "Devant un nom indéfini ou partitif à la forme négative, on remplace l'article par « de » (ou « d' » devant voyelle) : Je ne mange pas DE viande. Attention : après le verbe « être », l'article reste inchangé.",
          ruleEn = "In negative sentences, un/une/des/du/de la become 'de' (d' before vowel): Je ne bois pas de lait. Exception: after 'être', the article is preserved.",
          examples = listOf(
            GrammarExample("J'ai un vélo. → Je n'ai pas de vélo.", "Un becomes de in negative"),
            GrammarExample("Il mange de la viande. → Il ne mange pas de viande.", "Partitive becomes de in negative"),
            GrammarExample("C'est une erreur. → Ce n'est pas une erreur.", "After 'être', the article does NOT change!")
          ),
          note = "Contracted articles : à + le = au, à + les = aux ; de + le = du, de + les = des.",
          tableTitle = "Articles contractés",
          tableHeaders = listOf("Préposition", "+ le", "+ les", "+ la / l'"),
          tableRows = listOf(
            GrammarTableRow("à", "au", "aux", "à la / à l'"),
            GrammarTableRow("de", "du", "des", "de la / de l'")
          ),
          questions = listOf(
            QuizQuestion(
              id = "art_1",
              lessonId = "articles",
              promptFr = "Complétez : « Je n'ai pas ___ argent sur moi. »",
              promptEn = "Complete: 'I have no money on me.'",
              options = listOf("d'", "de l'", "du", "un"),
              correctIndex = 0,
              explanation = "À la négation, les partitifs (de l', du, etc.) deviennent « de » (ou « d' » devant voyelle).",
              type = QuestionType.FILL_BLANK
            ),
            QuizQuestion(
              id = "art_2",
              lessonId = "articles",
              promptFr = "Complétez : « Ce n'est pas ___ problème facile. »",
              promptEn = "Complete: 'This is not an easy problem.'",
              options = listOf("un", "de", "d'", "du"),
              correctIndex = 0,
              explanation = "Exception cruciale : après le verbe « être », l'article ne devient pas « de » !",
              type = QuestionType.FILL_BLANK
            ),
            QuizQuestion(
              id = "art_3",
              lessonId = "articles",
              promptFr = "« Je vais ___ cinéma avec des amis. »",
              promptEn = "Complete: 'I am going to the cinema with friends.'",
              options = listOf("au", "à le", "du", "en"),
              correctIndex = 0,
              explanation = "« à + le » se contracte obligatoirement en « au ».",
              type = QuestionType.FILL_BLANK
            ),
            QuizQuestion(
              id = "art_4",
              lessonId = "articles",
              promptFr = "« Elle est médecin. » Pourquoi n'y a-t-il pas d'article ?",
              promptEn = "Why is there no article in 'Elle est médecin'?",
              options = listOf(
                "Après « être », on omet l'article devant une profession non qualifiée",
                "C'est une faute, il faut dire « Elle est un médecin »",
                "Parce que médecin est un nom féminin"
              ),
              correctIndex = 0,
              explanation = "Après être, on ne met pas d'article devant une profession : « Il est avocat » vs « C'est un bon avocat ».",
              type = QuestionType.MULTIPLE_CHOICE
            ),
            QuizQuestion(
              id = "art_5",
              lessonId = "articles",
              promptFr = "« Nous venons ___ États-Unis. »",
              promptEn = "Complete: 'We come from the United States.'",
              options = listOf("des", "de les", "du", "aux"),
              correctIndex = 0,
              explanation = "« de + les » se contracte en « des ».",
              type = QuestionType.FILL_BLANK
            )
          )
        ),
        GrammarLesson(
          id = "cest-il-est",
          categoryId = "cat-nom",
          level = "A2",
          frTitle = "« C'est » et « il/elle est »",
          enSub = "Two ways to identify or describe someone/something",
          desc = "C'est + nom (avec déterminant) pour identifier. Il/elle est + adjectif ou profession sans article.",
          ruleFr = "Utilisez « C'est » + nom déterminé (C'est un ingénieur, C'est ma sœur) ou pour commenter une idée générale (C'est beau, C'est difficile). Utilisez « Il/Elle est » + adjectif (Elle est intelligente) ou nom de métier sans article (Il est architecte).",
          ruleEn = "Use 'C'est' + noun with determiner or for general commentary. Use 'Il/Elle est' + adjective or bare profession.",
          examples = listOf(
            GrammarExample("C'est un professeur réputé.", "Noun with article → C'est"),
            GrammarExample("Il est professeur.", "Bare profession → Il est"),
            GrammarExample("Apprendre le français, c'est amusant !", "General commentary → C'est + adj neutre")
          ),
          note = "Au pluriel devant un nom : « Ce sont mes amis » (ou « C'est » à l'oral familier).",
          questions = listOf(
            QuizQuestion(
              id = "cie_1",
              lessonId = "cest-il-est",
              promptFr = "« Regarde cette femme : ___ directrice de l'école. »",
              promptEn = "Look at this woman: she is the school principal.",
              options = listOf("c'est la", "elle est la", "elle est", "c'est"),
              correctIndex = 0,
              explanation = "Nom déterminé par un article (« la directrice ») → on utilise « c'est la ».",
              type = QuestionType.FILL_BLANK
            ),
            QuizQuestion(
              id = "cie_2",
              lessonId = "cest-il-est",
              promptFr = "« Thomas ? Oui, ___ avocat depuis trois ans. »",
              promptEn = "Thomas? Yes, he is a lawyer for 3 years.",
              options = listOf("il est", "c'est", "c'est un", "il est un"),
              correctIndex = 0,
              explanation = "Profession seule sans déterminant → « il est avocat ».",
              type = QuestionType.FILL_BLANK
            ),
            QuizQuestion(
              id = "cie_3",
              lessonId = "cest-il-est",
              promptFr = "« Voyager en train, ___ très reposant. »",
              promptEn = "Traveling by train, it is very relaxing.",
              options = listOf("c'est", "il est", "elle est", "ce sont"),
              correctIndex = 0,
              explanation = "Pour commenter une situation ou idée générale avec un adjectif, on emploie « c'est ».",
              type = QuestionType.FILL_BLANK
            )
          )
        ),
        GrammarLesson(
          id = "adjectifs-qualificatifs",
          categoryId = "cat-nom",
          level = "A2",
          frTitle = "Les adjectifs qualificatifs",
          enSub = "Agreement & position (BANGS rules)",
          desc = "La plupart des adjectifs se placent après le nom. Les adjectifs courts et fréquents se placent avant.",
          ruleFr = "La majorité des adjectifs se placent après le nom (couleur, forme, nationalité). Un groupe d'adjectifs courts se place avant le nom : Beauty, Age, Number, Goodness, Size (beau, jeune, vieux, bon, mauvais, grand, petit).",
          ruleEn = "Most adjectives go AFTER the noun. Common short adjectives go BEFORE (BANGS: Beauty, Age, Number, Goodness, Size).",
          examples = listOf(
            GrammarExample("Une table ronde, un ami sénégalais", "Shape and nationality go after"),
            GrammarExample("Une belle journée, un vieux livre", "Beauty & Age go before"),
            GrammarExample("Un bel appartement, un vieil homme", "Beau/vieux become bel/vieil before a vowel sound")
          ),
          note = "Certains adjectifs changent de sens selon leur place : un homme pauvre (sans argent) vs mon pauvre ami (à plaindre) !",
          questions = listOf(
            QuizQuestion(
              id = "adj_1",
              lessonId = "adjectifs-qualificatifs",
              promptFr = "Devant un nom masculin singulier commençant par une voyelle, « beau » devient...",
              promptEn = "Before a masculine noun starting with a vowel, 'beau' becomes...",
              options = listOf("bel (un bel appartement)", "beau (un beau appartement)", "belle (un belle appartement)"),
              correctIndex = 0,
              explanation = "Beau, nouveau, vieux deviennent bel, nouvel, vieil devant une voyelle ou un h muet.",
              type = QuestionType.MULTIPLE_CHOICE
            ),
            QuizQuestion(
              id = "adj_2",
              lessonId = "adjectifs-qualificatifs",
              promptFr = "Quelle phrase a l'ordre correct des mots ?",
              promptEn = "Which sentence has the correct word order?",
              options = listOf(
                "C'est une excellente nouvelle.",
                "C'est une nouvelle excellente.",
                "C'est un vin rouge bon."
              ),
              correctIndex = 0,
              explanation = "« Bon », « mauvais », « excellent » se placent généralement avant le nom.",
              type = QuestionType.MULTIPLE_CHOICE
            )
          )
        )
      )
    ),

    // 2. LES PRONOMS
    GrammarCategory(
      id = "cat-pronoms",
      number = 2,
      label = "2. Les pronoms",
      frLabel = "Les pronoms",
      desc = "Pronoms compléments (COD/COI), Y et En, relatifs (qui, que, dont, où, lequel).",
      levelRange = "A2–B2",
      iconName = "chat",
      colorHex = 0xFF0284C7,
      lessons = listOf(
        GrammarLesson(
          id = "pronoms-complements",
          categoryId = "cat-pronoms",
          level = "B1",
          frTitle = "Pronoms compléments, Y et En",
          enSub = "Direct / indirect objects, and Y / En",
          desc = "« Y » remplace à + nom ou un lieu. « En » remplace de + nom ou une quantité.",
          ruleFr = "Y remplace un lieu où l'on est / va, ou « à + chose » : Tu vas à Paris ? — J'y vais. En remplace « de + nom » ou une quantité : Tu as du café ? — J'en ai. Les pronoms se placent avant le verbe conjugué.",
          ruleEn = "Y replaces locations (to/in) or à + thing. En replaces de + noun or quantities. Placed before the conjugated verb.",
          examples = listOf(
            GrammarExample("Tu penses à ton avenir ? — Oui, j'y pense.", "Penser à + chose → Y"),
            GrammarExample("Tu as des frères ? — Oui, j'en ai deux.", "Quantifier → En"),
            GrammarExample("Je lui donne le livre. Je le lui donne.", "COI (lui) vs COD (le)")
          ),
          note = "À l'impératif affirmatif, les pronoms suivent le verbe : Donne-m'en ! Vas-y !",
          questions = listOf(
            QuizQuestion(
              id = "pc_1",
              lessonId = "pronoms-complements",
              promptFr = "« Tu vas à la gare ? — Oui, j'___ vais tout de suite. »",
              promptEn = "Are you going to the station? - Yes, I'm going there right away.",
              options = listOf("y", "en", "la", "le"),
              correctIndex = 0,
              explanation = "« Y » remplace un lieu introduit par « à » (à la gare → y).",
              type = QuestionType.FILL_BLANK
            ),
            QuizQuestion(
              id = "pc_2",
              lessonId = "pronoms-complements",
              promptFr = "« Tu veux du fromage ? — Non merci, je n'___ veux pas. »",
              promptEn = "Do you want cheese? - No thanks, I don't want any.",
              options = listOf("en", "y", "le", "de lui"),
              correctIndex = 0,
              explanation = "« En » remplace un nom précédé d'un partitif (du fromage → en).",
              type = QuestionType.FILL_BLANK
            ),
            QuizQuestion(
              id = "pc_3",
              lessonId = "pronoms-complements",
              promptFr = "« J'ai téléphoné à Marie. → Je ___ ai téléphoné. »",
              promptEn = "I called Marie. -> I called her.",
              options = listOf("lui", "la", "l'", "y"),
              correctIndex = 0,
              explanation = "Téléphoner à quelqu'un est un COI (indirect), donc on emploie « lui » pour les personnes.",
              type = QuestionType.MULTIPLE_CHOICE
            ),
            QuizQuestion(
              id = "pc_4",
              lessonId = "pronoms-complements",
              promptFr = "Où place-t-on le pronom dans une phrase négative au présent ?",
              promptEn = "Where do you place the pronoun in a present negative sentence?",
              options = listOf(
                "Entre « ne » et le verbe : « Je ne le vois pas »",
                "Après le verbe : « Je ne vois le pas »",
                "Après « pas » : « Je ne vois pas le »"
              ),
              correctIndex = 0,
              explanation = "Le pronom objet s'accroche directement devant le verbe conjugué : Je ne le vois pas.",
              type = QuestionType.MULTIPLE_CHOICE
            )
          )
        ),
        GrammarLesson(
          id = "relatifs-composes",
          categoryId = "cat-pronoms",
          level = "B1/B2",
          frTitle = "Pronoms relatifs : dont, où, lequel",
          enSub = "Compound & tricky relative pronouns",
          desc = "« Dont » remplace de + nom (parler de, avoir besoin de, dont les...).",
          ruleFr = "On emploie « dont » dès que le verbe ou l'expression demande la préposition « de » : C'est le livre dont je t'ai parlé (parler de). « Dont » traduit aussi « whose » : L'auteur dont j'ai lu les romans.",
          ruleEn = "Use 'dont' whenever the verb takes 'de' (avoir besoin de, parler de) or to express 'whose'.",
          examples = listOf(
            GrammarExample("Voici l'outil dont j'ai besoin.", "Avoir besoin de → dont"),
            GrammarExample("Le projet auquel je pense.", "Penser à + chose → auquel"),
            GrammarExample("Ce dont j'ai envie, c'est du repos.", "No specific noun antecedent → ce dont")
          ),
          note = "Ce qui / ce que / ce dont s'utilisent quand il n'y a pas d'antécédent précis (What / that which).",
          questions = listOf(
            QuizQuestion(
              id = "rc_1",
              lessonId = "relatifs-composes",
              promptFr = "« C'est le professeur ___ je t'ai parlé hier. »",
              promptEn = "That is the teacher I told you about yesterday.",
              options = listOf("dont", "que", "qui", "auquel"),
              correctIndex = 0,
              explanation = "On dit « parler de quelqu'un » : la préposition « de » exige le pronom relatif « dont ».",
              type = QuestionType.FILL_BLANK
            ),
            QuizQuestion(
              id = "rc_2",
              lessonId = "relatifs-composes",
              promptFr = "« Je ne comprends pas ___ tu veux dire. »",
              promptEn = "I don't understand what you mean.",
              options = listOf("ce que", "ce qui", "ce dont", "que"),
              correctIndex = 0,
              explanation = "« Vouloir dire » a pour objet direct « ce que » (COD sans antécédent).",
              type = QuestionType.FILL_BLANK
            ),
            QuizQuestion(
              id = "rc_3",
              lessonId = "relatifs-composes",
              promptFr = "« La réunion ___ j'ai assisté était passionnante. »",
              promptEn = "The meeting I attended was fascinating.",
              options = listOf("à laquelle", "dont", "que", "où"),
              correctIndex = 0,
              explanation = "Assister à quelque chose : après préposition « à », pour une chose féminine, on emploie « à laquelle ».",
              type = QuestionType.FILL_BLANK
            )
          )
        )
      )
    ),

    // 3. ADVERBES & NÉGATION
    GrammarCategory(
      id = "cat-adv",
      number = 3,
      label = "3. Adverbes & Négation",
      frLabel = "Adverbes, comparaison & négation",
      desc = "Comparatifs, superlatifs, formation en -ment, ne... que, ni... ni, personne, rien.",
      levelRange = "A1–B1",
      iconName = "lightning",
      colorHex = 0xFFD97706,
      lessons = listOf(
        GrammarLesson(
          id = "comparaison",
          categoryId = "cat-adv",
          level = "A2/B1",
          frTitle = "La comparaison & les irréguliers",
          enSub = "Comparatives, superlatives, meilleur vs mieux",
          desc = "Plus... que, aussi... que, moins... que. Attention à bon → meilleur et bien → mieux.",
          ruleFr = "Adjectif : plus / aussi / moins + adjectif + que. Nom : plus de / autant de / moins de + nom + que. Verbe : verbe + plus / autant / moins + que. Attention : le comparatif de l'adjectif « bon » est « meilleur », celui de l'adverbe « bien » est « mieux ».",
          ruleEn = "Comparatives use plus/aussi/moins... que. Bon (adjective) becomes meilleur; bien (adverb) becomes mieux.",
          examples = listOf(
            GrammarExample("Ce gâteau est meilleur que l'autre.", "Bon → meilleur (adjective)"),
            GrammarExample("Elle chante mieux que moi.", "Bien → mieux (adverb)"),
            GrammarExample("Il a plus de temps que nous.", "Noun comparison → plus de")
          ),
          note = "Ne confondez jamais « meilleur » (qualifie un nom) et « mieux » (modifie un verbe) !",
          questions = listOf(
            QuizQuestion(
              id = "cmp_1",
              lessonId = "comparaison",
              promptFr = "« Paul conduit ___ que son frère. » (bien)",
              promptEn = "Paul drives better than his brother.",
              options = listOf("mieux", "meilleur", "plus bien", "plus meilleur"),
              correctIndex = 0,
              explanation = "« Conduire » est un verbe : on modifie un verbe avec l'adverbe « mieux », pas « meilleur ».",
              type = QuestionType.FILL_BLANK
            ),
            QuizQuestion(
              id = "cmp_2",
              lessonId = "comparaison",
              promptFr = "« Cette boulangerie a de ___ croissants. » (bon)",
              promptEn = "This bakery has better croissants.",
              options = listOf("meilleurs", "mieux", "plus bons", "meilleur"),
              correctIndex = 0,
              explanation = "« Croissants » est un nom masculin pluriel : on emploie l'adjectif « meilleurs ».",
              type = QuestionType.FILL_BLANK
            )
          )
        ),
        GrammarLesson(
          id = "negation",
          categoryId = "cat-adv",
          level = "A1/A2",
          frTitle = "Les nuances de la négation",
          enSub = "Ne... pas, plus, jamais, rien, personne, ne... que",
          desc = "Ne... que exprime une restriction (= seulement). Rien et personne peuvent être sujets.",
          ruleFr = "Négations composées : ne... jamais (never), ne... plus (no longer), ne... rien (nothing), ne... personne (nobody). Au passé composé, « pas, plus, jamais, rien » encadrent l'auxiliaire, mais « personne » se place après le participe passé (Je n'ai vu personne).",
          ruleEn = "In compound tenses, rien/jamais/plus surround the auxiliary: Il n'a rien dit. But personne goes after the participle: Il n'a vu personne.",
          examples = listOf(
            GrammarExample("Je n'ai rien compris.", "Rien frames the auxiliary"),
            GrammarExample("Je n'ai vu personne.", "Personne follows the participle"),
            GrammarExample("Je n'ai que deux euros.", "Ne... que = restriction (only), not negation")
          ),
          note = "Ne... que n'est pas une négation mais une restriction : il ne prend pas de « pas » !",
          questions = listOf(
            QuizQuestion(
              id = "neg_1",
              lessonId = "negation",
              promptFr = "Complétez au passé composé : « Hier soir, il n'a ___ vu au cinéma. »",
              promptEn = "Yesterday evening, he saw nobody at the cinema.",
              options = listOf("personne", "rien", "jamais", "aucun"),
              correctIndex = 0,
              explanation = "« Personne » se place après le participe passé : « Il n'a vu personne ».",
              type = QuestionType.FILL_BLANK
            ),
            QuizQuestion(
              id = "neg_2",
              lessonId = "negation",
              promptFr = "Que signifie la phrase « Je ne mange que des fruits » ?",
              promptEn = "What does 'Je ne mange que des fruits' mean?",
              options = listOf(
                "Je mange seulement des fruits (restriction)",
                "Je refuse de manger des fruits (négation)",
                "Je ne mange jamais de fruits"
              ),
              correctIndex = 0,
              explanation = "« Ne... que » signifie « seulement » (restriction, pas une négation totale).",
              type = QuestionType.MULTIPLE_CHOICE
            )
          )
        )
      )
    ),

    // 4. LES VERBES & TEMPS
    GrammarCategory(
      id = "cat-verbes",
      number = 4,
      label = "4. Les verbes & temps",
      frLabel = "Les verbes et les modes",
      desc = "Présent, passé composé, imparfait, futur, conditionnel, subjonctif et accord du participe passé.",
      levelRange = "A1–B2",
      iconName = "book",
      colorHex = 0xFF7C3AED,
      lessons = listOf(
        GrammarLesson(
          id = "passe-compose",
          categoryId = "cat-verbes",
          level = "A1/A2",
          frTitle = "Passé composé : avoir ou être ?",
          enSub = "Everyday past tense and the 15 movement verbs",
          desc = "Auxiliaire être pour 15 verbes de mouvement/état + verbes pronominaux. Avoir pour les autres.",
          ruleFr = "La majorité des verbes se conjuguent avec « avoir ». 15 verbes de déplacement/changement d'état (aller, venir, partir, arriver, entrer, sortir, monter, descendre, tomber, naître, mourir, rester, retourner, passer, rentrer) et TOUS les verbes pronominaux se conjuguent avec « être ». Avec être, le participe s'accorde avec le sujet.",
          ruleEn = "Most verbs take 'avoir'. 15 movement/state verbs and all reflexive verbs take 'être', and agree in gender/number with the subject.",
          examples = listOf(
            GrammarExample("Elle est partie à huit heures.", "Être + feminine subject agreement (-e)"),
            GrammarExample("Ils sont nés en mai.", "Être + plural subject agreement (-s)"),
            GrammarExample("Elle a mangé une pomme.", "Avoir: no agreement with subject")
          ),
          note = "Avec avoir, le participe s'accorde seulement si le COD est placé AVANT le verbe : « La lettre que j'ai écrite » !",
          questions = listOf(
            QuizQuestion(
              id = "pcp_1",
              lessonId = "passe-compose",
              promptFr = "« Elles ___ arrivées avec dix minutes de retard. »",
              promptEn = "They arrived ten minutes late.",
              options = listOf("sont", "ont", "étaient", "seront"),
              correctIndex = 0,
              explanation = "Arriver fait partie des verbes de mouvement qui se conjuguent avec « être » au passé composé.",
              type = QuestionType.FILL_BLANK
            ),
            QuizQuestion(
              id = "pcp_2",
              lessonId = "passe-compose",
              promptFr = "Choisissez l'accord correct : « La pomme qu'il a ___ était délicieuse. »",
              promptEn = "The apple he ate was delicious.",
              options = listOf("mangée", "mangé", "manger", "mangés"),
              correctIndex = 0,
              explanation = "Avec avoir, le participe passé s'accorde avec le COD (« qu' » = la pomme, féminin singulier) car il est placé AVANT le verbe.",
              type = QuestionType.AGREEMENT
            ),
            QuizQuestion(
              id = "pcp_3",
              lessonId = "passe-compose",
              promptFr = "« Ils se sont ___ hier matin. » (parler)",
              promptEn = "They spoke to each other yesterday morning.",
              options = listOf("parlé", "parlés", "parlée", "parlées"),
              correctIndex = 0,
              explanation = "On dit « parler à quelqu'un » : le pronom réfléchi « se » est un COI (indirect), donc le participe passé reste invariable !",
              type = QuestionType.AGREEMENT
            )
          )
        ),
        GrammarLesson(
          id = "imparfait",
          categoryId = "cat-verbes",
          level = "A2",
          frTitle = "L'imparfait vs Passé composé",
          enSub = "Background, habit, description vs punctual completed action",
          desc = "Passé composé = événement ponctuel. Imparfait = décor, description ou habitude.",
          ruleFr = "L'imparfait sert à décrire le décor, la météo, un état d'esprit ou une habitude dans le passé. Le passé composé exprime une action ponctuelle, délimitée, qui fait avancer l'histoire. Radical de l'imparfait : radical de « nous » au présent + -ais, -ais, -ait, -ions, -iez, -aient.",
          ruleEn = "Imparfait sets the background scene, habits, descriptions. Passé composé marks specific, completed actions that drive the plot.",
          examples = listOf(
            GrammarExample("Il pleuvait quand je suis sorti.", "Il pleuvait (imparfait, background) quand je suis sorti (passé composé, action)"),
            GrammarExample("Quand j'étais petit, je jouais souvent dehors.", "Repeated past habit → Imparfait")
          ),
          note = "Seul le verbe « être » a un radical irrégulier à l'imparfait : ét- (j'étais, tu étais...).",
          questions = listOf(
            QuizQuestion(
              id = "imp_1",
              lessonId = "imparfait",
              promptFr = "« Pendant que je dormais, le téléphone ___ soudain. »",
              promptEn = "While I was sleeping, the phone suddenly rang.",
              options = listOf("a sonné", "sonnait", "sonne", "avait sonné"),
              correctIndex = 0,
              explanation = "« Dormais » est la toile de fond (imparfait) ; la sonnerie soudaine est l'événement ponctuel (passé composé).",
              type = QuestionType.FILL_BLANK
            ),
            QuizQuestion(
              id = "imp_2",
              lessonId = "imparfait",
              promptFr = "Quelle est la forme à l'imparfait pour « nous » du verbe « faire » ?",
              promptEn = "What is the imparfait form for 'nous' of verb 'faire'?",
              options = listOf("nous faisions", "nous faisons", "nous ferions", "nous fassions"),
              correctIndex = 0,
              explanation = "Radical de « nous faisons » → fais- + terminaison -ions = faisions.",
              type = QuestionType.MULTIPLE_CHOICE
            )
          )
        ),
        GrammarLesson(
          id = "subjonctif",
          categoryId = "cat-verbes",
          level = "B1",
          frTitle = "Le subjonctif présent",
          enSub = "Obligation, emotion, doubt, wish & two different subjects",
          desc = "Déclenché par la volonté, l'obligation, les sentiments lorsqu'il y a deux sujets différents.",
          ruleFr = "Le subjonctif s'utilise après des verbes de volonté (vouloir que), d'obligation (il faut que), de sentiment (être content que), ou de doute (douter que) quand la subordonnée a un sujet DIFFÉRENT du verbe principal. Terminaisons : -e, -es, -e, -ions, -iez, -ent.",
          ruleEn = "Subjunctive is triggered by obligation (il faut que), desire, emotion, or doubt — only when there are two DIFFERENT subjects.",
          examples = listOf(
            GrammarExample("Il faut que tu fasses attention.", "Obligation + two subjects → Subjonctif"),
            GrammarExample("Je veux que vous veniez.", "Will/desire toward someone else → Subjonctif"),
            GrammarExample("Je veux partir.", "Same subject → Infinitif (not subjunctive!)")
          ),
          note = "Verbes irréguliers essentiels au subjonctif : être (sois), avoir (aie), faire (fasse), aller (aille), savoir (sache), pouvoir (puisse), vouloir (veuille).",
          questions = listOf(
            QuizQuestion(
              id = "sbj_1",
              lessonId = "subjonctif",
              promptFr = "« Il faut absolument que tu ___ ce livre. »",
              promptEn = "You absolutely must read this book.",
              options = listOf("lises", "lis", "liras", "lirais"),
              correctIndex = 0,
              explanation = "« Il faut que » exige le subjonctif : que tu lises.",
              type = QuestionType.MOOD_PICKER
            ),
            QuizQuestion(
              id = "sbj_2",
              lessonId = "subjonctif",
              promptFr = "« J'espère qu'il ___ beau demain. »",
              promptEn = "I hope the weather will be nice tomorrow.",
              options = listOf("fera (indicatif)", "fasse (subjonctif)"),
              correctIndex = 0,
              explanation = "Piège majeur ! « Espérer que » est suivi de l'INDICATIF (futur ou présent), PAS du subjonctif !",
              type = QuestionType.MOOD_PICKER
            ),
            QuizQuestion(
              id = "sbj_3",
              lessonId = "subjonctif",
              promptFr = "Quelle est la forme du subjonctif pour « que je » du verbe « faire » ?",
              promptEn = "What is the subjunctive form for 'que je' with 'faire'?",
              options = listOf("fasse", "fais", "ferai", "faise"),
              correctIndex = 0,
              explanation = "Faire a pour radical irrégulier « fass- » au subjonctif : que je fasse.",
              type = QuestionType.MULTIPLE_CHOICE
            ),
            QuizQuestion(
              id = "sbj_4",
              lessonId = "subjonctif",
              promptFr = "« Je suis ravi que vous ___ venus. »",
              promptEn = "I am thrilled that you came.",
              options = listOf("soyez", "êtes", "seriez", "fussiez"),
              correctIndex = 0,
              explanation = "Expression de sentiment (« être ravi que ») → subjonctif (soyez).",
              type = QuestionType.FILL_BLANK
            )
          )
        ),
        GrammarLesson(
          id = "conditionnel",
          categoryId = "cat-verbes",
          level = "B1",
          frTitle = "Le conditionnel présent",
          enSub = "Politeness, hypothetical, wishes & journalistic reporting",
          desc = "Radical du futur + terminaisons de l'imparfait (-ais, -ais, -ait, -ions, -iez, -aient).",
          ruleFr = "Formation : radical du futur simple + terminaisons de l'imparfait. Emplois : politesse (Pourriez-vous...), souhait (J'aimerais...), hypothèse (Si j'avais le temps, je voyagerais), ou information non confirmée dans la presse (L'accord serait signé demain).",
          ruleEn = "Formation: Future stem + imparfait endings. Used for polite requests, wishes, hypothetical results, and unconfirmed news.",
          examples = listOf(
            GrammarExample("Pourriez-vous m'aider, s'il vous plaît ?", "Polite request"),
            GrammarExample("J'aimerais visiter le musée d'Orsay.", "Wish / desire"),
            GrammarExample("Le ministre démissionnerait selon des sources.", "Journalistic conditional (unconfirmed)")
          ),
          note = "Si tu connais le radical du futur et les terminaisons de l'imparfait, tu connais déjà le conditionnel !",
          questions = listOf(
            QuizQuestion(
              id = "cnd_1",
              lessonId = "conditionnel",
              promptFr = "« ___ -vous la gentillesse de fermer la fenêtre ? »",
              promptEn = "Would you be kind enough to close the window?",
              options = listOf("Auriez", "Avez", "Aurez", "Ayez"),
              correctIndex = 0,
              explanation = "Conditionnel de politesse : « Auriez-vous la gentillesse... ».",
              type = QuestionType.FILL_BLANK
            ),
            QuizQuestion(
              id = "cnd_2",
              lessonId = "conditionnel",
              promptFr = "Comment se forme le conditionnel présent ?",
              promptEn = "How is present conditional formed?",
              options = listOf(
                "Radical du futur + terminaisons de l'imparfait",
                "Radical du présent + terminaisons du futur",
                "Infinitif + -er"
              ),
              correctIndex = 0,
              explanation = "Par exemple : parler (futur: parler-) + imparfait (-ait) = il parlerait.",
              type = QuestionType.MULTIPLE_CHOICE
            )
          )
        )
      )
    ),

    // 5. STRUCTURES AVANCÉES
    GrammarCategory(
      id = "cat-avance",
      number = 5,
      label = "5. Structures avancées",
      frLabel = "Structures avancées",
      desc = "Phrases avec si, concession, mise en relief, discours indirect, prépositions géographiques.",
      levelRange = "A2–B2",
      iconName = "stars",
      colorHex = 0xFF059669,
      lessons = listOf(
        GrammarLesson(
          id = "si-conditions",
          categoryId = "cat-avance",
          level = "B1/B2",
          frTitle = "Les phrases avec « si »",
          enSub = "Real, hypothetical, and contrary-to-fact conditions",
          desc = "Les 3 combinaisons temporelles avec si : présent/futur, imparfait/conditionnel, plus-que-parfait/conditionnel passé.",
          ruleFr = "1. Réel : Si + présent → présent / futur / impératif (Si tu veux, viens). 2. Hypothèse présente : Si + imparfait → conditionnel présent (Si j'avais de l'argent, j'achèterais cette maison). 3. Irréel du passé : Si + plus-que-parfait → conditionnel passé (Si j'avais su, je serais venu). Jamais de futur ni de conditionnel juste après « si » !",
          ruleEn = "Three patterns: Si + présent → futur; Si + imparfait → conditionnel présent; Si + PQP → conditionnel passé. Never put future or conditional right after 'si'!",
          examples = listOf(
            GrammarExample("Si tu as faim, mange une pomme.", "Real: présent + impératif"),
            GrammarExample("Si j'avais le temps, j'irais au concert.", "Hypothetical: imparfait + conditionnel"),
            GrammarExample("Si tu m'avais prévenu, je t'aurais attendu.", "Past unreal: plus-que-parfait + conditionnel passé")
          ),
          note = "Règle mnémotechnique : les « si » n'aiment pas les « -rais » ! Jamais de conditionnel après le si de condition.",
          questions = listOf(
            QuizQuestion(
              id = "si_1",
              lessonId = "si-conditions",
              promptFr = "« Si j'___ riche, je ferais le tour du monde. »",
              promptEn = "If I were rich, I would travel around the world.",
              options = listOf("étais (imparfait)", "serais (conditionnel)", "suis (présent)", "serai (futur)"),
              correctIndex = 0,
              explanation = "Le résultat est au conditionnel (« ferais »), donc après « si » on utilise l'imparfait (« étais »). Jamais de conditionnel après si !",
              type = QuestionType.FILL_BLANK
            ),
            QuizQuestion(
              id = "si_2",
              lessonId = "si-conditions",
              promptFr = "« Si tu viens demain, nous ___ au cinéma ensemble. »",
              promptEn = "If you come tomorrow, we will go to the movies together.",
              options = listOf("irons", "irions", "allions", "allons été"),
              correctIndex = 0,
              explanation = "Si + présent (« viens ») → futur simple dans la proposition principale (« irons »).",
              type = QuestionType.FILL_BLANK
            )
          )
        ),
        GrammarLesson(
          id = "opposition-concession",
          categoryId = "cat-avance",
          level = "B2",
          frTitle = "Opposition et concession",
          enSub = "Bien que, alors que, malgré, pourtant",
          desc = "Bien que et quoique exigent le subjonctif. Malgré est suivi d'un nom.",
          ruleFr = "« Bien que » et « quoique » demandent le subjonctif : Bien qu'il pleuve, nous sortons. « Malgré » est toujours suivi d'un nom : Malgré la pluie. « Alors que » et « tandis que » opposent deux faits à l'indicatif : Il travaille alors qu'elle dort.",
          ruleEn = "'Bien que' takes the subjunctive (Bien qu'il pleuve). 'Malgré' takes a noun (Malgré la pluie). 'Alors que' takes the indicative.",
          examples = listOf(
            GrammarExample("Bien qu'elle soit fatiguée, elle continue.", "Bien que + subjonctif"),
            GrammarExample("Malgré ses efforts, il a échoué.", "Malgré + nom"),
            GrammarExample("Il est calme, tandis que sa sœur est agitée.", "Tandis que + indicatif")
          ),
          note = "On ne dit jamais « malgré que » en français standard (sauf dans l'expression figée « malgré qu'il en ait ») !",
          questions = listOf(
            QuizQuestion(
              id = "op_1",
              lessonId = "opposition-concession",
              promptFr = "« ___ ses difficultés, il a réussi son examen. »",
              promptEn = "Despite his difficulties, he passed his exam.",
              options = listOf("Malgré", "Bien que", "Pourtant", "Alors que"),
              correctIndex = 0,
              explanation = "« Ses difficultés » est un groupe nominal : on emploie « malgré » (+ nom).",
              type = QuestionType.FILL_BLANK
            ),
            QuizQuestion(
              id = "op_2",
              lessonId = "opposition-concession",
              promptFr = "« Bien qu'il ___ tard, le magasin reste ouvert. »",
              promptEn = "Although it is late, the store remains open.",
              options = listOf("soit", "est", "sera", "serait"),
              correctIndex = 0,
              explanation = "« Bien que » exige obligatoirement le subjonctif (soit).",
              type = QuestionType.MOOD_PICKER
            )
          )
        ),
        GrammarLesson(
          id = "prepositions-lieu",
          categoryId = "cat-avance",
          level = "A2/B1",
          frTitle = "Prépositions des pays et villes",
          enSub = "À, en, au, aux with geographical places",
          desc = "En pour les pays féminins, Au pour les pays masculins, Aux pour les pluriels, À pour les villes.",
          ruleFr = "Villes : « à » (à Paris, à Tokyo). Pays féminins (se terminant par -e) ou commençant par voyelle : « en » (en France, en Espagne, en Iran). Pays masculins : « au » (au Japon, au Canada). Pays pluriels : « aux » (aux États-Unis, aux Pays-Bas). Exceptions de genre : le Mexique, le Cambodge sont masculins (au Mexique).",
          ruleEn = "Cities: à. Feminine countries or vowel-starting countries: en. Masculine countries: au. Plural countries: aux.",
          examples = listOf(
            GrammarExample("Je vis à Montréal, au Canada.", "City (à), Masculine country (au)"),
            GrammarExample("Elle habite en Italie, puis elle ira au Japon.", "Feminine country (en), Masculine country (au)"),
            GrammarExample("Ils partent en vacances aux États-Unis.", "Plural country (aux)")
          ),
          note = "Pour la provenance : venir de France, du Japon, des États-Unis, de Rome.",
          questions = listOf(
            QuizQuestion(
              id = "geo_1",
              lessonId = "prepositions-lieu",
              promptFr = "« Cet été, nous voyageons ___ Mexique et ___ Colombie. »",
              promptEn = "This summer, we are traveling to Mexico and to Colombia.",
              options = listOf("au / en", "en / en", "au / au", "en / au"),
              correctIndex = 0,
              explanation = "Le Mexique est une exception masculine (au Mexique), tandis que la Colombie est féminine (en Colombie).",
              type = QuestionType.MULTIPLE_CHOICE
            ),
            QuizQuestion(
              id = "geo_2",
              lessonId = "prepositions-lieu",
              promptFr = "« Elle a fait ses études ___ Danemark et ___ Pays-Bas. »",
              promptEn = "She did her studies in Denmark and in the Netherlands.",
              options = listOf("au / aux", "en / au", "en / aux", "au / en"),
              correctIndex = 0,
              explanation = "Danemark est masculin consonne (au Danemark) et Pays-Bas est pluriel (aux Pays-Bas).",
              type = QuestionType.MULTIPLE_CHOICE
            )
          )
        )
      )
    ),

    // 6. CONJUGAISON REFERENCE
    GrammarCategory(
      id = "cat-conj",
      number = 6,
      label = "6. Tableaux de conjugaison",
      frLabel = "Conjugaison essentielle",
      desc = "Modèles réguliers (-er, -ir, -re) et les verbes irréguliers à haute fréquence.",
      levelRange = "A1–B1",
      iconName = "table",
      colorHex = 0xFF4F46E5,
      lessons = listOf(
        GrammarLesson(
          id = "verbes-reguliers",
          categoryId = "cat-conj",
          level = "A1/A2",
          frTitle = "Les 3 groupes réguliers",
          enSub = "Parler (-er), Finir (-ir), Vendre (-re)",
          desc = "Terminaisons du présent de l'indicatif pour les verbes réguliers.",
          ruleFr = "-er (parler) : -e, -es, -e, -ons, -ez, -ent. -ir (finir) : -is, -is, -it, -issons, -issez, -issent. -re (vendre) : -s, -s, -(rien/d), -ons, -ez, -ent.",
          ruleEn = "-er verbs take -e, -es, -e, -ons, -ez, -ent. -ir type finir inserts -iss- in plural. -re drops ending in 3rd person singular (il vend).",
          examples = listOf(
            GrammarExample("Nous parlons, nous finissons, nous vendons", "1st person plural comparison"),
            GrammarExample("Ils finissent leurs devoirs.", "Note the -iss- infix for 2nd group")
          ),
          tableTitle = "Présent de l'indicatif",
          tableHeaders = listOf("Personne", "1er (-er)", "2e (-ir)", "3e (-re)"),
          tableRows = listOf(
            GrammarTableRow("je", "parle", "finis", "vends"),
            GrammarTableRow("tu", "parles", "finis", "vends"),
            GrammarTableRow("il / elle", "parle", "finit", "vend"),
            GrammarTableRow("nous", "parlons", "finissons", "vendons"),
            GrammarTableRow("vous", "parlez", "finissez", "vendez"),
            GrammarTableRow("ils / elles", "parlent", "finissent", "vendent")
          ),
          questions = listOf(
            QuizQuestion(
              id = "vr_1",
              lessonId = "verbes-reguliers",
              promptFr = "Conjuguez « choisir » avec « nous » au présent :",
              promptEn = "Conjugate 'choisir' with 'nous' in the present:",
              options = listOf("nous choisissons", "nous choisons", "nous choisissont", "nous choisiez"),
              correctIndex = 0,
              explanation = "Choisir appartient au 2e groupe (-ir régulier) et s'élargit en -issons avec « nous ».",
              type = QuestionType.FILL_BLANK
            ),
            QuizQuestion(
              id = "vr_2",
              lessonId = "verbes-reguliers",
              promptFr = "Conjuguez « attendre » avec « il » au présent :",
              promptEn = "Conjugate 'attendre' with 'il' in the present:",
              options = listOf("il attend", "il attends", "il attendt", "il attende"),
              correctIndex = 0,
              explanation = "Les verbes en -dre comme attendre gardent le -d nu à la 3e personne du singulier (il attend).",
              type = QuestionType.FILL_BLANK
            )
          )
        )
      )
    ),

    // 7. C1–C2 MAÎTRISE AVANCÉE
    GrammarCategory(
      id = "cat-c1",
      number = 7,
      label = "7. C1–C2 Maîtrise",
      frLabel = "Nuances C1–C2 & Pièges d'experts",
      desc = "Tout accord, le fait que vs le fait est que, valeurs de si, de vs des, style journalistique.",
      levelRange = "C1–C2",
      iconName = "crown",
      colorHex = 0xFFDC2626,
      lessons = listOf(
        GrammarLesson(
          id = "c1-tout",
          categoryId = "cat-c1",
          level = "C1",
          frTitle = "Tout : adjectif, nom, pronom ou adverbe ?",
          enSub = "The many faces of 'tout' and tricky agreements",
          desc = "Tout s'accorde comme adjectif ou pronom. Comme adverbe, il est invariable sauf devant adjectif féminin commençant par une consonne.",
          ruleFr = "Adjectif : s'accorde (tous les jours, toute la nuit). Pronom : s'accorde (Elles sont toutes arrivées). Adverbe (= entièrement) : en principe invariable (Ils sont tout contents), mais s'accorde devant un adjectif féminin commençant par consonne ou h aspiré (Elles sont toutes pâles / toutes honteuses) !",
          ruleEn = "'Tout' agrees as adjective or pronoun. As adverb ('entirely'), it is invariable except before feminine adjectives starting with a consonant.",
          examples = listOf(
            GrammarExample("Ils sont tout étonnés.", "Adverb before vowel → invariable (tout)"),
            GrammarExample("Elles sont toutes surprises.", "Adverb before feminine consonant → agrees (toutes) !"),
            GrammarExample("Ils sont tous venus.", "Pronoun = all of them → tous")
          ),
          note = "Ne confondez pas : « Ils sont tout tristes » (très tristes, adverbe) et « Ils sont tous tristes » (chacun d'eux est triste, pronom) !",
          questions = listOf(
            QuizQuestion(
              id = "c1_t_1",
              lessonId = "c1-tout",
              promptFr = "Complétez : « Elles sont ___ contentes de leur résultat. »",
              promptEn = "They are entirely happy with their result.",
              options = listOf("toutes", "tout", "tous", "toute"),
              correctIndex = 0,
              explanation = "L'adverbe « tout » s'accorde exceptionnellement devant un adjectif féminin débutant par une consonne : « toutes contentes ».",
              type = QuestionType.AGREEMENT
            ),
            QuizQuestion(
              id = "c1_t_2",
              lessonId = "c1-tout",
              promptFr = "Complétez : « Elles sont ___ émues par cette histoire. »",
              promptEn = "They are completely moved by this story.",
              options = listOf("tout", "toutes", "toute"),
              correctIndex = 0,
              explanation = "« Émues » commence par une voyelle : l'adverbe reste invariable « tout émues ».",
              type = QuestionType.AGREEMENT
            )
          )
        ),
        GrammarLesson(
          id = "b2c2-le-fait-que",
          categoryId = "cat-c1",
          level = "C2",
          frTitle = "« Le fait que » vs « Le fait est que »",
          enSub = "Two look-alike expressions with opposite moods",
          desc = "« Le fait que » prend le subjonctif. « Le fait est que » prend l'indicatif.",
          ruleFr = "« Le fait que » introduit un fait présupposé pour le commenter ou porter un jugement : il est suivi du SUBJONCTIF (Le fait qu'il soit en retard m'étonne). En revanche, « Le fait est que » affirme une réalité comme constat direct : il prend l'INDICATIF (Le fait est qu'il n'a rien préparé).",
          ruleEn = "'Le fait que' introduces a presupposed premise to comment on → takes SUBJUNCTIVE. 'Le fait est que' states a direct factual reality → takes INDICATIVE.",
          examples = listOf(
            GrammarExample("Le fait qu'elle ait accepté me surprend.", "Le fait que + subjonctif (commenting on a fact)"),
            GrammarExample("Le fait est que nous n'avons plus de budget.", "Le fait est que + indicatif (stating a reality)")
          ),
          questions = listOf(
            QuizQuestion(
              id = "lfq_1",
              lessonId = "b2c2-le-fait-que",
              promptFr = "« Le fait qu'il ___ absent modifie nos projets. »",
              promptEn = "The fact that he is absent alters our plans.",
              options = listOf("soit (subjonctif)", "est (indicatif)", "serait (conditionnel)"),
              correctIndex = 0,
              explanation = "« Le fait que » requiert le subjonctif.",
              type = QuestionType.MOOD_PICKER
            ),
            QuizQuestion(
              id = "lfq_2",
              lessonId = "b2c2-le-fait-que",
              promptFr = "« Le fait est que nous ___ retardés par l'orage. »",
              promptEn = "The fact is that we were delayed by the storm.",
              options = listOf("avons été (indicatif)", "ayons été (subjonctif)"),
              correctIndex = 0,
              explanation = "« Le fait est que » assène un constat direct et prend l'indicatif.",
              type = QuestionType.MOOD_PICKER
            )
          )
        ),
        GrammarLesson(
          id = "c1-de-des",
          categoryId = "cat-c1",
          level = "C1",
          frTitle = "« De » ou « des » devant adjectif antéposé",
          enSub = "When plural 'des' shrinks to 'de'",
          desc = "Des devient de/d' devant un adjectif placé avant le nom pluriel (de beaux enfants).",
          ruleFr = "L'article indéfini pluriel « des » devient « de » (ou « d' ») quand l'adjectif qualificatif est placé AVANT le nom : « des idées intéressantes » MAIS « de bonnes idées ». Il devient aussi « de » après une expression de quantité (beaucoup de) ou à la négation.",
          ruleEn = "Plural 'des' changes to 'de' when the adjective precedes the noun: 'de bons amis' vs 'des amis fidèles'.",
          examples = listOf(
            GrammarExample("Il a fait de grands progrès.", "Adjective before noun → de"),
            GrammarExample("Il a fait des progrès remarquables.", "Adjective after noun → des"),
            GrammarExample("De nouveaux défis nous attendent.", "Nouveaux is antéposé → de nouveaux")
          ),
          note = "Cette règle s'applique aussi à « d'autres » et « de nombreux » : de nombreux exemples, d'autres personnes.",
          questions = listOf(
            QuizQuestion(
              id = "dd_1",
              lessonId = "c1-de-des",
              promptFr = "« Nous avons rencontré ___ personnes charmantes. »",
              promptEn = "We met charming people.",
              options = listOf("des", "de", "d'"),
              correctIndex = 0,
              explanation = "« Charmantes » est placé APRÈS le nom, donc l'article reste « des ».",
              type = QuestionType.FILL_BLANK
            ),
            QuizQuestion(
              id = "dd_2",
              lessonId = "c1-de-des",
              promptFr = "« Nous avons rencontré ___ charmantes personnes. »",
              promptEn = "We met charming people.",
              options = listOf("de", "des", "d'"),
              correctIndex = 0,
              explanation = "« Charmantes » est placé AVANT le nom, donc « des » devient « de » !",
              type = QuestionType.FILL_BLANK
            )
          )
        )
      )
    )
  )

  fun getAllLessons(): List<GrammarLesson> = CATEGORIES.flatMap { it.lessons }

  fun getLesson(lessonId: String): GrammarLesson? = getAllLessons().find { it.id == lessonId }

  fun getCategory(categoryId: String): GrammarCategory? = CATEGORIES.find { it.id == categoryId }

  fun getAllQuestions(): List<QuizQuestion> = getAllLessons().flatMap { it.questions }

  fun getSpeedDrillQuestions(count: Int = 15): List<QuizQuestion> {
    return getAllQuestions().shuffled().take(count)
  }
}
