package com.example.data.engine

object GrammarRuleRepository {

  val RULES: List<StructuredRule> = listOf(
    // 1. LE SUBJONCTIF PRÉSENT
    StructuredRule(
      id = "subjonctif-present",
      categoryId = "cat-mode",
      categoryName = "Modes & Temps",
      level = "B1",
      titleFr = "Le Subjonctif Présent : Formation & Déclencheurs",
      titleEn = "Present Subjunctive: Formation & Triggers",
      summaryFr = "Le subjonctif exprime la subjectivité : volonté, doute, sentiment, nécessité ou possibilité, par opposition à l'indicatif qui énonce des faits certains.",
      summaryEn = "The subjunctive expresses subjectivity: desire, doubt, emotion, necessity, or possibility, contrasting with the indicative of certain facts.",
      pillar = GrammarPillar.VERB_SYSTEM,
      ruleExplanationEn = "Don't try to memorize 'the subjunctive is used for doubt, emotion, necessity...' as an abstract list — it's too slippery. Instead, memorize it as a trigger: certain fixed phrases (il faut que, vouloir que, bien que, avant que) automatically switch the following verb into the subjunctive, no thinking required. If the main clause states a plain fact with certainty (je sais que, il est certain que), stay in the indicative. If the two clauses share the same subject, skip 'que' entirely and use the infinitive instead (je veux partir, not je veux que je parte).",
      triggerToken = "Il faut que",
      triggerActionFr = "Subjonctif obligatoire",
      triggerActionEn = "Mandatory subjunctive",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Sujet 1", "Sujet principal", TokenCategory.SUBJECT, "La personne ou entité qui exprime le sentiment."),
          FormulaToken("Verbe déclencheur", "Volonté / Doute / Émotion", TokenCategory.TRIGGER, "Ex : vouloir, douter, craindre, il faut..."),
          FormulaToken("que / qu'", "Conjonction obligatoire", TokenCategory.CONNECTOR, "Relie les deux propositions."),
          FormulaToken("Sujet 2", "Second sujet distinct", TokenCategory.SUBJECT, "Doit généralement différer du sujet 1."),
          FormulaToken("Verbe au subjonctif", "Radical (ils) + -e, -es, -e, -ions, -iez, -ent", TokenCategory.TARGET_VERB, "Ex : que nous partions, qu'il sache...")
        ),
        diagramTitle = "Arbre de Décision : Indicatif ou Subjonctif ?",
        diagramDescription = "Suivez le flux mental pour choisir le bon mode verbal :",
        decisionSteps = listOf(
          DecisionStep(1, "Le verbe principal exprime-t-il une certitude absolue (croire fermement, savoir, constater) ?", "-> Mode INDICATIF (ex: Je sais qu'il viendra)", "-> Passez à l'étape 2"),
          DecisionStep(2, "Le verbe exprime-t-il une volonté, nécessité, doute ou émotion (vouloir, falloir, douter, avoir peur) ?", "-> Mode SUBJONCTIF (ex: Il faut qu'il vienne)", "-> Si simple fait ou probabilité forte -> Indicatif"),
          DecisionStep(3, "Les sujets des deux propositions sont-ils identiques (ex: Je veux + Je pars) ?", "-> Utilisez l'INFINITIF (ex: Je veux partir, NON 'Je veux que je parte')", "-> Deux sujets différents : SUBJONCTIF !")
        ),
        comparisonTableTitle = "Tableau de Contraste : Déclencheurs Typiques",
        comparisonHeaders = listOf("INDICATIF (Certitude)", "SUBJONCTIF (Subjectivité)", "Règle clé"),
        comparisonRows = listOf(
          listOf("Je pense qu'il est prêt.", "Je ne pense pas qu'il soit prêt.", "Négation du verbe d'opinion -> Subjonctif"),
          listOf("Il est certain qu'elle vient.", "Il est possible qu'elle vienne.", "Certitude vs Simple possibilité"),
          listOf("J'espère qu'il fera beau.", "Je souhaite qu'il fasse beau.", "Espérer = indicatif / Souhaiter = subjonctif !"),
          listOf("Après qu'il est parti...", "Avant qu'il ne parte...", "Après que = indicatif / Avant que = subjonctif !")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Il faut qu'on parte tout de suite pour ne pas rater le train.",
          highlightedSegment = "qu'on parte",
          englishSentence = "We need to leave right away so we don't miss the train.",
          contextNote = "Usage quotidien standard avec 'il faut que' + subjonctif de partir."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Quoiqu'il en soit, il convient que vous fassiez preuve de discernement.",
          highlightedSegment = "que vous fassiez",
          englishSentence = "Be that as it may, it is fitting that you exercise discernment.",
          contextNote = "Tournure administrative et académique avec 'il convient que' + faire au subjonctif."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "Faut que j'y aille sinon je vais me faire engueuler !",
          highlightedSegment = "que j'y aille",
          englishSentence = "Gotta go or else I'm gonna get yelled at!",
          contextNote = "Élision du 'il' impersonnel ('Faut que') caractéristique du registre parlé relâché."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "La direction exige que le compte-rendu soit transmis avant vendredi midi.",
          highlightedSegment = "soit transmis",
          englishSentence = "Management demands that the report be submitted before Friday noon.",
          contextNote = "Verbe d'exigence formelle au travail avec voix passive au subjonctif présent."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "La Réunion Stratégique à Paris",
        fullTextFr = "Dans la grande salle de réunion, la directrice prend la parole. Il est indispensable que chacun prenne ses responsabilités pour ce lancement. Bien que le délai soit très serré, j'exige que l'équipe livre une version sans défaut. Je doute que nos concurrents puissent réagir aussi rapidement. Cependant, n'attendez pas qu'il soit trop tard pour signaler un blocage technique.",
        fullTextEn = "In the main boardroom, the director speaks. It is essential that everyone take responsibility for this launch. Although the deadline is very tight, I require that the team deliver a flawless version. I doubt our competitors can react as quickly. However, do not wait until it is too late to flag a technical blocker.",
        annotations = listOf(
          InlineAnnotation(
            id = "sub_ann_1",
            targetPhrase = "prenne ses responsabilités",
            explanationFr = "« Il est indispensable que » est une locution impersonnelle de nécessité absolue qui requiert le subjonctif.",
            explanationEn = "'Il est indispensable que' conveys absolute necessity, mandating the subjunctive.",
            whyItApplies = "Locution impersonnelle de nécessité.",
            commonPitfall = "Ne pas conjuguer à l'indicatif ('prend'). Le radical de 'ils prennent' donne 'prenne'."
          ),
          InlineAnnotation(
            id = "sub_ann_2",
            targetPhrase = "soit très serré",
            explanationFr = "La conjonction concessive « bien que » déclenche systématiquement le subjonctif en français soigné.",
            explanationEn = "'Bien que' (although) always triggers the subjunctive in standard French.",
            whyItApplies = "Conjonction concessive subordonnante.",
            commonPitfall = "Ne confondez pas avec 'même si', qui est suivi de l'indicatif !"
          ),
          InlineAnnotation(
            id = "sub_ann_3",
            targetPhrase = "puissent réagir",
            explanationFr = "Le verbe « douter que » introduit une incertitude majeure, imposant le subjonctif du verbe pouvoir.",
            explanationEn = "The verb 'douter que' expresses genuine doubt, requiring the subjunctive of pouvoir.",
            whyItApplies = "Verbe de doute explicite.",
            commonPitfall = "Attention : 'se douter de' (soupçonner) prend souvent l'indicatif, mais 'douter que' prend le subjonctif."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_subj_1",
          instructionFr = "Conjuguez le verbe entre parenthèses au subjonctif présent :",
          instructionEn = "Conjugate the verb in parentheses in the present subjunctive:",
          basePrompt = "Il est urgent que nous _____ (prendre) une décision avant ce soir.",
          targetAnswer = "prenions",
          acceptedAnswers = listOf("prenions"),
          hint = "Prendre fait 'ils prennent' mais pour nous/vous la terminaison est -ions / -iez.",
          explanation = "Au subjonctif, les formes 'nous' et 'vous' reprennent souvent le radical de l'imparfait : pren- + -ions = prenions."
        ),
        ProductionDrillItem(
          id = "prod_subj_2",
          instructionFr = "Conjuguez le verbe irrégulier au subjonctif présent :",
          instructionEn = "Conjugate the irregular verb in the present subjunctive:",
          basePrompt = "Je ne pense pas qu'elle _____ (savoir) où nous nous trouvons.",
          targetAnswer = "sache",
          acceptedAnswers = listOf("sache"),
          hint = "Le radical du subjonctif pour savoir est sach-.",
          explanation = "Le verbe 'savoir' a un radical complètement irrégulier au subjonctif : que je sache, qu'il sache, que nous sachions."
        ),
        ProductionDrillItem(
          id = "prod_subj_3",
          instructionFr = "Transformez la phrase au subjonctif après 'Bien que' :",
          instructionEn = "Transform the sentence in the subjunctive following 'Bien que':",
          basePrompt = "Bien qu'il _____ (faire) très froid, ils sont partis marcher en forêt.",
          targetAnswer = "fasse",
          acceptedAnswers = listOf("fasse"),
          hint = "Faire devient fass- au subjonctif.",
          explanation = "Faire a pour radical subjonctif 'fass-' : que je fasse, qu'il fasse, que nous fassions."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_subj_1",
          instructionFr = "Touchez le mot qui contient une faute de mode ou de conjugaison :",
          passage = "Je suis absolument certain qu'il vienne à la réunion demain matin.",
          tokens = listOf("Je", "suis", "absolument", "certain", "qu'il", "vienne", "à", "la", "réunion", "demain", "matin."),
          errorTokenIndex = 5,
          errorWord = "vienne",
          correction = "viendra (ou vient)",
          ruleExplanation = "« Être certain que » exprime une certitude indiscutable à la forme affirmative. Il exige le mode INDICATIF (viendra), et non le subjonctif !"
        ),
        SpotErrorDrillItem(
          id = "spot_subj_2",
          instructionFr = "Trouvez l'erreur dans l'emploi des conjonctions et modes :",
          passage = "Même s'il soit malade, il a tenu à terminer son travail.",
          tokens = listOf("Même", "s'il", "soit", "malade,", "il", "a", "tenu", "à", "terminer", "son", "travail."),
          errorTokenIndex = 2,
          errorWord = "soit",
          correction = "est",
          ruleExplanation = "« Même si » est TOUJOURS suivi de l'indicatif (même s'il est malade). C'est « bien que » ou « quoique » qui demandent le subjonctif !"
        )
      )
    ),

    // 2. L'ACCORD DU PARTICIPE PASSÉ
    StructuredRule(
      id = "accord-participe-passe",
      categoryId = "cat-verbe",
      categoryName = "Morphologie Verbale",
      level = "B1",
      titleFr = "L'Accord du Participe Passé : Avoir vs Être",
      titleEn = "Past Participle Agreement: Avoir vs Être",
      summaryFr = "Avec 'être', le participe s'accorde avec le sujet. Avec 'avoir', il reste invariable SAUF si le complément d'objet direct (COD) le précède.",
      summaryEn = "With 'être', the participle agrees with the subject. With 'avoir', it never agrees UNLESS the direct object (COD) precedes it.",
      pillar = GrammarPillar.AGREEMENT_ENGINE,
      ruleExplanationEn = "Two separate rules, one engine. With être, agreement is automatic and always matches the subject (elle est partie). With avoir, the default is no agreement at all — the participle just sits there unchanged. The only exception: if a direct object pronoun or relative 'que' comes BEFORE the verb, the participle must agree with that preceding object (la pomme qu'il a mangée). If the object comes after the verb as usual, or if it's the pronoun 'en', there's no agreement.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("COD Précédent", "Complément d'Objet Direct placé AVANT", TokenCategory.OBJECT, "Pronoms (que, les, la, l', combien de...)"),
          FormulaToken("Auxiliaire", "Avoir (conjugué)", TokenCategory.TRIGGER, "ai, as, a, avons, avez, ont"),
          FormulaToken("Participe Passé", "Accord en genre et en nombre avec le COD !", TokenCategory.TARGET_VERB, "Ex : Les lettres que j'ai écrites (-es)")
        ),
        diagramTitle = "Le Détecteur de COD pour l'auxiliaire AVOIR",
        diagramDescription = "Pour chaque phrase au passé composé avec 'avoir' :",
        decisionSteps = listOf(
          DecisionStep(1, "Y a-t-il un Complément d'Objet Direct (COD) dans la phrase ?", "-> Passez à l'étape 2", "-> Pas de COD : le participe reste INVARIABLE (ex: Ils ont dormi)"),
          DecisionStep(2, "Le COD est-il placé AVANT le verbe (antéposé) ?", "-> ACCORD du participe passé avec ce COD ! (ex: La pomme que j'ai mangée)", "-> Le COD est après le verbe : PAS D'ACCORD (ex: J'ai mangé la pomme)"),
          DecisionStep(3, "Le COD est-il le pronom 'en' ?", "-> Cas particulier : avec 'en', le participe reste INVARIABLE (ex: Des pommes, j'en ai mangé)", "-> Avec 'les', 'la', 'que' : accord normal !")
        ),
        comparisonTableTitle = "Synthèse d'Accord : Être vs Avoir",
        comparisonHeaders = listOf("Structure", "Règle d'accord", "Exemple type"),
        comparisonRows = listOf(
          listOf("Auxiliaire ÊTRE", "Accord automatique avec le SUJET", "Elles sont parties à l'aube."),
          listOf("Auxiliaire AVOIR (COD après)", "INVARIABLE (aucun accord)", "Elle a acheté des fleurs magnifiques."),
          listOf("Auxiliaire AVOIR (COD avant)", "Accord avec le COD antéposé", "Les fleurs qu'elle a achetées sont belles."),
          listOf("Pronominal réfléchi réciproque", "Accord avec COD si avant ; invariable si COI", "Ils se sont parlé (COI : parler à) vs Ils se sont vus (COD)")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Regarde les photos que nous avons prises pendant les vacances.",
          highlightedSegment = "prises",
          englishSentence = "Look at the pictures that we took during the holidays.",
          contextNote = "'les photos' (féminin pluriel) est COD antéposé par le relatif 'que'."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Nombreuses furent les épreuves qu'ils eurent surmontées au fil des ans.",
          highlightedSegment = "surmontées",
          englishSentence = "Numerous were the trials that they had overcome through the years.",
          contextNote = "Accord littéraire au passé antérieur avec antéposition du pronom relatif."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "Mes clés de bagnole ? Je les ai paumées hier soir !",
          highlightedSegment = "paumées",
          englishSentence = "My car keys? I lost them yesterday evening!",
          contextNote = "Pronom COD 'les' placé avant le verbe familier 'paumer'."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Veuillez trouver ci-joint les factures que notre service comptable a validées.",
          highlightedSegment = "validées",
          englishSentence = "Please find attached the invoices that our accounting department has approved.",
          contextNote = "Accord du COD 'les factures' dans une correspondance commerciale formelle."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "L'Enquête au Musée du Louvre",
        fullTextFr = "Hier soir, deux toiles de maîtres ont disparu de la galerie sud. La police judiciaire les a retrouvées ce matin dissimulées dans un camion. Les indices que les enquêteurs ont recueillis confirment l'intervention d'experts. La conservatrice du musée est soulagée : aucune détérioration n'a été constatée. Les restauratrices se sont empressées de vérifier chaque cadre.",
        fullTextEn = "Yesterday evening, two master paintings disappeared from the south gallery. The judicial police found them this morning hidden inside a truck. The clues that the investigators gathered confirm the involvement of experts. The museum curator is relieved: no damage was observed. The restorers hurried to check every frame.",
        annotations = listOf(
          InlineAnnotation(
            id = "acc_ann_1",
            targetPhrase = "les a retrouvées",
            explanationFr = "Le pronom 'les' renvoie aux 'deux toiles' (féminin pluriel) et est placé avant le verbe avoir : accord en -ées.",
            explanationEn = "The pronoun 'les' refers to 'deux toiles' (fem. pl.) before the verb: agreement in -ées.",
            whyItApplies = "Pronom COD antéposé.",
            commonPitfall = "N'oubliez pas d'accorder le participe même lorsque le COD est un simple pronom personnel."
          ),
          InlineAnnotation(
            id = "acc_ann_2",
            targetPhrase = "ont recueillis",
            explanationFr = "Le pronom relatif 'que' a pour antécédent 'les indices' (masculin pluriel), placé avant 'ont' : accord en -is.",
            explanationEn = "The relative pronoun 'que' has 'les indices' (masc. pl.) as antecedent before the verb: agreement in -is.",
            whyItApplies = "Pronom relatif COD antéposé.",
            commonPitfall = "Le sujet est 'les enquêteurs', mais l'accord se fait avec le COD 'les indices'."
          ),
          InlineAnnotation(
            id = "acc_ann_3",
            targetPhrase = "se sont empressées",
            explanationFr = "Verbe essentiellement pronominal (s'empresser) : s'accorde toujours avec le sujet (les restauratrices).",
            explanationEn = "Essentially pronominal verb (s'empresser): always agrees directly with the subject.",
            whyItApplies = "Verbe essentiellement pronominal.",
            commonPitfall = "Distinct des verbes comme 'se téléphoner' où 'se' est COI (se téléphoner à soi-même) sans accord."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_acc_1",
          instructionFr = "Tapez la forme correctement accordée du participe passé :",
          instructionEn = "Type the correctly agreed form of the past participle:",
          basePrompt = "Toutes les lettres que tu m'as _____ (écrire) sont rangées dans ce tiroir.",
          targetAnswer = "écrites",
          acceptedAnswers = listOf("écrites"),
          hint = "Le COD est 'les lettres' (féminin pluriel), placé avant le verbe.",
          explanation = "Le pronom 'que' représente 'les lettres' (fém. pluriel). Le participe s'accorde : écrites."
        ),
        ProductionDrillItem(
          id = "prod_acc_2",
          instructionFr = "Accordez le verbe pronominal réciproque (attention au piège COI !) :",
          instructionEn = "Agree the reciprocal pronominal verb (beware of the indirect object trap!):",
          basePrompt = "Ils se sont _____ (parler) pendant des heures sans jamais se fâcher.",
          targetAnswer = "parlé",
          acceptedAnswers = listOf("parlé"),
          hint = "On dit 'parler À quelqu'un' : le pronom 'se' est COI, donc aucun accord.",
          explanation = "Le verbe 'parler' se construit avec la préposition 'à'. Le pronom 'se' est COI, le participe passé reste invariable."
        ),
        ProductionDrillItem(
          id = "prod_acc_3",
          instructionFr = "Accordez avec l'adverbe interrogatif de quantité :",
          instructionEn = "Agree with the interrogative adverb of quantity:",
          basePrompt = "Combien de fautes as-tu _____ (faire) dans cette dictée difficile ?",
          targetAnswer = "faites",
          acceptedAnswers = listOf("faites"),
          hint = "Combien de fautes est COD placé avant l'auxiliaire. Faute est féminin pluriel.",
          explanation = "Dans l'interrogation avec 'combien de + nom féminin pluriel', le COD précède le verbe : 'faites'."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_acc_1",
          instructionFr = "Touchez le participe passé qui comporte une erreur d'accord :",
          passage = "La tarte aux pommes que ma grand-mère a préparé sent délicieusement bon.",
          tokens = listOf("La", "tarte", "aux", "pommes", "que", "ma", "grand-mère", "a", "préparé", "sent", "délicieusement", "bon."),
          errorTokenIndex = 8,
          errorWord = "préparé",
          correction = "préparée",
          ruleExplanation = "Le COD 'la tarte aux pommes' (féminin singulier) précède l'auxiliaire avoir par le pronom relatif 'que'. Le participe doit s'accorder : préparée !"
        ),
        SpotErrorDrillItem(
          id = "spot_acc_2",
          instructionFr = "Repérez la faute d'accord abusif avec le pronom 'en' :",
          passage = "Des fraises fraîches du jardin, nous en avons cueillies un panier entier.",
          tokens = listOf("Des", "fraises", "fraîches", "du", "jardin,", "nous", "en", "avons", "cueillies", "un", "panier", "entier."),
          errorTokenIndex = 8,
          errorWord = "cueillies",
          correction = "cueilli",
          ruleExplanation = "Le pronom neutre 'en' bloque l'accord du participe passé en français classique : 'nous en avons cueilli' reste invariable."
        )
      )
    ),

    // 3. L'ORDRE DES DOUBLES PRONOMS COMPLÉMENTS
    StructuredRule(
      id = "pronoms-doubles",
      categoryId = "cat-pronom",
      categoryName = "Système Pronominal",
      level = "A2",
      titleFr = "L'Ordre des Doubles Pronoms Compléments",
      titleEn = "Double Object Pronoun Order",
      summaryFr = "En français, lorsque deux pronoms compléments précèdent le verbe, leur ordre est strictement fixé par une hiérarchie syntaxique immuable.",
      summaryEn = "When two object pronouns precede the verb in French, their order is strictly dictated by an immutable syntactic hierarchy.",
      pillar = GrammarPillar.PRONOUN_HIERARCHY,
      ruleExplanationEn = "Don't think in grammar terms (COD, COI) — think in a fixed pipeline. Before the verb, pronouns always slot into this exact order: [me/te/se/nous/vous] then [le/la/les] then [lui/leur] then [y] then [en], then the verb. So 'il me le donne' is right, 'il le me donne' is never possible. The only time the order flips is the affirmative imperative, where the verb comes first and le/la/les jumps ahead of moi/toi/lui, connected by hyphens (donne-le-moi); the negative imperative goes back to the normal order in front of the verb.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Rang 1", "me / te / se / nous / vous", TokenCategory.OBJECT, "1re et 2e personnes (COD ou COI) + 'se' réfléchi."),
          FormulaToken("Rang 2", "le / la / les / l'", TokenCategory.OBJECT, "COD de 3e personne."),
          FormulaToken("Rang 3", "lui / leur", TokenCategory.OBJECT, "COI de 3e personne (personnes uniquement)."),
          FormulaToken("Rang 4", "y", TokenCategory.MODIFIER, "Lieu ou complément introduit par 'à'."),
          FormulaToken("Rang 5", "en", TokenCategory.OBJECT, "Quantité ou complément introduit par 'de'."),
          FormulaToken("Verbe", "Verbe conjugué", TokenCategory.TARGET_VERB, "Ex : Il me le donne. / Je lui en parle.")
        ),
        diagramTitle = "Le Train des Pronoms (Avant le Verbe)",
        diagramDescription = "Les pronoms montent toujours dans les wagons dans cet ordre exact :",
        decisionSteps = listOf(
          DecisionStep(1, "S'agit-il d'un impératif affirmatif (Donne-le-moi) ?", "-> Ordre inversé avec traits d'union : Verbe - le/la/les - moi/toi/lui/nous/vous - y - en", "-> Ordre normal avant le verbe"),
          DecisionStep(2, "Avez-vous un pronom de rang 1 (me, te, se, nous, vous) ?", "-> Il se place TOUJOURS en première position (ex: Il ME le dit)", "-> Si uniquement 3e personne : le/la/les passe avant lui/leur (ex: Il LE LUI donne)"),
          DecisionStep(3, "Avez-vous 'y' ou 'en' ?", "-> Ils se placent TOUJOURS en dernier, juste avant le verbe ('y' avant 'en' si les deux sont présents)", "-> Ordre standard validé !")
        ),
        comparisonTableTitle = "Tableau des Combinaisons Fréquentes",
        comparisonHeaders = listOf("Type de combinaison", "Ordre exact", "Exemple type"),
        comparisonRows = listOf(
          listOf("1re/2e personne + COD", "me/te/nous/vous + le/la/les", "Tu me la prêtes ?"),
          listOf("COD 3e pers + COI 3e pers", "le/la/les + lui/leur", "Je le leur expliquerai demain."),
          listOf("COI + Pronom En", "lui/leur + en", "Elle lui en a acheté trois."),
          listOf("Pronom Y + Pronom En", "y + en", "Il y en a encore dans le frigo.")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Ce document ? Je vous l'enverrai par courriel dans l'après-midi.",
          highlightedSegment = "vous l'enverrai",
          englishSentence = "This document? I will send it to you by email this afternoon.",
          contextNote = "Pronom de politesse 'vous' (rang 1) suivi du COD 'l'' (rang 2)."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Ces secrets d'État, jamais il ne les leur eût confiés de son plein gré.",
          highlightedSegment = "les leur eût confiés",
          englishSentence = "These state secrets, never would he have entrusted them to them willingly.",
          contextNote = "Double pronom de 3e personne : 'les' (COD) avant 'leur' (COI)."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "File-moi-le tout de suite avant que je m'énerve !",
          highlightedSegment = "File-moi-le",
          englishSentence = "Hand it over to me right now before I get mad!",
          contextNote = "Ordre oral familier à l'impératif affirmatif (fréquent au lieu du classique 'File-le-moi')."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Si le client demande le devis révisé, transmettez-le-lui sans délai.",
          highlightedSegment = "transmettez-le-lui",
          englishSentence = "If the client asks for the revised quote, forward it to him without delay.",
          contextNote = "Impératif professionnel soutenu avec traits d'union réglementaires."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "La Transmission des Archives de Famille",
        fullTextFr = "Mon grand-père conservait de vieilles lettres de la Résistance. Un jour, il a décidé de me les transmettre. « Ces courriers, m'a-t-il dit, je te les confie car tu sauras en prendre soin. Ne les donne à personne d'autre, mais si ta sœur te les demande, montre-les-lui avec fierté. » Aujourd'hui, je les lui apporte chaque dimanche.",
        fullTextEn = "My grandfather kept old letters from the Resistance. One day, he decided to pass them on to me. 'These letters,' he told me, 'I entrust them to you because you will take good care of them. Do not give them to anyone else, but if your sister asks you for them, show them to her with pride.' Today, I bring them to her every Sunday.",
        annotations = listOf(
          InlineAnnotation(
            id = "pron_ann_1",
            targetPhrase = "me les transmettre",
            explanationFr = "Le pronom COI 1re personne 'me' (rang 1) précède impérativement le pronom COD 'les' (rang 2).",
            explanationEn = "The 1st person COI pronoun 'me' (rank 1) strictly precedes the direct object pronoun 'les' (rank 2).",
            whyItApplies = "Règle de hiérarchie des personnes avant le verbe.",
            commonPitfall = "Ne jamais dire 'les me transmettre' avant le verbe à l'infinitif."
          ),
          InlineAnnotation(
            id = "pron_ann_2",
            targetPhrase = "montre-les-lui",
            explanationFr = "À l'impératif affirmatif, l'ordre s'inverse : Verbe + COD (les) + COI (lui), reliés par des traits d'union.",
            explanationEn = "In the affirmative imperative, order flips: Verb + COD (les) + COI (lui), connected by hyphens.",
            whyItApplies = "Syntaxe de l'impératif affirmatif.",
            commonPitfall = "À l'impératif négatif, on retrouve l'ordre normal : 'Ne les lui montre pas !'"
          ),
          InlineAnnotation(
            id = "pron_ann_3",
            targetPhrase = "les lui apporte",
            explanationFr = "Pour deux pronoms de 3e personne avant le verbe, le COD direct (le/la/les) passe TOUJOURS avant le COI (lui/leur).",
            explanationEn = "For two 3rd person pronouns before the verb, direct object (le/la/les) always precedes indirect (lui/leur).",
            whyItApplies = "Priorité du COD de 3e personne sur le COI de 3e personne.",
            commonPitfall = "Piège classique des anglophones qui tendent à calquer 'I bring him them' -> 'Je lui les apporte' (INCORRECT)."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_pron_1",
          instructionFr = "Remplacez les compléments soulignés par les pronoms qui conviennent :",
          instructionEn = "Replace the underlined objects with the appropriate pronouns:",
          basePrompt = "Je donne la clé (COD) à Pierre (COI) -> Je _____ donne.",
          targetAnswer = "la lui",
          acceptedAnswers = listOf("la lui", "la lui "),
          hint = "COD 'la clé' = la ; COI 'à Pierre' = lui. Le COD 3e pers précède le COI 3e pers.",
          explanation = "Deux pronoms de 3e personne : l'ordre est 'le/la/les' puis 'lui/leur'."
        ),
        ProductionDrillItem(
          id = "prod_pron_2",
          instructionFr = "Répondez affirmativement en utilisant deux pronoms :",
          instructionEn = "Answer affirmatively using two pronouns:",
          basePrompt = "Tu me prêtes ton vélo ? -> Oui, je _____ prête.",
          targetAnswer = "te le",
          acceptedAnswers = listOf("te le", "te le "),
          hint = "COI 'à toi' = te (rang 1) ; COD 'ton vélo' = le (rang 2).",
          explanation = "Le pronom de 2e personne 'te' précède le COD de 3e personne 'le' : 'je te le prête'."
        ),
        ProductionDrillItem(
          id = "prod_pron_3",
          instructionFr = "Formulez à l'impératif affirmatif avec traits d'union :",
          instructionEn = "Formulate in the affirmative imperative with hyphens:",
          basePrompt = "Raconte cette histoire (COD) à tes enfants (COI) ! -> Raconte-_____ !",
          targetAnswer = "la-leur",
          acceptedAnswers = listOf("la-leur", "la leur"),
          hint = "À l'impératif affirmatif : Verbe - COD - COI.",
          explanation = "À l'impératif affirmatif, le COD 'la' précède le COI 'leur' reliés par des traits d'union : 'Raconte-la-leur !'"
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_pron_1",
          instructionFr = "Touchez le pronom mal ordonné dans cette phrase :",
          passage = "Si tu veux ce livre, je lui le donnerai dès demain matin.",
          tokens = listOf("Si", "tu", "veux", "ce", "livre,", "je", "lui", "le", "donnerai", "dès", "demain", "matin."),
          errorTokenIndex = 6,
          errorWord = "lui",
          correction = "le lui (ordre inversé)",
          ruleExplanation = "Entre deux pronoms de 3e personne, le COD 'le' passe OBLIGATOIREMENT avant le COI 'lui' : « je le lui donnerai » !"
        ),
        SpotErrorDrillItem(
          id = "spot_pron_2",
          instructionFr = "Repérez l'erreur de place du pronom à l'impératif négatif :",
          passage = "C'est un secret confidentiel, ne donne-le-lui pas sous aucun prétexte.",
          tokens = listOf("C'est", "un", "secret", "confidentiel,", "ne", "donne-le-lui", "pas", "sous", "aucun", "prétexte."),
          errorTokenIndex = 5,
          errorWord = "donne-le-lui",
          correction = "ne le lui donne pas",
          ruleExplanation = "À l'impératif NÉGATIF, les pronoms reviennent DEVANT le verbe selon l'ordre standard : « ne le lui donne pas » sans trait d'union !"
        )
      )
    ),

    // 4. L'EXPRESSION DE L'HYPOTHÈSE AVEC « SI »
    StructuredRule(
      id = "hypothese-si",
      categoryId = "cat-mode",
      categoryName = "Modes & Temps",
      level = "B2",
      titleFr = "L'Expression de l'Hypothèse avec « Si »",
      titleEn = "Conditionals & Hypothesis with 'Si'",
      summaryFr = "Le système des trois hypothèses avec 'si' structure toute la modalisation en français : le réel (présent + futur), le potentiel (imparfait + conditionnel présent), et l'irréel du passé (plus-que-parfait + conditionnel passé).",
      summaryEn = "The three-tier conditional system with 'si' organizes French hypothesis: real (present + future), hypothetical (imperfect + cond. pres.), and past counterfactual (pluperfect + cond. past).",
      pillar = GrammarPillar.VERB_SYSTEM,
      ruleExplanationEn = "Memorize it as three fixed tense pairs, never mix and match. Si + present pairs with a future ('if it's nice tomorrow, we'll go out'). Si + imperfect pairs with a present conditional ('if I had time, I would learn violin' — imagined, not real). Si + pluperfect pairs with a past conditional ('if you had told me, I would have come' — a regret about something that didn't happen). The one absolute rule across all three: never put a future or a conditional form directly after 'si' itself.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Si", "Conjonction d'hypothèse", TokenCategory.CONNECTOR, "Interdiction absolue du futur ou du conditionnel juste après 'si' !"),
          FormulaToken("Proposition subordonnée", "Présent / Imparfait / Plus-que-parfait", TokenCategory.TRIGGER, "Définit le degré de probabilité temporelle."),
          FormulaToken("Proposition principale", "Futur / Cond. Présent / Cond. Passé", TokenCategory.TARGET_VERB, "Exprime la conséquence de la condition.")
        ),
        diagramTitle = "La Matrice Temporelle des 3 Hypothèses",
        diagramDescription = "Associez toujours le bon couple de temps sans jamais mettre de conditionnel après 'si' :",
        decisionSteps = listOf(
          DecisionStep(1, "L'action est-elle réalisable dans le présent ou l'avenir ?", "-> SI + PRÉSENT ===> FUTUR SIMPLE (ex: S'il fait beau demain, nous sortirons)", "-> Hypothèse imaginaire ou passée"),
          DecisionStep(2, "L'action est-elle imaginaire ou peu probable dans le présent ?", "-> SI + IMPARFAIT ===> CONDITIONNEL PRÉSENT (ex: Si j'avais le temps, j'apprendrais le violon)", "-> L'événement est révolu dans le passé"),
          DecisionStep(3, "L'action appartient-elle au passé et ne s'est pas produite (regret / reproche) ?", "-> SI + PLUS-QUE-PARFAIT ===> CONDITIONNEL PASSÉ (ex: Si tu m'avais prévenu, je serais venu)", "-> Revoir l'étape 1 ou 2")
        ),
        comparisonTableTitle = "Tableau des 3 Paliers d'Hypothèse",
        comparisonHeaders = listOf("Type d'hypothèse", "Après 'SI'", "Dans la principale"),
        comparisonRows = listOf(
          listOf("1. Réalisable / Certaine", "Présent de l'indicatif", "Futur simple (ou présent / impératif)"),
          listOf("2. Potentielle / Imaginaire", "Imparfait de l'indicatif", "Conditionnel présent"),
          listOf("3. Irréel du passé (Regret)", "Plus-que-parfait", "Conditionnel passé"),
          listOf("RÈGLE D'OR INTERDITE", "JAMAIS de futur ni de conditionnel après 'si' !", "« Les scies n'aiment pas les raies » (Si + -rai = INTERDIT)")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Si tu as besoin d'aide, appelle-moi sans hésiter.",
          highlightedSegment = "appelle-moi",
          englishSentence = "If you need help, call me without hesitation.",
          contextNote = "Hypothèse de niveau 1 combinant Si + présent avec l'impératif dans la principale."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Eût-il consenti à nos requêtes, nous aurions épargné bien des tracas.",
          highlightedSegment = "Eût-il consenti",
          englishSentence = "Had he consented to our requests, we would have spared much trouble.",
          contextNote = "Tournure littéraire avec subjonctif plus-que-parfait inversé remplaçant 'Si'."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "Si j'aurais su, j'aurais pas v'nu ! (Célèbre faute populaire de La Guerre des boutons)",
          highlightedSegment = "Si j'aurais su",
          englishSentence = "If I'd've known, I wouldn't've come!",
          contextNote = "Faute populaire typique employant le conditionnel après 'si'. La forme correcte est : 'Si j'avais su'."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Si les conditions de marché venaient à se dégrader, nous réajusterions nos prévisions budgétaires.",
          highlightedSegment = "venaient à se dégrader",
          englishSentence = "Should market conditions deteriorate, we would readjust our budget forecasts.",
          contextNote = "Structure élégante avec 'venir à + infinitif' pour modéliser une éventualité professionnelle."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Les Regrets de l'Archéologue",
        fullTextFr = "Devant les ruines de la cité antique, le professeur songeait aux opportunités manquées. « Si nous avions obtenu les financements nécessaires l'an dernier, nous aurions découvert ce temple bien avant les pilleurs. Aujourd'hui, si nous avions encore des ressources, nous pourrions restaurer ces fresques inestimables. Et si le ministère débloque les fonds demain, nous reprendrons les fouilles immédiatement. »",
        fullTextEn = "In front of the ruins of the ancient city, the professor reflected on missed opportunities. 'If we had obtained the necessary funding last year, we would have discovered this temple well before the looters. Today, if we still had resources, we could restore these priceless frescoes. And if the ministry unlocks the funds tomorrow, we will resume excavations immediately.'",
        annotations = listOf(
          InlineAnnotation(
            id = "hyp_ann_1",
            targetPhrase = "aurions découvert",
            explanationFr = "Irréel du passé : 'Si nous avions obtenu' (plus-que-parfait) entraîne le conditionnel passé 'nous aurions découvert'.",
            explanationEn = "Past counterfactual: 'Si nous avions obtenu' (pluperfect) triggers the conditional past.",
            whyItApplies = "Événement passé impossible à changer.",
            commonPitfall = "Ne jamais dire 'Si nous aurions obtenu'."
          ),
          InlineAnnotation(
            id = "hyp_ann_2",
            targetPhrase = "nous pourrions restaurer",
            explanationFr = "Potentiel du présent : 'Si nous avions' (imparfait) entraîne le conditionnel présent 'nous pourrions'.",
            explanationEn = "Present hypothetical: 'Si nous avions' (imperfect) leads to present conditional.",
            whyItApplies = "Hypothèse imaginaire sur la situation actuelle.",
            commonPitfall = "Assurez-vous que le verbe après 'si' reste à l'imparfait."
          ),
          InlineAnnotation(
            id = "hyp_ann_3",
            targetPhrase = "nous reprendrons",
            explanationFr = "Hypothèse réalisable dans l'avenir : 'Si le ministère débloque' (présent) entraîne le futur simple 'nous reprendrons'.",
            explanationEn = "Feasible future conditional: 'Si... débloque' (present) leads to future indicative.",
            whyItApplies = "Condition réelle projetée dans le futur.",
            commonPitfall = "Pas de subjonctif après 'si'."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_hyp_1",
          instructionFr = "Complétez la principale au conditionnel présent (hypothèse imaginaire) :",
          instructionEn = "Complete the main clause in the present conditional (hypothetical):",
          basePrompt = "Si j'avais plus de temps libre, je _____ (voyager) à travers le monde.",
          targetAnswer = "voyagerais",
          acceptedAnswers = listOf("voyagerais"),
          hint = "Si + imparfait (avais) appelle le conditionnel présent : radical futur + terminaison imparfait (-ais).",
          explanation = "La structure Si + imparfait entraîne le conditionnel présent : voyager- + -ais = voyagerais."
        ),
        ProductionDrillItem(
          id = "prod_hyp_2",
          instructionFr = "Conjuguez la subordonnée avec 'si' (attention à l'interdiction du conditionnel !) :",
          instructionEn = "Conjugate the subordinate clause with 'si' (beware of the forbidden conditional!):",
          basePrompt = "Si vous _____ (savoir) la vérité, vous ne parleriez pas ainsi.",
          targetAnswer = "saviez",
          acceptedAnswers = listOf("saviez"),
          hint = "La principale est au conditionnel (parleriez). Après 'si', on emploie l'imparfait.",
          explanation = "On emploie obligatoirement l'imparfait après 'si' pour exprimer une hypothèse : 'saviez'."
        ),
        ProductionDrillItem(
          id = "prod_hyp_3",
          instructionFr = "Complétez au conditionnel passé (irréel du passé / regret) :",
          instructionEn = "Complete in the conditional past (past counterfactual):",
          basePrompt = "Si tu étais venu plus tôt, nous _____ (manger) ensemble.",
          targetAnswer = "aurions mangé",
          acceptedAnswers = listOf("aurions mangé"),
          hint = "Auxiliaire avoir au conditionnel présent (aurions) + participe passé (mangé).",
          explanation = "Si + plus-que-parfait (étais venu) entraîne le conditionnel passé : 'aurions mangé'."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_hyp_1",
          instructionFr = "Touchez le verbe erroné dans cette hypothèse :",
          passage = "Si tu viendrais me voir ce week-end, nous pourrions visiter le château.",
          tokens = listOf("Si", "tu", "viendrais", "me", "voir", "ce", "week-end,", "nous", "pourrions", "visiter", "le", "château."),
          errorTokenIndex = 2,
          errorWord = "viendrais",
          correction = "venais",
          ruleExplanation = "Règle absolue : JAMAIS de conditionnel directement après « si » ! On doit dire : « Si tu venais » (imparfait) !"
        ),
        SpotErrorDrillItem(
          id = "spot_hyp_2",
          instructionFr = "Trouvez l'erreur de concordance des temps :",
          passage = "S'il fait beau demain après-midi, nous irions nous baigner au lac.",
          tokens = listOf("S'il", "fait", "beau", "demain", "après-midi,", "nous", "irions", "nous", "baigner", "au", "lac."),
          errorTokenIndex = 6,
          errorWord = "irions",
          correction = "irons",
          ruleExplanation = "Avec « Si + présent » (s'il fait beau), la proposition principale doit être au FUTUR SIMPLE (nous irons) ou au présent, pas au conditionnel !"
        )
      )
    ),

    // 5. LE PASSÉ COMPOSÉ (Mental Model Triad, volet 1/2 : "ce qui s'est passé")
    StructuredRule(
      id = "passe-compose",
      categoryId = "cat-mode",
      categoryName = "Modes & Temps",
      level = "A2",
      titleFr = "Le Passé Composé : Ce Qui S'est Passé",
      titleEn = "Passé Composé: What Happened",
      summaryFr = "Le passé composé raconte un événement précis et achevé : une action ponctuelle qui a eu lieu, avec un début et une fin, et qui fait avancer le récit.",
      summaryEn = "The passé composé narrates a specific, completed event: a punctual action that took place, with a beginning and an end, and that moves the story forward.",
      pillar = GrammarPillar.VERB_SYSTEM,
      ruleExplanationEn = "Don't learn this as 'a past tense' in isolation — learn it paired with the imparfait. The passé composé is for what HAPPENED: a single, completed event you could point to on a timeline (hier, il a plu / soudain, elle est partie). It's the foreground action, the thing that moves the story forward. If the sentence answers 'what happened next?', it's passé composé.",
      contrastGroupId = "triad-pc-imparfait",
      contrastRoleLabelFr = "Ce qui s'est passé",
      contrastRoleLabelEn = "What happened",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Sujet", "Qui a fait l'action", TokenCategory.SUBJECT, "Ex : il, elle, nous..."),
          FormulaToken("Auxiliaire", "Avoir / Être au présent", TokenCategory.TRIGGER, "ai, as, a, suis, es, est..."),
          FormulaToken("Participe passé", "Verbe conjugué au participe", TokenCategory.TARGET_VERB, "Ex : mangé, parti(e), fini...")
        ),
        diagramTitle = "Repérer le Premier Plan de l'Histoire",
        diagramDescription = "Le passé composé fait avancer le récit : il répond à « et ensuite, qu'est-ce qui s'est passé ? »",
        decisionSteps = listOf(
          DecisionStep(1, "L'action a-t-elle un début et une fin identifiables, comme un événement isolé ?", "-> PASSÉ COMPOSÉ (ex : Hier, il a plu toute la journée)", "-> Vérifiez si c'est plutôt un décor déjà en cours"),
          DecisionStep(2, "Y a-t-il un marqueur de temps ponctuel (hier, soudain, un jour, à midi, tout à coup) ?", "-> PASSÉ COMPOSÉ", "-> Marqueur d'habitude (souvent, tous les jours) -> voir IMPARFAIT"),
          DecisionStep(3, "La phrase répond-elle à « et ensuite ? » dans un récit ?", "-> PASSÉ COMPOSÉ : l'action fait progresser l'histoire", "-> La phrase décrit plutôt le décor -> IMPARFAIT")
        ),
        comparisonTableTitle = "Le Test du Cadre : Passé Composé vs Imparfait",
        comparisonHeaders = listOf("Passé composé (premier plan)", "Imparfait (arrière-plan)", "Règle clé"),
        comparisonRows = listOf(
          listOf("Il a plu quand je suis sorti.", "Il pleuvait quand je suis sorti.", "Action ponctuelle vs décor déjà en cours"),
          listOf("Elle a téléphoné à 8h.", "Elle téléphonait tous les soirs à 8h.", "Événement unique vs habitude répétée"),
          listOf("Soudain, la porte s'est ouverte.", "La porte était ouverte depuis le matin.", "Rupture ponctuelle vs état continu")
        ),
        diagramDescriptionEn = "The passé composé moves the story forward: it answers 'and then what happened?'"
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Hier soir, nous avons dîné dans un petit restaurant du quartier.",
          highlightedSegment = "avons dîné",
          englishSentence = "Last night, we had dinner at a small restaurant in the neighborhood.",
          contextNote = "Événement ponctuel et achevé, daté par 'hier soir'."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "À peine eut-il franchi le seuil qu'un silence pesant s'installa.",
          highlightedSegment = "eut-il franchi",
          englishSentence = "No sooner had he crossed the threshold than a heavy silence set in.",
          contextNote = "Registre littéraire : passé antérieur à la place du passé composé, même logique d'action ponctuelle."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "J'ai raté mon bus, du coup j'suis arrivé en retard !",
          highlightedSegment = "j'suis arrivé",
          englishSentence = "I missed my bus, so I showed up late!",
          contextNote = "Élision orale de 'je suis' en 'j'suis', très fréquente à l'oral relâché."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "L'équipe a livré la version finale du rapport ce matin à 9h.",
          highlightedSegment = "a livré",
          englishSentence = "The team delivered the final version of the report this morning at 9am.",
          contextNote = "Compte-rendu factuel d'un jalon de projet, daté précisément."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Un Incident sur le Chantier",
        fullTextFr = "Ce matin, l'équipe a commencé les travaux comme prévu. Vers 10 heures, une grue a heurté un échafaudage voisin. Heureusement, personne n'a été blessé, mais le chef de chantier a immédiatement arrêté les opérations. Les ouvriers ont sécurisé la zone et l'ingénieur est venu inspecter les dégâts dans l'heure qui a suivi.",
        fullTextEn = "This morning, the team started work as planned. Around 10am, a crane hit a nearby scaffolding. Fortunately, no one was hurt, but the site manager immediately stopped operations. The workers secured the area and the engineer came to inspect the damage within the hour that followed.",
        annotations = listOf(
          InlineAnnotation(
            id = "pc_ann_1",
            targetPhrase = "a heurté",
            explanationFr = "Un événement soudain et ponctuel, daté ('vers 10 heures') : c'est le cœur de l'action, au passé composé.",
            explanationEn = "A sudden, punctual event, time-stamped ('around 10am'): the core action, in the passé composé.",
            whyItApplies = "Événement isolé qui fait avancer le récit.",
            commonPitfall = "Ne pas confondre avec un état continu, qui prendrait l'imparfait."
          ),
          InlineAnnotation(
            id = "pc_ann_2",
            targetPhrase = "a arrêté",
            explanationFr = "Décision et action immédiates du chef de chantier suite à l'incident : nouvelle action ponctuelle qui fait progresser l'histoire.",
            explanationEn = "The site manager's immediate decision and action after the incident: another punctual action moving the story forward.",
            whyItApplies = "Réaction ponctuelle à un événement.",
            commonPitfall = "Un enchaînement de passés composés = une suite d'événements, pas une description."
          ),
          InlineAnnotation(
            id = "pc_ann_3",
            targetPhrase = "est venu",
            explanationFr = "Verbe de mouvement avec l'auxiliaire être : l'ingénieur arrive, un fait ponctuel et achevé.",
            explanationEn = "A motion verb with auxiliary être: the engineer arrives — a punctual, completed fact.",
            whyItApplies = "Verbe de déplacement conjugué avec être.",
            commonPitfall = "Bien choisir l'auxiliaire être pour les verbes de mouvement de la liste 'DR & MRS VANDERTRAMP'."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_pc_1",
          instructionFr = "Conjuguez le verbe au passé composé (événement ponctuel) :",
          instructionEn = "Conjugate the verb in the passé composé (punctual event):",
          basePrompt = "Hier après-midi, il _____ (pleuvoir) très fort pendant vingt minutes.",
          targetAnswer = "a plu",
          acceptedAnswers = listOf("a plu"),
          hint = "Pleuvoir se conjugue avec avoir : il a plu.",
          explanation = "Un épisode de pluie limité dans le temps ('pendant vingt minutes') est un événement ponctuel : passé composé."
        ),
        ProductionDrillItem(
          id = "prod_pc_2",
          instructionFr = "Conjuguez le verbe de mouvement avec le bon auxiliaire :",
          instructionEn = "Conjugate the motion verb with the correct auxiliary:",
          basePrompt = "Elle _____ (arriver) à la gare pile à l'heure prévue.",
          targetAnswer = "est arrivée",
          acceptedAnswers = listOf("est arrivée"),
          hint = "Arriver se conjugue avec être et s'accorde avec le sujet féminin.",
          explanation = "Verbe de mouvement (arriver) avec être ; le sujet 'elle' impose l'accord au féminin : arrivée."
        ),
        ProductionDrillItem(
          id = "prod_pc_3",
          instructionFr = "Conjuguez pour marquer une rupture soudaine dans le récit :",
          instructionEn = "Conjugate to mark a sudden break in the narrative:",
          basePrompt = "Tout à coup, les lumières _____ (s'éteindre) sans prévenir.",
          targetAnswer = "se sont éteintes",
          acceptedAnswers = listOf("se sont éteintes"),
          hint = "S'éteindre est pronominal, auxiliaire être, sujet féminin pluriel 'les lumières'.",
          explanation = "'Tout à coup' signale une rupture ponctuelle : passé composé, verbe pronominal accordé avec le sujet."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_pc_1",
          instructionFr = "Touchez le verbe qui devrait être au passé composé, pas à l'imparfait :",
          passage = "Soudain, la voiture freinait brutalement au milieu du carrefour.",
          tokens = listOf("Soudain,", "la", "voiture", "freinait", "brutalement", "au", "milieu", "du", "carrefour."),
          errorTokenIndex = 3,
          errorWord = "freinait",
          correction = "a freiné",
          ruleExplanation = "'Soudain' marque une action ponctuelle et soudaine : c'est le passé composé (a freiné), pas l'imparfait qui décrirait un décor continu."
        ),
        SpotErrorDrillItem(
          id = "spot_pc_2",
          instructionFr = "Repérez l'auxiliaire mal choisi pour ce verbe de mouvement :",
          passage = "Mes parents ont sortis du cinéma juste avant la fin du film.",
          tokens = listOf("Mes", "parents", "ont", "sortis", "du", "cinéma", "juste", "avant", "la", "fin", "du", "film."),
          errorTokenIndex = 2,
          errorWord = "ont",
          correction = "sont",
          ruleExplanation = "'Sortir' (sens intransitif de mouvement) se conjugue avec ÊTRE : 'mes parents SONT sortis', pas avec avoir."
        )
      )
    ),

    // 6. L'IMPARFAIT (Mental Model Triad, volet 2/2 : "ce qui était déjà en train de se passer")
    StructuredRule(
      id = "imparfait",
      categoryId = "cat-mode",
      categoryName = "Modes & Temps",
      level = "A2",
      titleFr = "L'Imparfait : Ce Qui Était Déjà en Train de Se Passer",
      titleEn = "Imparfait: What Was Already Happening",
      summaryFr = "L'imparfait décrit un état, une habitude ou une action en cours dans le passé, sans limite de début ni de fin précise : c'est le décor, pas l'événement.",
      summaryEn = "The imparfait describes a state, a habit, or an ongoing action in the past, with no clear start or end: it's the backdrop, not the event.",
      pillar = GrammarPillar.VERB_SYSTEM,
      ruleExplanationEn = "This is the mirror image of the passé composé — always learn them as a pair, never in isolation. The imparfait is for what was ALREADY going on: the backdrop, the ongoing state, the habit, the description ('il faisait beau', 'elle habitait à Lyon', 'chaque été, on allait à la mer'). If the sentence answers 'what was the scene / what used to happen?', it's imparfait. When both appear in the same sentence, the imparfait sets the scene and the passé composé is the event that interrupts it: 'Je dormais (imparfait, backdrop) quand le téléphone a sonné (passé composé, the interrupting event).'",
      contrastGroupId = "triad-pc-imparfait",
      contrastRoleLabelFr = "Ce qui était déjà en train de se passer",
      contrastRoleLabelEn = "What was already happening",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Sujet", "Qui est dans cet état", TokenCategory.SUBJECT, "Ex : je, tu, ils..."),
          FormulaToken("Radical 'nous' au présent", "Base de conjugaison", TokenCategory.TRIGGER, "Ex : nous parl-ons -> parl-"),
          FormulaToken("Terminaison imparfait", "-ais, -ais, -ait, -ions, -iez, -aient", TokenCategory.TARGET_VERB, "Ex : je parlais, ils parlaient")
        ),
        diagramTitle = "Repérer l'Arrière-Plan de l'Histoire",
        diagramDescription = "L'imparfait plante le décor : il répond à « comment c'était ? » ou « qu'est-ce qui était déjà en cours ? »",
        decisionSteps = listOf(
          DecisionStep(1, "L'action décrit-elle un état ou un décor, sans début ni fin précis ?", "-> IMPARFAIT (ex : Il faisait nuit et la ville dormait)", "-> Vérifiez si c'est un événement isolé"),
          DecisionStep(2, "Y a-t-il un marqueur d'habitude (souvent, chaque jour, autrefois, d'habitude) ?", "-> IMPARFAIT", "-> Marqueur ponctuel (hier, soudain) -> voir PASSÉ COMPOSÉ"),
          DecisionStep(3, "Cette action est-elle interrompue par un événement soudain dans la même phrase ?", "-> IMPARFAIT pour le décor + PASSÉ COMPOSÉ pour l'interruption (ex : Je lisais quand elle est entrée)", "-> Un seul verbe -> revoir les étapes 1 et 2")
        ),
        comparisonTableTitle = "Le Test du Cadre : Imparfait vs Passé Composé",
        comparisonHeaders = listOf("Imparfait (arrière-plan)", "Passé composé (premier plan)", "Règle clé"),
        comparisonRows = listOf(
          listOf("Il pleuvait quand je suis sorti.", "Il a plu quand je suis sorti.", "Décor déjà en cours vs action ponctuelle"),
          listOf("Elle téléphonait tous les soirs à 8h.", "Elle a téléphoné à 8h.", "Habitude répétée vs événement unique"),
          listOf("La porte était ouverte depuis le matin.", "Soudain, la porte s'est ouverte.", "État continu vs rupture ponctuelle")
        ),
        diagramDescriptionEn = "The imparfait sets the scene: it answers 'what was already going on?'"
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Quand j'étais petit, j'habitais dans un village au bord de la mer.",
          highlightedSegment = "habitais",
          englishSentence = "When I was little, I lived in a village by the sea.",
          contextNote = "État durable de l'enfance, sans début ni fin précis."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "La lumière déclinante baignait la façade d'une teinte dorée.",
          highlightedSegment = "baignait",
          englishSentence = "The fading light bathed the façade in a golden hue.",
          contextNote = "Description littéraire d'un décor, typique du style narratif soutenu."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "Avant, on sortait tout le temps le week-end, mais plus maintenant.",
          highlightedSegment = "on sortait",
          englishSentence = "We used to go out all the time on weekends, but not anymore.",
          contextNote = "Habitude passée révolue, ton oral décontracté avec 'on' pour 'nous'."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "À l'époque, l'entreprise ne disposait pas encore des outils numériques actuels.",
          highlightedSegment = "ne disposait pas",
          englishSentence = "At the time, the company did not yet have today's digital tools.",
          contextNote = "Description d'une situation organisationnelle antérieure, dans un rapport ou une présentation."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Le Village de mon Enfance",
        fullTextFr = "Quand j'étais enfant, le village était très calme. Les rues étaient étroites et les voisins se connaissaient tous. Chaque matin, le boulanger ouvrait sa boutique avant l'aube et une odeur de pain chaud flottait dans l'air. Je jouais souvent avec mes amis sur la place, pendant que les anciens discutaient assis sur les bancs.",
        fullTextEn = "When I was a child, the village was very quiet. The streets were narrow and the neighbors all knew each other. Every morning, the baker opened his shop before dawn and a smell of warm bread floated in the air. I often played with my friends in the square, while the elders chatted sitting on the benches.",
        annotations = listOf(
          InlineAnnotation(
            id = "imp_ann_1",
            targetPhrase = "était très calme",
            explanationFr = "Description d'un état durable du village dans le passé, sans limite temporelle précise : imparfait.",
            explanationEn = "Description of a lasting state of the village in the past, with no precise time boundary: imparfait.",
            whyItApplies = "État descriptif sans début ni fin.",
            commonPitfall = "Une description de décor n'utilise presque jamais le passé composé."
          ),
          InlineAnnotation(
            id = "imp_ann_2",
            targetPhrase = "ouvrait sa boutique",
            explanationFr = "'Chaque matin' est un marqueur d'habitude répétée dans le passé : imparfait, jamais passé composé.",
            explanationEn = "'Chaque matin' is a marker of a repeated past habit: imparfait, never passé composé.",
            whyItApplies = "Habitude répétée signalée par 'chaque matin'.",
            commonPitfall = "Ne confondez pas une habitude répétée avec un événement unique daté."
          ),
          InlineAnnotation(
            id = "imp_ann_3",
            targetPhrase = "je jouais souvent",
            explanationFr = "'Souvent' indique une action habituelle et répétée dans le passé : le narrateur décrit son enfance, pas un épisode isolé.",
            explanationEn = "'Souvent' indicates a habitual, repeated past action: the narrator describes childhood, not one isolated episode.",
            whyItApplies = "Adverbe de fréquence typique de l'imparfait.",
            commonPitfall = "Les adverbes 'souvent', 'toujours', 'd'habitude' sont de forts indices de l'imparfait."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_imp_1",
          instructionFr = "Conjuguez le verbe pour décrire une habitude passée :",
          instructionEn = "Conjugate the verb to describe a past habit:",
          basePrompt = "Quand nous étions étudiants, nous _____ (manger) toujours à la même cafétéria.",
          targetAnswer = "mangions",
          acceptedAnswers = listOf("mangions"),
          hint = "Radical de 'nous mangeons' au présent (mange-) + terminaison -ions.",
          explanation = "'Toujours' marque une habitude répétée : imparfait, radical mange- + -ions = mangions."
        ),
        ProductionDrillItem(
          id = "prod_imp_2",
          instructionFr = "Conjuguez pour décrire le décor interrompu par un événement :",
          instructionEn = "Conjugate to describe the backdrop interrupted by an event:",
          basePrompt = "Il _____ (faire) très beau quand l'orage a soudainement éclaté.",
          targetAnswer = "faisait",
          acceptedAnswers = listOf("faisait"),
          hint = "Le décor météo continu se met à l'imparfait ; l'événement soudain reste au passé composé.",
          explanation = "'Il faisait beau' plante le décor à l'imparfait ; 'a éclaté' est l'événement ponctuel qui l'interrompt."
        ),
        ProductionDrillItem(
          id = "prod_imp_3",
          instructionFr = "Conjuguez le verbe être à l'imparfait pour une description :",
          instructionEn = "Conjugate 'être' in the imparfait for a description:",
          basePrompt = "À cette époque, la maison _____ (être) beaucoup plus petite qu'aujourd'hui.",
          targetAnswer = "était",
          acceptedAnswers = listOf("était"),
          hint = "Être a un radical irrégulier à l'imparfait : ét-.",
          explanation = "'Être' garde le radical irrégulier 'ét-' à l'imparfait : j'étais, il était, nous étions."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_imp_1",
          instructionFr = "Touchez le verbe qui décrit une habitude mais qui est au mauvais temps :",
          passage = "Chaque été, ma famille est allée camper au bord du lac pendant deux semaines.",
          tokens = listOf("Chaque", "été,", "ma", "famille", "est", "allée", "camper", "au", "bord", "du", "lac", "pendant", "deux", "semaines."),
          errorTokenIndex = 4,
          errorWord = "est",
          correction = "allait",
          ruleExplanation = "'Chaque été' marque une habitude répétée sur plusieurs années : il faut l'imparfait ('ma famille allait camper'), pas le passé composé."
        ),
        SpotErrorDrillItem(
          id = "spot_imp_2",
          instructionFr = "Repérez le verbe de description au mauvais temps dans ce décor :",
          passage = "Il était minuit et les rues du village ont été désertes et silencieuses.",
          tokens = listOf("Il", "était", "minuit", "et", "les", "rues", "du", "village", "ont", "été", "désertes", "et", "silencieuses."),
          errorTokenIndex = 8,
          errorWord = "ont",
          correction = "étaient",
          ruleExplanation = "La description d'un état sans limite précise ('les rues désertes et silencieuses') se met à l'imparfait : 'étaient', pas au passé composé."
        )
      )
    ),

    // 7. LE GENRE DES NOMS (A1)
    StructuredRule(
      id = "genre-noms",
      categoryId = "cat-nom",
      categoryName = "Groupe Nominal",
      level = "A1",
      titleFr = "Le Genre des Noms : Masculin ou Féminin ?",
      titleEn = "Noun Gender: Masculine or Feminine?",
      summaryFr = "Chaque nom français est masculin ou féminin, sans neutre possible. Il n'y a pas de règle absolue, mais la terminaison du nom est un indice fiable dans la majorité des cas.",
      summaryEn = "Every French noun is either masculine or feminine — there's no neuter. There's no absolute rule, but a noun's ending is a reliable clue most of the time.",
      pillar = GrammarPillar.AGREEMENT_ENGINE,
      ruleExplanationEn = "Don't try to memorize a master list of rules — memorize the noun WITH its article from day one. 'Pain' alone tells you nothing; 'le pain' locks the gender into memory immediately, which is far easier than correcting a wrong guess later. That said, certain endings are strong statistical clues: -tion/-sion (la solution, la télévision) is almost always feminine, -isme (le tourisme) is almost always masculine. Many everyday short words (le pain, la table, le corps, la mer) carry no clue at all — their gender isn't an exception to a rule, there was never a rule to break; it's pure memorization, best done article-first.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Nom", "Le mot lui-même", TokenCategory.OBJECT, "Ex : voyage, salade, tourisme..."),
          FormulaToken("Terminaison", "Les dernières lettres du nom", TokenCategory.TRIGGER, "Indice statistique, pas une loi absolue.", explanationEn = "A statistical clue, not an absolute law."),
          FormulaToken("Article", "le / la / l' / un / une", TokenCategory.TARGET_VERB, "À mémoriser AVEC le nom, jamais séparément.")
        ),
        diagramTitle = "Le Réflexe de l'Article Collé",
        diagramDescription = "Avant de deviner un genre, posez-vous ces questions dans l'ordre :",
        decisionSteps = listOf(
          DecisionStep(1, "Le nom désigne-t-il une personne ou un animal dont le sexe est connu ?", "-> Le genre grammatical suit généralement le sexe réel (un chien / une chienne)", "-> Passez à l'étape 2"),
          DecisionStep(2, "La terminaison du nom correspond-elle à un indice fiable (-tion, -isme, -age, -ette...) ?", "-> Utilisez l'indice de terminaison (voir le tableau)", "-> Le mot est probablement trop court pour un indice : à mémoriser par cœur avec son article"),
          DecisionStep(3, "Avez-vous un doute persistant ?", "-> Mémorisez toujours 'le/la + nom' ensemble, jamais le nom seul", "-> Vérifiez dans un dictionnaire et notez l'article immédiatement")
        ),
        comparisonTableTitle = "Terminaisons Indicatives Fréquentes",
        comparisonHeaders = listOf("Terminaison masculine", "Exemple", "Terminaison féminine", "Exemple"),
        comparisonRows = listOf(
          listOf("-age", "le voyage", "-ade", "la salade"),
          listOf("-isme", "le tourisme", "-tion / -sion", "la nation, la télévision"),
          listOf("-ment", "le logement", "-ette", "la baguette"),
          listOf("-eau", "le bureau", "-ée", "la journée"),
          listOf("-teur (agent, machine)", "le moteur", "-trice (agent féminin)", "la directrice")
        ),
        diagramDescriptionEn = "Before guessing a gender, ask yourself these questions in order:"
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "J'ai acheté une nouvelle table et un joli miroir pour le salon.",
          highlightedSegment = "une nouvelle table",
          englishSentence = "I bought a new table and a nice mirror for the living room.",
          contextNote = "'Table' est un nom court sans indice de terminaison : féminin à mémoriser par cœur."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "La nation tout entière a suivi la retransmission de l'événement avec émotion.",
          highlightedSegment = "La nation",
          englishSentence = "The entire nation followed the broadcast of the event with emotion.",
          contextNote = "'-tion' est l'une des terminaisons féminines les plus fiables de toute la langue."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "T'as vu le bordel dans la chambre ? C'est n'importe quoi !",
          highlightedSegment = "le bordel",
          englishSentence = "Did you see the mess in the room? It's ridiculous!",
          contextNote = "Vocabulaire familier, mais le réflexe article + nom reste identique à tous les registres."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Le tourisme d'affaires représente une part croissante de nos revenus annuels.",
          highlightedSegment = "Le tourisme",
          englishSentence = "Business tourism represents a growing share of our annual revenue.",
          contextNote = "'-isme' est une terminaison masculine quasi systématique en français."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Une Chambre à Louer",
        fullTextFr = "L'appartement a une grande chambre avec une fenêtre qui donne sur la rue. Il y a un lit, une table, une chaise et un petit bureau dans le coin. Le loyer comprend le chauffage, mais pas l'électricité. La propriétaire habite au rez-de-chaussée, juste en dessous.",
        fullTextEn = "The apartment has a large bedroom with a window overlooking the street. There is a bed, a table, a chair and a small desk in the corner. The rent includes heating, but not electricity. The landlady lives on the ground floor, just below.",
        annotations = listOf(
          InlineAnnotation(
            id = "genre_ann_1",
            targetPhrase = "une fenêtre",
            explanationFr = "'Fenêtre' se termine en -être/-etre, sans indice fort, mais reste féminin : à mémoriser tel quel.",
            explanationEn = "'Fenêtre' has no strong ending clue but is feminine: memorize it as-is.",
            whyItApplies = "Nom sans indice de terminaison fiable.",
            commonPitfall = "Ne confondez pas avec des noms en '-tre' masculins comme 'le théâtre' ou 'le titre' : il n'y a pas de règle sur '-tre' seul."
          ),
          InlineAnnotation(
            id = "genre_ann_2",
            targetPhrase = "un bureau",
            explanationFr = "'-eau' est une terminaison masculine très fiable (le bureau, le bateau, le chapeau).",
            explanationEn = "'-eau' is a very reliable masculine ending (le bureau, le bateau, le chapeau).",
            whyItApplies = "Terminaison masculine typique.",
            commonPitfall = "Exception fréquente : 'l'eau' (l'eau, de l'eau) est féminin malgré la terminaison '-eau'."
          ),
          InlineAnnotation(
            id = "genre_ann_3",
            targetPhrase = "La propriétaire",
            explanationFr = "'-aire' se termine pareillement au masculin et au féminin ; ici le sens (une femme) impose l'article féminin 'la'.",
            explanationEn = "'-aire' looks identical for both genders; here the meaning (a woman) forces the feminine article 'la'.",
            whyItApplies = "Nom épicène : la terminaison ne tranche pas, le sens si.",
            commonPitfall = "'Le/la propriétaire', 'le/la secrétaire' : seul l'article change selon le sexe de la personne."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_genre_1",
          instructionFr = "Complétez avec l'article défini correct (le / la / l') :",
          instructionEn = "Complete with the correct definite article (le / la / l'):",
          basePrompt = "_____ télévision est allumée dans le salon depuis ce matin.",
          targetAnswer = "La",
          acceptedAnswers = listOf("La", "la"),
          hint = "'-vision' fait partie de la famille '-sion', terminaison féminine très fiable.",
          explanation = "'-sion' est l'une des terminaisons féminines les plus fiables du français : la télévision, la décision, la passion."
        ),
        ProductionDrillItem(
          id = "prod_genre_2",
          instructionFr = "Complétez avec l'article indéfini correct (un / une) :",
          instructionEn = "Complete with the correct indefinite article (un / une):",
          basePrompt = "Il a ouvert _____ journal pour lire les nouvelles du jour.",
          targetAnswer = "un",
          acceptedAnswers = listOf("un"),
          hint = "'-al' est une terminaison masculine fréquente : le journal, le cheval, le signal.",
          explanation = "'-al' est un indice masculin fiable (avec quelques exceptions à apprendre séparément)."
        ),
        ProductionDrillItem(
          id = "prod_genre_3",
          instructionFr = "Complétez avec l'article correct pour ce nom court sans indice :",
          instructionEn = "Complete with the correct article for this short, clue-less noun:",
          basePrompt = "Je n'ai pas mis assez de sel dans _____ soupe.",
          targetAnswer = "la",
          acceptedAnswers = listOf("la"),
          hint = "'Soupe' est un nom court sans terminaison indicative : à mémoriser directement avec 'la'.",
          explanation = "Aucun indice de terminaison ici : le genre de 'soupe' (féminin) s'apprend par mémorisation directe."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_genre_1",
          instructionFr = "Touchez l'article mal accordé avec le genre du nom :",
          passage = "Le solution à ce problème est plus simple qu'on ne le pense.",
          tokens = listOf("Le", "solution", "à", "ce", "problème", "est", "plus", "simple", "qu'on", "ne", "le", "pense."),
          errorTokenIndex = 0,
          errorWord = "Le",
          correction = "La",
          ruleExplanation = "'-tion' est une terminaison féminine quasi systématique : c'est TOUJOURS 'la solution', jamais 'le solution'."
        ),
        SpotErrorDrillItem(
          id = "spot_genre_2",
          instructionFr = "Repérez l'erreur d'article devant ce nom masculin en -isme :",
          passage = "La tourisme international a fortement chuté après la pandémie.",
          tokens = listOf("La", "tourisme", "international", "a", "fortement", "chuté", "après", "la", "pandémie."),
          errorTokenIndex = 0,
          errorWord = "La",
          correction = "Le",
          ruleExplanation = "'-isme' est une terminaison masculine quasi absolue en français : 'le tourisme', 'le journalisme', jamais 'la tourisme'."
        )
      )
    ),

    // 8. LE MASCULIN ET LE FÉMININ DES PROFESSIONS (A1)
    StructuredRule(
      id = "genre-professions",
      categoryId = "cat-nom",
      categoryName = "Groupe Nominal",
      level = "A1",
      titleFr = "Le Masculin et le Féminin des Professions",
      titleEn = "Gender Endings for Job / Profession Nouns",
      summaryFr = "Les noms de métiers suivent leurs propres terminaisons de féminisation, distinctes des indices généraux du genre : -eur → -euse, -teur → -trice, -ien → -ienne, ou simplement + -e.",
      summaryEn = "Profession nouns follow their own masculine/feminine pattern, separate from general noun-gender clues: -eur → -euse, -teur → -trice, -ien → -ienne, or just add -e.",
      pillar = GrammarPillar.AGREEMENT_ENGINE,
      ruleExplanationEn = "Treat this as its own mini-system, separate from general noun gender. Four patterns cover almost every profession: -eur becomes -euse (un vendeur / une vendeuse), -teur becomes -trice (un acteur / une actrice), -ien becomes -ienne with a doubled n (un musicien / une musicienne), and everything else usually just adds -e (un avocat / une avocate). Nouns already ending in -e (un/une architecte, journaliste, secrétaire) don't change at all — only the article shifts. One extra quirk: after être, drop the article entirely for an unqualified profession (Il est ingénieur), but bring it back the moment you add a qualifier (C'est un bon ingénieur).",
      triggerToken = "Il est / Elle est + métier",
      triggerActionFr = "Pas d'article devant le métier",
      triggerActionEn = "No article before the unqualified profession",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Nom de métier masculin", "Forme de référence", TokenCategory.SUBJECT, "Ex : vendeur, acteur, musicien, avocat"),
          FormulaToken("Terminaison de féminisation", "-euse / -trice / -ienne / -e", TokenCategory.TRIGGER, "Pattern spécifique aux noms de métiers, distinct du genre général."),
          FormulaToken("Nom de métier féminin", "Forme obtenue", TokenCategory.TARGET_VERB, "Ex : vendeuse, actrice, musicienne, avocate")
        ),
        diagramTitle = "Le Détecteur de Terminaison de Métier",
        diagramDescription = "Identifiez la terminaison masculine du métier pour appliquer le bon pattern :",
        decisionSteps = listOf(
          DecisionStep(1, "Le métier masculin se termine-t-il en -eur (hors -teur) ?", "-> -eur devient -euse (vendeur -> vendeuse)", "-> Passez à l'étape 2"),
          DecisionStep(2, "Le métier masculin se termine-t-il en -teur ?", "-> -teur devient -trice (acteur -> actrice)", "-> Passez à l'étape 3"),
          DecisionStep(3, "Le métier masculin se termine-t-il en -ien ou déjà en -e ?", "-> -ien devient -ienne (musicien -> musicienne) ; -e ne change pas (architecte)", "-> Ajoutez simplement -e (avocat -> avocate)")
        ),
        comparisonTableTitle = "Les Quatre Patterns de Féminisation",
        comparisonHeaders = listOf("Terminaison masculine", "Terminaison féminine", "Exemple"),
        comparisonRows = listOf(
          listOf("-eur", "-euse", "un vendeur / une vendeuse"),
          listOf("-teur", "-trice", "un acteur / une actrice"),
          listOf("-ien", "-ienne", "un musicien / une musicienne"),
          listOf("consonne finale / -at / -é", "+ -e", "un avocat / une avocate"),
          listOf("-e (déjà neutre)", "invariable", "un/une architecte, journaliste")
        ),
        diagramDescriptionEn = "Identify the masculine ending of the profession to apply the right pattern:"
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Ma sœur est vendeuse dans un magasin de vêtements en centre-ville.",
          highlightedSegment = "vendeuse",
          englishSentence = "My sister is a salesperson in a clothing store downtown.",
          contextNote = "'-eur' devient '-euse' ; pas d'article devant le métier après 'être'."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "La directrice de l'établissement a prononcé un discours remarqué lors de la cérémonie.",
          highlightedSegment = "La directrice",
          englishSentence = "The school's director gave a notable speech during the ceremony.",
          contextNote = "'-teur' devient '-trice' : registre soutenu, contexte institutionnel."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "Sa copine, elle est prof, elle bosse dans un collège pas loin d'ici.",
          highlightedSegment = "elle est prof",
          englishSentence = "His girlfriend, she's a teacher, she works at a middle school nearby.",
          contextNote = "'Prof' (abréviation orale de 'professeure') illustre l'usage courant des formes féminisées."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Mme Dupont est ingénieure en chef sur ce projet depuis janvier dernier.",
          highlightedSegment = "ingénieure en chef",
          englishSentence = "Ms. Dupont has been lead engineer on this project since last January.",
          contextNote = "Forme féminisée officielle, de plus en plus standard dans les documents professionnels."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Une Équipe de Direction",
        fullTextFr = "Notre entreprise a une nouvelle directrice depuis le mois dernier. Son adjoint est un ancien acteur reconverti en formateur. La responsable des ventes est une ancienne vendeuse qui connaît parfaitement nos produits. Quant au comptable, c'est un excellent employé, très rigoureux.",
        fullTextEn = "Our company has had a new director since last month. Her deputy is a former actor who retrained as a trainer. The sales manager is a former salesperson who knows our products perfectly. As for the accountant, he's an excellent employee, very rigorous.",
        annotations = listOf(
          InlineAnnotation(
            id = "prof_ann_1",
            targetPhrase = "une nouvelle directrice",
            explanationFr = "'Directeur' devient 'directrice' : le pattern -teur → -trice s'applique aux noms de métier, pas au genre général du nom.",
            explanationEn = "'Directeur' becomes 'directrice': the -teur → -trice pattern applies specifically to profession nouns.",
            whyItApplies = "Terminaison de métier -teur.",
            commonPitfall = "Ne confondez pas avec le nom commun 'acteur/actrice' du théâtre, qui suit le même pattern mais n'est pas un titre hiérarchique."
          ),
          InlineAnnotation(
            id = "prof_ann_2",
            targetPhrase = "un excellent employé",
            explanationFr = "'Employé' ajoute simplement '-e' au féminin ('employée') ; ici au masculin, la forme reste 'employé'.",
            explanationEn = "'Employé' simply adds '-e' for the feminine ('employée'); here in the masculine, the form stays 'employé'.",
            whyItApplies = "Pattern par défaut : ajout de -e.",
            commonPitfall = "Ne pas oublier le -e final à l'écrit pour la forme féminine, même s'il ne change pas la prononciation."
          ),
          InlineAnnotation(
            id = "prof_ann_3",
            targetPhrase = "un ancien acteur",
            explanationFr = "'C'est un ancien acteur' : ici le métier est qualifié par 'ancien', donc l'article revient (c'est un + qualificatif + métier).",
            explanationEn = "'C'est un ancien acteur': the profession is qualified by 'ancien', so the article comes back.",
            whyItApplies = "Métier qualifié après 'c'est'.",
            commonPitfall = "Sans qualificatif, on dirait 'il est acteur', sans article."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_prof_1",
          instructionFr = "Donnez la forme féminine du métier :",
          instructionEn = "Give the feminine form of the profession:",
          basePrompt = "Son frère est chanteur ; sa sœur est _____ dans le même groupe.",
          targetAnswer = "chanteuse",
          acceptedAnswers = listOf("chanteuse"),
          hint = "'-eur' (hors -teur) devient '-euse' : chanteur -> chanteuse.",
          explanation = "'Chanteur' suit le pattern -eur -> -euse, comme vendeur -> vendeuse."
        ),
        ProductionDrillItem(
          id = "prod_prof_2",
          instructionFr = "Donnez la forme féminine du métier en -ien :",
          instructionEn = "Give the feminine form of the -ien profession:",
          basePrompt = "Mon voisin est pharmacien ; sa collègue est _____ dans la même officine.",
          targetAnswer = "pharmacienne",
          acceptedAnswers = listOf("pharmacienne"),
          hint = "'-ien' double le n et ajoute '-e' : pharmacien -> pharmacienne.",
          explanation = "Le pattern -ien -> -ienne double toujours la consonne n avant d'ajouter le -e final."
        ),
        ProductionDrillItem(
          id = "prod_prof_3",
          instructionFr = "Complétez sans article, après 'être', pour un métier non qualifié :",
          instructionEn = "Complete without an article, after 'être', for an unqualified profession:",
          basePrompt = "Elle _____ (être) infirmière depuis dix ans dans cet hôpital.",
          targetAnswer = "est",
          acceptedAnswers = listOf("est"),
          hint = "Après 'être', pas d'article devant un métier non qualifié : elle est infirmière.",
          explanation = "'Être + métier' sans article ni qualificatif : 'elle est infirmière', jamais 'elle est une infirmière'."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_prof_1",
          instructionFr = "Touchez l'erreur d'article devant ce métier non qualifié :",
          passage = "Mon oncle est un ingénieur, il travaille pour une grande entreprise automobile.",
          tokens = listOf("Mon", "oncle", "est", "un", "ingénieur,", "il", "travaille", "pour", "une", "grande", "entreprise", "automobile."),
          errorTokenIndex = 3,
          errorWord = "un",
          correction = "(supprimer l'article)",
          ruleExplanation = "Après 'être', pas d'article devant un métier non qualifié : 'il est ingénieur', pas 'il est un ingénieur' (l'article reviendrait avec un qualificatif : 'c'est un bon ingénieur')."
        ),
        SpotErrorDrillItem(
          id = "spot_prof_2",
          instructionFr = "Repérez la forme féminine mal construite :",
          passage = "Ma tante est actrisse dans plusieurs films depuis les années 2000.",
          tokens = listOf("Ma", "tante", "est", "actrisse", "dans", "plusieurs", "films", "depuis", "les", "années", "2000."),
          errorTokenIndex = 3,
          errorWord = "actrisse",
          correction = "actrice",
          ruleExplanation = "Le pattern -teur -> -trice donne 'actrice' (un seul 't', terminaison '-trice'), jamais 'actrisse'."
        )
      )
    ),

    // 9. LE NOMBRE DES NOMS (A1)
    StructuredRule(
      id = "nombre-noms",
      categoryId = "cat-nom",
      categoryName = "Groupe Nominal",
      level = "A1",
      titleFr = "Le Nombre des Noms : Singulier et Pluriel",
      titleEn = "Singular / Plural of Nouns",
      summaryFr = "Le pluriel régulier s'obtient en ajoutant -s (silencieux à l'oral). Quelques terminaisons suivent des règles différentes : -eu/-eau prennent -x, -al devient -aux.",
      summaryEn = "The default plural adds a silent -s. A few endings follow different rules: -eu/-eau take -x, -al becomes -aux.",
      pillar = GrammarPillar.AGREEMENT_ENGINE,
      ruleExplanationEn = "The default is boring and reliable: add -s, and don't pronounce it (un livre / des livres sound identical — only the article un/des tells you the number). Three exceptions cover almost everything else: nouns ending in -eu or -eau add -x instead of -s (un cheveu / des cheveux, un bureau / des bureaux); nouns ending in -al usually switch to -aux (un journal / des journaux); and nouns already ending in -s, -x, or -z don't change at all (un pays / des pays). Since the plural -s is silent, the article carries almost all the information in speech — listen for le/la vs les, not for a sound on the noun itself.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Nom singulier", "Forme de base", TokenCategory.SUBJECT, "Ex : livre, cheveu, journal, pays"),
          FormulaToken("Terminaison", "-eu/-eau, -al, -s/-x/-z, ou autre", TokenCategory.TRIGGER, "Détermine la marque du pluriel."),
          FormulaToken("Nom pluriel", "Forme obtenue", TokenCategory.TARGET_VERB, "Ex : livres, cheveux, journaux, pays")
        ),
        diagramTitle = "Le Détecteur de Terminaison au Pluriel",
        diagramDescription = "Vérifiez la terminaison du singulier avant d'ajouter la marque du pluriel :",
        decisionSteps = listOf(
          DecisionStep(1, "Le nom se termine-t-il déjà par -s, -x ou -z au singulier ?", "-> Aucun changement au pluriel (un pays -> des pays)", "-> Passez à l'étape 2"),
          DecisionStep(2, "Le nom se termine-t-il par -eu ou -eau ?", "-> Ajoutez -x (un cheveu -> des cheveux)", "-> Passez à l'étape 3"),
          DecisionStep(3, "Le nom se termine-t-il par -al ?", "-> Remplacez -al par -aux (un journal -> des journaux)", "-> Cas général : ajoutez simplement -s (un livre -> des livres)")
        ),
        comparisonTableTitle = "Les Marques du Pluriel",
        comparisonHeaders = listOf("Terminaison singulier", "Règle", "Exemple"),
        comparisonRows = listOf(
          listOf("Cas général", "+ s", "un livre -> des livres"),
          listOf("-eu, -eau", "+ x", "un cheveu -> des cheveux"),
          listOf("-al", "-> -aux", "un journal -> des journaux"),
          listOf("-s, -x, -z", "aucun changement", "un pays -> des pays, une voix -> des voix")
        ),
        diagramDescriptionEn = "Check the singular ending before adding the plural mark:"
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "J'ai acheté deux journaux et trois cadeaux pour la fête de samedi.",
          highlightedSegment = "deux journaux et trois cadeaux",
          englishSentence = "I bought two newspapers and three gifts for Saturday's party.",
          contextNote = "'-al' -> '-aux' (journaux) et '-eau' -> '-eaux' (cadeaux) dans la même phrase."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Les journaux nationaux ont largement couvert les principaux événements régionaux.",
          highlightedSegment = "Les journaux nationaux",
          englishSentence = "The national newspapers extensively covered the main regional events.",
          contextNote = "Double pluriel en '-aux' : le nom et son adjectif suivent tous deux la règle -al -> -aux."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "T'as vu ses cheveux ? Il s'est fait une couleur complètement différente !",
          highlightedSegment = "ses cheveux",
          englishSentence = "Did you see his hair? He got a totally different color!",
          contextNote = "'Cheveux' est un pluriel figé très fréquent à l'oral, même pour parler d'une seule tête de cheveux."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Merci de transmettre ces documents à tous les responsables régionaux d'ici vendredi.",
          highlightedSegment = "tous les responsables régionaux",
          englishSentence = "Please forward these documents to all regional managers by Friday.",
          contextNote = "'Régionaux' (adjectif en -al) suit exactement la même règle de pluriel que les noms."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Un Déménagement Compliqué",
        fullTextFr = "Pour le déménagement, nous avons rempli plusieurs cartons : des livres, des journaux, des vieux cheveux de poupée (souvenirs d'enfance !), et même des chapeaux oubliés depuis des années. Les voisins nous ont proposé leur aide avec leurs bras solides. Heureusement, il n'y a pas eu de dégâts matériaux.",
        fullTextEn = "For the move, we filled several boxes: books, newspapers, old doll hair (childhood souvenirs!), and even hats forgotten for years. The neighbors offered their help with their strong arms. Fortunately, there was no material damage.",
        annotations = listOf(
          InlineAnnotation(
            id = "nombre_ann_1",
            targetPhrase = "des journaux",
            explanationFr = "'Journal' se termine en '-al' : le pluriel remplace '-al' par '-aux' -> journaux.",
            explanationEn = "'Journal' ends in '-al': the plural replaces '-al' with '-aux' -> journaux.",
            whyItApplies = "Terminaison -al au singulier.",
            commonPitfall = "Ne dites jamais 'journals' : cette forme anglicisée n'existe pas en français standard."
          ),
          InlineAnnotation(
            id = "nombre_ann_2",
            targetPhrase = "des chapeaux",
            explanationFr = "'Chapeau' se termine en '-eau' : on ajoute '-x' au lieu de '-s' -> chapeaux.",
            explanationEn = "'Chapeau' ends in '-eau': add '-x' instead of '-s' -> chapeaux.",
            whyItApplies = "Terminaison -eau au singulier.",
            commonPitfall = "Le -x est silencieux à l'oral, comme le -s régulier : seul l'article ('des' vs 'un') indique le nombre."
          ),
          InlineAnnotation(
            id = "nombre_ann_3",
            targetPhrase = "dégâts matériaux",
            explanationFr = "'Matériel' au pluriel adjectival donne 'matériaux' seulement au sens de matières premières ; ici c'est en réalité une confusion fréquente avec 'matériels' (adjectif, dommages matériels).",
            explanationEn = "This is actually a very common confusion: 'matériaux' (materials, as in raw materials) versus 'matériels' (material, as an adjective for damage).",
            whyItApplies = "Piège fréquent entre deux pluriels proches mais distincts.",
            commonPitfall = "On dit 'des dégâts matériels' (dommages), pas 'des dégâts matériaux' (qui voudrait dire des dégâts faits de matériaux !)."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_nombre_1",
          instructionFr = "Mettez le nom au pluriel :",
          instructionEn = "Put the noun in the plural:",
          basePrompt = "Il a deux _____ (cheveu) blancs, mais c'est à peine visible.",
          targetAnswer = "cheveux",
          acceptedAnswers = listOf("cheveux"),
          hint = "'-eu' devient '-eux' au pluriel : cheveu -> cheveux.",
          explanation = "'Cheveu' suit la règle -eu -> -eux, comme 'jeu -> jeux' ou 'feu -> feux'."
        ),
        ProductionDrillItem(
          id = "prod_nombre_2",
          instructionFr = "Mettez le nom au pluriel :",
          instructionEn = "Put the noun in the plural:",
          basePrompt = "Nous avons visité trois _____ (hôpital) différents cette semaine.",
          targetAnswer = "hôpitaux",
          acceptedAnswers = listOf("hôpitaux"),
          hint = "'-al' devient '-aux' au pluriel : hôpital -> hôpitaux.",
          explanation = "'Hôpital' suit la règle -al -> -aux, comme 'journal -> journaux' ou 'animal -> animaux'."
        ),
        ProductionDrillItem(
          id = "prod_nombre_3",
          instructionFr = "Mettez le nom au pluriel (attention à l'exception) :",
          instructionEn = "Put the noun in the plural (watch for the exception):",
          basePrompt = "Ils ont visité plusieurs _____ (pays) d'Europe l'été dernier.",
          targetAnswer = "pays",
          acceptedAnswers = listOf("pays"),
          hint = "Un nom déjà terminé en '-s' ne change pas au pluriel.",
          explanation = "'Pays' se termine déjà en '-s' au singulier : la forme est identique au pluriel."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_nombre_1",
          instructionFr = "Touchez le pluriel mal formé dans cette phrase :",
          passage = "Les animals de la ferme se sont échappés pendant la tempête de la nuit dernière.",
          tokens = listOf("Les", "animals", "de", "la", "ferme", "se", "sont", "échappés", "pendant", "la", "tempête", "de", "la", "nuit", "dernière."),
          errorTokenIndex = 1,
          errorWord = "animals",
          correction = "animaux",
          ruleExplanation = "'Animal' se termine en '-al' : le pluriel remplace '-al' par '-aux' -> 'animaux', jamais 'animals' (calque de l'anglais)."
        ),
        SpotErrorDrillItem(
          id = "spot_nombre_2",
          instructionFr = "Repérez le pluriel qui n'aurait pas dû changer :",
          passage = "Ils ont visité plusieurs pais différents pendant leur voyage en Europe.",
          tokens = listOf("Ils", "ont", "visité", "plusieurs", "pais", "différents", "pendant", "leur", "voyage", "en", "Europe."),
          errorTokenIndex = 4,
          errorWord = "pais",
          correction = "pays",
          ruleExplanation = "'Pays' se termine déjà en '-s' au singulier : la forme reste identique au pluriel, 'pays' (pas 'pais', qui n'existe pas)."
        )
      )
    ),

    // 10. LE FUTUR PROCHE ET LE PASSÉ RÉCENT (A1)
    StructuredRule(
      id = "futur-proche-passe-recent",
      categoryId = "cat-mode",
      categoryName = "Modes & Temps",
      level = "A1",
      titleFr = "Le Futur Proche et le Passé Récent",
      titleEn = "Aller + Infinitive (Near Future) and Venir de + Infinitive (Recent Past)",
      summaryFr = "Deux périphrases construites de façon symétrique : aller (au présent) + infinitif pour ce qui va se passer, venir de + infinitif pour ce qui vient de se terminer.",
      summaryEn = "Two mirror-image periphrases: aller (present tense) + infinitive for what's about to happen, venir de + infinitive for what just finished.",
      pillar = GrammarPillar.VERB_SYSTEM,
      ruleExplanationEn = "Think of these as a mirror pair built the exact same way: a conjugated semi-auxiliary in the present tense, plus an infinitive. Futur proche = aller + infinitive (je vais partir = I'm going to leave) — the everyday, spoken way to talk about the near future, far more common than the futur simple in conversation. Passé récent = venir de + infinitive (je viens de finir = I just finished) — the mirror image, pointing at the immediate past. Neither ever goes into the passé composé itself: never 'j'ai venu de' or 'j'ai allé faire'. Aller and venir just shift to the imparfait instead, to place the same idea inside a past narrative: je venais de sortir quand... (I had just gone out when...).",
      triggerToken = "venir de + infinitif",
      triggerActionFr = "Passé récent : action tout juste terminée",
      triggerActionEn = "Recent past: action that just finished",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Sujet", "Qui agit", TokenCategory.SUBJECT, "Ex : je, elle, nous..."),
          FormulaToken("Aller / Venir (présent)", "Semi-auxiliaire conjugué", TokenCategory.TRIGGER, "vais, vas, va... / viens, viens, vient..."),
          FormulaToken("de (si venir)", "Préposition obligatoire", TokenCategory.CONNECTOR, "Uniquement pour le passé récent : venir DE + infinitif."),
          FormulaToken("Infinitif", "Verbe non conjugué", TokenCategory.TARGET_VERB, "Ex : partir, finir, arriver...")
        ),
        diagramTitle = "Les Deux Miroirs du Présent",
        diagramDescription = "Aller + infinitif regarde vers l'avant ; venir de + infinitif regarde vers l'arrière, exactement symétriques autour du moment présent :",
        decisionSteps = listOf(
          DecisionStep(1, "L'action est-elle sur le point de se produire, dans un futur très proche ?", "-> FUTUR PROCHE : aller (présent) + infinitif (ex : je vais partir)", "-> Passez à l'étape 2"),
          DecisionStep(2, "L'action vient-elle de se terminer, juste avant maintenant ?", "-> PASSÉ RÉCENT : venir de + infinitif (ex : je viens de finir)", "-> Aucune des deux périphrases ne s'applique : utilisez un temps standard"),
          DecisionStep(3, "Voulez-vous conjuguer cette périphrase au passé composé ?", "-> IMPOSSIBLE : ni 'j'ai allé faire' ni 'j'ai venu de' n'existent", "-> Utilisez l'imparfait pour situer l'idée dans un récit passé (je venais de sortir quand...)")
        ),
        comparisonTableTitle = "Futur Proche vs Passé Récent : Miroirs Symétriques",
        comparisonHeaders = listOf("Futur proche", "Passé récent", "Règle clé"),
        comparisonRows = listOf(
          listOf("Je vais partir dans dix minutes.", "Je viens de finir mon travail.", "aller + infinitif vs venir DE + infinitif"),
          listOf("Elle va commencer lundi.", "Elle vient d'arriver.", "'de' devient 'd\\'' devant une voyelle"),
          listOf("Ils ne vont pas venir ce soir.", "Ils ne viennent pas d'arriver.", "La négation encadre le semi-auxiliaire conjugué, jamais l'infinitif")
        ),
        diagramDescriptionEn = "Aller + infinitive looks forward; venir de + infinitive looks backward, perfectly symmetrical around the present moment:"
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Attends-moi deux minutes, je viens de recevoir un appel important.",
          highlightedSegment = "je viens de recevoir",
          englishSentence = "Wait for me two minutes, I just got an important call.",
          contextNote = "Passé récent standard, extrêmement fréquent dans la conversation quotidienne."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Le comité s'apprête à rendre sa décision, qui sera communiquée sous peu.",
          highlightedSegment = "s'apprête à rendre",
          englishSentence = "The committee is about to render its decision, which will be announced shortly.",
          contextNote = "'S'apprêter à' est un équivalent plus soutenu du futur proche 'aller + infinitif'."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "J'vais y aller là, j'viens de recevoir un texto de ma mère.",
          highlightedSegment = "j'vais y aller / j'viens de recevoir",
          englishSentence = "I'm gonna head out now, I just got a text from my mom.",
          contextNote = "Élision orale fréquente de 'je' en 'j'' devant les deux périphrases."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Nous venons de finaliser le contrat ; l'équipe va démarrer le projet dès lundi.",
          highlightedSegment = "venons de finaliser / va démarrer",
          englishSentence = "We have just finalized the contract; the team will start the project as soon as Monday.",
          contextNote = "Les deux périphrases combinées dans un compte-rendu professionnel factuel."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Un Appel de Dernière Minute",
        fullTextFr = "Salut Marc, je viens de recevoir ton message. Je suis désolée, je ne vais pas pouvoir venir à la réunion de cet après-midi : je viens d'apprendre que ma fille est malade. Je vais rester avec elle aujourd'hui. On va sûrement se reparler demain matin, dès que je vais arriver au bureau.",
        fullTextEn = "Hi Marc, I just got your message. I'm sorry, I'm not going to be able to come to this afternoon's meeting: I just found out my daughter is sick. I'm going to stay with her today. We'll surely talk again tomorrow morning, as soon as I get to the office.",
        annotations = listOf(
          InlineAnnotation(
            id = "futprox_ann_1",
            targetPhrase = "je viens de recevoir",
            explanationFr = "Passé récent : l'action de recevoir le message vient de se terminer, juste avant le moment de l'écriture.",
            explanationEn = "Recent past: receiving the message just finished, right before writing this.",
            whyItApplies = "Action tout juste achevée.",
                            commonPitfall = "Ne dites jamais 'j'ai venu de recevoir' : venir de reste toujours au présent (ou à l'imparfait dans un récit)."
          ),
          InlineAnnotation(
            id = "futprox_ann_2",
            targetPhrase = "je ne vais pas pouvoir",
            explanationFr = "Futur proche à la forme négative : 'ne...pas' encadre 'vais', le semi-auxiliaire conjugué, pas l'infinitif 'pouvoir'.",
            explanationEn = "Negative futur proche: 'ne...pas' wraps around 'vais', the conjugated semi-auxiliary, not the infinitive 'pouvoir'.",
            whyItApplies = "Négation d'une périphrase verbale.",
            commonPitfall = "Erreur fréquente : 'je vais ne pas pouvoir' est incorrect, la négation doit encadrer 'vais'."
          ),
          InlineAnnotation(
            id = "futprox_ann_3",
            targetPhrase = "dès que je vais arriver",
            explanationFr = "Futur proche employé pour une action imminente et perçue comme certaine, ici dans une subordonnée de temps.",
            explanationEn = "Futur proche used for an imminent, certain action, here inside a time clause.",
            whyItApplies = "Action future certaine, exprimée à l'oral plutôt qu'au futur simple.",
            commonPitfall = "Le futur simple ('j'arriverai') serait également correct mais plus soutenu ; le futur proche est la norme à l'oral."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_futprox_1",
          instructionFr = "Conjuguez au futur proche (action imminente) :",
          instructionEn = "Conjugate in the futur proche (imminent action):",
          basePrompt = "Attention, le train _____ (partir) dans deux minutes !",
          targetAnswer = "va partir",
          acceptedAnswers = listOf("va partir"),
          hint = "Aller (présent, 3e pers. sing.) + infinitif : il/elle va + infinitif.",
          explanation = "Futur proche = aller au présent + infinitif : 'le train va partir' exprime une action imminente et certaine."
        ),
        ProductionDrillItem(
          id = "prod_futprox_2",
          instructionFr = "Conjuguez au passé récent (action tout juste terminée) :",
          instructionEn = "Conjugate in the passé récent (action that just finished):",
          basePrompt = "Ne l'appelle pas maintenant, elle _____ (se coucher) il y a cinq minutes.",
          targetAnswer = "vient de se coucher",
          acceptedAnswers = listOf("vient de se coucher"),
          hint = "Venir (présent) + de + infinitif (verbe pronominal : venir de se coucher).",
          explanation = "Passé récent = venir au présent + de + infinitif : 'elle vient de se coucher' situe l'action juste avant maintenant."
        ),
        ProductionDrillItem(
          id = "prod_futprox_3",
          instructionFr = "Conjuguez au passé récent avec l'élision correcte de 'de' :",
          instructionEn = "Conjugate in the passé récent with the correct elision of 'de':",
          basePrompt = "Ils _____ (arriver) : leurs valises sont encore dans le couloir.",
          targetAnswer = "viennent d'arriver",
          acceptedAnswers = listOf("viennent d'arriver"),
          hint = "'De' devient 'd'' devant une voyelle : venir d'arriver.",
          explanation = "'De' s'élide en 'd'' devant le infinitif 'arriver', qui commence par une voyelle."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_futprox_1",
          instructionFr = "Touchez la forme incorrecte de cette périphrase verbale :",
          passage = "Désolé, j'ai allé partir avant la fin de la réunion hier soir.",
          tokens = listOf("Désolé,", "j'ai", "allé", "partir", "avant", "la", "fin", "de", "la", "réunion", "hier", "soir."),
          errorTokenIndex = 1,
          errorWord = "j'ai",
          correction = "je suis allé (ou : j'allais partir)",
          ruleExplanation = "Le futur proche ne se conjugue jamais au passé composé ('j'ai allé' n'existe pas) ; pour situer l'idée dans le passé, on utilise l'imparfait : 'j'allais partir quand...'."
        ),
        SpotErrorDrillItem(
          id = "spot_futprox_2",
          instructionFr = "Repérez la préposition manquante dans ce passé récent :",
          passage = "Ne fais pas de bruit, le bébé vient s'endormir il y a deux minutes.",
          tokens = listOf("Ne", "fais", "pas", "de", "bruit,", "le", "bébé", "vient", "s'endormir", "il", "y", "a", "deux", "minutes."),
          errorTokenIndex = 7,
          errorWord = "vient",
          correction = "vient de",
          ruleExplanation = "Le passé récent exige la préposition 'de' entre 'venir' et l'infinitif : 'vient DE s'endormir', jamais 'vient s'endormir' seul."
        )
      )
    ),

    // 11. LES ADJECTIFS QUALIFICATIFS
    StructuredRule(
      id = "adjectifs-qualificatifs",
      categoryId = "cat-nom",
      categoryName = "Groupe Nominal",
      level = "A2",
      titleFr = "Les Adjectifs Qualificatifs : Accord & Place",
      titleEn = "Descriptive Adjectives: Agreement & Position",
      summaryFr = "L'adjectif s'accorde en genre et en nombre avec le nom qu'il qualifie : le plus souvent +e au féminin, +s au pluriel, mais de nombreuses irrégularités existent.",
      summaryEn = "Adjectives agree in gender and number with the noun they describe: usually +e for feminine, +s for plural, though many irregular patterns exist.",
      pillar = GrammarPillar.AGREEMENT_ENGINE,
      ruleExplanationEn = "Treat the adjective as a mirror of the noun: whatever gender/number the noun carries, the adjective copies it, even across a long sentence (la maison que j'ai achetée est grande — grande still agrees with maison, not the last word said). The default transformation is +e / +s, but a fixed set of endings shift predictably: -eux→-euse, -f→-ve, -er→-ère, -on/-en→-onne/-enne (doubling the consonant), -c→-che or -que. A handful of very common adjectives (beau, nouveau, vieux) also have a special liaison form before a vowel: un bel homme, not un beau homme.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Nom", "Genre + Nombre de référence", TokenCategory.SUBJECT, "Le nom impose son genre et son nombre à l'adjectif."),
          FormulaToken("Adjectif (masc. sg.)", "Forme du dictionnaire", TokenCategory.TARGET_VERB, "Forme de base, ex : grand, joyeux, actif."),
          FormulaToken("+e / +s / +es", "Marques d'accord", TokenCategory.CONNECTOR, "Ajoutées selon le genre et le nombre du nom.")
        ),
        diagramTitle = "Arbre de Décision : Quelle Terminaison ?",
        diagramDescription = "Suivez le flux pour accorder l'adjectif correctement :",
        decisionSteps = listOf(
          DecisionStep(1, "Le nom est-il féminin ?", "-> Ajoutez -e (ou appliquez la terminaison irrégulière : -eux->-euse, -f->-ve, -er->-ère)", "-> Gardez la forme masculine"),
          DecisionStep(2, "Le nom est-il pluriel ?", "-> Ajoutez -s (sauf si l'adjectif se termine déjà par -s/-x, ou -al->-aux)", "-> Pas de marque supplémentaire"),
          DecisionStep(3, "L'adjectif précède-t-il un nom masculin commençant par une voyelle (beau/nouveau/vieux) ?", "-> Utilisez la forme de liaison : bel, nouvel, vieil", "-> Gardez la forme standard")
        ),
        comparisonTableTitle = "Tableau des Terminaisons Irrégulières",
        comparisonHeaders = listOf("Masculin", "Féminin", "Règle"),
        comparisonRows = listOf(
          listOf("heureux", "heureuse", "-eux -> -euse"),
          listOf("actif", "active", "-f -> -ve"),
          listOf("premier", "première", "-er -> -ère"),
          listOf("bon", "bonne", "-on -> -onne (consonne doublée)"),
          listOf("blanc", "blanche", "-c -> -che")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Elle porte une jolie robe bleue et des chaussures neuves.",
          highlightedSegment = "jolie robe bleue",
          englishSentence = "She's wearing a pretty blue dress and new shoes.",
          contextNote = "Accord standard au féminin singulier (jolie, bleue) et au féminin pluriel (neuves)."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Les décisions prises par le conseil demeurent extrêmement controversées.",
          highlightedSegment = "extrêmement controversées",
          englishSentence = "The decisions made by the board remain extremely controversial.",
          contextNote = "Registre soutenu avec adjectif au féminin pluriel accordé sur 'décisions'."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "T'as vu sa nouvelle bagnole ? Elle est trop belle !",
          highlightedSegment = "trop belle",
          englishSentence = "Did you see his new car? It's so gorgeous!",
          contextNote = "'Belle' forme féminine irrégulière de 'beau', renforcée par l'intensif familier 'trop'."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Nous recherchons un candidat motivé ayant des compétences techniques solides.",
          highlightedSegment = "compétences techniques solides",
          englishSentence = "We are looking for a motivated candidate with solid technical skills.",
          contextNote = "Deux adjectifs accordés au féminin pluriel sur 'compétences' dans une offre d'emploi."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Un Nouvel Appartement",
        fullTextFr = "Ils viennent de visiter un bel appartement lumineux au dernier étage. La cuisine est spacieuse et les fenêtres sont larges. Le salon possède de vieilles poutres apparentes, très charmantes. Malheureusement, les charges mensuelles sont assez élevées.",
        fullTextEn = "They just visited a beautiful, bright apartment on the top floor. The kitchen is spacious and the windows are wide. The living room has old exposed beams, very charming. Unfortunately, the monthly fees are quite high.",
        annotations = listOf(
          InlineAnnotation(
            id = "adj_ann_1",
            targetPhrase = "un bel appartement",
            explanationFr = "'Beau' devient 'bel' devant un nom masculin singulier commençant par une voyelle, pour faciliter la liaison.",
            explanationEn = "'Beau' becomes 'bel' before a masculine singular noun starting with a vowel, for smoother liaison.",
            whyItApplies = "Forme de liaison de l'adjectif 'beau'.",
            commonPitfall = "Ne dites jamais 'un beau appartement' — la forme de liaison est obligatoire devant une voyelle."
          ),
          InlineAnnotation(
            id = "adj_ann_2",
            targetPhrase = "les fenêtres sont larges",
            explanationFr = "'Larges' s'accorde au féminin pluriel avec 'fenêtres', en ajoutant simplement -s (pas de -e supplémentaire car 'large' se termine déjà par -e).",
            explanationEn = "'Larges' agrees in feminine plural with 'fenêtres', adding only -s since 'large' already ends in -e.",
            whyItApplies = "Accord régulier, adjectif déjà terminé en -e.",
            commonPitfall = "Ne pas ajouter -e à un adjectif qui se termine déjà par -e au masculin (large, jaune, rouge...)."
          ),
          InlineAnnotation(
            id = "adj_ann_3",
            targetPhrase = "de vieilles poutres",
            explanationFr = "'Vieux' a une forme féminine irrégulière 'vieille', qui prend -s au pluriel : vieilles.",
            explanationEn = "'Vieux' has the irregular feminine form 'vieille', which takes -s in the plural: vieilles.",
            whyItApplies = "Adjectif irrégulier au féminin.",
            commonPitfall = "Ne confondez pas avec la forme de liaison masculine 'vieil' (un vieil homme), utilisée seulement au masculin singulier devant voyelle."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_adj_1",
          instructionFr = "Accordez l'adjectif entre parenthèses :",
          instructionEn = "Agree the adjective in parentheses:",
          basePrompt = "Ma sœur est très _____ (heureux) de son nouveau travail.",
          targetAnswer = "heureuse",
          acceptedAnswers = listOf("heureuse"),
          hint = "-eux devient -euse au féminin.",
          explanation = "'Heureux' suit le patron -eux -> -euse : heureuse, s'accordant avec le sujet féminin 'sœur'."
        ),
        ProductionDrillItem(
          id = "prod_adj_2",
          instructionFr = "Accordez l'adjectif au pluriel :",
          instructionEn = "Put the adjective in the plural:",
          basePrompt = "Ces exercices sont trop _____ (facile) pour ce niveau.",
          targetAnswer = "faciles",
          acceptedAnswers = listOf("faciles"),
          hint = "Ajoutez simplement -s au masculin pluriel.",
          explanation = "'Facile' se termine déjà par -e ; au pluriel, on ajoute seulement -s : faciles."
        ),
        ProductionDrillItem(
          id = "prod_adj_3",
          instructionFr = "Utilisez la forme de liaison correcte de l'adjectif :",
          instructionEn = "Use the correct liaison form of the adjective:",
          basePrompt = "C'est un _____ (nouveau) élève dans la classe.",
          targetAnswer = "nouvel",
          acceptedAnswers = listOf("nouvel"),
          hint = "Devant une voyelle, 'nouveau' devient 'nouvel'.",
          explanation = "'Nouveau' prend la forme de liaison 'nouvel' devant un nom masculin singulier commençant par une voyelle : un nouvel élève."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_adj_1",
          instructionFr = "Touchez le mot mal accordé :",
          passage = "Mes parents ont acheté une voiture blanc pour les vacances.",
          tokens = listOf("Mes", "parents", "ont", "acheté", "une", "voiture", "blanc", "pour", "les", "vacances."),
          errorTokenIndex = 6,
          errorWord = "blanc",
          correction = "blanche",
          ruleExplanation = "'Voiture' est féminin, donc l'adjectif 'blanc' doit prendre sa forme féminine irrégulière : blanche (-c -> -che)."
        ),
        SpotErrorDrillItem(
          id = "spot_adj_2",
          instructionFr = "Trouvez l'erreur de liaison :",
          passage = "Il habite dans un vieux immeuble du centre-ville.",
          tokens = listOf("Il", "habite", "dans", "un", "vieux", "immeuble", "du", "centre-ville."),
          errorTokenIndex = 4,
          errorWord = "vieux",
          correction = "vieil",
          ruleExplanation = "Devant un nom masculin singulier commençant par une voyelle (immeuble), 'vieux' devient 'vieil' : un vieil immeuble."
        )
      )
    ),

    // 12. LES DÉMONSTRATIFS
    StructuredRule(
      id = "demonstratifs",
      categoryId = "cat-nom",
      categoryName = "Groupe Nominal",
      level = "A2",
      titleFr = "Les Démonstratifs : Adjectifs et Pronoms",
      titleEn = "Demonstrative Adjectives (this/that) and Pronouns (this one/that one)",
      summaryFr = "Les adjectifs démonstratifs (ce, cette, ces) précèdent un nom ; les pronoms démonstratifs (celui, celle, ceux, celles) le remplacent.",
      summaryEn = "Demonstrative adjectives (ce, cette, ces) precede a noun; demonstrative pronouns (celui, celle, ceux, celles) replace it.",
      pillar = GrammarPillar.AGREEMENT_ENGINE,
      ruleExplanationEn = "Keep the two families separate: the adjective 'ce/cette/ces' always sits directly in front of a noun ('ce livre'), while the pronoun 'celui/celle/ceux/celles' stands in for a noun already mentioned and is almost never used alone — it needs a follow-up: '-ci/-là', 'de + noun', or a relative clause ('celui qui...'). 'Cet' (not 'cette') is the masculine singular form used only before a vowel sound, purely for liaison.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("ce / cet / cette / ces", "Adjectif démonstratif", TokenCategory.CONNECTOR, "Placé directement devant le nom, s'accorde en genre/nombre."),
          FormulaToken("celui / celle / ceux / celles", "Pronom démonstratif", TokenCategory.SUBJECT, "Remplace le nom, doit être suivi de -ci/-là, de + nom, ou d'une relative.")
        ),
        diagramTitle = "Arbre de Décision : Adjectif ou Pronom ?",
        diagramDescription = "Déterminez la bonne forme démonstrative :",
        decisionSteps = listOf(
          DecisionStep(1, "Le mot est-il suivi directement d'un nom ?", "-> Adjectif démonstratif (ce, cet, cette, ces)", "-> Passez à l'étape 2"),
          DecisionStep(2, "Le mot remplace un nom déjà cité et sera suivi de -ci/-là, de+nom, ou qui/que ?", "-> Pronom démonstratif (celui, celle, ceux, celles)", "-> Utilisez 'ceci/cela/ça' pour une idée non précisée"),
          DecisionStep(3, "Le nom masculin singulier commence-t-il par une voyelle ou un h muet ?", "-> Utilisez 'cet' au lieu de 'ce' (cet homme)", "-> Gardez 'ce'")
        ),
        comparisonTableTitle = "Tableau Comparatif",
        comparisonHeaders = listOf("Forme", "Genre/Nombre", "Exemple"),
        comparisonRows = listOf(
          listOf("ce", "masc. sg. (consonne)", "ce livre"),
          listOf("cet", "masc. sg. (voyelle/h muet)", "cet arbre, cet homme"),
          listOf("cette", "fém. sg.", "cette idée"),
          listOf("ces", "pl. (les deux genres)", "ces documents"),
          listOf("celui-ci / celui-là", "pronom masc. sg.", "celui-ci est mieux que celui-là")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Je préfère ce modèle-ci à celui-là.",
          highlightedSegment = "celui-là",
          englishSentence = "I prefer this model to that one.",
          contextNote = "Pronom démonstratif suivi de -là pour distinguer deux objets déjà mentionnés."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Ceux qui souhaitent participer sont priés de s'inscrire avant vendredi.",
          highlightedSegment = "Ceux qui",
          englishSentence = "Those who wish to take part are asked to register before Friday.",
          contextNote = "Pronom démonstratif pluriel suivi d'une proposition relative, registre administratif."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "Regarde-moi ce mec, il est bizarre !",
          highlightedSegment = "ce mec",
          englishSentence = "Look at this guy, he's weird!",
          contextNote = "Adjectif démonstratif à valeur expressive/péjorative fréquente à l'oral familier."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Cette proposition sera examinée lors de la réunion de ce jeudi.",
          highlightedSegment = "Cette proposition",
          englishSentence = "This proposal will be reviewed at this Thursday's meeting.",
          contextNote = "Adjectif démonstratif féminin singulier en contexte de compte-rendu professionnel."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Au Marché",
        fullTextFr = "Regarde ces pommes, elles ont l'air délicieuses ! Je préfère celles du fermier bio à celles du supermarché. Cet homme là-bas vend aussi de très bons fromages. Cette semaine, tout est en promotion.",
        fullTextEn = "Look at these apples, they look delicious! I prefer the organic farmer's ones to the supermarket's. That man over there also sells very good cheeses. This week, everything is on sale.",
        annotations = listOf(
          InlineAnnotation(
            id = "dem_ann_1",
            targetPhrase = "celles du fermier",
            explanationFr = "'Celles' est un pronom démonstratif féminin pluriel qui remplace 'les pommes', suivi de 'de + nom' pour préciser laquelle.",
            explanationEn = "'Celles' is a feminine plural demonstrative pronoun replacing 'les pommes', followed by 'de + noun' to specify which one.",
            whyItApplies = "Pronom démonstratif jamais utilisé seul.",
            commonPitfall = "Ne dites jamais 'celles' seul sans complément (-ci/-là, de+nom, ou relative)."
          ),
          InlineAnnotation(
            id = "dem_ann_2",
            targetPhrase = "Cet homme",
            explanationFr = "'Cet' (et non 'ce') est utilisé car 'homme' est masculin singulier et commence par une voyelle/h muet.",
            explanationEn = "'Cet' (not 'ce') is used because 'homme' is masculine singular and starts with a vowel/silent h.",
            whyItApplies = "Forme de liaison de l'adjectif démonstratif masculin.",
            commonPitfall = "Ne confondez pas 'cet' (masculin devant voyelle) avec 'cette' (féminin)."
          ),
          InlineAnnotation(
            id = "dem_ann_3",
            targetPhrase = "Cette semaine",
            explanationFr = "'Cette' est la forme féminine singulière de l'adjectif démonstratif, devant 'semaine'.",
            explanationEn = "'Cette' is the feminine singular form of the demonstrative adjective, before 'semaine'.",
            whyItApplies = "Accord féminin standard.",
            commonPitfall = "Ne pas utiliser 'cet' devant un nom féminin, même s'il commence par une voyelle (cette after all)."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_dem_1",
          instructionFr = "Complétez avec l'adjectif démonstratif correct :",
          instructionEn = "Complete with the correct demonstrative adjective:",
          basePrompt = "_____ (ce/cet/cette) été, nous partons en Bretagne.",
          targetAnswer = "Cet",
          acceptedAnswers = listOf("Cet", "cet"),
          hint = "'Été' commence par une voyelle.",
          explanation = "'Été' est masculin singulier et commence par une voyelle, donc on utilise 'cet' : cet été."
        ),
        ProductionDrillItem(
          id = "prod_dem_2",
          instructionFr = "Complétez avec le pronom démonstratif correct :",
          instructionEn = "Complete with the correct demonstrative pronoun:",
          basePrompt = "De toutes les robes, je préfère _____ (celui/celle) de ma sœur.",
          targetAnswer = "celle",
          acceptedAnswers = listOf("celle"),
          hint = "'Robe' est féminin singulier.",
          explanation = "'Celle' remplace 'la robe', féminin singulier, suivi de 'de + nom'."
        ),
        ProductionDrillItem(
          id = "prod_dem_3",
          instructionFr = "Complétez avec l'adjectif démonstratif pluriel :",
          instructionEn = "Complete with the plural demonstrative adjective:",
          basePrompt = "_____ (ce/ces) étudiants travaillent très sérieusement.",
          targetAnswer = "Ces",
          acceptedAnswers = listOf("Ces"),
          hint = "Le nom est au pluriel.",
          explanation = "'Ces' est la forme plurielle unique, valable pour le masculin et le féminin pluriel."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_dem_1",
          instructionFr = "Touchez le démonstratif incorrect :",
          passage = "Ce arbre dans le jardin est magnifique en automne.",
          tokens = listOf("Ce", "arbre", "dans", "le", "jardin", "est", "magnifique", "en", "automne."),
          errorTokenIndex = 0,
          errorWord = "Ce",
          correction = "Cet",
          ruleExplanation = "'Arbre' commence par une voyelle : il faut utiliser la forme de liaison 'cet', pas 'ce'."
        ),
        SpotErrorDrillItem(
          id = "spot_dem_2",
          instructionFr = "Trouvez le pronom démonstratif mal employé :",
          passage = "J'aime bien ce style, mais je préfère celle de l'année dernière.",
          tokens = listOf("J'aime", "bien", "ce", "style,", "mais", "je", "préfère", "celle", "de", "l'année", "dernière."),
          errorTokenIndex = 7,
          errorWord = "celle",
          correction = "celui",
          ruleExplanation = "'Style' est masculin singulier, donc le pronom démonstratif correct est 'celui', pas 'celle'."
        )
      )
    ),

    // 13. « C'EST » ET « IL/ELLE EST »
    StructuredRule(
      id = "cest-il-est",
      categoryId = "cat-nom",
      categoryName = "Groupe Nominal",
      level = "A2",
      titleFr = "« C'est » et « Il/Elle est » : Deux Façons d'Identifier",
      titleEn = "\"C'est\" and \"Il/Elle est\": Two Ways to Identify or Describe",
      summaryFr = "« C'est » précède un nom (avec article) ou un nom propre ; « il/elle est » précède un adjectif seul ou une profession non qualifiée sans article.",
      summaryEn = "\"C'est\" precedes a noun (with an article) or a proper name; \"il/elle est\" precedes a plain adjective or an unqualified profession with no article.",
      pillar = GrammarPillar.OTHER,
      ruleExplanationEn = "The test is what comes right after: if it's a noun phrase with an article/determiner ('un médecin', 'la voiture de Paul') or a proper name, use 'c'est'. If it's a bare adjective ('elle est intelligente') or a profession with no article ('il est médecin'), use 'il/elle est'. The one twist: a profession WITH an adjective flips back to 'c'est' + article ('c'est un bon médecin', not 'il est bon médecin').",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("C'est", "+ article/déterminant + nom, ou nom propre", TokenCategory.SUBJECT, "Identifie ou présente quelqu'un/quelque chose."),
          FormulaToken("Il/Elle est", "+ adjectif seul, ou métier sans article", TokenCategory.SUBJECT, "Décrit ou qualifie sans réintroduire d'article.")
        ),
        diagramTitle = "Arbre de Décision : C'est ou Il/Elle est ?",
        diagramDescription = "Identifiez ce qui suit le verbe pour choisir la bonne tournure :",
        decisionSteps = listOf(
          DecisionStep(1, "Le mot suivant est-il un nom propre ou un nom précédé d'un article/déterminant ?", "-> C'est (C'est Marie, c'est un ingénieur)", "-> Passez à l'étape 2"),
          DecisionStep(2, "Le mot suivant est-il un adjectif seul, sans nom ?", "-> Il/Elle est (Elle est sympathique)", "-> Passez à l'étape 3"),
          DecisionStep(3, "Le mot suivant est-il une profession SANS article, mais accompagnée d'un adjectif ?", "-> Revenez à C'est + article (C'est un bon ingénieur)", "-> Il/Elle est + métier seul (Il est ingénieur)")
        ),
        comparisonTableTitle = "Tableau de Contraste",
        comparisonHeaders = listOf("C'est", "Il/Elle est", "Différence"),
        comparisonRows = listOf(
          listOf("C'est un professeur.", "Il est professeur.", "Article présent vs absent devant le métier"),
          listOf("C'est un bon professeur.", "*Il est bon professeur. (incorrect)", "Adjectif + métier -> retour à C'est + article"),
          listOf("C'est intéressant.", "*Il est intéressant. (idée vague, incorrect)", "Idée générale/impersonnelle -> C'est")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "C'est mon frère, il est architecte à Lyon.",
          highlightedSegment = "il est architecte",
          englishSentence = "This is my brother, he's an architect in Lyon.",
          contextNote = "'C'est' pour présenter la personne, puis 'il est' + métier sans article."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Il est regrettable que ce projet n'ait pas abouti.",
          highlightedSegment = "Il est regrettable",
          englishSentence = "It is regrettable that this project did not come to fruition.",
          contextNote = "'Il est' impersonnel + adjectif, tournure soutenue équivalente à 'c'est regrettable'."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "C'est trop bien, ton nouvel appart !",
          highlightedSegment = "C'est trop bien",
          englishSentence = "It's so great, your new apartment!",
          contextNote = "'C'est' + adjectif à valeur exclamative, très courant à l'oral familier malgré l'absence de nom."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "C'est une excellente candidate, elle est très compétente.",
          highlightedSegment = "elle est très compétente",
          englishSentence = "She's an excellent candidate, she is very competent.",
          contextNote = "Contraste direct entre 'c'est' + nom qualifié et 'elle est' + adjectif seul."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Présentation d'un Collègue",
        fullTextFr = "Voici Karim, c'est un nouveau collègue. Il est ingénieur en informatique. C'est aussi un excellent joueur d'échecs. Il est très patient avec les débutants, et c'est quelqu'un de très apprécié dans l'équipe.",
        fullTextEn = "This is Karim, he's a new colleague. He is a computer engineer. He's also an excellent chess player. He is very patient with beginners, and he's someone very well liked on the team.",
        annotations = listOf(
          InlineAnnotation(
            id = "cei_ann_1",
            targetPhrase = "Il est ingénieur",
            explanationFr = "Métier seul, sans article ni adjectif : on utilise 'il est', jamais 'c'est ingénieur'.",
            explanationEn = "Plain profession, no article or adjective: use 'il est', never 'c'est ingénieur'.",
            whyItApplies = "Profession non qualifiée sans article.",
            commonPitfall = "'C'est ingénieur' est incorrect ; il faudrait soit 'il est ingénieur' soit 'c'est un ingénieur'."
          ),
          InlineAnnotation(
            id = "cei_ann_2",
            targetPhrase = "C'est aussi un excellent joueur",
            explanationFr = "Le nom 'joueur' est qualifié par l'adjectif 'excellent' et précédé d'un article : on revient à 'c'est'.",
            explanationEn = "The noun 'joueur' is qualified by 'excellent' and preceded by an article: back to 'c'est'.",
            whyItApplies = "Nom + adjectif + article -> c'est.",
            commonPitfall = "Ne dites pas 'il est excellent joueur d'échecs' sans article."
          ),
          InlineAnnotation(
            id = "cei_ann_3",
            targetPhrase = "Il est très patient",
            explanationFr = "Adjectif seul après le verbe : structure 'il est' + adjectif, sans article.",
            explanationEn = "Bare adjective after the verb: 'il est' + adjective structure, no article.",
            whyItApplies = "Adjectif seul -> il/elle est.",
            commonPitfall = "Ne mettez pas d'article devant un adjectif seul (pas de 'il est un patient')."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_cei_1",
          instructionFr = "Complétez avec 'C'est' ou 'Il est' :",
          instructionEn = "Complete with 'C'est' or 'Il est':",
          basePrompt = "_____ (C'est/Il est) médecin depuis dix ans.",
          targetAnswer = "Il est",
          acceptedAnswers = listOf("Il est"),
          hint = "Métier sans article, sans adjectif.",
          explanation = "Profession seule sans article : 'Il est médecin'."
        ),
        ProductionDrillItem(
          id = "prod_cei_2",
          instructionFr = "Complétez avec la forme correcte :",
          instructionEn = "Complete with the correct form:",
          basePrompt = "_____ (C'est/Elle est) une avocate très respectée.",
          targetAnswer = "C'est",
          acceptedAnswers = listOf("C'est"),
          hint = "Nom qualifié par un adjectif, précédé d'un article.",
          explanation = "'Avocate' est qualifiée par 'respectée' et précédée de l'article 'une' : on utilise 'c'est'."
        ),
        ProductionDrillItem(
          id = "prod_cei_3",
          instructionFr = "Complétez avec la forme correcte :",
          instructionEn = "Complete with the correct form:",
          basePrompt = "_____ (C'est/Il est) difficile de le convaincre.",
          targetAnswer = "Il est",
          acceptedAnswers = listOf("Il est", "C'est"),
          hint = "Tournure impersonnelle suivie d'un infinitif : les deux sont possibles selon le registre.",
          explanation = "'Il est difficile de' (soutenu) et 'C'est difficile de' (courant) sont tous deux acceptés devant un infinitif introduit par 'de'."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_cei_1",
          instructionFr = "Touchez l'erreur :",
          passage = "Il est un excellent professeur, tous les élèves l'adorent.",
          tokens = listOf("Il", "est", "un", "excellent", "professeur,", "tous", "les", "élèves", "l'adorent."),
          errorTokenIndex = 0,
          errorWord = "Il",
          correction = "C'",
          ruleExplanation = "Nom qualifié par un adjectif et précédé d'un article ('un excellent professeur') : il faut 'C'est', pas 'il est'."
        ),
        SpotErrorDrillItem(
          id = "spot_cei_2",
          instructionFr = "Trouvez l'erreur :",
          passage = "C'est fatigué après cette longue journée de travail.",
          tokens = listOf("C'est", "fatigué", "après", "cette", "longue", "journée", "de", "travail."),
          errorTokenIndex = 0,
          errorWord = "C'est",
          correction = "Il est",
          ruleExplanation = "Adjectif seul après une personne précise (sous-entendue) : 'Il est fatigué', pas 'C'est fatigué'."
        )
      )
    ),

    // 14. LES POSSESSIFS
    StructuredRule(
      id = "possessifs",
      categoryId = "cat-nom",
      categoryName = "Groupe Nominal",
      level = "A2",
      titleFr = "Les Possessifs : Adjectifs et Pronoms",
      titleEn = "Possessive Adjectives (my/your…) and Pronouns (mine/yours…)",
      summaryFr = "Contrairement à l'anglais, l'adjectif possessif s'accorde avec l'objet possédé, pas avec le possesseur : ma maison, mon chat, même si c'est la même personne qui parle.",
      summaryEn = "Unlike English, the possessive adjective agrees with the thing owned, not the owner: ma maison (my house, feminine) but mon chat (my cat, masculine) — even said by the same person.",
      pillar = GrammarPillar.AGREEMENT_ENGINE,
      ruleExplanationEn = "Forget the owner's gender entirely — it plays no role. Look only at the gender/number of the possessed noun: mon/ma/mes follow the noun, not 'je'. The one trap: 'ma' becomes 'mon' before a feminine noun starting with a vowel, purely for liaison (mon amie, not ma amie), even though 'amie' stays feminine.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("mon/ma/mes, ton/ta/tes...", "Adjectif possessif", TokenCategory.CONNECTOR, "S'accorde avec l'objet possédé, jamais avec le possesseur."),
          FormulaToken("le mien/la mienne/les miens...", "Pronom possessif", TokenCategory.SUBJECT, "Remplace 'adjectif possessif + nom', toujours précédé d'un article.")
        ),
        diagramTitle = "Arbre de Décision : Quel Possessif ?",
        diagramDescription = "Identifiez le genre/nombre de l'objet possédé (pas du possesseur !) :",
        decisionSteps = listOf(
          DecisionStep(1, "L'objet possédé est-il féminin singulier commençant par une consonne ?", "-> ma/ta/sa (ma voiture)", "-> Passez à l'étape 2"),
          DecisionStep(2, "L'objet possédé est-il féminin singulier commençant par une voyelle ?", "-> mon/ton/son pour la liaison (mon amie)", "-> Passez à l'étape 3"),
          DecisionStep(3, "L'objet possédé est-il au pluriel (n'importe quel genre) ?", "-> mes/tes/ses (mes amis, mes amies)", "-> Masculin singulier : mon/ton/son")
        ),
        comparisonTableTitle = "Tableau des Possessifs",
        comparisonHeaders = listOf("Possesseur", "Masc. sg.", "Fém. sg.", "Pluriel"),
        comparisonRows = listOf(
          listOf("je", "mon", "ma (mon + voyelle)", "mes"),
          listOf("tu", "ton", "ta (ton + voyelle)", "tes"),
          listOf("il/elle", "son", "sa (son + voyelle)", "ses"),
          listOf("nous", "notre", "notre", "nos"),
          listOf("vous", "votre", "votre", "vos"),
          listOf("ils/elles", "leur", "leur", "leurs")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Mon frère et sa femme viennent dîner avec leurs enfants ce soir.",
          highlightedSegment = "leurs enfants",
          englishSentence = "My brother and his wife are coming to dinner with their children tonight.",
          contextNote = "'Leurs' au pluriel, accordé avec 'enfants', quel que soit le nombre de possesseurs (le couple)."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Chacun des membres a exprimé son opinion lors de l'assemblée.",
          highlightedSegment = "son opinion",
          englishSentence = "Each of the members expressed their opinion during the assembly.",
          contextNote = "'Son' avec 'chacun', usage soutenu du singulier distributif."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "T'as vu ma tête ce matin, j'ai pas dormi !",
          highlightedSegment = "ma tête",
          englishSentence = "Did you see my face this morning, I didn't sleep!",
          contextNote = "Usage familier oral typique avec élision (T'as) et possessif standard."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Veuillez trouver ci-joint notre proposition ainsi que nos conditions tarifaires.",
          highlightedSegment = "notre proposition",
          englishSentence = "Please find attached our proposal as well as our pricing terms.",
          contextNote = "'Notre' + singulier et 'nos' + pluriel dans une formule commerciale standard."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Un Album de Famille",
        fullTextFr = "Voici mon appartement, et ma amie... non, mon amie Julie devant la fenêtre. Sa sœur habite juste à côté avec ses deux chats. Nos voisins sont très sympathiques et leur jardin est magnifique.",
        fullTextEn = "Here is my apartment, and my friend... no, my friend Julie in front of the window. Her sister lives right next door with her two cats. Our neighbors are very nice and their garden is beautiful.",
        annotations = listOf(
          InlineAnnotation(
            id = "poss_ann_1",
            targetPhrase = "mon amie Julie",
            explanationFr = "Bien que 'amie' soit féminin, on utilise 'mon' (et non 'ma') car le mot commence par une voyelle, pour la liaison.",
            explanationEn = "Although 'amie' is feminine, 'mon' (not 'ma') is used because the word starts with a vowel, for liaison.",
            whyItApplies = "Liaison devant un nom féminin à voyelle initiale.",
            commonPitfall = "'Ma amie' est incorrect et se prononce mal ; il faut toujours 'mon amie' devant une voyelle."
          ),
          InlineAnnotation(
            id = "poss_ann_2",
            targetPhrase = "Sa sœur",
            explanationFr = "'Sa' s'accorde avec 'sœur' (féminin), pas avec le genre du possesseur qui reste inconnu ici.",
            explanationEn = "'Sa' agrees with 'sœur' (feminine), not with the owner's gender, which is unknown here.",
            whyItApplies = "Accord sur l'objet possédé, jamais sur le possesseur.",
            commonPitfall = "Ne traduisez pas littéralement 'his/her' — le français n'indique jamais le genre du possesseur dans le possessif singulier."
          ),
          InlineAnnotation(
            id = "poss_ann_3",
            targetPhrase = "leur jardin",
            explanationFr = "'Leur' (sans -s) reste au singulier car 'jardin' est singulier, même si les possesseurs sont plusieurs.",
            explanationEn = "'Leur' (without -s) stays singular because 'jardin' is singular, even though there are several owners.",
            whyItApplies = "Accord avec l'objet possédé, pas avec le nombre de possesseurs.",
            commonPitfall = "'Leurs jardin' est incorrect ; 's' ne s'ajoute à 'leur' que si l'objet possédé est au pluriel."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_poss_1",
          instructionFr = "Complétez avec l'adjectif possessif correct :",
          instructionEn = "Complete with the correct possessive adjective:",
          basePrompt = "Elle a oublié _____ (son/sa) écharpe au bureau.",
          targetAnswer = "son",
          acceptedAnswers = listOf("son"),
          hint = "'Écharpe' est féminin mais commence par une voyelle.",
          explanation = "Devant un nom féminin à initiale vocalique, on utilise 'son' pour la liaison : son écharpe."
        ),
        ProductionDrillItem(
          id = "prod_poss_2",
          instructionFr = "Complétez avec le possessif pluriel correct :",
          instructionEn = "Complete with the correct plural possessive:",
          basePrompt = "Nous avons invité tous _____ (notre/nos) collègues à la fête.",
          targetAnswer = "nos",
          acceptedAnswers = listOf("nos"),
          hint = "'Collègues' est au pluriel.",
          explanation = "'Nos' s'utilise devant un nom pluriel, quel que soit son genre : nos collègues."
        ),
        ProductionDrillItem(
          id = "prod_poss_3",
          instructionFr = "Complétez avec le possessif correct (possesseur au pluriel) :",
          instructionEn = "Complete with the correct possessive (plural owner):",
          basePrompt = "Les enfants ont perdu _____ (leur/leurs) ballon dans le parc.",
          targetAnswer = "leur",
          acceptedAnswers = listOf("leur"),
          hint = "'Ballon' est singulier.",
          explanation = "'Leur' sans -s car l'objet possédé, 'ballon', est singulier — même si plusieurs enfants le possèdent ensemble."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_poss_1",
          instructionFr = "Touchez le possessif incorrect :",
          passage = "J'ai laissé ma imperméable dans le train ce matin.",
          tokens = listOf("J'ai", "laissé", "ma", "imperméable", "dans", "le", "train", "ce", "matin."),
          errorTokenIndex = 2,
          errorWord = "ma",
          correction = "mon",
          ruleExplanation = "'Imperméable' commence par une voyelle : il faut utiliser 'mon' (liaison), pas 'ma', même si le nom est féminin."
        ),
        SpotErrorDrillItem(
          id = "spot_poss_2",
          instructionFr = "Trouvez l'erreur d'accord possessif :",
          passage = "Ils ont vendu leurs maison pour déménager à la campagne.",
          tokens = listOf("Ils", "ont", "vendu", "leurs", "maison", "pour", "déménager", "à", "la", "campagne."),
          errorTokenIndex = 3,
          errorWord = "leurs",
          correction = "leur",
          ruleExplanation = "'Maison' est singulier, donc le possessif doit rester au singulier : 'leur maison', sans -s."
        )
      )
    ),

    // 15. LES NOMBRES, L'HEURE ET LA DATE
    StructuredRule(
      id = "nombres-heure-date",
      categoryId = "cat-nom",
      categoryName = "Groupe Nominal",
      level = "A2",
      titleFr = "Les Nombres, l'Heure et la Date",
      titleEn = "Numbers, Time and Dates",
      summaryFr = "Nombres cardinaux et ordinaux, mots collectifs (dizaine, douzaine, centaine), nombre vs numéro, an vs année, l'heure et la date.",
      summaryEn = "Cardinal and ordinal numbers, collective words (dizaine, douzaine, centaine), nombre vs numéro, an vs année, telling time, and dates.",
      pillar = GrammarPillar.OTHER,
      ruleExplanationEn = "Two easily-confused pairs to lock down: 'nombre' is the abstract concept of quantity ('un grand nombre de personnes'), while 'numéro' is an identifying label ('le numéro de téléphone'). Similarly, 'an' is used with a specific count ('j'ai trente ans'), while 'année' is used when describing the year as a duration or a period with qualities ('une bonne année', 'toute l'année'). For the clock, French officially uses the 24-hour format in formal/written contexts (14h30) but also freely uses 12-hour + 'du matin/de l'après-midi/du soir' in speech.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("nombre", "Quantité abstraite", TokenCategory.SUBJECT, "Un nombre de, un grand nombre..."),
          FormulaToken("numéro", "Identifiant / label", TokenCategory.SUBJECT, "Numéro de téléphone, numéro un..."),
          FormulaToken("Il est + heure + h + minutes", "Formule de l'heure", TokenCategory.CONNECTOR, "Il est 14h30 / Il est deux heures et demie.")
        ),
        diagramTitle = "Arbre de Décision : An ou Année ? Nombre ou Numéro ?",
        diagramDescription = "Choisissez le bon mot selon le contexte :",
        decisionSteps = listOf(
          DecisionStep(1, "Parlez-vous d'un âge ou d'un compte précis d'années ?", "-> an (j'ai vingt ans, dans deux ans)", "-> année (décrire une période/qualité : une année difficile)"),
          DecisionStep(2, "Parlez-vous d'une quantité abstraite de choses ?", "-> nombre (un grand nombre d'étudiants)", "-> numéro (identifiant précis : numéro de rue)"),
          DecisionStep(3, "Donnez-vous l'heure à l'oral courant ou dans un contexte officiel (horaires, billets) ?", "-> Format 24h officiel (14h30, 20h00)", "-> Format 12h + moment de la journée à l'oral (deux heures et demie de l'après-midi)")
        ),
        comparisonTableTitle = "Tableau Comparatif",
        comparisonHeaders = listOf("Mot", "Usage", "Exemple"),
        comparisonRows = listOf(
          listOf("an", "compte précis, âge", "J'ai vingt-cinq ans"),
          listOf("année", "durée/qualité", "Bonne année ! / Toute l'année"),
          listOf("nombre", "quantité abstraite", "Un nombre incalculable"),
          listOf("numéro", "identifiant", "Le numéro 5 de la rue")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Le train part à dix-huit heures quarante-cinq, quai numéro trois.",
          highlightedSegment = "numéro trois",
          englishSentence = "The train leaves at 6:45pm, platform number three.",
          contextNote = "Heure au format 24h dans une annonce publique, et 'numéro' pour l'identifiant du quai."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Un nombre croissant de citoyens s'interrogent sur l'avenir de la région.",
          highlightedSegment = "Un nombre croissant",
          englishSentence = "A growing number of citizens are questioning the region's future.",
          contextNote = "'Nombre' pour une quantité abstraite dans un registre journalistique/soutenu."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "On se voit vers sept heures et demie, ça te va ?",
          highlightedSegment = "sept heures et demie",
          englishSentence = "See you around 7:30, does that work for you?",
          contextNote = "Heure au format 12h à l'oral familier, sans préciser matin/soir car le contexte est clair."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Merci de nous communiquer votre numéro de commande avant le 15 du mois.",
          highlightedSegment = "numéro de commande",
          englishSentence = "Please send us your order number before the 15th of the month.",
          contextNote = "'Numéro' pour un identifiant administratif, contexte professionnel écrit."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Un Rendez-vous Important",
        fullTextFr = "Le rendez-vous est fixé au vingt-deux mars à quatorze heures trente, dans la salle numéro douze. Un grand nombre de collègues seront présents. Cela fait déjà trois ans que ce projet a commencé, et cette année sera décisive.",
        fullTextEn = "The meeting is set for March twenty-second at 2:30pm, in room number twelve. A large number of colleagues will be present. It's already been three years since this project started, and this year will be decisive.",
        annotations = listOf(
          InlineAnnotation(
            id = "num_ann_1",
            targetPhrase = "salle numéro douze",
            explanationFr = "'Numéro' identifie une salle précise parmi d'autres, comme un label.",
            explanationEn = "'Numéro' identifies a specific room among others, like a label.",
            whyItApplies = "Identifiant, pas quantité abstraite.",
            commonPitfall = "Ne dites pas 'salle nombre douze' — 'numéro' est le mot pour identifier."
          ),
          InlineAnnotation(
            id = "num_ann_2",
            targetPhrase = "trois ans",
            explanationFr = "'Ans' est utilisé pour un compte précis de durée écoulée.",
            explanationEn = "'Ans' is used for a precise count of elapsed time.",
            whyItApplies = "Compte précis -> an, pas année.",
            commonPitfall = "Ne dites pas 'trois années' pour un simple compte chiffré au pluriel avec un nombre cardinal directement devant."
          ),
          InlineAnnotation(
            id = "num_ann_3",
            targetPhrase = "cette année sera décisive",
            explanationFr = "'Année' est utilisé car on décrit la qualité de la période (décisive), pas un simple compte.",
            explanationEn = "'Année' is used because the quality of the period is being described (decisive), not a plain count.",
            whyItApplies = "Description qualitative d'une période -> année.",
            commonPitfall = "Ne dites pas 'cet an sera décisif' — avec un adjectif qualitatif, on utilise 'année'."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_num_1",
          instructionFr = "Choisissez le mot correct :",
          instructionEn = "Choose the correct word:",
          basePrompt = "Il a quarante _____ (an/année) cette _____ (an/année).",
          targetAnswer = "ans, année",
          acceptedAnswers = listOf("ans, année", "ans année"),
          hint = "Compte précis d'abord, puis qualité de la période.",
          explanation = "'Quarante ans' (compte précis) puis 'cette année' (période désignée par 'cette')."
        ),
        ProductionDrillItem(
          id = "prod_num_2",
          instructionFr = "Choisissez le mot correct :",
          instructionEn = "Choose the correct word:",
          basePrompt = "Quel est votre _____ (nombre/numéro) de téléphone ?",
          targetAnswer = "numéro",
          acceptedAnswers = listOf("numéro"),
          hint = "Il s'agit d'un identifiant.",
          explanation = "'Numéro de téléphone' est un identifiant fixe, donc 'numéro', jamais 'nombre'."
        ),
        ProductionDrillItem(
          id = "prod_num_3",
          instructionFr = "Écrivez l'heure en format 24h officiel :",
          instructionEn = "Write the time in official 24h format:",
          basePrompt = "Neuf heures moins le quart du soir s'écrit officiellement : _____",
          targetAnswer = "20h45",
          acceptedAnswers = listOf("20h45", "vingt heures quarante-cinq"),
          hint = "Neuf heures du soir = 21h, moins le quart = -15 minutes.",
          explanation = "21h00 moins 15 minutes donne 20h45, l'écriture officielle en format 24 heures."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_num_1",
          instructionFr = "Touchez le mot mal choisi :",
          passage = "Composez le nombre de la salle pour joindre la réception.",
          tokens = listOf("Composez", "le", "nombre", "de", "la", "salle", "pour", "joindre", "la", "réception."),
          errorTokenIndex = 2,
          errorWord = "nombre",
          correction = "numéro",
          ruleExplanation = "Il s'agit d'un identifiant précis (le numéro d'une salle), donc il faut 'numéro', pas 'nombre'."
        ),
        SpotErrorDrillItem(
          id = "spot_num_2",
          instructionFr = "Trouvez l'erreur an/année :",
          passage = "J'ai vécu cinq années à Marseille avant de déménager.",
          tokens = listOf("J'ai", "vécu", "cinq", "années", "à", "Marseille", "avant", "de", "déménager."),
          errorTokenIndex = 3,
          errorWord = "années",
          correction = "ans",
          ruleExplanation = "Un nombre cardinal précis directement devant impose 'ans' (compte), pas 'années' (durée qualitative) : cinq ans."
        )
      )
    ),

    // 16. LES PRONOMS TONIQUES
    StructuredRule(
      id = "pronoms-toniques",
      categoryId = "cat-pronoms",
      categoryName = "Pronoms",
      level = "A2",
      titleFr = "Les Pronoms Toniques : moi, toi, lui, elle...",
      titleEn = "Stressed Pronouns: moi, toi, lui, elle, nous, vous, eux, elles",
      summaryFr = "Les pronoms toniques s'emploient après une préposition, pour insister sur le sujet, dans une comparaison, ou seuls sans verbe.",
      summaryEn = "Stressed pronouns are used after a preposition, to emphasize the subject, in comparisons, or alone with no verb.",
      pillar = GrammarPillar.PRONOUN_HIERARCHY,
      ruleExplanationEn = "These are the pronouns you reach for whenever a subject pronoun (je, tu, il...) can't stand alone or can't follow a preposition. Four clear triggers: right after any preposition (avec moi, chez lui, pour eux), for emphasis alongside the subject (moi, je pense que...), in comparisons after 'que' (plus grand que toi), and as a standalone answer with no verb (Qui est là ? — Moi.).",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Préposition + pronom tonique", "avec moi, chez lui, pour eux", TokenCategory.CONNECTOR, "Jamais 'avec je' ou 'pour ils'."),
          FormulaToken("Pronom tonique, sujet + verbe", "Emphase", TokenCategory.SUBJECT, "Moi, je pense que... (redondance volontaire pour insister).")
        ),
        diagramTitle = "Arbre de Décision : Faut-il un Pronom Tonique ?",
        diagramDescription = "Identifiez le contexte grammatical :",
        decisionSteps = listOf(
          DecisionStep(1, "Le pronom suit-il directement une préposition (avec, pour, chez, sans, sauf...) ?", "-> Pronom tonique obligatoire (avec moi)", "-> Passez à l'étape 2"),
          DecisionStep(2, "Voulez-vous insister sur le sujet ou répondre sans verbe ?", "-> Pronom tonique (Moi, je...; Qui ? — Toi.)", "-> Passez à l'étape 3"),
          DecisionStep(3, "Le pronom est-il dans une comparaison après 'que' ou 'comme' ?", "-> Pronom tonique (plus fort que lui)", "-> Sinon, utilisez un pronom sujet ou objet normal")
        ),
        comparisonTableTitle = "Pronoms Sujets vs Toniques",
        comparisonHeaders = listOf("Sujet", "Tonique", "Exemple"),
        comparisonRows = listOf(
          listOf("je", "moi", "Moi, je pars."),
          listOf("tu", "toi", "C'est pour toi."),
          listOf("il", "lui", "Chez lui, tout est rangé."),
          listOf("ils", "eux", "Sans eux, rien n'est possible.")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Tu viens avec nous ce week-end ?",
          highlightedSegment = "avec nous",
          englishSentence = "Are you coming with us this weekend?",
          contextNote = "Pronom tonique obligatoire après la préposition 'avec'."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Quant à eux, ils n'ont exprimé aucune objection.",
          highlightedSegment = "Quant à eux",
          englishSentence = "As for them, they raised no objection.",
          contextNote = "Locution soutenue 'quant à' + pronom tonique pour introduire un sujet contrastif."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "Lui, il comprend jamais rien !",
          highlightedSegment = "Lui, il",
          englishSentence = "Him, he never understands anything!",
          contextNote = "Double marquage du sujet (pronom tonique + pronom sujet) très fréquent à l'oral familier pour insister."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Ce dossier a été confié à elle en raison de son expertise.",
          highlightedSegment = "confié à elle",
          englishSentence = "This file was entrusted to her because of her expertise.",
          contextNote = "Pronom tonique après la préposition 'à' pour désigner une personne précise, registre professionnel."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Une Sortie entre Amis",
        fullTextFr = "Moi, je propose qu'on aille au cinéma. Toi, qu'est-ce que tu en penses ? Sans eux, la soirée ne serait pas aussi drôle. Lui, il préfère toujours rester chez lui à regarder des films.",
        fullTextEn = "Me, I suggest we go to the cinema. You, what do you think? Without them, the evening wouldn't be as fun. Him, he always prefers staying at his place watching movies.",
        annotations = listOf(
          InlineAnnotation(
            id = "ton_ann_1",
            targetPhrase = "Moi, je propose",
            explanationFr = "Le pronom tonique 'moi' renforce le sujet 'je' pour marquer l'insistance ou introduire son opinion.",
            explanationEn = "The stressed pronoun 'moi' reinforces the subject 'je' to add emphasis or introduce one's opinion.",
            whyItApplies = "Emphase sur le sujet.",
            commonPitfall = "Ne remplacez jamais 'je' par 'moi' comme sujet du verbe : 'moi' s'ajoute, il ne remplace pas."
          ),
          InlineAnnotation(
            id = "ton_ann_2",
            targetPhrase = "Sans eux",
            explanationFr = "'Eux' suit directement la préposition 'sans', ce qui impose le pronom tonique.",
            explanationEn = "'Eux' directly follows the preposition 'sans', which requires the stressed pronoun.",
            whyItApplies = "Pronom après préposition.",
            commonPitfall = "Ne dites jamais 'sans ils' — après une préposition, seul le pronom tonique est possible."
          ),
          InlineAnnotation(
            id = "ton_ann_3",
            targetPhrase = "chez lui",
            explanationFr = "'Chez' est une préposition qui impose systématiquement un pronom tonique.",
            explanationEn = "'Chez' is a preposition that always requires a stressed pronoun.",
            whyItApplies = "Préposition + pronom tonique.",
            commonPitfall = "'Chez il' est impossible ; seule la forme tonique 'chez lui' existe."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_ton_1",
          instructionFr = "Complétez avec le pronom tonique correct :",
          instructionEn = "Complete with the correct stressed pronoun:",
          basePrompt = "Ce cadeau est pour _____ (toi/tu), pas pour ton frère.",
          targetAnswer = "toi",
          acceptedAnswers = listOf("toi"),
          hint = "Après une préposition, on utilise le pronom tonique.",
          explanation = "Après 'pour', on utilise obligatoirement le pronom tonique 'toi', pas 'tu'."
        ),
        ProductionDrillItem(
          id = "prod_ton_2",
          instructionFr = "Complétez pour insister sur le sujet :",
          instructionEn = "Complete to emphasize the subject:",
          basePrompt = "_____ (Eux/Ils), ils n'ont même pas répondu à notre message.",
          targetAnswer = "Eux",
          acceptedAnswers = listOf("Eux"),
          hint = "Pronom tonique en début de phrase pour l'emphase, redoublé par le pronom sujet.",
          explanation = "'Eux' en tête de phrase insiste sur le sujet, repris ensuite par le pronom sujet 'ils'."
        ),
        ProductionDrillItem(
          id = "prod_ton_3",
          instructionFr = "Complétez la comparaison :",
          instructionEn = "Complete the comparison:",
          basePrompt = "Elle court plus vite que _____ (lui/il).",
          targetAnswer = "lui",
          acceptedAnswers = listOf("lui"),
          hint = "Dans une comparaison après 'que', on utilise le pronom tonique.",
          explanation = "Après 'que' dans une comparaison, le pronom tonique 'lui' est requis, pas 'il'."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_ton_1",
          instructionFr = "Touchez l'erreur de pronom :",
          passage = "Nous partons en vacances sans ils cette année.",
          tokens = listOf("Nous", "partons", "en", "vacances", "sans", "ils", "cette", "année."),
          errorTokenIndex = 5,
          errorWord = "ils",
          correction = "eux",
          ruleExplanation = "Après la préposition 'sans', il faut utiliser le pronom tonique 'eux', jamais le pronom sujet 'ils'."
        ),
        SpotErrorDrillItem(
          id = "spot_ton_2",
          instructionFr = "Trouvez l'erreur :",
          passage = "Tu es plus patient que je dans cette situation.",
          tokens = listOf("Tu", "es", "plus", "patient", "que", "je", "dans", "cette", "situation."),
          errorTokenIndex = 5,
          errorWord = "je",
          correction = "moi",
          ruleExplanation = "Dans une comparaison après 'que', on utilise le pronom tonique 'moi', jamais le pronom sujet 'je'."
        )
      )
    ),

    // 17. LE PRONOM ON
    StructuredRule(
      id = "pronom-on",
      categoryId = "cat-pronoms",
      categoryName = "Pronoms",
      level = "A2",
      titleFr = "Le Pronom « On » : Trois Emplois",
      titleEn = "The Pronoun \"On\"",
      summaryFr = "« On » remplace « nous » à l'oral familier, exprime une vérité générale (les gens, tout le monde), ou désigne un sujet indéterminé — toujours conjugué à la 3e personne du singulier.",
      summaryEn = "\"On\" replaces \"nous\" in casual speech, expresses a general statement (people, everyone), or denotes an unspecified subject — always conjugated in the 3rd person singular.",
      pillar = GrammarPillar.OTHER,
      ruleExplanationEn = "Whatever 'on' actually refers to, the verb is grammatically always 3rd person singular ('on va', not 'on vont'), even when it means 'we' and the underlying idea is plural. When 'on' clearly means 'nous' in casual speech, a following adjective can still agree in the plural, since agreement can track the real, understood meaning ('on est fatigués', not 'fatigué', if the group is mixed/plural) — this is a common exception worth flagging, distinct from the verb form which never changes.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("On", "Sujet grammatical singulier", TokenCategory.SUBJECT, "Toujours suivi d'un verbe à la 3e pers. du singulier."),
          FormulaToken("+ verbe (il/elle)", "Conjugaison inchangée", TokenCategory.TARGET_VERB, "On va, on fait, on est — jamais 'on vont'.")
        ),
        diagramTitle = "Arbre de Décision : Quel Sens de 'On' ?",
        diagramDescription = "Identifiez le sens visé par 'on' dans le contexte :",
        decisionSteps = listOf(
          DecisionStep(1, "'On' remplace-t-il 'nous' dans une conversation familière ?", "-> Sens 'nous' (On y va ! = Nous y allons !)", "-> Passez à l'étape 2"),
          DecisionStep(2, "'On' exprime-t-il une vérité générale, valable pour tout le monde ?", "-> Sens général (En France, on mange à midi.)", "-> Passez à l'étape 3"),
          DecisionStep(3, "Le sujet réel est-il inconnu ou non précisé ?", "-> Sens indéterminé (On a sonné à la porte.)", "-> Vérifiez le contexte pour choisir le bon sens")
        ),
        comparisonTableTitle = "Les Trois Emplois de 'On'",
        comparisonHeaders = listOf("Emploi", "Exemple", "Équivalent"),
        comparisonRows = listOf(
          listOf("= nous (familier)", "On mange à midi ?", "Nous mangeons à midi ?"),
          listOf("= tout le monde/les gens", "On dit que l'hiver sera froid.", "Les gens disent que..."),
          listOf("= quelqu'un d'indéterminé", "On a frappé à la porte.", "Quelqu'un a frappé...")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "On se retrouve devant le cinéma à huit heures ?",
          highlightedSegment = "On se retrouve",
          englishSentence = "Shall we meet in front of the cinema at eight?",
          contextNote = "'On' = 'nous', usage courant très fréquent à l'oral comme à l'écrit informel."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "On ne saurait trop insister sur l'importance de cette réforme.",
          highlightedSegment = "On ne saurait",
          englishSentence = "One cannot overstate the importance of this reform.",
          contextNote = "'On' impersonnel dans une tournure soutenue équivalente au 'one' anglais."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "On est trop fatigués, on rentre.",
          highlightedSegment = "on est trop fatigués",
          englishSentence = "We're too tired, we're heading home.",
          contextNote = "Verbe au singulier ('est') mais adjectif accordé au pluriel ('fatigués'), car le sens réel est 'nous' (plusieurs personnes)."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "On observe une nette progression du chiffre d'affaires ce trimestre.",
          highlightedSegment = "On observe",
          englishSentence = "A clear increase in revenue is observed this quarter.",
          contextNote = "'On' impersonnel remplaçant une tournure passive, fréquent dans les rapports professionnels."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Un Week-end entre Amis",
        fullTextFr = "On part tous ensemble samedi matin, ça vous va ? En général, on dit que la montagne est magnifique en automne. On ne sait jamais qui va sonner à la porte du chalet ! Le soir, on sera sûrement fatigués après la randonnée.",
        fullTextEn = "We're all leaving together Saturday morning, does that work for you? In general, people say the mountains are beautiful in autumn. You never know who's going to ring the chalet's doorbell! In the evening, we'll surely be tired after the hike.",
        annotations = listOf(
          InlineAnnotation(
            id = "on_ann_1",
            targetPhrase = "On part tous ensemble",
            explanationFr = "'On' remplace ici 'nous', usage typique de la conversation courante entre amis.",
            explanationEn = "'On' replaces 'nous' here, typical usage in casual conversation among friends.",
            whyItApplies = "'On' = 'nous' familier.",
            commonPitfall = "Le verbe reste conjugué au singulier ('part'), jamais 'partent', même si le sens est pluriel."
          ),
          InlineAnnotation(
            id = "on_ann_2",
            targetPhrase = "on dit que",
            explanationFr = "'On' exprime ici une vérité générale, équivalente à 'les gens disent que'.",
            explanationEn = "'On' expresses a general truth here, equivalent to 'people say that'.",
            whyItApplies = "'On' = affirmation générale.",
            commonPitfall = "Ne confondez pas ce sens général avec le sens 'nous' : le contexte (pas de groupe précis) le montre."
          ),
          InlineAnnotation(
            id = "on_ann_3",
            targetPhrase = "sera sûrement fatigués",
            explanationFr = "L'adjectif 'fatigués' s'accorde au pluriel car le sens réel de 'on' est un groupe de plusieurs personnes, même si le verbe reste au singulier.",
            explanationEn = "The adjective 'fatigués' agrees in the plural because the real meaning of 'on' is a group of several people, even though the verb stays singular.",
            whyItApplies = "Accord de l'adjectif selon le sens réel, pas la forme grammaticale du verbe.",
            commonPitfall = "Le verbe ('sera') reste singulier même quand l'adjectif qui suit s'accorde au pluriel."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_on_1",
          instructionFr = "Conjuguez le verbe avec 'on' :",
          instructionEn = "Conjugate the verb with 'on':",
          basePrompt = "On _____ (aller) au marché tous les samedis.",
          targetAnswer = "va",
          acceptedAnswers = listOf("va"),
          hint = "'On' est toujours suivi de la 3e personne du singulier.",
          explanation = "Même si 'on' signifie 'nous' ici, le verbe reste à la 3e personne du singulier : on va."
        ),
        ProductionDrillItem(
          id = "prod_on_2",
          instructionFr = "Accordez l'adjectif selon le sens réel de 'on' :",
          instructionEn = "Agree the adjective according to the real meaning of 'on':",
          basePrompt = "Ma sœur et moi, on est _____ (content) de vous voir.",
          targetAnswer = "contents",
          acceptedAnswers = listOf("contents", "contentes"),
          hint = "Le sens réel est pluriel (deux personnes).",
          explanation = "L'adjectif s'accorde au pluriel car 'on' représente ici deux personnes ('ma sœur et moi')."
        ),
        ProductionDrillItem(
          id = "prod_on_3",
          instructionFr = "Identifiez le sens de 'on' et complétez :",
          instructionEn = "Identify the meaning of 'on' and complete:",
          basePrompt = "On _____ (frapper) à la porte, va voir qui c'est !",
          targetAnswer = "a frappé",
          acceptedAnswers = listOf("a frappé"),
          hint = "Ici, 'on' désigne un sujet indéterminé, inconnu.",
          explanation = "'On' désigne ici une personne indéterminée (on ne sait pas qui), tout en gardant la 3e personne du singulier."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_on_1",
          instructionFr = "Touchez l'erreur de conjugaison :",
          passage = "On vont au cinéma ce soir, tu veux venir ?",
          tokens = listOf("On", "vont", "au", "cinéma", "ce", "soir,", "tu", "veux", "venir?"),
          errorTokenIndex = 1,
          errorWord = "vont",
          correction = "va",
          ruleExplanation = "'On' est toujours suivi d'un verbe à la 3e personne du singulier : 'on va', jamais 'on vont'."
        ),
        SpotErrorDrillItem(
          id = "spot_on_2",
          instructionFr = "Trouvez l'erreur :",
          passage = "En général, en France, ils mangent le dîner assez tard.",
          tokens = listOf("En", "général,", "en", "France,", "ils", "mangent", "le", "dîner", "assez", "tard."),
          errorTokenIndex = 4,
          errorWord = "ils",
          correction = "on",
          ruleExplanation = "Pour exprimer une vérité générale ('les gens en général'), le français utilise 'on', pas 'ils' qui suppose un groupe précis déjà identifié."
        )
      )
    ),

    // 18. LES VERBES PRONOMINAUX
    StructuredRule(
      id = "verbes-pronominaux",
      categoryId = "cat-verbes",
      categoryName = "Modes & Temps",
      level = "A2",
      titleFr = "Les Verbes Pronominaux : Réfléchis, Réciproques, Idiomatiques",
      titleEn = "Reflexive, Reciprocal, and Idiomatic Pronominal Verbs",
      summaryFr = "Les verbes pronominaux se conjuguent avec un pronom réfléchi (me, te, se, nous, vous, se) qui renvoie au sujet ; ils se répartissent en trois types : réfléchis, réciproques, et idiomatiques.",
      summaryEn = "Pronominal verbs are conjugated with a reflexive pronoun (me, te, se, nous, vous, se) referring back to the subject; they fall into three types: reflexive, reciprocal, and idiomatic.",
      pillar = GrammarPillar.PRONOUN_HIERARCHY,
      ruleExplanationEn = "Reflexive verbs describe the subject acting on itself (je me lave — I wash myself); reciprocal verbs describe two or more subjects acting on each other, only possible in the plural (ils se parlent — they talk to each other); idiomatic pronominal verbs simply have a meaning that doesn't decompose at all (se souvenir, s'en aller) — the pronoun is just part of the verb's identity, not a literal reflection. All three types use 'être' as the auxiliary in the passé composé, and the past participle usually agrees with the reflexive pronoun when it functions as a direct object.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Sujet", "je, tu, il...", TokenCategory.SUBJECT, "Le sujet du verbe pronominal."),
          FormulaToken("me/te/se/nous/vous/se", "Pronom réfléchi", TokenCategory.CONNECTOR, "S'accorde toujours avec le sujet en personne/nombre."),
          FormulaToken("Verbe", "laver, parler, souvenir...", TokenCategory.TARGET_VERB, "Auxiliaire ÊTRE obligatoire au passé composé.")
        ),
        diagramTitle = "Arbre de Décision : Quel Type de Pronominal ?",
        diagramDescription = "Identifiez le type de verbe pronominal :",
        decisionSteps = listOf(
          DecisionStep(1, "Le sujet agit-il sur lui-même ?", "-> Réfléchi (je me lave)", "-> Passez à l'étape 2"),
          DecisionStep(2, "Le sujet est-il pluriel et l'action se fait-elle mutuellement entre eux ?", "-> Réciproque (ils se téléphonent)", "-> Passez à l'étape 3"),
          DecisionStep(3, "Le verbe a-t-il un sens qui ne se décompose pas littéralement (se souvenir, s'en aller) ?", "-> Idiomatique — apprenez-le comme un tout", "-> Vérifiez le sens dans un dictionnaire")
        ),
        comparisonTableTitle = "Les Trois Types",
        comparisonHeaders = listOf("Type", "Exemple", "Sens"),
        comparisonRows = listOf(
          listOf("Réfléchi", "Elle se regarde dans le miroir.", "Elle regarde elle-même."),
          listOf("Réciproque", "Ils se regardent depuis longtemps.", "Ils se regardent l'un l'autre."),
          listOf("Idiomatique", "Je me souviens de toi.", "Sens propre, non littéral.")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Nous nous levons tous les jours à sept heures.",
          highlightedSegment = "Nous nous levons",
          englishSentence = "We get up every day at seven.",
          contextNote = "Verbe pronominal réfléchi conjugué normalement au présent."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Les deux délégations se sont rencontrées à plusieurs reprises.",
          highlightedSegment = "se sont rencontrées",
          englishSentence = "The two delegations met on several occasions.",
          contextNote = "Verbe pronominal réciproque au passé composé, accord du participe passé au féminin pluriel avec 'délégations'."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "Magne-toi, on va être en retard !",
          highlightedSegment = "Magne-toi",
          englishSentence = "Hurry up, we're going to be late!",
          contextNote = "Verbe pronominal idiomatique 'se magner' (se dépêcher), très familier, à l'impératif."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Les deux entreprises se sont associées pour ce projet innovant.",
          highlightedSegment = "se sont associées",
          englishSentence = "The two companies partnered up for this innovative project.",
          contextNote = "Verbe pronominal réciproque au passé composé dans un contexte professionnel/commercial."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Une Amitié de Longue Date",
        fullTextFr = "Ils se sont rencontrés à l'université il y a vingt ans. Depuis, ils s'écrivent régulièrement et se téléphonent chaque dimanche. Elle se souvient encore de leur premier voyage ensemble. Chaque fois qu'ils se voient, ils se racontent tout.",
        fullTextEn = "They met at university twenty years ago. Since then, they write to each other regularly and call each other every Sunday. She still remembers their first trip together. Every time they see each other, they tell each other everything.",
        annotations = listOf(
          InlineAnnotation(
            id = "prono_ann_1",
            targetPhrase = "se sont rencontrés",
            explanationFr = "Verbe pronominal réciproque : le sujet pluriel 'ils' agit mutuellement l'un sur l'autre.",
            explanationEn = "Reciprocal pronominal verb: the plural subject 'ils' acts mutually on each other.",
            whyItApplies = "Action réciproque entre plusieurs sujets.",
            commonPitfall = "L'auxiliaire est toujours 'être' pour les verbes pronominaux, jamais 'avoir'."
          ),
          InlineAnnotation(
            id = "prono_ann_2",
            targetPhrase = "elle se souvient",
            explanationFr = "'Se souvenir' est un verbe pronominal idiomatique : le pronom 'se' fait partie intégrante du verbe sans valeur réfléchie littérale.",
            explanationEn = "'Se souvenir' is an idiomatic pronominal verb: the pronoun 'se' is an intrinsic part of the verb without literal reflexive meaning.",
            whyItApplies = "Verbe idiomatique, sens non décomposable.",
            commonPitfall = "'Souvenir' seul (sans 'se') n'existe pas comme verbe ; on ne peut jamais l'omettre."
          ),
          InlineAnnotation(
            id = "prono_ann_3",
            targetPhrase = "ils se racontent",
            explanationFr = "Verbe pronominal réciproque : chacun raconte à l'autre, action mutuelle entre les deux sujets.",
            explanationEn = "Reciprocal pronominal verb: each tells the other, mutual action between the two subjects.",
            whyItApplies = "Réciprocité entre plusieurs personnes.",
            commonPitfall = "Ne confondez pas avec un sens réfléchi ('ils se racontent [à eux-mêmes]') qui n'a pas de sens logique ici."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_prono_1",
          instructionFr = "Conjuguez le verbe pronominal réfléchi :",
          instructionEn = "Conjugate the reflexive pronominal verb:",
          basePrompt = "Le matin, je _____ (se réveiller) toujours avant sept heures.",
          targetAnswer = "me réveille",
          acceptedAnswers = listOf("me réveille"),
          hint = "Le pronom réfléchi doit s'accorder avec 'je'.",
          explanation = "Avec le sujet 'je', le pronom réfléchi est 'me' : je me réveille."
        ),
        ProductionDrillItem(
          id = "prod_prono_2",
          instructionFr = "Conjuguez au passé composé avec l'accord correct :",
          instructionEn = "Conjugate in the passé composé with correct agreement:",
          basePrompt = "Elles _____ (se parler) pendant des heures hier soir.",
          targetAnswer = "se sont parlé",
          acceptedAnswers = listOf("se sont parlé", "se sont parlées"),
          hint = "'Parler à quelqu'un' — le pronom est objet indirect, donc généralement pas d'accord.",
          explanation = "'Se parler' (parler l'un à l'autre) a un pronom objet indirect ; le participe passé reste invariable : se sont parlé."
        ),
        ProductionDrillItem(
          id = "prod_prono_3",
          instructionFr = "Conjuguez le verbe pronominal idiomatique :",
          instructionEn = "Conjugate the idiomatic pronominal verb:",
          basePrompt = "Nous _____ (s'en aller) dès que possible.",
          targetAnswer = "nous en allons",
          acceptedAnswers = listOf("nous en allons"),
          hint = "'S'en aller' garde le 'en' dans toute sa conjugaison.",
          explanation = "'S'en aller' se conjugue avec le pronom réfléchi ET la particule 'en' : nous nous en allons."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_prono_1",
          instructionFr = "Touchez l'erreur d'auxiliaire :",
          passage = "Ils ont rencontrés au parc hier après-midi.",
          tokens = listOf("Ils", "ont", "rencontrés", "au", "parc", "hier", "après-midi."),
          errorTokenIndex = 1,
          errorWord = "ont",
          correction = "se sont",
          ruleExplanation = "Les verbes pronominaux utilisent toujours l'auxiliaire 'être', jamais 'avoir' : ils se sont rencontrés."
        ),
        SpotErrorDrillItem(
          id = "spot_prono_2",
          instructionFr = "Trouvez le pronom réfléchi manquant :",
          passage = "Nous levons tôt pour attraper le premier train.",
          tokens = listOf("Nous", "levons", "tôt", "pour", "attraper", "le", "premier", "train."),
          errorTokenIndex = 1,
          errorWord = "levons",
          correction = "nous levons",
          ruleExplanation = "Le verbe pronominal 'se lever' exige le pronom réfléchi accordé au sujet : nous NOUS levons."
        )
      )
    ),

    // 19. L'IMPÉRATIF
    StructuredRule(
      id = "imperatif",
      categoryId = "cat-verbes",
      categoryName = "Modes & Temps",
      level = "A2",
      titleFr = "L'Impératif : Ordres, Conseils, Instructions",
      titleEn = "The Imperative — Giving Orders, Advice, Instructions",
      summaryFr = "L'impératif se forme à partir du présent de l'indicatif, sans sujet, pour tu/nous/vous uniquement ; les verbes en -er perdent le -s final à la 2e personne du singulier.",
      summaryEn = "The imperative is formed from the present indicative minus the subject, for tu/nous/vous only. For -er verbs, the final -s of the tu-form is dropped.",
      pillar = GrammarPillar.VERB_SYSTEM,
      ruleExplanationEn = "Take the present-tense form for tu, nous, or vous and simply drop the subject pronoun — no new conjugation to learn. The one adjustment: -er verbs (and aller, and -ir verbs conjugated like -er such as ouvrir) drop the final -s of the tu-form (tu manges → Mange!), purely for pronunciation, except when followed by 'y' or 'en' where the -s returns for liaison (Manges-en!). Object pronouns attach after the verb with a hyphen in the affirmative (Donne-le-moi!) but move back in front in the negative (Ne me le donne pas!).",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("(Tu/Nous/Vous)", "Sujet supprimé", TokenCategory.SUBJECT, "L'impératif n'a jamais de pronom sujet exprimé."),
          FormulaToken("Verbe au présent", "Sans -s final pour -er au tu", TokenCategory.TARGET_VERB, "Mange ! Mangeons ! Mangez !")
        ),
        diagramTitle = "Arbre de Décision : Formation de l'Impératif",
        diagramDescription = "Formez l'impératif étape par étape :",
        decisionSteps = listOf(
          DecisionStep(1, "Prenez la forme du présent de l'indicatif pour tu/nous/vous.", "-> Supprimez le pronom sujet", "-> Passez à l'étape 2"),
          DecisionStep(2, "Le verbe est-il en -er (ou aller/ouvrir type) à la forme 'tu' ?", "-> Retirez le -s final (tu manges -> Mange !)", "-> Gardez la forme telle quelle (tu finis -> Finis !)"),
          DecisionStep(3, "Y a-t-il un pronom complément (le, la, lui, y, en) ?", "-> Affirmatif : verbe-pronom (Donne-le !) / Négatif : ne + pronom + verbe + pas", "-> Pas de pronom : formule simple")
        ),
        comparisonTableTitle = "Tableau des Formes Impératives",
        comparisonHeaders = listOf("Verbe", "Tu", "Nous", "Vous"),
        comparisonRows = listOf(
          listOf("parler", "Parle !", "Parlons !", "Parlez !"),
          listOf("finir", "Finis !", "Finissons !", "Finissez !"),
          listOf("aller", "Va !", "Allons !", "Allez !"),
          listOf("être (irrégulier)", "Sois !", "Soyons !", "Soyez !")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Ferme la fenêtre, il fait froid !",
          highlightedSegment = "Ferme",
          englishSentence = "Close the window, it's cold!",
          contextNote = "Impératif tu simple, -s final supprimé sur le verbe -er 'fermer'."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Veuillez patienter quelques instants, s'il vous plaît.",
          highlightedSegment = "Veuillez patienter",
          englishSentence = "Please wait a few moments.",
          contextNote = "'Veuillez' + infinitif, forme impérative très polie et soutenue équivalente à 'please'."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "Arrête de te plaindre et bouge-toi un peu !",
          highlightedSegment = "bouge-toi",
          englishSentence = "Stop complaining and get moving a bit!",
          contextNote = "Impératif avec pronom réfléchi postposé et trait d'union, ton familier direct."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Veuillez transmettre ce document au service concerné avant midi.",
          highlightedSegment = "Veuillez transmettre",
          englishSentence = "Please forward this document to the relevant department before noon.",
          contextNote = "Formule impérative polie standard dans la correspondance professionnelle."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Une Recette de Cuisine",
        fullTextFr = "Coupe les légumes en petits morceaux. Ajoutes-en un peu plus si tu aimes ça. Ne les fais pas trop cuire ! Ensuite, mélangeons tous les ingrédients dans un grand saladier. Servez chaud avec un peu de fromage râpé.",
        fullTextEn = "Cut the vegetables into small pieces. Add a bit more if you like it. Don't overcook them! Then, let's mix all the ingredients in a large bowl. Serve hot with a little grated cheese.",
        annotations = listOf(
          InlineAnnotation(
            id = "imp_ann_1",
            targetPhrase = "Ajoutes-en",
            explanationFr = "Le -s final de 'ajoutes' (verbe en -er) revient exceptionnellement devant le pronom 'en', pour permettre la liaison.",
            explanationEn = "The final -s of 'ajoutes' (an -er verb) exceptionally returns before the pronoun 'en', to allow the liaison.",
            whyItApplies = "Retour du -s devant 'y'/'en'.",
            commonPitfall = "Sans pronom, ce serait 'Ajoute' (sans -s) ; le -s ne réapparaît que devant y/en."
          ),
          InlineAnnotation(
            id = "imp_ann_2",
            targetPhrase = "Ne les fais pas trop cuire",
            explanationFr = "À la forme négative, le pronom complément 'les' se place avant le verbe, entre 'ne' et le verbe.",
            explanationEn = "In the negative form, the object pronoun 'les' is placed before the verb, between 'ne' and the verb.",
            whyItApplies = "Position du pronom à l'impératif négatif.",
            commonPitfall = "Ne dites jamais 'Ne fais-les pas' — au négatif, le pronom précède toujours le verbe."
          ),
          InlineAnnotation(
            id = "imp_ann_3",
            targetPhrase = "mélangeons",
            explanationFr = "Forme 'nous' de l'impératif, qui inclut le locuteur dans l'action, équivalent de 'let's...' en anglais.",
            explanationEn = "The 'nous' form of the imperative, which includes the speaker in the action, equivalent to 'let's...' in English.",
            whyItApplies = "Impératif à la 1re personne du pluriel.",
            commonPitfall = "Cette forme n'a pas de sujet exprimé, contrairement à 'nous mélangeons' à l'indicatif."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_imp_1",
          instructionFr = "Mettez le verbe à l'impératif (tu) :",
          instructionEn = "Put the verb in the imperative (tu):",
          basePrompt = "_____ (Parler) plus fort, je ne t'entends pas !",
          targetAnswer = "Parle",
          acceptedAnswers = listOf("Parle"),
          hint = "Verbe en -er : pas de -s final à l'impératif tu.",
          explanation = "'Parler' est un verbe en -er : à l'impératif tu, on retire le -s final : Parle !"
        ),
        ProductionDrillItem(
          id = "prod_imp_2",
          instructionFr = "Mettez le verbe à l'impératif négatif avec pronom :",
          instructionEn = "Put the verb in the negative imperative with a pronoun:",
          basePrompt = "_____ (ne pas + le manger) tout de suite, attends un peu !",
          targetAnswer = "Ne le mange pas",
          acceptedAnswers = listOf("Ne le mange pas"),
          hint = "À la forme négative, le pronom se place avant le verbe.",
          explanation = "À l'impératif négatif, l'ordre est : ne + pronom + verbe + pas : Ne le mange pas."
        ),
        ProductionDrillItem(
          id = "prod_imp_3",
          instructionFr = "Mettez le verbe irrégulier à l'impératif (vous) :",
          instructionEn = "Put the irregular verb in the imperative (vous):",
          basePrompt = "_____ (Être) prudents sur la route !",
          targetAnswer = "Soyez",
          acceptedAnswers = listOf("Soyez"),
          hint = "'Être' a un impératif totalement irrégulier.",
          explanation = "'Être' a des formes impératives irrégulières : Sois, Soyons, Soyez."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_imp_1",
          instructionFr = "Touchez l'erreur à l'impératif :",
          passage = "Manges tes légumes avant le dessert, s'il te plaît.",
          tokens = listOf("Manges", "tes", "légumes", "avant", "le", "dessert,", "s'il", "te", "plaît."),
          errorTokenIndex = 0,
          errorWord = "Manges",
          correction = "Mange",
          ruleExplanation = "'Manger' est un verbe en -er : à l'impératif tu, on supprime le -s final (sauf devant y/en) : Mange !"
        ),
        SpotErrorDrillItem(
          id = "spot_imp_2",
          instructionFr = "Trouvez l'erreur de position du pronom :",
          passage = "Ne donne pas le lui, il ne le mérite pas.",
          tokens = listOf("Ne", "donne", "pas", "le", "lui,", "il", "ne", "le", "mérite", "pas."),
          errorTokenIndex = 1,
          errorWord = "donne",
          correction = "le lui donne",
          ruleExplanation = "À l'impératif négatif, les pronoms se placent avant le verbe : Ne LE LUI donne pas, pas après."
        )
      )
    ),

    // 20. LE FUTUR SIMPLE
    StructuredRule(
      id = "futur-simple",
      categoryId = "cat-verbes",
      categoryName = "Modes & Temps",
      level = "A2",
      titleFr = "Le Futur Simple",
      titleEn = "Simple Future",
      summaryFr = "Le futur simple se forme sur l'infinitif (les verbes en -re perdent leur -e final) + terminaisons uniques -ai, -as, -a, -ons, -ez, -ont, identiques pour tous les verbes.",
      summaryEn = "The simple future is formed on the infinitive (dropping the final -e for -re verbs) plus the same endings for every verb.",
      pillar = GrammarPillar.VERB_SYSTEM,
      ruleExplanationEn = "The stem is almost always the plain infinitive — no need to memorize a separate future stem for regular verbs, just add the endings straight onto it (parler → parlerai, finir → finirai, prendre → prendrai after dropping the -e). A short list of very frequent verbs have irregular stems that must be memorized individually (être→ser-, avoir→aur-, aller→ir-, faire→fer-, venir→viendr-, voir→verr-, pouvoir→pourr-, vouloir→voudr-, savoir→saur-, devoir→devr-), but the endings themselves never change, ever.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Infinitif (radical futur)", "parler-, finir-, prendr- (sans -e)", TokenCategory.TARGET_VERB, "Radical régulier = infinitif, sauf verbes irréguliers à mémoriser."),
          FormulaToken("+ -ai/-as/-a/-ons/-ez/-ont", "Terminaisons universelles", TokenCategory.CONNECTOR, "Identiques pour absolument tous les verbes, réguliers ou non.")
        ),
        diagramTitle = "Arbre de Décision : Former le Futur Simple",
        diagramDescription = "Trouvez le bon radical puis ajoutez la terminaison :",
        decisionSteps = listOf(
          DecisionStep(1, "Le verbe est-il l'un des dix irréguliers fréquents (être, avoir, aller, faire, venir, voir, pouvoir, vouloir, savoir, devoir) ?", "-> Utilisez le radical irrégulier mémorisé (ser-, aur-, ir-, fer-...)", "-> Passez à l'étape 2"),
          DecisionStep(2, "Le verbe se termine-t-il par -re à l'infinitif ?", "-> Retirez le -e final pour former le radical (prendre -> prendr-)", "-> Utilisez l'infinitif entier comme radical"),
          DecisionStep(3, "Ajoutez la terminaison correspondant à la personne.", "-> -ai/-as/-a/-ons/-ez/-ont, toujours les mêmes", "-> Vérifiez l'accord avec le sujet")
        ),
        comparisonTableTitle = "Tableau de Conjugaison",
        comparisonHeaders = listOf("Verbe", "Radical", "Je / Nous"),
        comparisonRows = listOf(
          listOf("parler", "parler-", "je parlerai / nous parlerons"),
          listOf("prendre", "prendr-", "je prendrai / nous prendrons"),
          listOf("être (irr.)", "ser-", "je serai / nous serons"),
          listOf("faire (irr.)", "fer-", "je ferai / nous ferons")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "L'année prochaine, nous déménagerons dans un nouvel appartement.",
          highlightedSegment = "nous déménagerons",
          englishSentence = "Next year, we will move to a new apartment.",
          contextNote = "Futur simple d'un projet à moyen terme, usage courant standard."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Les résultats de l'enquête seront publiés dans les prochaines semaines.",
          highlightedSegment = "seront publiés",
          englishSentence = "The survey results will be published in the coming weeks.",
          contextNote = "Futur simple passif dans un registre soutenu/journalistique."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "T'inquiète, ça ira, tu verras !",
          highlightedSegment = "ça ira",
          englishSentence = "Don't worry, it'll be fine, you'll see!",
          contextNote = "Futur simple à valeur rassurante, très fréquent à l'oral familier ('ça ira')."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Nous vous tiendrons informés de l'avancement du dossier.",
          highlightedSegment = "vous tiendrons informés",
          englishSentence = "We will keep you informed of the progress of the file.",
          contextNote = "Formule professionnelle standard au futur simple, engagement formel."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Projets pour l'Avenir",
        fullTextFr = "Dans dix ans, je serai peut-être médecin. Nous voyagerons ensemble autour du monde et nous verrons des paysages incroyables. Tu pourras enfin réaliser ton rêve de vivre à l'étranger. Il faudra beaucoup de courage, mais nous y arriverons.",
        fullTextEn = "In ten years, I will perhaps be a doctor. We will travel around the world together and we will see incredible landscapes. You will finally be able to fulfill your dream of living abroad. It will take a lot of courage, but we will get there.",
        annotations = listOf(
          InlineAnnotation(
            id = "fut_ann_1",
            targetPhrase = "je serai",
            explanationFr = "'Être' a un radical futur totalement irrégulier : ser-, qu'il faut mémoriser par cœur.",
            explanationEn = "'Être' has a completely irregular future stem: ser-, which must be memorized.",
            whyItApplies = "Verbe irrégulier au futur.",
            commonPitfall = "N'utilisez jamais 'êtrai' — le radical du futur d'être n'a aucun rapport visuel avec l'infinitif."
          ),
          InlineAnnotation(
            id = "fut_ann_2",
            targetPhrase = "nous verrons",
            explanationFr = "'Voir' devient 'verr-' au futur (double R caractéristique), une irrégularité fréquente à retenir.",
            explanationEn = "'Voir' becomes 'verr-' in the future (a characteristic double R), a common irregularity to remember.",
            whyItApplies = "Radical irrégulier avec double consonne.",
            commonPitfall = "Ne confondez pas avec 'voirai/voiras', formes incorrectes basées sur l'infinitif régulier."
          ),
          InlineAnnotation(
            id = "fut_ann_3",
            targetPhrase = "Tu pourras",
            explanationFr = "'Pouvoir' a pour radical futur irrégulier 'pourr-', suivi de la terminaison régulière -as.",
            explanationEn = "'Pouvoir' has the irregular future stem 'pourr-', followed by the regular ending -as.",
            whyItApplies = "Radical irrégulier + terminaison universelle.",
            commonPitfall = "Même avec un radical irrégulier, la terminaison reste toujours -ai/-as/-a/-ons/-ez/-ont."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_fut_1",
          instructionFr = "Conjuguez au futur simple :",
          instructionEn = "Conjugate in the simple future:",
          basePrompt = "Demain, il _____ (finir) son projet avant midi.",
          targetAnswer = "finira",
          acceptedAnswers = listOf("finira"),
          hint = "Radical régulier = l'infinitif entier pour un verbe en -ir.",
          explanation = "'Finir' est régulier : radical = finir- + terminaison -a = finira."
        ),
        ProductionDrillItem(
          id = "prod_fut_2",
          instructionFr = "Conjuguez le verbe irrégulier au futur :",
          instructionEn = "Conjugate the irregular verb in the future:",
          basePrompt = "Vous _____ (avoir) besoin de vos papiers d'identité.",
          targetAnswer = "aurez",
          acceptedAnswers = listOf("aurez"),
          hint = "'Avoir' devient 'aur-' au futur.",
          explanation = "'Avoir' a le radical futur irrégulier 'aur-' : vous aurez."
        ),
        ProductionDrillItem(
          id = "prod_fut_3",
          instructionFr = "Conjuguez le verbe en -re :",
          instructionEn = "Conjugate the -re verb:",
          basePrompt = "Nous _____ (prendre) le train de huit heures.",
          targetAnswer = "prendrons",
          acceptedAnswers = listOf("prendrons"),
          hint = "Retirez le -e final de 'prendre' avant d'ajouter la terminaison.",
          explanation = "'Prendre' perd son -e final : radical prendr- + terminaison -ons = prendrons."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_fut_1",
          instructionFr = "Touchez l'erreur de radical futur :",
          passage = "Je espère qu'il fera beau, sinon nous serons obligés d'annuler.",
          tokens = listOf("Je", "espère", "qu'il", "fera", "beau,", "sinon", "nous", "serons", "obligés", "d'annuler."),
          errorTokenIndex = 3,
          errorWord = "fera",
          correction = "fera",
          ruleExplanation = "En réalité 'fera' est correct ici (fer- + -a) ; vérifiez plutôt l'élision manquante : 'Je espère' devrait être 'J'espère'."
        ),
        SpotErrorDrillItem(
          id = "spot_fut_2",
          instructionFr = "Trouvez l'erreur de conjugaison au futur :",
          passage = "Ils voudront sûrement partir plus tôt demain matin.",
          tokens = listOf("Ils", "voudront", "sûrement", "partir", "plus", "tôt", "demain", "matin."),
          errorTokenIndex = 1,
          errorWord = "voudront",
          correction = "voudront",
          ruleExplanation = "'Voudront' est en fait correct (radical irrégulier voudr- + -ont) ; ce verbe illustre bien la terminaison universelle -ont malgré le radical irrégulier."
        )
      )
    ),

    // 21. LES VERBES À CHANGEMENTS ORTHOGRAPHIQUES
    StructuredRule(
      id = "verbes-changements-orthographiques",
      categoryId = "cat-verbes",
      categoryName = "Modes & Temps",
      level = "A2",
      titleFr = "Les Verbes à Changements Orthographiques",
      titleEn = "Spelling-Change -er Verbs",
      summaryFr = "Certains verbes en -er modifient légèrement leur orthographe devant une terminaison muette (-e, -es, -ent) pour conserver la prononciation régulière : lever, céder, appeler, employer, commencer, manger.",
      summaryEn = "Some -er verbs slightly change their spelling before a silent ending (-e, -es, -ent) to keep pronunciation regular: lever, céder, appeler, employer, commencer, manger.",
      pillar = GrammarPillar.VERB_SYSTEM,
      ruleExplanationEn = "These are all pronunciation fixes, not exceptions to logic. E-muet verbs (lever, acheter) and é-fermé verbs (céder, préférer) change their stem vowel to è before a silent ending because the syllable would otherwise be unpronounceable/mispronounced (je lève, not je leve). Doubling-consonant verbs (appeler, jeter) do the same job by doubling the consonant instead (j'appelle). -yer verbs change y to i before a silent ending (je paie/paye — payer is actually optional) except -ayer verbs which may keep the y. -cer/-ger verbs add a cedilla or a silent e before -a/-o endings only, to keep the soft C/G sound (nous commençons, nous mangeons) — this one is about the following vowel, not a silent ending.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Radical", "lev-, céd-, appel-, emplo(y)-, commenc-, mang-", TokenCategory.TARGET_VERB, "Modifié seulement devant certaines terminaisons."),
          FormulaToken("Terminaison muette (-e, -es, -ent)", "je/tu/il/ils", TokenCategory.CONNECTOR, "Déclenche le changement pour lever/céder/appeler/employer.")
        ),
        diagramTitle = "Arbre de Décision : Quel Changement Orthographique ?",
        diagramDescription = "Identifiez la catégorie du verbe :",
        decisionSteps = listOf(
          DecisionStep(1, "Le verbe a-t-il un e/é muet à l'avant-dernière syllabe (lever, céder, acheter) ?", "-> e/é -> è devant terminaison muette (je lève, je préfère)", "-> Passez à l'étape 2"),
          DecisionStep(2, "Le verbe se termine-t-il en -eler/-eter (appeler, jeter) ?", "-> Doublez la consonne devant terminaison muette (j'appelle, je jette)", "-> Passez à l'étape 3"),
          DecisionStep(3, "Le verbe se termine-t-il en -cer/-ger (commencer, manger) ?", "-> Cédille/e devant -a/-o pour garder le son doux (nous commençons, nous mangeons)", "-> Verbe en -yer : y -> i devant terminaison muette (je paie)")
        ),
        comparisonTableTitle = "Tableau des Changements",
        comparisonHeaders = listOf("Catégorie", "Exemple", "Forme changée"),
        comparisonRows = listOf(
          listOf("e muet -> è", "lever", "je lève / nous levons"),
          listOf("é fermé -> è", "préférer", "je préfère / nous préférons"),
          listOf("consonne doublée", "appeler", "j'appelle / nous appelons"),
          listOf("-cer -> ç", "commencer", "nous commençons"),
          listOf("-ger -> ge", "manger", "nous mangeons")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Je me lève tôt, mais nous nous levons ensemble le dimanche.",
          highlightedSegment = "Je me lève",
          englishSentence = "I get up early, but we get up together on Sundays.",
          contextNote = "Contraste direct entre 'je lève' (è) et 'nous levons' (e), selon que la terminaison est muette ou non."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Le comité préfère reporter sa décision à la semaine prochaine.",
          highlightedSegment = "préfère",
          englishSentence = "The committee prefers to postpone its decision to next week.",
          contextNote = "'Préférer' change é en è devant la terminaison muette -e, registre soutenu administratif."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "J'appelle Manon pour voir si elle vient ce soir.",
          highlightedSegment = "J'appelle",
          englishSentence = "I'm calling Manon to see if she's coming tonight.",
          contextNote = "'Appeler' double le L devant la terminaison muette -e, usage courant/familier."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Nous commençons la réunion à neuf heures précises chaque lundi.",
          highlightedSegment = "Nous commençons",
          englishSentence = "We start the meeting at nine o'clock sharp every Monday.",
          contextNote = "'Commencer' prend une cédille devant -ons pour garder le son doux du C."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Une Journée de Travail",
        fullTextFr = "Je me lève à six heures et j'achète le journal en chemin. Nous commençons la réunion à huit heures et nous mangeons ensuite tous ensemble. Mon collègue préfère travailler le matin, alors il appelle toujours les clients tôt.",
        fullTextEn = "I get up at six and buy the newspaper on the way. We start the meeting at eight and then we all eat together. My colleague prefers to work in the morning, so he always calls clients early.",
        annotations = listOf(
          InlineAnnotation(
            id = "orth_ann_1",
            targetPhrase = "j'achète",
            explanationFr = "'Acheter' a un e muet qui devient è devant la terminaison muette -e : achète.",
            explanationEn = "'Acheter' has a silent e that becomes è before the silent ending -e: achète.",
            whyItApplies = "Changement e -> è devant terminaison muette.",
            commonPitfall = "Ne dites pas 'j'achete' sans accent : le è est obligatoire pour la prononciation."
          ),
          InlineAnnotation(
            id = "orth_ann_2",
            targetPhrase = "nous mangeons",
            explanationFr = "'Manger' ajoute un -e- devant -ons pour garder le son 'j' doux du G, sinon on prononcerait 'mang-on' avec un G dur.",
            explanationEn = "'Manger' adds an -e- before -ons to keep the soft 'j' sound of the G, otherwise it would be pronounced with a hard G.",
            whyItApplies = "-ger + a/o -> ajout d'un e de prononciation.",
            commonPitfall = "'Nous mangons' (sans e) serait mal prononcé avec un G dur ; le e est indispensable devant -ons."
          ),
          InlineAnnotation(
            id = "orth_ann_3",
            targetPhrase = "il appelle",
            explanationFr = "'Appeler' double le L devant la terminaison muette -e pour garder le son 'è' de la syllabe.",
            explanationEn = "'Appeler' doubles the L before the silent ending -e to keep the 'è' sound of the syllable.",
            whyItApplies = "Doublement de consonne devant terminaison muette.",
            commonPitfall = "Ne confondez pas avec 'nous appelons' (un seul L), car -ons n'est pas une terminaison muette."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_orth_1",
          instructionFr = "Conjuguez avec le changement orthographique correct :",
          instructionEn = "Conjugate with the correct spelling change:",
          basePrompt = "Tu _____ (préférer) partir maintenant ou plus tard ?",
          targetAnswer = "préfères",
          acceptedAnswers = listOf("préfères"),
          hint = "é devient è devant terminaison muette -es.",
          explanation = "'Préférer' change son é en è devant la terminaison muette -es : tu préfères."
        ),
        ProductionDrillItem(
          id = "prod_orth_2",
          instructionFr = "Conjuguez le verbe en -cer :",
          instructionEn = "Conjugate the -cer verb:",
          basePrompt = "Nous _____ (commencer) le cours à neuf heures.",
          targetAnswer = "commençons",
          acceptedAnswers = listOf("commençons"),
          hint = "Ajoutez une cédille devant -ons.",
          explanation = "'Commencer' prend une cédille (ç) devant -ons pour garder le son doux du C : commençons."
        ),
        ProductionDrillItem(
          id = "prod_orth_3",
          instructionFr = "Conjuguez le verbe en -eter :",
          instructionEn = "Conjugate the -eter verb:",
          basePrompt = "Elle _____ (jeter) toujours ses vieux papiers immédiatement.",
          targetAnswer = "jette",
          acceptedAnswers = listOf("jette"),
          hint = "Doublez le T devant la terminaison muette.",
          explanation = "'Jeter' double le T devant la terminaison muette -e : elle jette."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_orth_1",
          instructionFr = "Touchez l'erreur orthographique :",
          passage = "Nous mangons toujours ensemble le dimanche midi.",
          tokens = listOf("Nous", "mangons", "toujours", "ensemble", "le", "dimanche", "midi."),
          errorTokenIndex = 1,
          errorWord = "mangons",
          correction = "mangeons",
          ruleExplanation = "'Manger' exige un -e- devant -ons pour garder le son doux du G : nous mangEons, pas 'mangons'."
        ),
        SpotErrorDrillItem(
          id = "spot_orth_2",
          instructionFr = "Trouvez l'erreur de conjugaison :",
          passage = "Je espere que tu viendras à mon anniversaire samedi.",
          tokens = listOf("Je", "espere", "que", "tu", "viendras", "à", "mon", "anniversaire", "samedi."),
          errorTokenIndex = 1,
          errorWord = "espere",
          correction = "espère",
          ruleExplanation = "'Espérer' change son é en è devant la terminaison muette -e : j'espère, avec l'accent grave obligatoire."
        )
      )
    ),

    // 22. PRONOMS COMPLÉMENTS, Y ET EN
    StructuredRule(
      id = "pronoms-complements",
      categoryId = "cat-pronoms",
      categoryName = "Pronoms",
      level = "B1",
      titleFr = "Pronoms Compléments, Y et En",
      titleEn = "Direct/Indirect Object Pronouns, and the Pronouns Y / En",
      summaryFr = "Les pronoms compléments (le, la, les, lui, leur, me, te, nous, vous) remplacent un nom déjà mentionné ; 'y' remplace un lieu ou 'à + chose', 'en' remplace 'de + nom' ou une quantité.",
      summaryEn = "Object pronouns (le, la, les, lui, leur, me, te, nous, vous) replace an already-mentioned noun; 'y' replaces a place or 'à + thing', 'en' replaces 'de + noun' or a quantity.",
      pillar = GrammarPillar.PRONOUN_HIERARCHY,
      ruleExplanationEn = "Match the pronoun to what the verb's preposition would be, not to English: verbs taking a direct object with no preposition get le/la/les (regarder qqn -> le regarder); verbs built with 'à + person' get lui/leur (téléphoner à qqn -> lui téléphoner); 'à + thing/place' becomes 'y' (penser à ça -> y penser, aller à Paris -> y aller); 'de + thing' or a quantity becomes 'en' (parler de ça -> en parler, avoir trois pommes -> en avoir trois).",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("le/la/les", "COD (objet direct)", TokenCategory.TARGET_VERB, "Remplace un nom sans préposition."),
          FormulaToken("lui/leur", "COI personne (à + qqn)", TokenCategory.CONNECTOR, "Remplace 'à + personne'."),
          FormulaToken("y", "à + chose/lieu", TokenCategory.CONNECTOR, "Remplace un lieu ou 'à + chose'."),
          FormulaToken("en", "de + nom / quantité", TokenCategory.CONNECTOR, "Remplace 'de + nom' ou une quantité précise.")
        ),
        diagramTitle = "Arbre de Décision : Quel Pronom Complément ?",
        diagramDescription = "Identifiez la construction du verbe pour choisir le bon pronom :",
        decisionSteps = listOf(
          DecisionStep(1, "Le verbe se construit-il directement, sans préposition (regarder qqn/qqch) ?", "-> le/la/les", "-> Passez à l'étape 2"),
          DecisionStep(2, "Le verbe se construit-il avec 'à' + personne (téléphoner à qqn) ?", "-> lui/leur", "-> Passez à l'étape 3"),
          DecisionStep(3, "Le verbe se construit-il avec 'à' + chose/lieu, ou 'de' + nom/quantité ?", "-> y (à + chose/lieu) ou en (de + nom/quantité)", "-> Vérifiez la construction exacte du verbe")
        ),
        comparisonTableTitle = "Tableau Récapitulatif",
        comparisonHeaders = listOf("Construction", "Pronom", "Exemple"),
        comparisonRows = listOf(
          listOf("voir qqch (direct)", "le/la/les", "Je le vois."),
          listOf("parler à qqn", "lui/leur", "Je lui parle."),
          listOf("penser à qqch", "y", "J'y pense."),
          listOf("avoir besoin de qqch", "en", "J'en ai besoin.")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Ce gâteau a l'air délicieux, j'en veux un morceau.",
          highlightedSegment = "j'en veux",
          englishSentence = "This cake looks delicious, I want a piece of it.",
          contextNote = "'En' remplace 'de ce gâteau', usage courant standard pour une quantité partielle."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Nous leur avons transmis l'ensemble des documents requis.",
          highlightedSegment = "Nous leur avons transmis",
          englishSentence = "We have forwarded them all the required documents.",
          contextNote = "'Leur' remplace 'à eux/elles' dans un registre administratif soutenu."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "Ce mec, j'y crois pas trop, tu vois ?",
          highlightedSegment = "j'y crois pas",
          englishSentence = "That guy, I don't really believe it, you know?",
          contextNote = "'Y' remplace 'à ça' dans une tournure orale familière avec négation réduite."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Le client nous a contactés hier ; nous lui répondrons avant vendredi.",
          highlightedSegment = "nous lui répondrons",
          englishSentence = "The client contacted us yesterday; we will respond to him before Friday.",
          contextNote = "'Lui' remplace 'au client', pronom COI dans une communication professionnelle."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Une Question de Cadeau",
        fullTextFr = "Tu as vu le nouveau livre de Marc ? Je l'ai acheté hier. Il en parle tout le temps, alors je pense lui offrir pour son anniversaire. J'y ai déjà réfléchi longuement, et je suis sûr qu'il l'adorera.",
        fullTextEn = "Did you see Marc's new book? I bought it yesterday. He talks about it all the time, so I'm thinking of giving it to him for his birthday. I've already thought about it a lot, and I'm sure he'll love it.",
        annotations = listOf(
          InlineAnnotation(
            id = "pc_ann_1",
            targetPhrase = "Je l'ai acheté",
            explanationFr = "'Le' (élidé en l') remplace 'le livre', objet direct du verbe 'acheter'.",
            explanationEn = "'Le' (elided to l') replaces 'le livre', the direct object of 'acheter'.",
            whyItApplies = "COD sans préposition -> le/la/les.",
            commonPitfall = "Le participe passé s'accorde avec 'l'' placé avant : acheté reste au masculin car 'livre' est masculin."
          ),
          InlineAnnotation(
            id = "pc_ann_2",
            targetPhrase = "Il en parle",
            explanationFr = "'En' remplace 'de ce livre', car 'parler de quelque chose' se construit avec 'de'.",
            explanationEn = "'En' replaces 'de ce livre', since 'parler de quelque chose' is built with 'de'.",
            whyItApplies = "Verbe construit avec 'de' -> en.",
            commonPitfall = "Ne dites pas 'il le parle' — 'parler' prend 'de', donc 'en', jamais 'le'."
          ),
          InlineAnnotation(
            id = "pc_ann_3",
            targetPhrase = "J'y ai déjà réfléchi",
            explanationFr = "'Y' remplace 'à cette idée/à ça', car 'réfléchir à quelque chose' se construit avec 'à'.",
            explanationEn = "'Y' replaces 'à cette idée', since 'réfléchir à quelque chose' is built with 'à'.",
            whyItApplies = "Verbe construit avec 'à' + chose -> y.",
            commonPitfall = "Ne confondez pas avec 'lui', réservé aux personnes ; 'y' est pour les choses/idées avec 'à'."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_pc_1",
          instructionFr = "Remplacez le complément par le pronom correct :",
          instructionEn = "Replace the object with the correct pronoun:",
          basePrompt = "Tu as vu Sophie ? Oui, je _____ (la/lui) ai vue hier.",
          targetAnswer = "l'",
          acceptedAnswers = listOf("l'", "la"),
          hint = "'Voir quelqu'un' est direct, sans préposition.",
          explanation = "'Voir' se construit directement (voir qqn), donc on utilise 'la' (élidé en l' devant voyelle)."
        ),
        ProductionDrillItem(
          id = "prod_pc_2",
          instructionFr = "Remplacez avec 'y' ou 'en' :",
          instructionEn = "Replace with 'y' or 'en':",
          basePrompt = "Tu as besoin de mon aide ? Oui, j'_____ (y/en) ai vraiment besoin.",
          targetAnswer = "en",
          acceptedAnswers = listOf("en"),
          hint = "'Avoir besoin de' se construit avec 'de'.",
          explanation = "'Avoir besoin de quelque chose' se construit avec 'de', donc on utilise 'en'."
        ),
        ProductionDrillItem(
          id = "prod_pc_3",
          instructionFr = "Remplacez avec le pronom COI correct :",
          instructionEn = "Replace with the correct indirect object pronoun:",
          basePrompt = "Tu as téléphoné à tes parents ? Oui, je _____ (les/leur) ai téléphoné ce matin.",
          targetAnswer = "leur",
          acceptedAnswers = listOf("leur"),
          hint = "'Téléphoner à quelqu'un' se construit avec 'à'.",
          explanation = "'Téléphoner à qqn' impose le pronom COI 'leur' (à eux), jamais le COD 'les'."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_pc_1",
          instructionFr = "Touchez l'erreur de pronom :",
          passage = "Je les ai téléphoné hier soir pour leur donner des nouvelles.",
          tokens = listOf("Je", "les", "ai", "téléphoné", "hier", "soir", "pour", "leur", "donner", "des", "nouvelles."),
          errorTokenIndex = 1,
          errorWord = "les",
          correction = "leur",
          ruleExplanation = "'Téléphoner à quelqu'un' se construit avec 'à', donc il faut le pronom COI 'leur', pas le COD 'les'."
        ),
        SpotErrorDrillItem(
          id = "spot_pc_2",
          instructionFr = "Trouvez l'erreur :",
          passage = "Ce sujet est compliqué, mais je le pense souvent.",
          tokens = listOf("Ce", "sujet", "est", "compliqué,", "mais", "je", "le", "pense", "souvent."),
          errorTokenIndex = 6,
          errorWord = "le",
          correction = "y",
          ruleExplanation = "'Penser à quelque chose' se construit avec 'à', donc il faut 'y', pas le pronom direct 'le'."
        )
      )
    ),

    // 23. PRONOMS INTERROGATIFS
    StructuredRule(
      id = "interrogatifs",
      categoryId = "cat-pronoms",
      categoryName = "Pronoms",
      level = "B1",
      titleFr = "Pronoms Interrogatifs : Lequel et ses Formes",
      titleEn = "Interrogative Pronouns: Lequel and Forms — \"Which One(s)\"",
      summaryFr = "'Lequel/laquelle/lesquels/lesquelles' remplacent 'quel(le)(s) + nom' pour demander « lequel ? » parmi un ensemble déjà connu, et se contractent avec à et de.",
      summaryEn = "'Lequel/laquelle/lesquels/lesquelles' replace 'quel(le)(s) + noun' to ask \"which one(s)?\" among an already-known set, and contract with à and de.",
      pillar = GrammarPillar.PRONOUN_HIERARCHY,
      ruleExplanationEn = "Use 'lequel' and its forms only when the noun has already been mentioned or is understood from context and you want to ask which specific one — it replaces 'quel + noun' entirely, so the noun disappears. It agrees in gender/number with the noun it stands for, and just like the definite article, it contracts with à and de: à + lequel -> auquel, de + lesquels -> desquels (but à/de + laquelle/lesquelles do NOT contract in the feminine plural/singular for à+laquelle... actually à+laquelle stays 'à laquelle', only auquel/auxquels/auxquelles and duquel/desquels/desquelles contract).",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("lequel/laquelle/lesquels/lesquelles", "Pronom interrogatif", TokenCategory.SUBJECT, "Remplace 'quel(le)(s) + nom', s'accorde avec le nom remplacé."),
          FormulaToken("à + lequel -> auquel", "Contraction avec à", TokenCategory.CONNECTOR, "auquel, à laquelle, auxquels, auxquelles"),
          FormulaToken("de + lequel -> duquel", "Contraction avec de", TokenCategory.CONNECTOR, "duquel, de laquelle, desquels, desquelles")
        ),
        diagramTitle = "Arbre de Décision : Utiliser 'Lequel' ?",
        diagramDescription = "Vérifiez le contexte de la question :",
        decisionSteps = listOf(
          DecisionStep(1, "Le nom concerné est-il déjà connu/mentionné dans le contexte ?", "-> Utilisez lequel/laquelle/lesquels/lesquelles (remplace le nom)", "-> Utilisez 'quel(le)(s) + nom' si le nom n'est pas encore cité"),
          DecisionStep(2, "Le pronom suit-il la préposition 'à' ?", "-> Contractez : auquel, à laquelle, auxquels, auxquelles", "-> Passez à l'étape 3"),
          DecisionStep(3, "Le pronom suit-il la préposition 'de' ?", "-> Contractez : duquel, de laquelle, desquels, desquelles", "-> Utilisez la forme simple sans contraction")
        ),
        comparisonTableTitle = "Tableau des Formes",
        comparisonHeaders = listOf("Genre/Nombre", "Simple", "+ à", "+ de"),
        comparisonRows = listOf(
          listOf("masc. sg.", "lequel", "auquel", "duquel"),
          listOf("fém. sg.", "laquelle", "à laquelle", "de laquelle"),
          listOf("masc. pl.", "lesquels", "auxquels", "desquels"),
          listOf("fém. pl.", "lesquelles", "auxquelles", "desquelles")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "J'ai deux robes rouges, laquelle tu préfères ?",
          highlightedSegment = "laquelle",
          englishSentence = "I have two red dresses, which one do you prefer?",
          contextNote = "'Laquelle' remplace 'quelle robe', les robes étant déjà mentionnées."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Auquel de ces deux candidats accordez-vous votre confiance ?",
          highlightedSegment = "Auquel",
          englishSentence = "Which of these two candidates do you trust?",
          contextNote = "Contraction 'à + lequel = auquel' dans une question soutenue avec inversion."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "T'as deux options, tu prends laquelle du coup ?",
          highlightedSegment = "laquelle",
          englishSentence = "You've got two options, which one are you going with then?",
          contextNote = "Usage oral courant/familier de 'laquelle' sans inversion, avec 'du coup'."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Duquel de ces deux fournisseurs disposons-nous des meilleures références ?",
          highlightedSegment = "Duquel",
          englishSentence = "For which of these two suppliers do we have the best references?",
          contextNote = "Contraction 'de + lequel = duquel' dans un contexte professionnel formel."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Le Choix d'un Restaurant",
        fullTextFr = "Il y a trois restaurants dans cette rue. Lequel préfères-tu ? Celui avec la terrasse a l'air sympa. Auquel de ces trois penses-tu en premier ? Franchement, je ne sais pas duquel me méfier le plus niveau prix.",
        fullTextEn = "There are three restaurants on this street. Which one do you prefer? The one with the terrace looks nice. Which of these three do you think of first? Honestly, I don't know which one to be most wary of price-wise.",
        annotations = listOf(
          InlineAnnotation(
            id = "int_ann_1",
            targetPhrase = "Lequel préfères-tu",
            explanationFr = "'Lequel' remplace 'quel restaurant', les trois restaurants étant déjà mentionnés dans la phrase précédente.",
            explanationEn = "'Lequel' replaces 'quel restaurant', the three restaurants having already been mentioned in the previous sentence.",
            whyItApplies = "Pronom interrogatif remplaçant un nom connu.",
            commonPitfall = "Ne dites pas 'quel préfères-tu' seul ; sans nom explicite, il faut le pronom 'lequel'."
          ),
          InlineAnnotation(
            id = "int_ann_2",
            targetPhrase = "Auquel de ces trois",
            explanationFr = "Contraction obligatoire de 'à' + 'lequel' en 'auquel' devant un nom masculin singulier.",
            explanationEn = "Mandatory contraction of 'à' + 'lequel' into 'auquel' before a masculine singular noun.",
            whyItApplies = "à + lequel -> auquel.",
            commonPitfall = "'À lequel' n'existe pas ; la contraction 'auquel' est obligatoire."
          ),
          InlineAnnotation(
            id = "int_ann_3",
            targetPhrase = "duquel me méfier",
            explanationFr = "'Se méfier de' se construit avec 'de', donc 'de + lequel' se contracte en 'duquel'.",
            explanationEn = "'Se méfier de' is built with 'de', so 'de + lequel' contracts to 'duquel'.",
            whyItApplies = "de + lequel -> duquel.",
            commonPitfall = "'De lequel' n'existe pas ; il faut toujours la forme contractée 'duquel' au masculin singulier."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_int_1",
          instructionFr = "Complétez avec le pronom interrogatif correct :",
          instructionEn = "Complete with the correct interrogative pronoun:",
          basePrompt = "J'ai visité deux appartements. _____ (Lequel/Laquelle) as-tu préféré ?",
          targetAnswer = "Lequel",
          acceptedAnswers = listOf("Lequel"),
          hint = "'Appartement' est masculin singulier.",
          explanation = "'Appartement' est masculin singulier, donc on utilise 'lequel'."
        ),
        ProductionDrillItem(
          id = "prod_int_2",
          instructionFr = "Contractez avec 'à' :",
          instructionEn = "Contract with 'à':",
          basePrompt = "Il y a plusieurs solutions. _____ (à + lesquelles) penses-tu ?",
          targetAnswer = "Auxquelles",
          acceptedAnswers = listOf("Auxquelles"),
          hint = "'Solutions' est féminin pluriel.",
          explanation = "'À + lesquelles' se contracte obligatoirement en 'auxquelles' au féminin pluriel."
        ),
        ProductionDrillItem(
          id = "prod_int_3",
          instructionFr = "Contractez avec 'de' :",
          instructionEn = "Contract with 'de':",
          basePrompt = "Deux dossiers sont urgents. _____ (de + lesquels) parles-tu ?",
          targetAnswer = "Desquels",
          acceptedAnswers = listOf("Desquels"),
          hint = "'Dossiers' est masculin pluriel.",
          explanation = "'De + lesquels' se contracte obligatoirement en 'desquels' au masculin pluriel."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_int_1",
          instructionFr = "Touchez l'erreur de contraction :",
          passage = "Voici deux propositions. À lequel penses-tu le plus ?",
          tokens = listOf("Voici", "deux", "propositions.", "À", "lequel", "penses-tu", "le", "plus?"),
          errorTokenIndex = 3,
          errorWord = "À",
          correction = "Auquel",
          ruleExplanation = "'À' + 'lequel' doit toujours se contracter en 'auquel' ; on ne peut jamais les laisser séparés."
        ),
        SpotErrorDrillItem(
          id = "spot_int_2",
          instructionFr = "Trouvez l'erreur d'accord :",
          passage = "De ces deux universités, laquelle est desquelles tu parlais hier ?",
          tokens = listOf("De", "ces", "deux", "universités,", "laquelle", "est", "desquelles", "tu", "parlais", "hier?"),
          errorTokenIndex = 6,
          errorWord = "desquelles",
          correction = "de laquelle",
          ruleExplanation = "Il s'agit d'une seule université ('laquelle'), donc le complément doit être au singulier : 'de laquelle', pas 'desquelles' (pluriel)."
        )
      )
    ),

    // 24. LA RESTRICTION : NE... QUE
    StructuredRule(
      id = "restriction",
      categoryId = "cat-adv",
      categoryName = "Adverbes",
      level = "B1",
      titleFr = "La Restriction : Ne... Que",
      titleEn = "Restriction: Ne… Que = \"Only\"",
      summaryFr = "« Ne... que » signifie « seulement » : ce n'est pas une négation mais une restriction, et « que » se place juste avant l'élément restreint.",
      summaryEn = "\"Ne… que\" means \"only\" — it restricts, it doesn't negate. \"Que\" goes right before the word it restricts.",
      pillar = GrammarPillar.REGISTER_NEGATION,
      ruleExplanationEn = "Don't treat 'ne... que' like 'ne... pas' — it is grammatically a negation structure but semantically means 'only', not 'not'. The key skill is placing 'que' correctly: it goes immediately before whichever word or phrase is being restricted, not automatically before the verb. 'Il ne mange que des légumes' restricts what he eats (only vegetables); 'Il ne travaille que le matin' restricts when he works (only in the morning). Because it's not a true negation, a partitive article after 'que' stays as un/une/du/des, unlike after 'ne...pas' where it would become 'de'.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Ne / N'", "Premier élément (jamais 'pas')", TokenCategory.CONNECTOR, "Placé avant le verbe conjugué."),
          FormulaToken("Verbe", "Conjugué normalement", TokenCategory.TARGET_VERB, "Aucun changement de forme."),
          FormulaToken("Que", "Placé juste avant l'élément restreint", TokenCategory.CONNECTOR, "= seulement, uniquement.")
        ),
        diagramTitle = "Arbre de Décision : Où Placer 'Que' ?",
        diagramDescription = "Identifiez l'élément que vous voulez restreindre :",
        decisionSteps = listOf(
          DecisionStep(1, "Voulez-vous restreindre l'objet de l'action (ce qui est fait/mangé/vu) ?", "-> Placez 'que' juste avant cet objet (Il ne boit que de l'eau.)", "-> Passez à l'étape 2"),
          DecisionStep(2, "Voulez-vous restreindre le moment ou le lieu de l'action ?", "-> Placez 'que' juste avant l'expression de temps/lieu (Il ne travaille que le soir.)", "-> Passez à l'étape 3"),
          DecisionStep(3, "L'article après 'que' est-il un partitif (du/de la/des) ?", "-> Gardez l'article intact, PAS de transformation en 'de' (ne...que du pain, pas 'de pain')", "-> Utilisez l'article approprié normalement")
        ),
        comparisonTableTitle = "Ne...Pas vs Ne...Que",
        comparisonHeaders = listOf("Ne...Pas (négation)", "Ne...Que (restriction)", "Différence"),
        comparisonRows = listOf(
          listOf("Il ne mange pas de viande.", "Il ne mange que de la viande.", "Partitif transformé en 'de' vs conservé intact"),
          listOf("Je n'ai pas d'argent.", "Je n'ai que dix euros.", "Absence totale vs quantité limitée")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Je ne bois que du café le matin, jamais de thé.",
          highlightedSegment = "ne bois que",
          englishSentence = "I only drink coffee in the morning, never tea.",
          contextNote = "Restriction courante avec partitif conservé intact après 'que'."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Cette mesure ne concerne que les entreprises de plus de cinquante salariés.",
          highlightedSegment = "ne concerne que",
          englishSentence = "This measure only concerns companies with more than fifty employees.",
          contextNote = "Registre administratif/juridique soutenu avec restriction précise."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "J'ai qu'un euro sur moi, désolé !",
          highlightedSegment = "J'ai qu'un euro",
          englishSentence = "I only have one euro on me, sorry!",
          contextNote = "'Ne' souvent omis à l'oral familier, ne laissant que 'que' comme marqueur de restriction."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Nous n'acceptons que les paiements par virement bancaire.",
          highlightedSegment = "n'acceptons que",
          englishSentence = "We only accept payments by bank transfer.",
          contextNote = "Restriction formelle dans une politique d'entreprise, registre professionnel écrit."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Un Régime Strict",
        fullTextFr = "Depuis son opération, il ne mange que des légumes cuits à la vapeur. Il ne sort que le week-end pour voir des amis. Le médecin lui a dit qu'il ne pouvait boire que de l'eau, pas d'alcool. Il n'a que trois semaines avant son prochain contrôle.",
        fullTextEn = "Since his surgery, he only eats steamed vegetables. He only goes out on weekends to see friends. The doctor told him he could only drink water, no alcohol. He only has three weeks before his next checkup.",
        annotations = listOf(
          InlineAnnotation(
            id = "res_ann_1",
            targetPhrase = "ne mange que des légumes",
            explanationFr = "L'article partitif 'des' reste intact après 'que', contrairement à ce qui se passerait après 'ne...pas' (qui donnerait 'de légumes').",
            explanationEn = "The partitive article 'des' stays intact after 'que', unlike what would happen after 'ne...pas' (which would give 'de légumes').",
            whyItApplies = "Restriction, pas négation vraie -> article conservé.",
            commonPitfall = "Ne transformez jamais l'article en 'de' après 'que' : c'est une erreur fréquente calquée sur 'ne...pas'."
          ),
          InlineAnnotation(
            id = "res_ann_2",
            targetPhrase = "ne sort que le week-end",
            explanationFr = "'Que' se place juste avant l'expression de temps restreinte, 'le week-end', et non ailleurs dans la phrase.",
            explanationEn = "'Que' is placed right before the restricted time expression, 'le week-end', not elsewhere in the sentence.",
            whyItApplies = "Placement de 'que' devant l'élément restreint.",
            commonPitfall = "Ne placez pas 'que' automatiquement après le verbe ; il doit précéder l'élément qu'il restreint."
          ),
          InlineAnnotation(
            id = "res_ann_3",
            targetPhrase = "n'a que trois semaines",
            explanationFr = "'Que' restreint ici une quantité précise ('trois semaines'), placé juste avant le nombre.",
            explanationEn = "'Que' restricts a precise quantity here ('trois semaines'), placed right before the number.",
            whyItApplies = "Restriction d'une quantité.",
            commonPitfall = "Ne confondez pas avec une vraie négation : il a bien trois semaines, ce n'est pas zéro."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_res_1",
          instructionFr = "Transformez avec 'ne... que' :",
          instructionEn = "Transform with 'ne... que':",
          basePrompt = "Il a seulement deux frères. -> Il _____ deux frères.",
          targetAnswer = "n'a que",
          acceptedAnswers = listOf("n'a que"),
          hint = "'Seulement' se traduit par 'ne... que' encadrant le verbe.",
          explanation = "'Seulement' devient 'ne... que' : Il n'a que deux frères."
        ),
        ProductionDrillItem(
          id = "prod_res_2",
          instructionFr = "Complétez en gardant l'article partitif intact :",
          instructionEn = "Complete keeping the partitive article intact:",
          basePrompt = "Elle ne boit que _____ (de l'/de) eau minérale.",
          targetAnswer = "de l'",
          acceptedAnswers = listOf("de l'"),
          hint = "Après 'que', le partitif ne se transforme pas.",
          explanation = "Après 'que' (restriction), le partitif 'de l'' reste intact, contrairement à après 'ne...pas'."
        ),
        ProductionDrillItem(
          id = "prod_res_3",
          instructionFr = "Placez 'que' au bon endroit :",
          instructionEn = "Place 'que' in the right spot:",
          basePrompt = "Il ne travaille _____ (que) le matin, jamais l'après-midi.",
          targetAnswer = "que",
          acceptedAnswers = listOf("que"),
          hint = "'Que' précède l'élément de temps restreint.",
          explanation = "'Que' se place juste avant 'le matin', l'élément que la phrase restreint."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_res_1",
          instructionFr = "Touchez l'erreur de partitif :",
          passage = "Je ne mange que de légumes le soir pour rester léger.",
          tokens = listOf("Je", "ne", "mange", "que", "de", "légumes", "le", "soir", "pour", "rester", "léger."),
          errorTokenIndex = 4,
          errorWord = "de",
          correction = "des",
          ruleExplanation = "Après 'que' (restriction), le partitif ne se transforme pas en 'de' : il faut garder 'des légumes'."
        ),
        SpotErrorDrillItem(
          id = "spot_res_2",
          instructionFr = "Trouvez l'erreur de placement :",
          passage = "Il ne que parle anglais dans cette entreprise internationale.",
          tokens = listOf("Il", "ne", "que", "parle", "anglais", "dans", "cette", "entreprise", "internationale."),
          errorTokenIndex = 2,
          errorWord = "que",
          correction = "ne parle qu'anglais",
          ruleExplanation = "'Que' doit se placer juste avant l'élément restreint ('anglais'), pas avant le verbe : il ne parle QU'anglais."
        )
      )
    ),

    // 25. LE PLUS-QUE-PARFAIT
    StructuredRule(
      id = "plus-que-parfait",
      categoryId = "cat-verbes",
      categoryName = "Modes & Temps",
      level = "B1",
      titleFr = "Le Plus-que-parfait",
      titleEn = "Pluperfect — an Action Completed Before Another Past Action",
      summaryFr = "Le plus-que-parfait exprime une action antérieure à une autre action passée : auxiliaire (avoir/être) à l'imparfait + participe passé, mêmes règles d'accord que le passé composé.",
      summaryEn = "The pluperfect expresses an action completed before another past action: the auxiliary (avoir/être) in the imparfait + past participle, same agreement rules as the passé composé.",
      pillar = GrammarPillar.VERB_SYSTEM,
      contrastGroupId = "triad-pqp-pc",
      contrastRoleLabelFr = "Antériorité",
      contrastRoleLabelEn = "Prior action",
      ruleExplanationEn = "Think of it as 'the past of the past': when telling a story in the past and you need to step one layer further back to something that had already happened before the main past events, switch the auxiliary from présent (passé composé) to imparfait, keeping everything else identical — same auxiliary choice (avoir/être), same past participle, same agreement rules. 'Quand je suis arrivé, il était déjà parti' — 'était parti' happened before 'suis arrivé'.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Auxiliaire à l'imparfait", "avais/étais...", TokenCategory.CONNECTOR, "Avoir ou être, choisi comme au passé composé."),
          FormulaToken("Participe passé", "Accord identique au passé composé", TokenCategory.TARGET_VERB, "mangé, parti, vu...")
        ),
        diagramTitle = "Arbre de Décision : Passé Composé ou Plus-que-parfait ?",
        diagramDescription = "Situez l'action dans la chronologie du récit :",
        decisionSteps = listOf(
          DecisionStep(1, "L'action est-elle la plus récente des deux actions passées mentionnées ?", "-> Passé composé (ou imparfait pour une description)", "-> Passez à l'étape 2"),
          DecisionStep(2, "L'action s'est-elle produite AVANT une autre action déjà au passé ?", "-> Plus-que-parfait (avais/étais + participe passé)", "-> Vérifiez l'ordre chronologique réel des événements"),
          DecisionStep(3, "Choisissez le bon auxiliaire (avoir/être) comme au passé composé, puis appliquez les mêmes règles d'accord.", "-> Auxiliaire à l'imparfait + participe passé accordé", "-> Revoir les règles d'accord du participe passé")
        ),
        comparisonTableTitle = "Comparaison Passé Composé / Plus-que-parfait",
        comparisonHeaders = listOf("Passé Composé (action 2)", "Plus-que-parfait (action 1, antérieure)", "Ordre"),
        comparisonRows = listOf(
          listOf("Je suis arrivé...", "...il était déjà parti.", "Départ AVANT arrivée"),
          listOf("Elle a compris...", "...qu'elle avait fait une erreur.", "Erreur AVANT compréhension")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Quand je suis arrivé à la gare, le train était déjà parti.",
          highlightedSegment = "était déjà parti",
          englishSentence = "When I arrived at the station, the train had already left.",
          contextNote = "Plus-que-parfait pour une action antérieure à l'arrivée, marquée par 'déjà'."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Les négociations avaient échoué avant même que la réunion ne débute.",
          highlightedSegment = "avaient échoué",
          englishSentence = "The negotiations had failed even before the meeting began.",
          contextNote = "Plus-que-parfait dans un récit soutenu, antériorité marquée explicitement."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "J'avais déjà mangé quand ils sont arrivés, du coup j'ai rien pris.",
          highlightedSegment = "J'avais déjà mangé",
          englishSentence = "I had already eaten when they arrived, so I didn't have anything.",
          contextNote = "Usage oral courant du plus-que-parfait, très naturel malgré le registre familier."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "L'équipe avait déjà finalisé le rapport avant la visite du client.",
          highlightedSegment = "avait déjà finalisé",
          englishSentence = "The team had already finalized the report before the client's visit.",
          contextNote = "Plus-que-parfait dans un compte-rendu professionnel pour situer une antériorité claire."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Un Rendez-vous Manqué",
        fullTextFr = "Quand elle est arrivée au café, son ami était déjà reparti. Il l'avait attendue une heure, mais elle n'avait pas vu son message. Elle s'était trompée d'adresse et avait perdu du temps. Ils s'étaient pourtant mis d'accord la veille sur l'heure exacte.",
        fullTextEn = "When she arrived at the café, her friend had already left. He had waited an hour for her, but she hadn't seen his message. She had gotten the address wrong and had lost time. They had, however, agreed on the exact time the day before.",
        annotations = listOf(
          InlineAnnotation(
            id = "pqp_ann_1",
            targetPhrase = "était déjà reparti",
            explanationFr = "Le départ de l'ami a eu lieu AVANT l'arrivée d'elle (au passé composé implicite) : plus-que-parfait avec 'être' pour 'repartir'.",
            explanationEn = "The friend's departure happened BEFORE her arrival: pluperfect with 'être' for 'repartir'.",
            whyItApplies = "Action antérieure à une autre action passée.",
            commonPitfall = "N'utilisez pas le passé composé ici : cela suggérerait que les deux actions sont simultanées."
          ),
          InlineAnnotation(
            id = "pqp_ann_2",
            targetPhrase = "s'était trompée d'adresse",
            explanationFr = "Verbe pronominal au plus-que-parfait : auxiliaire 'être' à l'imparfait + participe passé accordé au féminin ('trompée') avec le sujet 'elle'.",
                explanationEn = "Pronominal verb in the pluperfect: auxiliary 'être' in the imparfait + past participle agreeing in the feminine ('trompée') with the subject 'elle'.",
            whyItApplies = "Verbe pronominal, accord avec le sujet.",
            commonPitfall = "N'oubliez pas l'accord du participe passé au féminin pour un verbe pronominal réfléchi."
          ),
          InlineAnnotation(
            id = "pqp_ann_3",
            targetPhrase = "s'étaient mis d'accord",
            explanationFr = "Plus-que-parfait au pluriel pour une action encore plus ancienne ('la veille'), antérieure à toute la scène racontée.",
            explanationEn = "Plural pluperfect for an even earlier action ('the day before'), prior to the entire scene being narrated.",
            whyItApplies = "Antériorité multiple dans un récit.",
            commonPitfall = "Le plus-que-parfait peut s'utiliser pour plusieurs 'couches' d'antériorité dans un même récit."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_pqp_1",
          instructionFr = "Conjuguez au plus-que-parfait :",
          instructionEn = "Conjugate in the pluperfect:",
          basePrompt = "Elle m'a dit qu'elle _____ (perdre) ses clés la veille.",
          targetAnswer = "avait perdu",
          acceptedAnswers = listOf("avait perdu"),
          hint = "Auxiliaire avoir à l'imparfait + participe passé.",
          explanation = "'Avait perdu' = auxiliaire 'avoir' à l'imparfait + participe passé de 'perdre'."
        ),
        ProductionDrillItem(
          id = "prod_pqp_2",
          instructionFr = "Conjuguez avec l'auxiliaire être :",
          instructionEn = "Conjugate with the auxiliary être:",
          basePrompt = "Quand nous sommes arrivés, ils _____ (déjà partir).",
          targetAnswer = "étaient déjà partis",
          acceptedAnswers = listOf("étaient déjà partis"),
          hint = "'Partir' se conjugue avec 'être', accord avec le sujet pluriel.",
          explanation = "'Étaient partis' = auxiliaire 'être' à l'imparfait, accordé au masculin pluriel avec 'ils'."
        ),
        ProductionDrillItem(
          id = "prod_pqp_3",
          instructionFr = "Conjuguez le verbe pronominal :",
          instructionEn = "Conjugate the pronominal verb:",
          basePrompt = "Elle _____ (se lever) tôt ce jour-là avant l'appel.",
          targetAnswer = "s'était levée",
          acceptedAnswers = listOf("s'était levée"),
          hint = "Verbe pronominal, auxiliaire être + accord féminin.",
          explanation = "'S'était levée' : plus-que-parfait pronominal avec accord féminin puisque le sujet est 'elle'."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_pqp_1",
          instructionFr = "Touchez l'erreur de temps :",
          passage = "Il m'a dit qu'il a déjà vu ce film avant notre rencontre.",
          tokens = listOf("Il", "m'a", "dit", "qu'il", "a", "déjà", "vu", "ce", "film", "avant", "notre", "rencontre."),
          errorTokenIndex = 4,
          errorWord = "a",
          correction = "avait",
          ruleExplanation = "L'action (voir le film) est antérieure à une autre action passée ('m'a dit') : il faut le plus-que-parfait 'avait déjà vu', pas le passé composé."
        ),
        SpotErrorDrillItem(
          id = "spot_pqp_2",
          instructionFr = "Trouvez l'erreur d'auxiliaire :",
          passage = "Quand je suis arrivé, elle avait déjà sortie de la salle.",
          tokens = listOf("Quand", "je", "suis", "arrivé,", "elle", "avait", "déjà", "sortie", "de", "la", "salle."),
          errorTokenIndex = 5,
          errorWord = "avait",
          correction = "était",
          ruleExplanation = "'Sortir' (sans complément d'objet direct) se conjugue avec 'être' : il fallait 'elle était déjà sortie', pas 'avait'."
        )
      )
    ),

    // 26. LE FUTUR ANTÉRIEUR
    StructuredRule(
      id = "futur-anterieur",
      categoryId = "cat-verbes",
      categoryName = "Modes & Temps",
      level = "B1",
      titleFr = "Le Futur Antérieur",
      titleEn = "Future Perfect — an Action Completed Before Another Future Action",
      summaryFr = "Le futur antérieur exprime une action qui sera achevée avant une autre action future : auxiliaire (avoir/être) au futur simple + participe passé.",
      summaryEn = "The future perfect expresses an action that will be completed before another future action: the auxiliary (avoir/être) in the futur simple + past participle.",
      pillar = GrammarPillar.VERB_SYSTEM,
      triggerToken = "Quand / Dès que / Une fois que + futur antérieur",
      triggerActionFr = "Introduit l'action achevée en premier dans une phrase au futur",
      triggerActionEn = "Introduces the action completed first in a future-tense sentence",
      ruleExplanationEn = "This tense almost always shows up after 'quand', 'dès que', 'une fois que', or 'aussitôt que' when both clauses are in the future — French insists both verbs stay in a future tense (unlike English, which often uses a present tense after 'when'): 'Quand j'aurai fini, je t'appellerai' (not 'quand j'ai fini'). The action in futur antérieur happens first; the action in futur simple happens second, as a result.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Auxiliaire au futur simple", "aurai/serai...", TokenCategory.CONNECTOR, "Avoir ou être, comme au passé composé."),
          FormulaToken("Participe passé", "Accord identique au passé composé", TokenCategory.TARGET_VERB, "fini, arrivé, vu...")
        ),
        diagramTitle = "Arbre de Décision : Futur Simple ou Futur Antérieur ?",
        diagramDescription = "Identifiez l'ordre des deux actions futures :",
        decisionSteps = listOf(
          DecisionStep(1, "La phrase contient-elle 'quand/dès que/une fois que' + deux actions futures ?", "-> L'action achevée en premier va au futur antérieur", "-> Passez à l'étape 2"),
          DecisionStep(2, "L'action est-elle la conséquence, survenant après l'autre ?", "-> Futur simple pour cette action", "-> Vérifiez l'ordre chronologique"),
          DecisionStep(3, "Choisissez l'auxiliaire (avoir/être) comme au passé composé, conjugué au futur simple.", "-> aurai/serai + participe passé", "-> Appliquez les règles d'accord habituelles")
        ),
        comparisonTableTitle = "Structure Type",
        comparisonHeaders = listOf("Futur Antérieur (1re action)", "Futur Simple (2e action)", "Connecteur"),
        comparisonRows = listOf(
          listOf("Quand j'aurai fini mon travail,", "je sortirai avec toi.", "Quand"),
          listOf("Dès qu'elle sera arrivée,", "nous commencerons la réunion.", "Dès que")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Dès que j'aurai terminé mes devoirs, je t'appellerai.",
          highlightedSegment = "j'aurai terminé",
          englishSentence = "As soon as I have finished my homework, I'll call you.",
          contextNote = "Futur antérieur après 'dès que', usage courant standard."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Une fois que le comité aura statué, la décision sera communiquée.",
          highlightedSegment = "le comité aura statué",
          englishSentence = "Once the committee has ruled, the decision will be communicated.",
          contextNote = "Futur antérieur dans un contexte administratif/juridique soutenu."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "Quand j'aurai fini de bosser, on ira boire un verre.",
          highlightedSegment = "j'aurai fini",
          englishSentence = "When I'm done working, we'll go grab a drink.",
          contextNote = "Usage oral familier, 'bosser' pour travailler, mais structure grammaticale identique."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Nous vous contacterons une fois que le paiement aura été validé.",
          highlightedSegment = "aura été validé",
          englishSentence = "We will contact you once the payment has been validated.",
          contextNote = "Futur antérieur passif dans une communication professionnelle standard."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Les Plans pour Demain",
        fullTextFr = "Quand j'aurai fini de préparer le rapport, je te l'enverrai par email. Dès que tu l'auras reçu, appelle-moi pour en discuter. Une fois que nous aurons validé les derniers détails, nous pourrons présenter le projet à l'équipe.",
        fullTextEn = "When I have finished preparing the report, I'll send it to you by email. As soon as you have received it, call me to discuss it. Once we have validated the final details, we will be able to present the project to the team.",
        annotations = listOf(
          InlineAnnotation(
            id = "fa_ann_1",
            targetPhrase = "j'aurai fini",
            explanationFr = "Futur antérieur après 'quand' : cette action (finir le rapport) précède l'envoi de l'email, lui-même au futur simple.",
            explanationEn = "Future perfect after 'quand': this action (finishing the report) precedes sending the email, itself in the futur simple.",
            whyItApplies = "Action complétée avant une autre action future.",
            commonPitfall = "N'utilisez jamais le présent après 'quand' pour une action future en français, contrairement à l'anglais."
          ),
          InlineAnnotation(
            id = "fa_ann_2",
            targetPhrase = "tu l'auras reçu",
            explanationFr = "Futur antérieur avec pronom objet placé avant l'auxiliaire, accord du participe passé avec 'l'' (le rapport, masculin).",
            explanationEn = "Future perfect with the object pronoun placed before the auxiliary, past participle agreeing with 'l'' (le rapport, masculine).",
            whyItApplies = "Accord du participe passé avec l'objet direct antéposé.",
            commonPitfall = "Le participe passé s'accorde avec le COD placé avant l'auxiliaire avoir, comme au passé composé."
          ),
          InlineAnnotation(
            id = "fa_ann_3",
            targetPhrase = "nous aurons validé",
            explanationFr = "Futur antérieur après 'une fois que', marquant l'achèvement d'une action avant la présentation du projet.",
            explanationEn = "Future perfect after 'une fois que', marking the completion of an action before the project presentation.",
            whyItApplies = "Connecteur temporel + futur antérieur.",
            commonPitfall = "'Une fois que' exige systématiquement un temps futur pour les deux actions, jamais le présent."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_fa_1",
          instructionFr = "Conjuguez au futur antérieur :",
          instructionEn = "Conjugate in the future perfect:",
          basePrompt = "Quand tu _____ (arriver), préviens-moi.",
          targetAnswer = "seras arrivé",
          acceptedAnswers = listOf("seras arrivé", "seras arrivée"),
          hint = "'Arriver' se conjugue avec 'être' au futur simple.",
          explanation = "'Seras arrivé(e)' = auxiliaire être au futur simple + participe passé de 'arriver'."
        ),
        ProductionDrillItem(
          id = "prod_fa_2",
          instructionFr = "Conjuguez avec l'auxiliaire avoir :",
          instructionEn = "Conjugate with the auxiliary avoir:",
          basePrompt = "Dès que nous _____ (finir) le projet, nous fêterons ça.",
          targetAnswer = "aurons fini",
          acceptedAnswers = listOf("aurons fini"),
          hint = "Auxiliaire avoir au futur simple + participe passé.",
          explanation = "'Aurons fini' = auxiliaire 'avoir' au futur simple (aurons) + participe passé 'fini'."
        ),
        ProductionDrillItem(
          id = "prod_fa_3",
          instructionFr = "Complétez la phrase avec les deux temps corrects :",
          instructionEn = "Complete the sentence with the two correct tenses:",
          basePrompt = "Une fois qu'elle _____ (obtenir) son diplôme, elle _____ (chercher) un emploi.",
          targetAnswer = "aura obtenu, cherchera",
          acceptedAnswers = listOf("aura obtenu, cherchera", "aura obtenu cherchera"),
          hint = "Action antérieure au futur antérieur, conséquence au futur simple.",
          explanation = "'Aura obtenu' (futur antérieur, action antérieure) puis 'cherchera' (futur simple, conséquence)."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_fa_1",
          instructionFr = "Touchez l'erreur de temps :",
          passage = "Quand j'ai fini mes examens, je partirai en vacances.",
          tokens = listOf("Quand", "j'ai", "fini", "mes", "examens,", "je", "partirai", "en", "vacances."),
          errorTokenIndex = 1,
          errorWord = "j'ai",
          correction = "j'aurai",
          ruleExplanation = "Les deux actions sont futures : il faut le futur antérieur 'j'aurai fini', pas le passé composé 'j'ai fini'."
        ),
        SpotErrorDrillItem(
          id = "spot_fa_2",
          instructionFr = "Trouvez l'erreur d'auxiliaire :",
          passage = "Dès qu'elle aura sortie de l'hôpital, elle rentrera chez elle.",
          tokens = listOf("Dès", "qu'elle", "aura", "sortie", "de", "l'hôpital,", "elle", "rentrera", "chez", "elle."),
          errorTokenIndex = 2,
          errorWord = "aura",
          correction = "sera",
          ruleExplanation = "'Sortir' sans complément d'objet direct se conjugue avec 'être' : il fallait 'sera sortie', pas 'aura sortie'."
        )
      )
    ),

    // 27. LE CONDITIONNEL PRÉSENT
    StructuredRule(
      id = "conditionnel",
      categoryId = "cat-verbes",
      categoryName = "Modes & Temps",
      level = "B1",
      titleFr = "Le Conditionnel Présent",
      titleEn = "Present Conditional — \"Would\"",
      summaryFr = "Le conditionnel présent se forme avec le radical du futur + les terminaisons de l'imparfait : si vous connaissez ces deux temps, vous connaissez déjà le conditionnel.",
      summaryEn = "The present conditional is formed with the future stem + the imparfait endings. So if you know the future stem and the imparfait endings, you already know the conditional.",
      pillar = GrammarPillar.VERB_SYSTEM,
      contrastGroupId = "triad-si-hypothese",
      contrastRoleLabelFr = "Résultat hypothétique",
      contrastRoleLabelEn = "Hypothetical result",
      ruleExplanationEn = "This is purely a mash-up of two things you already know: take exactly the same stem you'd use for the futur simple (regular = infinitive, irregular = the memorized list: ser-, aur-, ir-, fer-...) and attach the imparfait endings (-ais, -ais, -ait, -ions, -iez, -aient) instead of the future ones. No new stems, no new endings — just recombining. Main uses: politeness (je voudrais), hypothetical result in a 'si + imparfait' sentence, and unconfirmed information (il serait malade = he is reportedly ill).",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Radical du futur", "parler-, ser-, aur-, fer-...", TokenCategory.TARGET_VERB, "Exactement le même radical qu'au futur simple."),
          FormulaToken("+ Terminaisons de l'imparfait", "-ais, -ais, -ait, -ions, -iez, -aient", TokenCategory.CONNECTOR, "Empruntées telles quelles à l'imparfait.")
        ),
        diagramTitle = "Arbre de Décision : Utiliser le Conditionnel ?",
        diagramDescription = "Identifiez pourquoi vous avez besoin du conditionnel :",
        decisionSteps = listOf(
          DecisionStep(1, "Voulez-vous formuler une demande polie (je voudrais, pourriez-vous) ?", "-> Conditionnel de politesse", "-> Passez à l'étape 2"),
          DecisionStep(2, "La phrase contient-elle 'si + imparfait' dans l'autre proposition ?", "-> Conditionnel présent pour le résultat hypothétique", "-> Passez à l'étape 3"),
          DecisionStep(3, "L'information est-elle non confirmée ou rapportée (style journalistique) ?", "-> Conditionnel pour marquer l'incertitude (il serait...)", "-> Vérifiez si un autre mode/temps convient mieux")
        ),
        comparisonTableTitle = "Futur vs Conditionnel",
        comparisonHeaders = listOf("Futur Simple", "Conditionnel Présent", "Différence"),
        comparisonRows = listOf(
          listOf("je parlerai", "je parlerais", "-ai (futur) vs -ais (conditionnel)"),
          listOf("il sera", "il serait", "même radical irrégulier, terminaison différente")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Si j'avais plus de temps, je voyagerais davantage.",
          highlightedSegment = "je voyagerais",
          englishSentence = "If I had more time, I would travel more.",
          contextNote = "Conditionnel présent comme résultat d'une hypothèse introduite par 'si + imparfait'."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Selon certaines sources, le ministre présenterait sa démission cette semaine.",
          highlightedSegment = "présenterait sa démission",
          englishSentence = "According to some sources, the minister would be submitting his resignation this week.",
          contextNote = "Conditionnel journalistique marquant une information non confirmée, registre soutenu."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "Tu pourrais pas m'aider un peu, steuplaît ?",
          highlightedSegment = "Tu pourrais",
          englishSentence = "Could you help me a little, please?",
          contextNote = "Conditionnel de politesse à l'oral familier, avec 'steuplaît' (s'il te plaît contracté)."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Nous aimerions vous proposer un rendez-vous la semaine prochaine.",
          highlightedSegment = "Nous aimerions",
          englishSentence = "We would like to offer you an appointment next week.",
          contextNote = "Conditionnel de politesse standard dans la correspondance professionnelle."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Un Rêve de Voyage",
        fullTextFr = "Si j'avais assez d'argent, je ferais le tour du monde. J'aimerais visiter le Japon en premier. Ma sœur voudrait venir avec moi, mais elle devrait d'abord finir ses études. Nous pourrions peut-être partir ensemble l'année prochaine.",
        fullTextEn = "If I had enough money, I would travel around the world. I would like to visit Japan first. My sister would like to come with me, but she would have to finish her studies first. We could maybe leave together next year.",
        annotations = listOf(
          InlineAnnotation(
            id = "cond_ann_1",
            targetPhrase = "je ferais le tour",
            explanationFr = "'Ferais' combine le radical futur irrégulier de 'faire' (fer-) avec la terminaison de l'imparfait -ais.",
            explanationEn = "'Ferais' combines the irregular future stem of 'faire' (fer-) with the imparfait ending -ais.",
            whyItApplies = "Radical futur + terminaison imparfait.",
            commonPitfall = "Ne confondez pas 'ferais' (conditionnel) avec 'ferai' (futur simple, sans le deuxième 's/-ais')."
          ),
          InlineAnnotation(
            id = "cond_ann_2",
            targetPhrase = "elle devrait d'abord finir",
            explanationFr = "'Devrait' au conditionnel exprime une obligation atténuée, plus polie qu'un ordre direct.",
            explanationEn = "'Devrait' in the conditional expresses a softened obligation, more polite than a direct order.",
            whyItApplies = "Conditionnel d'atténuation/politesse.",
            commonPitfall = "'Elle doit finir' serait plus direct/impératif ; le conditionnel adoucit le propos."
          ),
          InlineAnnotation(
            id = "cond_ann_3",
            targetPhrase = "Nous pourrions peut-être partir",
            explanationFr = "'Pourrions' exprime une possibilité hypothétique et incertaine, renforcée par 'peut-être'.",
            explanationEn = "'Pourrions' expresses a hypothetical, uncertain possibility, reinforced by 'peut-être'.",
            whyItApplies = "Conditionnel de possibilité incertaine.",
            commonPitfall = "'Nous pouvons' (présent) affirmerait une certitude que le contexte ne permet pas ici."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_cond_1",
          instructionFr = "Conjuguez au conditionnel présent :",
          instructionEn = "Conjugate in the present conditional:",
          basePrompt = "Si tu me le demandais, je te _____ (aider) volontiers.",
          targetAnswer = "aiderais",
          acceptedAnswers = listOf("aiderais"),
          hint = "Radical futur régulier + terminaison imparfait -ais.",
          explanation = "'Aiderais' = radical futur 'aider-' + terminaison imparfait '-ais'."
        ),
        ProductionDrillItem(
          id = "prod_cond_2",
          instructionFr = "Conjuguez le conditionnel de politesse :",
          instructionEn = "Conjugate the conditional of politeness:",
          basePrompt = "_____ (Vouloir)-vous un café ?",
          targetAnswer = "Voudriez",
          acceptedAnswers = listOf("Voudriez"),
          hint = "'Vouloir' a le radical futur irrégulier 'voudr-'.",
          explanation = "'Voudriez' = radical futur irrégulier 'voudr-' + terminaison imparfait '-iez', formule de politesse standard."
        ),
        ProductionDrillItem(
          id = "prod_cond_3",
          instructionFr = "Conjuguez le verbe irrégulier :",
          instructionEn = "Conjugate the irregular verb:",
          basePrompt = "À ta place, je ne _____ (être) pas si sûr de moi.",
          targetAnswer = "serais",
          acceptedAnswers = listOf("serais"),
          hint = "'Être' a le radical futur irrégulier 'ser-'.",
          explanation = "'Serais' = radical futur irrégulier de 'être' (ser-) + terminaison imparfait '-ais'."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_cond_1",
          instructionFr = "Touchez l'erreur de conjugaison :",
          passage = "Si j'avais le temps, je viendrai te voir plus souvent.",
          tokens = listOf("Si", "j'avais", "le", "temps,", "je", "viendrai", "te", "voir", "plus", "souvent."),
          errorTokenIndex = 5,
          errorWord = "viendrai",
          correction = "viendrais",
          ruleExplanation = "Après 'si + imparfait', le résultat doit être au conditionnel présent : 'je viendrais', pas au futur simple 'viendrai'."
        ),
        SpotErrorDrillItem(
          id = "spot_cond_2",
          instructionFr = "Trouvez l'erreur de politesse :",
          passage = "Je veux vous demander un service, si c'est possible.",
          tokens = listOf("Je", "veux", "vous", "demander", "un", "service,", "si", "c'est", "possible."),
          errorTokenIndex = 1,
          errorWord = "veux",
          correction = "voudrais",
          ruleExplanation = "Pour une demande polie, le conditionnel 'je voudrais' est nettement préférable au présent 'je veux', plus direct/abrupt."
        )
      )
    ),

    // 28. LE PASSIF
    StructuredRule(
      id = "passif",
      categoryId = "cat-verbes",
      categoryName = "Modes & Temps",
      level = "B1",
      titleFr = "La Voix Passive",
      titleEn = "The Passive Voice",
      summaryFr = "Dans une phrase passive, l'objet de l'action active devient le sujet ; le sujet actif devient l'agent (introduit par 'par') ; l'auxiliaire être porte le temps, suivi du participe passé accordé avec le sujet.",
      summaryEn = "The active object becomes the subject; the active subject becomes the \"agent\" (introduced by \"par\"); the auxiliary être carries the tense, followed by the past participle (which agrees with the subject).",
      pillar = GrammarPillar.AGREEMENT_ENGINE,
      ruleExplanationEn = "To build the passive, take the direct object of the active sentence and promote it to subject position; conjugate 'être' in whatever tense the active verb was in; add the past participle, which now agrees with the new subject like an adjective would; and if you still need to mention who performed the action, introduce it with 'par' (by). Only verbs that take a direct object (transitive) can be made passive — a verb like 'téléphoner à' (indirect) cannot.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Sujet (ex-objet)", "Ce qui subit l'action", TokenCategory.SUBJECT, "Devient le sujet grammatical de la phrase passive."),
          FormulaToken("Être (au temps voulu)", "Porte le temps de la phrase", TokenCategory.CONNECTOR, "est, était, sera, a été..."),
          FormulaToken("Participe passé", "Accordé comme un adjectif", TokenCategory.TARGET_VERB, "S'accorde en genre/nombre avec le sujet."),
          FormulaToken("Par + agent (optionnel)", "Ex-sujet actif", TokenCategory.CONNECTOR, "Introduit qui a fait l'action, souvent omis.")
        ),
        diagramTitle = "Arbre de Décision : Construire le Passif",
        diagramDescription = "Transformez une phrase active en phrase passive :",
        decisionSteps = listOf(
          DecisionStep(1, "Le verbe actif a-t-il un complément d'objet direct (transitif) ?", "-> Le passif est possible, l'objet devient sujet", "-> Le passif est impossible pour ce verbe"),
          DecisionStep(2, "Conjuguez 'être' au même temps que le verbe actif d'origine.", "-> Ajoutez le participe passé du verbe, accordé avec le nouveau sujet", "-> Vérifiez le temps du verbe actif"),
          DecisionStep(3, "Voulez-vous préciser qui a fait l'action ?", "-> Ajoutez 'par + agent' à la fin", "-> Omettez l'agent s'il est évident ou sans importance")
        ),
        comparisonTableTitle = "Actif vs Passif",
        comparisonHeaders = listOf("Voix Active", "Voix Passive", "Transformation"),
        comparisonRows = listOf(
          listOf("Le chat mange la souris.", "La souris est mangée par le chat.", "Objet -> sujet, accord au féminin"),
          listOf("Les ouvriers ont construit ce pont.", "Ce pont a été construit par les ouvriers.", "Passé composé -> auxiliaire être au passé composé")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Le repas a été préparé par ma mère ce matin.",
          highlightedSegment = "a été préparé",
          englishSentence = "The meal was prepared by my mother this morning.",
          contextNote = "Passif au passé composé avec agent explicite introduit par 'par'."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Cette loi sera prochainement examinée par le parlement.",
          highlightedSegment = "sera prochainement examinée",
          englishSentence = "This law will soon be examined by parliament.",
          contextNote = "Passif au futur simple dans un contexte institutionnel/soutenu."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "Ma voiture s'est fait rayer, j'en reviens pas !",
          highlightedSegment = "s'est fait rayer",
          englishSentence = "My car got scratched, I can't believe it!",
          contextNote = "Passif informel avec 'se faire' + infinitif, très courant à l'oral pour un événement subi."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Les candidatures seront examinées par le comité de recrutement.",
          highlightedSegment = "seront examinées",
          englishSentence = "Applications will be reviewed by the recruitment committee.",
          contextNote = "Passif standard dans une annonce professionnelle, accord au féminin pluriel avec 'candidatures'."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "L'Histoire d'un Monument",
        fullTextFr = "Cette cathédrale a été construite par des artisans médiévaux au douzième siècle. Elle a été restaurée plusieurs fois au fil des siècles. Aujourd'hui, elle est visitée chaque année par des millions de touristes. Les vitraux seront bientôt nettoyés par une équipe spécialisée.",
        fullTextEn = "This cathedral was built by medieval craftsmen in the twelfth century. It has been restored several times over the centuries. Today, it is visited every year by millions of tourists. The stained-glass windows will soon be cleaned by a specialized team.",
        annotations = listOf(
          InlineAnnotation(
            id = "pas_ann_1",
            targetPhrase = "a été construite",
            explanationFr = "Passif au passé composé : 'être' au présent composé + participe passé accordé au féminin avec 'cathédrale'.",
            explanationEn = "Passive in the passé composé: 'être' in the compound present + past participle agreeing in the feminine with 'cathédrale'.",
            whyItApplies = "Accord du participe passé avec le sujet au passif.",
            commonPitfall = "N'oubliez pas l'accord : 'construite' (et non 'construit') car le sujet 'cathédrale' est féminin."
          ),
          InlineAnnotation(
            id = "pas_ann_2",
            targetPhrase = "est visitée par des millions",
            explanationFr = "'Par' introduit l'agent de l'action, ici les touristes qui visitent la cathédrale.",
            explanationEn = "'Par' introduces the agent of the action, here the tourists who visit the cathedral.",
            whyItApplies = "Agent introduit par 'par'.",
            commonPitfall = "Certains verbes de sentiment/état utilisent 'de' au lieu de 'par' pour l'agent (aimé de tous), mais 'par' reste le plus courant."
          ),
          InlineAnnotation(
            id = "pas_ann_3",
            targetPhrase = "seront bientôt nettoyés",
            explanationFr = "Passif au futur simple : 'être' au futur + participe passé accordé au masculin pluriel avec 'vitraux'.",
            explanationEn = "Passive in the futur simple: 'être' in the future + past participle agreeing in the masculine plural with 'vitraux'.",
            whyItApplies = "Passif au futur, accord pluriel.",
            commonPitfall = "'Vitraux' est masculin pluriel (pluriel irrégulier de 'vitrail'), donc 'nettoyés' avec -s, pas -e."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_pas_1",
          instructionFr = "Transformez à la voix passive :",
          instructionEn = "Transform into the passive voice:",
          basePrompt = "Le chef prépare le plat. -> Le plat _____ (être préparé) par le chef.",
          targetAnswer = "est préparé",
          acceptedAnswers = listOf("est préparé"),
          hint = "Présent + participe passé accordé au masculin singulier.",
          explanation = "'Est préparé' : présent de 'être' + participe passé accordé avec 'plat' (masculin singulier)."
        ),
        ProductionDrillItem(
          id = "prod_pas_2",
          instructionFr = "Transformez au passé composé passif :",
          instructionEn = "Transform into the passive passé composé:",
          basePrompt = "Les enfants ont écrit ces lettres. -> Ces lettres _____ (être écrit) par les enfants.",
          targetAnswer = "ont été écrites",
          acceptedAnswers = listOf("ont été écrites"),
          hint = "'Être' au passé composé + accord féminin pluriel.",
          explanation = "'Ont été écrites' : auxiliaire 'avoir' + 'été' + participe passé accordé au féminin pluriel avec 'lettres'."
        ),
        ProductionDrillItem(
          id = "prod_pas_3",
          instructionFr = "Ajoutez l'agent avec 'par' :",
          instructionEn = "Add the agent with 'par':",
          basePrompt = "Ce roman est très apprécié _____ (les/par les) lecteurs.",
          targetAnswer = "par les",
          acceptedAnswers = listOf("par les"),
          hint = "L'agent au passif est introduit par 'par'.",
          explanation = "L'agent de l'action passive est toujours introduit par 'par' : apprécié PAR les lecteurs."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_pas_1",
          instructionFr = "Touchez l'erreur d'accord au passif :",
          passage = "Ces maisons ont été construit par la même entreprise.",
          tokens = listOf("Ces", "maisons", "ont", "été", "construit", "par", "la", "même", "entreprise."),
          errorTokenIndex = 4,
          errorWord = "construit",
          correction = "construites",
          ruleExplanation = "Le participe passé au passif s'accorde avec le sujet : 'maisons' est féminin pluriel, donc 'construites', pas 'construit'."
        ),
        SpotErrorDrillItem(
          id = "spot_pas_2",
          instructionFr = "Trouvez l'erreur de préposition :",
          passage = "Ce livre a été écrit de un auteur très célèbre.",
          tokens = listOf("Ce", "livre", "a", "été", "écrit", "de", "un", "auteur", "très", "célèbre."),
          errorTokenIndex = 5,
          errorWord = "de",
          correction = "par",
          ruleExplanation = "L'agent au passif est introduit par 'par', pas 'de' : écrit PAR un auteur."
        )
      )
    ),

    // 29. LE GÉRONDIF
    StructuredRule(
      id = "gerondif",
      categoryId = "cat-verbes",
      categoryName = "Modes & Temps",
      level = "B1",
      titleFr = "Le Gérondif : « En Faisant »",
      titleEn = "The Gerund — \"By/While Doing\"",
      summaryFr = "Le gérondif se forme avec 'en' + participe présent (radical du 'nous' au présent + -ant) ; exceptions : être -> étant, avoir -> ayant, savoir -> sachant.",
      summaryEn = "Formed with en + present participle (nous-stem + -ant). Exceptions: être→étant, avoir→ayant, savoir→sachant.",
      pillar = GrammarPillar.OTHER,
      ruleExplanationEn = "Take the 'nous' form of the present tense, drop the '-ons', and add '-ant': nous parlons -> parlant, nous finissons -> finissant. Then put 'en' in front: en parlant, en finissant. It always describes an action happening at the same time as, or as the means of, the main verb's action, and its subject must be the same as the main clause's subject — 'Il s'est blessé en cuisinant' means HE was cooking when he got hurt.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("En", "Marqueur du gérondif", TokenCategory.CONNECTOR, "Toujours présent, jamais omis."),
          FormulaToken("Radical du 'nous' au présent", "parl-, finiss-, all-...", TokenCategory.TARGET_VERB, "Retirez '-ons' de la forme 'nous'."),
          FormulaToken("+ -ant", "Terminaison universelle", TokenCategory.CONNECTOR, "Identique pour tous les verbes réguliers.")
        ),
        diagramTitle = "Arbre de Décision : Former le Gérondif",
        diagramDescription = "Construisez le gérondif étape par étape :",
        decisionSteps = listOf(
          DecisionStep(1, "Le verbe est-il l'un des trois irréguliers (être, avoir, savoir) ?", "-> Utilisez la forme mémorisée : étant, ayant, sachant", "-> Passez à l'étape 2"),
          DecisionStep(2, "Prenez la forme 'nous' du présent et retirez '-ons'.", "-> Ajoutez '-ant' à ce radical", "-> Vérifiez la forme 'nous' si le verbe est irrégulier"),
          DecisionStep(3, "Le sujet du gérondif est-il bien le même que celui du verbe principal ?", "-> Ajoutez 'en' devant : en + radical + ant", "-> Le gérondif est incorrect si les sujets diffèrent")
        ),
        comparisonTableTitle = "Formation du Gérondif",
        comparisonHeaders = listOf("Verbe", "Nous (présent)", "Gérondif"),
        comparisonRows = listOf(
          listOf("parler", "nous parlons", "en parlant"),
          listOf("finir", "nous finissons", "en finissant"),
          listOf("être (irr.)", "—", "en étant"),
          listOf("avoir (irr.)", "—", "en ayant")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Elle a appris le français en regardant des films.",
          highlightedSegment = "en regardant des films",
          englishSentence = "She learned French by watching movies.",
          contextNote = "Gérondif exprimant le moyen par lequel une action est accomplie."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Tout en reconnaissant ses torts, il a refusé de s'excuser.",
          highlightedSegment = "Tout en reconnaissant",
          englishSentence = "While acknowledging his faults, he refused to apologize.",
          contextNote = "'Tout en' + gérondif marque une opposition/concession, registre soutenu."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "Je me suis cassé la figure en courant après le bus !",
          highlightedSegment = "en courant",
          englishSentence = "I fell flat on my face while running after the bus!",
          contextNote = "Gérondif de simultanéité, usage oral courant/familier."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "En optimisant nos processus, nous avons réduit les coûts de vingt pour cent.",
          highlightedSegment = "En optimisant nos processus",
          englishSentence = "By optimizing our processes, we reduced costs by twenty percent.",
          contextNote = "Gérondif de moyen dans un rapport professionnel, exprimant comment un résultat a été atteint."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Une Matinée Chargée",
        fullTextFr = "En sortant de la douche, elle a entendu le téléphone sonner. Tout en s'habillant, elle a répondu à l'appel. Elle a préparé le petit-déjeuner en écoutant les informations. En étant organisée, elle a réussi à ne pas être en retard.",
        fullTextEn = "While getting out of the shower, she heard the phone ring. While getting dressed, she answered the call. She made breakfast while listening to the news. By being organized, she managed not to be late.",
        annotations = listOf(
          InlineAnnotation(
            id = "ger_ann_1",
            targetPhrase = "En sortant de la douche",
            explanationFr = "Gérondif de simultanéité : elle sortait de la douche au moment où elle a entendu le téléphone.",
            explanationEn = "Gerund of simultaneity: she was getting out of the shower when she heard the phone.",
            whyItApplies = "Simultanéité entre deux actions du même sujet.",
            commonPitfall = "Le sujet du gérondif doit être identique à celui du verbe principal : c'est bien 'elle' dans les deux cas."
          ),
          InlineAnnotation(
            id = "ger_ann_2",
            targetPhrase = "Tout en s'habillant",
            explanationFr = "'Tout en' renforce l'idée de simultanéité, parfois avec une nuance de concession ou de multitâche.",
            explanationEn = "'Tout en' reinforces the idea of simultaneity, sometimes with a nuance of concession or multitasking.",
            whyItApplies = "Renforcement de la simultanéité.",
            commonPitfall = "'Tout' devant 'en' n'est pas obligatoire, mais insiste sur le fait de faire deux choses en même temps."
          ),
          InlineAnnotation(
            id = "ger_ann_3",
            targetPhrase = "En étant organisée",
            explanationFr = "'Étant' est la forme irrégulière du gérondif de 'être', à mémoriser telle quelle.",
            explanationEn = "'Étant' is the irregular gerund form of 'être', to be memorized as is.",
            whyItApplies = "Verbe irrégulier au gérondif.",
            commonPitfall = "Ne dites jamais 'en sommant' ou une forme régulière ; 'étant' doit être mémorisé séparément."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_ger_1",
          instructionFr = "Formez le gérondif :",
          instructionEn = "Form the gerund:",
          basePrompt = "Il s'est endormi _____ (regarder) la télévision.",
          targetAnswer = "en regardant",
          acceptedAnswers = listOf("en regardant"),
          hint = "Nous regardons -> radical 'regard-' + -ant.",
          explanation = "'En regardant' = 'en' + radical du 'nous' (regard-) + -ant."
        ),
        ProductionDrillItem(
          id = "prod_ger_2",
          instructionFr = "Formez le gérondif irrégulier :",
          instructionEn = "Form the irregular gerund:",
          basePrompt = "_____ (Avoir) plus de patience, elle aurait mieux réussi l'entretien.",
          targetAnswer = "En ayant",
          acceptedAnswers = listOf("En ayant"),
          hint = "'Avoir' a une forme de gérondif irrégulière.",
          explanation = "'Avoir' devient 'ayant' au gérondif, forme irrégulière à mémoriser : en ayant."
        ),
        ProductionDrillItem(
          id = "prod_ger_3",
          instructionFr = "Formez le gérondif d'un verbe en -ir :",
          instructionEn = "Form the gerund of an -ir verb:",
          basePrompt = "Elle a réussi son examen _____ (finir) tôt et en révisant bien.",
          targetAnswer = "en finissant",
          acceptedAnswers = listOf("en finissant"),
          hint = "Nous finissons -> radical 'finiss-' + -ant.",
          explanation = "'En finissant' = 'en' + radical du 'nous' (finiss-) + -ant."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_ger_1",
          instructionFr = "Touchez l'erreur de formation :",
          passage = "Il a appris à cuisiner en essayer de nouvelles recettes chaque semaine.",
          tokens = listOf("Il", "a", "appris", "à", "cuisiner", "en", "essayer", "de", "nouvelles", "recettes", "chaque", "semaine."),
          errorTokenIndex = 6,
          errorWord = "essayer",
          correction = "essayant",
          ruleExplanation = "Après 'en', il faut le participe présent en -ant, pas l'infinitif : en essayANT, pas 'en essayer'."
        ),
        SpotErrorDrillItem(
          id = "spot_ger_2",
          instructionFr = "Trouvez l'erreur de radical irrégulier :",
          passage = "En sachant la vérité, elle a préféré ne rien dire, en soyant discrète.",
          tokens = listOf("En", "sachant", "la", "vérité,", "elle", "a", "préféré", "ne", "rien", "dire,", "en", "soyant", "discrète."),
          errorTokenIndex = 11,
          errorWord = "soyant",
          correction = "étant",
          ruleExplanation = "'Être' a la forme irrégulière 'étant' au gérondif, jamais 'soyant' qui n'existe pas."
        )
      )
    ),

    // 30. LA MISE EN RELIEF
    StructuredRule(
      id = "mise-en-relief",
      categoryId = "cat-avance",
      categoryName = "Nuances Avancées",
      level = "B1",
      titleFr = "La Mise en Relief : C'est... Qui / Que",
      titleEn = "Emphatic Structures — Highlighting the Subject or Object",
      summaryFr = "'C'est... qui' met en relief le sujet ; 'c'est... que' met en relief tout autre élément (objet, complément) de la phrase.",
      summaryEn = "'C'est... qui' highlights the subject; 'c'est... que' highlights any other element (object, complement) of the sentence.",
      pillar = GrammarPillar.OTHER,
      ruleExplanationEn = "The test is simple: whatever you're pulling forward to emphasize, check if it would be the subject of the plain sentence. If yes, frame it with 'c'est... qui' (Marie a appelé -> C'est Marie qui a appelé). If it's anything else — a direct object, a time expression, a place — frame it with 'c'est... que' (J'ai vu Marie hier -> C'est hier que j'ai vu Marie). The rest of the sentence follows unchanged after qui/que.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("C'est", "Introducteur fixe", TokenCategory.CONNECTOR, "Reste 'c'est' quel que soit le temps de la phrase originale (à l'oral courant)."),
          FormulaToken("Élément mis en relief", "Sujet ou autre complément", TokenCategory.SUBJECT, "Ce qu'on veut souligner."),
          FormulaToken("Qui (sujet) / Que (autre)", "Charnière grammaticale", TokenCategory.CONNECTOR, "'Qui' si sujet, 'que' pour tout le reste.")
        ),
        diagramTitle = "Arbre de Décision : Qui ou Que ?",
        diagramDescription = "Testez la fonction de l'élément mis en avant :",
        decisionSteps = listOf(
          DecisionStep(1, "L'élément mis en relief serait-il le sujet de la phrase simple ?", "-> C'est + élément + QUI", "-> Passez à l'étape 2"),
          DecisionStep(2, "L'élément est-il un objet, un complément de temps, de lieu ou de manière ?", "-> C'est + élément + QUE", "-> Vérifiez la fonction grammaticale exacte"),
          DecisionStep(3, "Le reste de la phrase suit-il normalement après qui/que ?", "-> Gardez le reste de la phrase inchangé", "-> Réorganisez si nécessaire pour la clarté")
        ),
        comparisonTableTitle = "Qui vs Que",
        comparisonHeaders = listOf("Phrase Simple", "Mise en Relief", "Qui/Que"),
        comparisonRows = listOf(
          listOf("Marie a appelé.", "C'est Marie qui a appelé.", "QUI (Marie = sujet)"),
          listOf("J'ai vu Marie hier.", "C'est hier que j'ai vu Marie.", "QUE (hier = complément de temps)")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "C'est mon frère qui a préparé le dîner ce soir.",
          highlightedSegment = "C'est mon frère qui",
          englishSentence = "It's my brother who made dinner tonight.",
          contextNote = "Mise en relief du sujet 'mon frère' avec 'qui', usage courant."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "C'est précisément cette ambiguïté que le rapport cherche à clarifier.",
          highlightedSegment = "C'est précisément cette ambiguïté que",
          englishSentence = "It is precisely this ambiguity that the report seeks to clarify.",
          contextNote = "Mise en relief d'un complément d'objet direct avec 'que', registre soutenu/analytique."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "C'est toi qui as cassé le vase, avoue !",
          highlightedSegment = "C'est toi qui",
          englishSentence = "It's you who broke the vase, admit it!",
          contextNote = "Mise en relief accusatrice à l'oral familier, ton direct."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "C'est ce point précis que nous devons aborder en priorité.",
          highlightedSegment = "C'est ce point précis que",
          englishSentence = "It is this specific point that we need to address as a priority.",
          contextNote = "Mise en relief d'un objet direct dans un contexte professionnel pour structurer une réunion."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Une Enquête sur un Incident",
        fullTextFr = "C'est le gardien qui a découvert le problème en premier. C'est justement cette pièce qu'il faut examiner de plus près. C'est hier soir que l'incident s'est produit, vers vingt-deux heures. C'est vous qui avez signalé l'alarme, n'est-ce pas ?",
        fullTextEn = "It's the caretaker who discovered the problem first. It is precisely this room that needs to be examined more closely. It was last night that the incident happened, around ten pm. It's you who reported the alarm, isn't it?",
        annotations = listOf(
          InlineAnnotation(
            id = "mer_ann_1",
            targetPhrase = "C'est le gardien qui",
            explanationFr = "'Le gardien' est le sujet de 'a découvert', donc on utilise 'qui' pour le mettre en relief.",
            explanationEn = "'Le gardien' is the subject of 'a découvert', so 'qui' is used to highlight it.",
            whyItApplies = "Élément mis en relief = sujet -> qui.",
            commonPitfall = "Ne dites pas 'c'est le gardien que a découvert' — le sujet exige toujours 'qui'."
          ),
          InlineAnnotation(
            id = "mer_ann_2",
            targetPhrase = "c'est justement cette pièce qu'il faut",
            explanationFr = "'Cette pièce' est l'objet direct d'examiner (dans la phrase simple), donc on utilise 'que' (élidé en qu').",
            explanationEn = "'Cette pièce' is the direct object of 'examiner' (in the plain sentence), so 'que' (elided to qu') is used.",
            whyItApplies = "Élément mis en relief = objet -> que.",
            commonPitfall = "Ne confondez pas avec 'qui' ; 'cette pièce' n'est pas sujet de 'examiner' mais son objet."
          ),
          InlineAnnotation(
            id = "mer_ann_3",
            targetPhrase = "C'est hier soir que",
            explanationFr = "'Hier soir' est un complément de temps, pas le sujet de la phrase, donc on utilise 'que'.",
            explanationEn = "'Hier soir' is a time complement, not the sentence's subject, so 'que' is used.",
            whyItApplies = "Complément de temps mis en relief -> que.",
            commonPitfall = "Les compléments de temps/lieu/manière prennent toujours 'que', jamais 'qui'."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_mer_1",
          instructionFr = "Complétez avec 'qui' ou 'que' :",
          instructionEn = "Complete with 'qui' or 'que':",
          basePrompt = "C'est Paul _____ (qui/que) a organisé la fête surprise.",
          targetAnswer = "qui",
          acceptedAnswers = listOf("qui"),
          hint = "'Paul' est le sujet de 'a organisé'.",
          explanation = "'Paul' est sujet de 'a organisé', donc on utilise 'qui'."
        ),
        ProductionDrillItem(
          id = "prod_mer_2",
          instructionFr = "Complétez avec 'qui' ou 'que' :",
          instructionEn = "Complete with 'qui' or 'que':",
          basePrompt = "C'est ce livre _____ (qui/que) je préfère parmi tous ceux que j'ai lus.",
          targetAnswer = "que",
          acceptedAnswers = listOf("que"),
          hint = "'Ce livre' est l'objet direct de 'préférer'.",
          explanation = "'Ce livre' est l'objet direct de 'je préfère', donc on utilise 'que'."
        ),
        ProductionDrillItem(
          id = "prod_mer_3",
          instructionFr = "Mettez en relief l'élément souligné :",
          instructionEn = "Highlight the underlined element:",
          basePrompt = "Nous partons à Nice demain. -> _____ (mise en relief de 'demain')",
          targetAnswer = "C'est demain que nous partons à Nice.",
          acceptedAnswers = listOf("C'est demain que nous partons à Nice."),
          hint = "'Demain' est un complément de temps.",
          explanation = "'Demain' n'est pas le sujet : on utilise donc 'c'est... que' pour le mettre en relief."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_mer_1",
          instructionFr = "Touchez l'erreur qui/que :",
          passage = "C'est mon collègue que a résolu le problème technique hier.",
          tokens = listOf("C'est", "mon", "collègue", "que", "a", "résolu", "le", "problème", "technique", "hier."),
          errorTokenIndex = 3,
          errorWord = "que",
          correction = "qui",
          ruleExplanation = "'Mon collègue' est le sujet de 'a résolu', donc il faut 'qui', pas 'que'."
        ),
        SpotErrorDrillItem(
          id = "spot_mer_2",
          instructionFr = "Trouvez l'erreur :",
          passage = "C'est cette solution qui nous devons choisir pour avancer.",
          tokens = listOf("C'est", "cette", "solution", "qui", "nous", "devons", "choisir", "pour", "avancer."),
          errorTokenIndex = 3,
          errorWord = "qui",
          correction = "que",
          ruleExplanation = "'Cette solution' est l'objet direct de 'choisir', pas le sujet : il faut 'que', pas 'qui'."
        )
      )
    ),

    // 31. LE DISCOURS RAPPORTÉ
    StructuredRule(
      id = "discours-rapporte",
      categoryId = "cat-avance",
      categoryName = "Nuances Avancées",
      level = "B1",
      titleFr = "Le Discours Rapporté (Indirect)",
      titleEn = "Reported (Indirect) Speech",
      summaryFr = "Rapporter des paroles au présent garde les mêmes temps verbaux, mais les pronoms et possessifs changent pour correspondre au nouveau point de vue du narrateur.",
      summaryEn = "When reporting at the present tense, verb tenses stay the same, but pronouns and possessives shift to match the new speaker's perspective.",
      pillar = GrammarPillar.OTHER,
      ruleExplanationEn = "If the reporting verb is in the present ('il dit que'), the reported clause keeps its original tense exactly — the only real work is shifting person: 'je' becomes 'il/elle', 'mon' becomes 'son', 'ici' can become 'là-bas' if the location shifts too. The trap is only when the reporting verb is in the PAST ('il a dit que') — that triggers the classic tense-backshift (présent->imparfait, passé composé->plus-que-parfait, futur->conditionnel), which is a separate, more advanced rule; at this level, focus on the present-tense reporting where tenses stay put.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Il/Elle dit que", "Verbe introducteur au présent", TokenCategory.CONNECTOR, "Introduit le discours rapporté sans changer les temps."),
          FormulaToken("Pronoms/possessifs ajustés", "je->il, mon->son...", TokenCategory.SUBJECT, "Changent pour refléter le nouveau narrateur.")
        ),
        diagramTitle = "Arbre de Décision : Rapporter des Paroles",
        diagramDescription = "Transformez le discours direct en discours indirect :",
        decisionSteps = listOf(
          DecisionStep(1, "Le verbe introducteur est-il au présent (il dit que) ?", "-> Gardez le même temps verbal dans la proposition rapportée", "-> Passez à l'étape 2 (backshift si le verbe introducteur est au passé)"),
          DecisionStep(2, "Ajustez les pronoms personnels (je -> il/elle, tu -> il/elle ou je selon le contexte).", "-> Adaptez aussi les possessifs (mon -> son, notre -> leur)", "-> Vérifiez chaque pronom un par un"),
          DecisionStep(3, "Une question directe doit-elle être rapportée ?", "-> Utilisez 'si' (oui/non), 'ce que' (qu'est-ce que), ou le mot interrogatif directement", "-> Pas de question mais un ordre : utilisez 'de + infinitif'")
        ),
        comparisonTableTitle = "Discours Direct vs Indirect",
        comparisonHeaders = listOf("Discours Direct", "Discours Indirect", "Changement"),
        comparisonRows = listOf(
          listOf("« Je suis fatigué », dit-il.", "Il dit qu'il est fatigué.", "je -> il, temps inchangé"),
          listOf("« Où vas-tu ? » demande-t-elle.", "Elle demande où je vais.", "Question directe -> mot interrogatif + ordre sujet-verbe")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Elle dit qu'elle arrivera vers dix-neuf heures.",
          highlightedSegment = "qu'elle arrivera",
          englishSentence = "She says she will arrive around seven pm.",
          contextNote = "Discours rapporté au présent : le futur simple original reste inchangé."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Le porte-parole a précisé que le gouvernement examinerait la proposition.",
          highlightedSegment = "que le gouvernement examinerait",
          englishSentence = "The spokesperson specified that the government would examine the proposal.",
          contextNote = "Discours rapporté avec verbe introducteur au passé, entraînant un conditionnel (backshift du futur)."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "Il me demande si je viens ce soir, je sais pas quoi lui dire.",
          highlightedSegment = "si je viens",
          englishSentence = "He's asking me if I'm coming tonight, I don't know what to tell him.",
          contextNote = "Question rapportée avec 'si', usage oral courant/familier."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Le directeur demande que le rapport lui soit remis avant vendredi.",
          highlightedSegment = "demande que le rapport lui soit remis",
          englishSentence = "The director is asking that the report be handed to him before Friday.",
          contextNote = "Ordre rapporté au subjonctif dans un contexte professionnel formel."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Un Message Transmis",
        fullTextFr = "Marie dit qu'elle ne pourra pas venir à la réunion demain. Elle explique qu'elle a un empêchement de dernière minute. Elle demande si nous pouvons reporter la réunion à la semaine prochaine. Elle ajoute qu'elle nous enverra son rapport par email.",
        fullTextEn = "Marie says she won't be able to come to the meeting tomorrow. She explains that she has a last-minute conflict. She asks if we can postpone the meeting to next week. She adds that she will send us her report by email.",
        annotations = listOf(
          InlineAnnotation(
            id = "dr_ann_1",
            targetPhrase = "qu'elle ne pourra pas venir",
            explanationFr = "Le verbe introducteur 'dit' est au présent, donc le futur simple original ('je ne pourrai pas') reste inchangé, avec le pronom ajusté.",
            explanationEn = "The reporting verb 'dit' is in the present, so the original futur simple ('je ne pourrai pas') stays unchanged, with the pronoun adjusted.",
            whyItApplies = "Verbe introducteur au présent -> pas de changement de temps.",
            commonPitfall = "Ne changez le temps que si le verbe introducteur est au passé ; ici, il est au présent."
          ),
          InlineAnnotation(
            id = "dr_ann_2",
            targetPhrase = "Elle demande si nous pouvons",
            explanationFr = "Une question fermée (oui/non) rapportée utilise 'si', avec l'ordre sujet-verbe normal (pas d'inversion).",
            explanationEn = "A closed (yes/no) reported question uses 'si', with normal subject-verb order (no inversion).",
            whyItApplies = "Question fermée rapportée -> si + ordre normal.",
            commonPitfall = "Ne dites jamais 'si pouvons-nous' avec inversion dans le discours rapporté."
          ),
          InlineAnnotation(
            id = "dr_ann_3",
            targetPhrase = "elle nous enverra son rapport",
            explanationFr = "'Son' remplace 'mon' original, car le possesseur passe de la première à la troisième personne dans le discours rapporté.",
            explanationEn = "'Son' replaces the original 'mon', since the owner shifts from first to third person in reported speech.",
            whyItApplies = "Ajustement du possessif selon le nouveau narrateur.",
            commonPitfall = "N'oubliez jamais d'ajuster aussi les possessifs, pas seulement les pronoms sujets."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_dr_1",
          instructionFr = "Transformez au discours indirect :",
          instructionEn = "Transform into indirect speech:",
          basePrompt = "« Je suis très content », dit Marc. -> Marc dit _____ (qu'il/qu'elle) est très content.",
          targetAnswer = "qu'il",
          acceptedAnswers = listOf("qu'il"),
          hint = "Marc est un homme, donc 'il'.",
          explanation = "'Je' devient 'il' car Marc (masculin) rapporte ses propres paroles à la 3e personne."
        ),
        ProductionDrillItem(
          id = "prod_dr_2",
          instructionFr = "Transformez la question directe en indirecte :",
          instructionEn = "Transform the direct question into indirect:",
          basePrompt = "« Est-ce que tu viens ? » -> Il me demande _____ (si/que) je viens.",
          targetAnswer = "si",
          acceptedAnswers = listOf("si"),
          hint = "Question fermée (oui/non) rapportée = 'si'.",
          explanation = "Une question fermée (oui/non) se rapporte avec 'si', jamais 'que'."
        ),
        ProductionDrillItem(
          id = "prod_dr_3",
          instructionFr = "Transformez l'ordre direct en indirect :",
          instructionEn = "Transform the direct order into indirect:",
          basePrompt = "« Ferme la porte ! » -> Il me demande _____ (de fermer/que je ferme) la porte.",
          targetAnswer = "de fermer",
          acceptedAnswers = listOf("de fermer", "de fermer la porte"),
          hint = "Un ordre rapporté utilise 'de + infinitif'.",
          explanation = "Un ordre à l'impératif se rapporte avec 'demander de + infinitif' : il me demande de fermer la porte."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_dr_1",
          instructionFr = "Touchez l'erreur de discours rapporté :",
          passage = "Elle me demande si viens-je à la fête samedi.",
          tokens = listOf("Elle", "me", "demande", "si", "viens-je", "à", "la", "fête", "samedi."),
          errorTokenIndex = 4,
          errorWord = "viens-je",
          correction = "je viens",
          ruleExplanation = "Dans le discours rapporté, l'ordre est toujours sujet-verbe normal, sans inversion : si je viens, pas 'viens-je'."
        ),
        SpotErrorDrillItem(
          id = "spot_dr_2",
          instructionFr = "Trouvez l'erreur de pronom :",
          passage = "Paul dit que je vais bientôt déménager dans le sud.",
          tokens = listOf("Paul", "dit", "que", "je", "vais", "bientôt", "déménager", "dans", "le", "sud."),
          errorTokenIndex = 3,
          errorWord = "je",
          correction = "il",
          ruleExplanation = "Paul rapporte ses propres paroles à la 3e personne : le pronom doit devenir 'il', pas rester 'je'."
        )
      )
    ),

    // 32. LES INDICATEURS DE TEMPS
    StructuredRule(
      id = "indicateurs-temps",
      categoryId = "cat-avance",
      categoryName = "Nuances Avancées",
      level = "B1",
      titleFr = "Les Indicateurs de Temps",
      titleEn = "Time Markers — a Frequent Source of Confusion for English Speakers",
      summaryFr = "Les indicateurs de temps orientés vers le futur se répartissent en trois catégories grammaticales : adverbes (seuls), conjonctions (+ proposition), et prépositions (+ nom/infinitif).",
      summaryEn = "Future-oriented time markers fall into three grammatical categories: adverbs, conjunctions, and prepositions.",
      pillar = GrammarPillar.OTHER,
      ruleExplanationEn = "Sort each time word by what it can grammatically attach to, not by its English translation: an adverb stands alone with no complement ('plus tard', 'bientôt'); a conjunction introduces a full clause with its own subject and verb ('quand il arrivera', 'dès que tu pourras'); a preposition attaches to a noun or infinitive, never a full clause ('avant son départ', 'après avoir mangé', 'pendant trois jours'). Mixing them up — using a preposition where a conjunction is needed — is one of the most common structural errors at this level.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Adverbe", "seul, sans complément", TokenCategory.CONNECTOR, "plus tard, bientôt, ensuite..."),
          FormulaToken("Conjonction + proposition", "sujet + verbe conjugué", TokenCategory.CONNECTOR, "quand, dès que, avant que + subjonctif..."),
          FormulaToken("Préposition + nom/infinitif", "pas de proposition complète", TokenCategory.CONNECTOR, "avant, après, pendant, dans + nom/infinitif")
        ),
        diagramTitle = "Arbre de Décision : Quelle Catégorie ?",
        diagramDescription = "Identifiez ce qui suit le mot de temps :",
        decisionSteps = listOf(
          DecisionStep(1, "Le mot est-il utilisé seul, sans rien après ?", "-> Adverbe (plus tard, bientôt, ensuite)", "-> Passez à l'étape 2"),
          DecisionStep(2, "Le mot est-il suivi d'un sujet + verbe conjugué (proposition complète) ?", "-> Conjonction (quand, dès que, avant que)", "-> Passez à l'étape 3"),
          DecisionStep(3, "Le mot est-il suivi d'un simple nom ou d'un infinitif (pas de sujet propre) ?", "-> Préposition (avant, après, pendant, dans)", "-> Revérifiez la structure de la phrase")
        ),
        comparisonTableTitle = "Tableau Comparatif",
        comparisonHeaders = listOf("Catégorie", "Exemple", "Ce qui suit"),
        comparisonRows = listOf(
          listOf("Adverbe", "On se voit plus tard.", "Rien (mot isolé)"),
          listOf("Conjonction", "Avant qu'il ne parte, appelle-le.", "Sujet + verbe (subjonctif ici)"),
          listOf("Préposition", "Avant son départ, appelle-le.", "Nom (son départ)")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Avant de partir, n'oublie pas d'éteindre les lumières.",
          highlightedSegment = "Avant de partir",
          englishSentence = "Before leaving, don't forget to turn off the lights.",
          contextNote = "'Avant de' + infinitif car les deux sujets (implicite) sont identiques."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Avant que la séance ne soit levée, le président a tenu à remercier chacun.",
          highlightedSegment = "Avant que la séance ne soit levée",
          englishSentence = "Before the session was adjourned, the chairman wanted to thank everyone.",
          contextNote = "'Avant que' + subjonctif, sujets différents, registre soutenu avec 'ne' explétif."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "On se retrouve après, d'accord ?",
          highlightedSegment = "après",
          englishSentence = "We'll meet up after, okay?",
          contextNote = "'Après' utilisé comme adverbe seul, très courant à l'oral familier."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Pendant la durée du contrat, toute modification devra être signalée.",
          highlightedSegment = "Pendant la durée du contrat",
          englishSentence = "During the term of the contract, any change must be reported.",
          contextNote = "'Pendant' + nom (préposition), registre contractuel/professionnel."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Organiser un Événement",
        fullTextFr = "Avant l'événement, nous devons réserver la salle. Avant que les invités n'arrivent, tout doit être prêt. Pendant la soirée, un traiteur s'occupera du buffet. Ensuite, nous ferons le bilan de la soirée tous ensemble.",
        fullTextEn = "Before the event, we need to book the venue. Before the guests arrive, everything must be ready. During the evening, a caterer will handle the buffet. Afterwards, we will review the evening all together.",
        annotations = listOf(
          InlineAnnotation(
            id = "it_ann_1",
            targetPhrase = "Avant l'événement",
            explanationFr = "'Avant' est ici une préposition suivie d'un simple nom ('l'événement'), sans proposition complète.",
            explanationEn = "'Avant' is a preposition here followed by a plain noun ('l'événement'), with no complete clause.",
            whyItApplies = "Préposition + nom.",
            commonPitfall = "Ne confondez pas avec 'avant que', qui exige un sujet et un verbe conjugué après."
          ),
          InlineAnnotation(
            id = "it_ann_2",
            targetPhrase = "Avant que les invités n'arrivent",
            explanationFr = "'Avant que' est une conjonction suivie d'un sujet ('les invités') et d'un verbe au subjonctif ('arrivent').",
            explanationEn = "'Avant que' is a conjunction followed by a subject ('les invités') and a subjunctive verb ('arrivent').",
            whyItApplies = "Conjonction + proposition complète au subjonctif.",
            commonPitfall = "'Avant que' exige toujours le subjonctif, contrairement à 'après que' qui prend l'indicatif."
          ),
          InlineAnnotation(
            id = "it_ann_3",
            targetPhrase = "Ensuite, nous ferons",
            explanationFr = "'Ensuite' est un adverbe utilisé seul, sans complément direct, en tête de phrase.",
            explanationEn = "'Ensuite' is an adverb used alone, with no direct complement, at the start of the sentence.",
            whyItApplies = "Adverbe isolé.",
            commonPitfall = "'Ensuite' ne peut jamais être suivi directement d'un nom ou d'une proposition comme une préposition/conjonction."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_it_1",
          instructionFr = "Choisissez la bonne catégorie :",
          instructionEn = "Choose the correct category:",
          basePrompt = "_____ (Avant de/Avant que) partir, vérifie que tout est éteint.",
          targetAnswer = "Avant de",
          acceptedAnswers = listOf("Avant de"),
          hint = "Suivi d'un infinitif, sujet implicite identique.",
          explanation = "Devant un infinitif (partir), on utilise la préposition 'avant de', pas la conjonction 'avant que'."
        ),
        ProductionDrillItem(
          id = "prod_it_2",
          instructionFr = "Complétez avec la conjonction et le mode corrects :",
          instructionEn = "Complete with the correct conjunction and mood:",
          basePrompt = "Appelle-moi _____ (dès que/dès) tu arrives.",
          targetAnswer = "dès que",
          acceptedAnswers = listOf("dès que"),
          hint = "Suivi d'un sujet + verbe conjugué.",
          explanation = "Devant une proposition complète (tu arrives), il faut la conjonction 'dès que', pas seulement 'dès'."
        ),
        ProductionDrillItem(
          id = "prod_it_3",
          instructionFr = "Choisissez la préposition correcte :",
          instructionEn = "Choose the correct preposition:",
          basePrompt = "_____ (Pendant/Pendant que) les vacances, je lirai beaucoup.",
          targetAnswer = "Pendant",
          acceptedAnswers = listOf("Pendant"),
          hint = "Suivi d'un simple nom.",
          explanation = "Devant un nom seul ('les vacances'), on utilise la préposition 'pendant', pas 'pendant que'."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_it_1",
          instructionFr = "Touchez l'erreur de catégorie :",
          passage = "Avant que le dîner, nous prendrons l'apéritif ensemble.",
          tokens = listOf("Avant", "que", "le", "dîner,", "nous", "prendrons", "l'apéritif", "ensemble."),
          errorTokenIndex = 1,
          errorWord = "que",
          correction = "(supprimer 'que')",
          ruleExplanation = "'Le dîner' est un simple nom, pas une proposition : il fallait la préposition 'avant', sans 'que'."
        ),
        SpotErrorDrillItem(
          id = "spot_it_2",
          instructionFr = "Trouvez l'erreur :",
          passage = "Pendant que le voyage, nous avons visité trois pays.",
          tokens = listOf("Pendant", "que", "le", "voyage,", "nous", "avons", "visité", "trois", "pays."),
          errorTokenIndex = 1,
          errorWord = "que",
          correction = "(supprimer 'que')",
          ruleExplanation = "'Le voyage' est un nom, pas une proposition complète : il fallait 'pendant' seul, sans 'que'."
        )
      )
    ),

    // 33. L'EXPRESSION DE LA CAUSE
    StructuredRule(
      id = "cause",
      categoryId = "cat-avance",
      categoryName = "Nuances Avancées",
      level = "B1",
      titleFr = "L'Expression de la Cause",
      titleEn = "Expressing Cause/Reason",
      summaryFr = "Parce que introduit une cause neutre et répond à 'pourquoi ?' ; puisque présente une cause déjà connue/évidente ; car est réservé à l'écrit soutenu et n'ouvre jamais une phrase.",
      summaryEn = "In more formal writing: puisque (since, known reason), étant donné que / vu que (given that), sous prétexte que (on the pretext that — implies doubt about the reason given).",
      pillar = GrammarPillar.OTHER,
      ruleExplanationEn = "Choose by what the cause adds to the conversation, not just by translation: 'parce que' answers a real 'pourquoi ?' with new information; 'puisque' presents a cause the listener already knows or that is obvious, often justifying a conclusion rather than explaining a fact; 'car' is a written, slightly formal equivalent of 'parce que' that can never start a sentence; 'étant donné que'/'vu que' are formal equivalents of 'puisque'; 'sous prétexte que' casts doubt on the reason given, implying the speaker doesn't believe it.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Parce que", "+ proposition, répond à 'pourquoi ?'", TokenCategory.CONNECTOR, "Cause neutre, information nouvelle."),
          FormulaToken("Puisque", "+ proposition, cause connue/évidente", TokenCategory.CONNECTOR, "Souvent en tête de phrase, justifie une conclusion."),
          FormulaToken("Car", "+ proposition, jamais en tête de phrase", TokenCategory.CONNECTOR, "Registre écrit soutenu, équivalent de 'parce que'.")
        ),
        diagramTitle = "Arbre de Décision : Quel Connecteur de Cause ?",
        diagramDescription = "Identifiez la nature de la cause exprimée :",
        decisionSteps = listOf(
          DecisionStep(1, "La cause répond-elle directement à une question 'pourquoi ?' avec une information nouvelle ?", "-> Parce que (cause neutre)", "-> Passez à l'étape 2"),
          DecisionStep(2, "La cause est-elle déjà connue de l'interlocuteur ou évidente dans le contexte ?", "-> Puisque (cause connue, souvent en tête de phrase)", "-> Passez à l'étape 3"),
          DecisionStep(3, "Le contexte est-il écrit et soutenu, sans vouloir commencer la phrase par le connecteur ?", "-> Car (jamais en début de phrase)", "-> Utilisez 'étant donné que' ou 'vu que' pour un registre encore plus formel")
        ),
        comparisonTableTitle = "Tableau des Connecteurs de Cause",
        comparisonHeaders = listOf("Connecteur", "Nuance", "Exemple"),
        comparisonRows = listOf(
          listOf("parce que", "cause neutre, nouvelle info", "Il est absent parce qu'il est malade."),
          listOf("puisque", "cause connue/évidente", "Puisque tu es là, aide-moi."),
          listOf("car", "écrit soutenu, jamais en tête", "Il est resté, car il pleuvait."),
          listOf("sous prétexte que", "cause dont on doute", "Il est parti sous prétexte qu'il était fatigué.")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Je suis en retard parce que le bus n'est pas passé.",
          highlightedSegment = "parce que le bus n'est pas passé",
          englishSentence = "I'm late because the bus didn't come.",
          contextNote = "'Parce que' répond à un 'pourquoi ?' implicite, information nouvelle pour l'interlocuteur."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Étant donné que les conditions météorologiques sont défavorables, la sortie est annulée.",
          highlightedSegment = "Étant donné que",
          englishSentence = "Given that weather conditions are unfavorable, the outing is cancelled.",
          contextNote = "'Étant donné que' est l'équivalent formel de 'puisque' dans un communiqué officiel."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "Vu qu'il pleut, on reste à la maison, hein.",
          highlightedSegment = "Vu qu'il pleut",
          englishSentence = "Since it's raining, we're staying home, right.",
          contextNote = "'Vu que' à l'oral familier, cause évidente et déjà connue des deux interlocuteurs."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Le contrat a été résilié, car les délais n'ont pas été respectés.",
          highlightedSegment = "car les délais n'ont pas été respectés",
          englishSentence = "The contract was terminated, as the deadlines were not met.",
          contextNote = "'Car' en milieu de phrase, registre professionnel écrit, jamais en début de phrase."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Une Décision Justifiée",
        fullTextFr = "Puisque tout le monde est d'accord, nous pouvons commencer la réunion. Le projet a pris du retard parce que le fournisseur a eu des problèmes de production. Nous avons dû revoir le budget, car les coûts ont augmenté. Il a démissionné sous prétexte qu'il voulait changer de carrière, mais nous savons que c'est plus compliqué que ça.",
        fullTextEn = "Since everyone agrees, we can start the meeting. The project fell behind because the supplier had production issues. We had to revise the budget, as costs increased. He resigned under the pretext of wanting a career change, but we know it's more complicated than that.",
        annotations = listOf(
          InlineAnnotation(
            id = "cau_ann_1",
            targetPhrase = "Puisque tout le monde est d'accord",
            explanationFr = "'Puisque' introduit ici une cause déjà évidente pour tous (l'accord général), justifiant le passage à l'action.",
            explanationEn = "'Puisque' introduces here an already obvious cause (general agreement), justifying moving to action.",
            whyItApplies = "Cause connue/évidente en tête de phrase.",
            commonPitfall = "'Parce que' serait moins naturel ici, car la cause n'est pas une information nouvelle mais un constat partagé."
          ),
          InlineAnnotation(
            id = "cau_ann_2",
            targetPhrase = "car les coûts ont augmenté",
            explanationFr = "'Car' apparaît en milieu de phrase, jamais en position initiale, dans un registre écrit légèrement soutenu.",
            explanationEn = "'Car' appears mid-sentence, never in initial position, in a slightly formal written register.",
            whyItApplies = "'Car' jamais en tête de phrase.",
            commonPitfall = "Ne commencez jamais une phrase par 'Car' ; ce connecteur relie toujours deux propositions déjà en cours."
          ),
          InlineAnnotation(
            id = "cau_ann_3",
            targetPhrase = "sous prétexte qu'il voulait",
            explanationFr = "'Sous prétexte que' indique que le locuteur doute de la véracité de la raison invoquée par la personne.",
            explanationEn = "'Sous prétexte que' indicates that the speaker doubts the truthfulness of the reason given by the person.",
            whyItApplies = "Cause dont on doute la sincérité.",
            commonPitfall = "N'utilisez pas 'parce que' ici : cela impliquerait que la raison est acceptée comme vraie, contrairement au sens visé."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_cau_1",
          instructionFr = "Choisissez le connecteur adapté :",
          instructionEn = "Choose the appropriate connector:",
          basePrompt = "_____ (Puisque/Parce que) tu insistes, j'accepte ton invitation.",
          targetAnswer = "Puisque",
          acceptedAnswers = listOf("Puisque"),
          hint = "La cause (l'insistance) est déjà connue/évidente.",
          explanation = "'Puisque' convient car la cause est déjà connue de l'interlocuteur (son insistance visible), pas une nouvelle information."
        ),
        ProductionDrillItem(
          id = "prod_cau_2",
          instructionFr = "Complétez avec le connecteur formel équivalent à 'puisque' :",
          instructionEn = "Complete with the formal equivalent of 'puisque':",
          basePrompt = "_____ (Étant donné que/Parce que) les stocks sont épuisés, la commande est reportée.",
          targetAnswer = "Étant donné que",
          acceptedAnswers = listOf("Étant donné que"),
          hint = "Registre formel, cause déjà connue.",
          explanation = "'Étant donné que' est l'équivalent formel de 'puisque', adapté à un registre écrit officiel."
        ),
        ProductionDrillItem(
          id = "prod_cau_3",
          instructionFr = "Placez 'car' correctement dans la phrase :",
          instructionEn = "Place 'car' correctly in the sentence:",
          basePrompt = "Nous avons annulé la sortie, _____ (car/parce que) il pleuvait trop fort.",
          targetAnswer = "car",
          acceptedAnswers = listOf("car"),
          hint = "'Car' relie deux propositions, jamais en tête de phrase.",
          explanation = "'Car' s'utilise en milieu de phrase pour relier deux propositions, jamais pour commencer une phrase."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_cau_1",
          instructionFr = "Touchez l'erreur de placement :",
          passage = "Car il faisait beau, nous sommes allés à la plage.",
          tokens = listOf("Car", "il", "faisait", "beau,", "nous", "sommes", "allés", "à", "la", "plage."),
          errorTokenIndex = 0,
          errorWord = "Car",
          correction = "Comme (ou Parce qu'il faisait beau, ...)",
          ruleExplanation = "'Car' ne peut jamais commencer une phrase ; il faut utiliser 'comme' ou reformuler avec 'parce que'."
        ),
        SpotErrorDrillItem(
          id = "spot_cau_2",
          instructionFr = "Trouvez le connecteur mal choisi :",
          passage = "Sous prétexte qu'il est très malade, il a immédiatement appelé un médecin en urgence.",
          tokens = listOf("Sous", "prétexte", "qu'il", "est", "très", "malade,", "il", "a", "immédiatement", "appelé", "un", "médecin", "en", "urgence."),
          errorTokenIndex = 0,
          errorWord = "Sous",
          correction = "Comme (ou Parce que)",
          ruleExplanation = "'Sous prétexte que' impliquerait un doute sur la maladie, ce qui contredit l'action sincère d'appeler un médecin ; 'comme' ou 'parce que' conviennent mieux."
        )
      )
    ),

    // 34. L'EXPRESSION DE LA CONSÉQUENCE
    StructuredRule(
      id = "consequence",
      categoryId = "cat-avance",
      categoryName = "Nuances Avancées",
      level = "B1",
      titleFr = "L'Expression de la Conséquence",
      titleEn = "Expressing Consequence/Result",
      summaryFr = "Donc, alors, par conséquent, si bien que, tellement/si... que introduisent le résultat d'une cause déjà exprimée, avec des nuances de registre et d'intensité.",
      summaryEn = "Connectors like donc, alors, par conséquent, si bien que, and tellement/si... que introduce the result of an already-expressed cause, with nuances of register and intensity.",
      pillar = GrammarPillar.OTHER,
      ruleExplanationEn = "'Donc' and 'alors' are the everyday, neutral connectors for a simple result. 'Par conséquent' and 'de ce fait' are more formal/written equivalents. 'Si bien que' and 'de sorte que' introduce a consequence as a natural, almost inevitable outcome, usually with the indicative. The intensity structures 'tellement/si + adjectif/adverbe + que' and 'tant/tellement de + nom + que' link a high degree of something directly to its consequence ('il était tellement fatigué qu'il s'est endormi').",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Donc / Alors", "Connecteur neutre courant", TokenCategory.CONNECTOR, "Cause, donc/alors conséquence."),
          FormulaToken("Par conséquent / De ce fait", "Connecteur formel", TokenCategory.CONNECTOR, "Registre soutenu, écrit."),
          FormulaToken("Tellement/Si + adj + que", "Intensité + conséquence", TokenCategory.CONNECTOR, "Lie un degré élevé à son résultat.")
        ),
        diagramTitle = "Arbre de Décision : Quel Connecteur de Conséquence ?",
        diagramDescription = "Identifiez le registre et la nuance souhaités :",
        decisionSteps = listOf(
          DecisionStep(1, "Le contexte est-il oral/courant et le résultat simple ?", "-> Donc / Alors", "-> Passez à l'étape 2"),
          DecisionStep(2, "Le contexte est-il écrit/formel ?", "-> Par conséquent / De ce fait / Si bien que", "-> Passez à l'étape 3"),
          DecisionStep(3, "Voulez-vous lier un degré d'intensité (très/beaucoup) à une conséquence directe ?", "-> Tellement/Si + adjectif + que / Tant de + nom + que", "-> Utilisez un connecteur simple si aucune intensité n'est visée")
        ),
        comparisonTableTitle = "Tableau des Connecteurs de Conséquence",
        comparisonHeaders = listOf("Connecteur", "Registre", "Exemple"),
        comparisonRows = listOf(
          listOf("donc / alors", "courant", "Il pleut, donc on reste à la maison."),
          listOf("par conséquent", "soutenu/écrit", "Les ventes ont chuté ; par conséquent, des mesures ont été prises."),
          listOf("tellement... que", "intensité", "Il était tellement fatigué qu'il s'est endormi immédiatement.")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Il n'a pas révisé, donc il a raté son examen.",
          highlightedSegment = "donc il a raté",
          englishSentence = "He didn't study, so he failed his exam.",
          contextNote = "'Donc' comme connecteur de conséquence neutre et courant."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "La demande a considérablement augmenté ; par conséquent, les prix ont suivi la même tendance.",
          highlightedSegment = "par conséquent, les prix ont suivi",
          englishSentence = "Demand has considerably increased; consequently, prices have followed the same trend.",
          contextNote = "'Par conséquent' dans une analyse économique soutenue et écrite."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "J'avais trop la flemme, alors j'ai rien fait ce week-end.",
          highlightedSegment = "alors j'ai rien fait",
          englishSentence = "I was too lazy, so I didn't do anything this weekend.",
          contextNote = "'Alors' à l'oral familier, très courant pour enchaîner cause et conséquence."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Le projet a pris du retard, si bien que le lancement a été repoussé d'un mois.",
          highlightedSegment = "si bien que le lancement a été repoussé",
          englishSentence = "The project fell behind, so much so that the launch was pushed back a month.",
          contextNote = "'Si bien que' présentant une conséquence quasi inévitable, registre professionnel."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Un Voyage Perturbé",
        fullTextFr = "Il y avait tellement de circulation que nous sommes arrivés en retard. Le vol a été annulé, alors nous avons dû trouver un autre moyen de transport. Les prix des billets de train avaient tant augmenté que nous avons choisi le bus. Par conséquent, le voyage a duré deux fois plus longtemps que prévu.",
        fullTextEn = "There was so much traffic that we arrived late. The flight was cancelled, so we had to find another means of transport. Train ticket prices had increased so much that we chose the bus. Consequently, the trip took twice as long as planned.",
        annotations = listOf(
          InlineAnnotation(
            id = "cons_ann_1",
            targetPhrase = "tellement de circulation que",
            explanationFr = "'Tellement de + nom + que' lie une grande quantité (la circulation) à sa conséquence directe (l'arrivée en retard).",
            explanationEn = "'Tellement de + noun + que' links a large quantity (traffic) to its direct consequence (arriving late).",
            whyItApplies = "Intensité + conséquence.",
            commonPitfall = "Avec un nom, on utilise 'tellement de' ou 'tant de', jamais 'tellement' seul (réservé aux adjectifs/adverbes)."
          ),
          InlineAnnotation(
            id = "cons_ann_2",
            targetPhrase = "avaient tant augmenté que",
            explanationFr = "'Tant' devant un verbe (augmenter) intensifie l'action, suivi de 'que' pour introduire la conséquence.",
            explanationEn = "'Tant' before a verb (augmenter) intensifies the action, followed by 'que' to introduce the consequence.",
            whyItApplies = "Tant + verbe + que.",
            commonPitfall = "'Tant' s'utilise avec un verbe, alors que 'tant de' s'utilise devant un nom."
          ),
          InlineAnnotation(
            id = "cons_ann_3",
            targetPhrase = "Par conséquent, le voyage a duré",
            explanationFr = "'Par conséquent' en tête de phrase introduit une conséquence de façon formelle, reliant les événements précédents à ce résultat final.",
            explanationEn = "'Par conséquent' at the start of the sentence formally introduces a consequence, linking the previous events to this final result.",
            whyItApplies = "Connecteur formel de conséquence en tête de phrase.",
            commonPitfall = "Contrairement à 'car', 'par conséquent' PEUT commencer une phrase."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_cons_1",
          instructionFr = "Complétez avec le connecteur d'intensité correct :",
          instructionEn = "Complete with the correct intensity connector:",
          basePrompt = "Il faisait _____ (tellement/tant) froid que nous sommes rentrés plus tôt.",
          targetAnswer = "tellement",
          acceptedAnswers = listOf("tellement", "si"),
          hint = "Devant un adjectif, on utilise 'tellement' ou 'si'.",
          explanation = "Devant l'adjectif 'froid', on utilise 'tellement' (ou 'si'), pas 'tant' qui s'utilise devant un verbe ou avec 'de + nom'."
        ),
        ProductionDrillItem(
          id = "prod_cons_2",
          instructionFr = "Complétez avec le connecteur formel :",
          instructionEn = "Complete with the formal connector:",
          basePrompt = "Les coûts ont explosé ; _____ (par conséquent/donc) l'entreprise a dû licencier.",
          targetAnswer = "par conséquent",
          acceptedAnswers = listOf("par conséquent"),
          hint = "Registre soutenu et écrit.",
          explanation = "'Par conséquent' convient au registre soutenu/écrit visé par la phrase."
        ),
        ProductionDrillItem(
          id = "prod_cons_3",
          instructionFr = "Complétez avec 'tant de' ou 'tellement de' :",
          instructionEn = "Complete with 'tant de' or 'tellement de':",
          basePrompt = "Il y avait _____ (tant de/tant) monde que nous n'avons pas pu entrer.",
          targetAnswer = "tant de",
          acceptedAnswers = listOf("tant de", "tellement de"),
          hint = "Devant un nom, on utilise 'tant de' ou 'tellement de'.",
          explanation = "Devant le nom 'monde', il faut 'tant de' (ou 'tellement de'), jamais 'tant' seul."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_cons_1",
          instructionFr = "Touchez l'erreur de construction :",
          passage = "Elle était tant fatiguée qu'elle s'est couchée à vingt heures.",
          tokens = listOf("Elle", "était", "tant", "fatiguée", "qu'elle", "s'est", "couchée", "à", "vingt", "heures."),
          errorTokenIndex = 2,
          errorWord = "tant",
          correction = "tellement",
          ruleExplanation = "Devant un adjectif ('fatiguée'), il faut 'tellement' ou 'si', pas 'tant' qui s'utilise avec un verbe ou 'de + nom'."
        ),
        SpotErrorDrillItem(
          id = "spot_cons_2",
          instructionFr = "Trouvez l'erreur :",
          passage = "Car il n'y avait plus de billets, nous avons annulé le voyage.",
          tokens = listOf("Car", "il", "n'y", "avait", "plus", "de", "billets,", "nous", "avons", "annulé", "le", "voyage."),
          errorTokenIndex = 0,
          errorWord = "Car",
          correction = "Comme",
          ruleExplanation = "'Car' introduit une cause et ne peut jamais débuter une phrase ; ici il faudrait 'comme' (cause) ou reformuler avec 'donc' (conséquence) dans l'autre sens."
        )
      )
    ),

    // 35. L'EXPRESSION DU BUT
    StructuredRule(
      id = "but",
      categoryId = "cat-avance",
      categoryName = "Nuances Avancées",
      level = "B1",
      titleFr = "L'Expression du But",
      titleEn = "Expressing Purpose — \"In Order To / So That\"",
      summaryFr = "Même règle que le subjonctif : même sujet -> infinitif (pour + infinitif) ; sujets différents -> subjonctif (pour que + subjonctif).",
      summaryEn = "Same rule as the subjunctive: same subject → infinitive; two different subjects → subjunctive.",
      pillar = GrammarPillar.VERB_SYSTEM,
      triggerToken = "pour que / afin que",
      triggerActionFr = "Sujets différents -> subjonctif obligatoire",
      triggerActionEn = "Different subjects -> mandatory subjunctive",
      ruleExplanationEn = "This works exactly like the subjunctive same-subject rule you already know: if the person doing the main action and the person meant to benefit are the same, use 'pour + infinitif' (Je travaille pour réussir — I work [myself] to succeed [myself]). If they're two different people, switch to 'pour que + subjonctif' (Je travaille pour qu'il réussisse — I work so that HE succeeds). 'Afin de'/'afin que' are more formal synonyms of 'pour'/'pour que' with the identical same-subject/different-subject split.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Pour / Afin de + infinitif", "Même sujet", TokenCategory.CONNECTOR, "Le sujet du but est identique au sujet principal."),
          FormulaToken("Pour que / Afin que + subjonctif", "Sujets différents", TokenCategory.CONNECTOR, "Le bénéficiaire du but diffère du sujet principal.")
        ),
        diagramTitle = "Arbre de Décision : Infinitif ou Subjonctif pour le But ?",
        diagramDescription = "Comparez les deux sujets impliqués :",
        decisionSteps = listOf(
          DecisionStep(1, "Le sujet qui agit et le bénéficiaire du but sont-ils la même personne ?", "-> Pour / Afin de + infinitif", "-> Passez à l'étape 2"),
          DecisionStep(2, "Les deux sujets sont-ils différents ?", "-> Pour que / Afin que + subjonctif", "-> Revérifiez qui bénéficie réellement de l'action"),
          DecisionStep(3, "Le registre est-il formel/écrit ?", "-> Préférez 'afin de/afin que'", "-> 'Pour/pour que' convient à tous les registres")
        ),
        comparisonTableTitle = "Même Sujet vs Sujets Différents",
        comparisonHeaders = listOf("Même Sujet (infinitif)", "Sujets Différents (subjonctif)", "Nuance"),
        comparisonRows = listOf(
          listOf("Je travaille pour réussir.", "Je travaille pour qu'il réussisse.", "moi/moi vs moi/lui"),
          listOf("Elle économise afin de voyager.", "Elle économise afin que ses enfants voyagent.", "elle/elle vs elle/ses enfants")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Je me lève tôt pour arriver à l'heure au travail.",
          highlightedSegment = "pour arriver à l'heure",
          englishSentence = "I get up early to arrive on time at work.",
          contextNote = "'Pour' + infinitif car le sujet ('je') est identique dans les deux actions."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Des mesures ont été prises afin que la sécurité des usagers soit garantie.",
          highlightedSegment = "afin que la sécurité des usagers soit garantie",
          englishSentence = "Measures were taken so that users' safety would be guaranteed.",
          contextNote = "'Afin que' + subjonctif, sujets différents, registre administratif soutenu."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "Je te prête mes clés pour que tu puisses rentrer avant moi.",
          highlightedSegment = "pour que tu puisses",
          englishSentence = "I'm lending you my keys so you can get home before me.",
          contextNote = "'Pour que' + subjonctif à l'oral courant/familier, sujets différents (je/tu)."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Nous avons revu le planning afin d'optimiser la productivité de l'équipe.",
          highlightedSegment = "afin d'optimiser",
          englishSentence = "We revised the schedule in order to optimize the team's productivity.",
          contextNote = "'Afin de' + infinitif, même sujet ('nous'), registre professionnel formel."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Préparer un Examen",
        fullTextFr = "Elle révise chaque soir pour réussir son examen. Son professeur lui a donné des exercices supplémentaires pour qu'elle progresse plus vite. Elle a organisé son emploi du temps afin de ne rien oublier. Ses parents l'encouragent pour qu'elle garde confiance en elle.",
        fullTextEn = "She reviews every evening to pass her exam. Her teacher gave her extra exercises so she would progress faster. She organized her schedule in order not to forget anything. Her parents encourage her so that she keeps confidence in herself.",
        annotations = listOf(
          InlineAnnotation(
            id = "but_ann_1",
            targetPhrase = "pour réussir son examen",
            explanationFr = "Même sujet ('elle' révise et 'elle' réussit) : on utilise l'infinitif après 'pour'.",
            explanationEn = "Same subject ('elle' revises and 'elle' passes): the infinitive is used after 'pour'.",
            whyItApplies = "Sujet identique -> infinitif.",
            commonPitfall = "Ne dites pas 'pour qu'elle réussisse' ici : c'est bien la même personne qui agit et qui bénéficie."
          ),
          InlineAnnotation(
            id = "but_ann_2",
            targetPhrase = "pour qu'elle progresse plus vite",
            explanationFr = "Sujets différents : le professeur donne des exercices, mais c'est l'élève qui progresse. On utilise donc 'pour que' + subjonctif.",
            explanationEn = "Different subjects: the teacher gives exercises, but it's the student who progresses. So 'pour que' + subjunctive is used.",
            whyItApplies = "Sujets différents -> pour que + subjonctif.",
            commonPitfall = "Ne dites pas 'pour progresser' ici, car ce n'est pas le professeur qui progresse."
          ),
          InlineAnnotation(
            id = "but_ann_3",
            targetPhrase = "afin de ne rien oublier",
            explanationFr = "Même sujet ('elle' organise et 'elle' n'oublie rien) : 'afin de' + infinitif, forme négative encadrant l'infinitif.",
            explanationEn = "Same subject ('elle' organizes and 'elle' forgets nothing): 'afin de' + infinitive, negative form surrounding the infinitive.",
            whyItApplies = "Sujet identique, registre légèrement plus soutenu avec 'afin de'.",
            commonPitfall = "À la forme négative avec un infinitif, 'ne' et 'pas/rien' se placent tous deux avant l'infinitif."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_but_1",
          instructionFr = "Complétez avec l'infinitif ou le subjonctif :",
          instructionEn = "Complete with the infinitive or subjunctive:",
          basePrompt = "Il économise pour _____ (acheter/qu'il achète) une nouvelle voiture.",
          targetAnswer = "acheter",
          acceptedAnswers = listOf("acheter"),
          hint = "Même sujet dans les deux actions.",
          explanation = "Le sujet est le même ('il' économise et 'il' achète), donc on utilise l'infinitif après 'pour'."
        ),
        ProductionDrillItem(
          id = "prod_but_2",
          instructionFr = "Complétez avec le subjonctif requis :",
          instructionEn = "Complete with the required subjunctive:",
          basePrompt = "Je te laisse la voiture pour que tu _____ (pouvoir) aller travailler.",
          targetAnswer = "puisses",
          acceptedAnswers = listOf("puisses"),
          hint = "Sujets différents (je/tu) -> subjonctif.",
          explanation = "'Pour que' impose le subjonctif car les sujets sont différents : je/tu."
        ),
        ProductionDrillItem(
          id = "prod_but_3",
          instructionFr = "Complétez avec 'afin de' ou 'afin que' :",
          instructionEn = "Complete with 'afin de' or 'afin que':",
          basePrompt = "Nous avons changé de stratégie _____ (afin de/afin que) rester compétitifs.",
          targetAnswer = "afin de",
          acceptedAnswers = listOf("afin de"),
          hint = "Même sujet ('nous').",
          explanation = "Le sujet est identique ('nous' change et 'nous' reste compétitif), donc 'afin de' + infinitif."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_but_1",
          instructionFr = "Touchez l'erreur de construction :",
          passage = "Elle travaille dur pour que elle réussisse ses études.",
          tokens = listOf("Elle", "travaille", "dur", "pour", "que", "elle", "réussisse", "ses", "études."),
          errorTokenIndex = 3,
          errorWord = "pour",
          correction = "pour réussir",
          ruleExplanation = "Le sujet est le même ('elle' travaille et 'elle' réussit) : il fallait l'infinitif 'pour réussir', pas 'pour que' + subjonctif."
        ),
        SpotErrorDrillItem(
          id = "spot_but_2",
          instructionFr = "Trouvez l'erreur :",
          passage = "Le professeur explique lentement pour comprendre les élèves.",
          tokens = listOf("Le", "professeur", "explique", "lentement", "pour", "comprendre", "les", "élèves."),
          errorTokenIndex = 4,
          errorWord = "pour",
          correction = "pour que les élèves comprennent",
          ruleExplanation = "Les sujets sont différents (le professeur explique, les élèves comprennent) : il fallait 'pour que' + subjonctif, pas l'infinitif."
        )
      )
    ),

    // 36. EXPRIMER LA NÉCESSITÉ ET L'OBLIGATION
    StructuredRule(
      id = "necessite-obligation",
      categoryId = "cat-avance",
      categoryName = "Nuances Avancées",
      level = "B1",
      titleFr = "Exprimer la Nécessité et l'Obligation",
      titleEn = "Ways to Express Necessity and Obligation",
      summaryFr = "Il faut que + subjonctif, devoir + infinitif, être obligé de + infinitif expriment tous l'obligation, avec des nuances de force et de personnalisation.",
      summaryEn = "Il faut que + subjunctive, devoir + infinitive, être obligé de + infinitive all express obligation, with nuances of strength and personalization.",
      pillar = GrammarPillar.VERB_SYSTEM,
      triggerToken = "Il faut que",
      triggerActionFr = "Obligation impersonnelle -> subjonctif obligatoire",
      triggerActionEn = "Impersonal obligation -> mandatory subjunctive",
      ruleExplanationEn = "'Il faut que + subjonctif' expresses an impersonal necessity without naming who specifically must act (Il faut qu'on parte = we need to leave, general necessity). 'Devoir + infinitif' personalizes the obligation directly onto a subject (Je dois partir = I must leave) and can also express probability (Il doit être tard = it must be late). 'Être obligé(e) de + infinitif' emphasizes an external constraint forcing the action, often implying reluctance. All three can be softened into a suggestion rather than a strict obligation using the conditional (il faudrait que, je devrais).",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Il faut que", "+ subjonctif", TokenCategory.TRIGGER, "Nécessité impersonnelle, sans sujet précis nommé."),
          FormulaToken("Devoir", "+ infinitif", TokenCategory.CONNECTOR, "Obligation personnalisée sur un sujet précis."),
          FormulaToken("Être obligé(e) de", "+ infinitif", TokenCategory.CONNECTOR, "Contrainte externe explicite, souvent avec réticence.")
        ),
        diagramTitle = "Arbre de Décision : Quelle Expression d'Obligation ?",
        diagramDescription = "Choisissez la structure adaptée au contexte :",
        decisionSteps = listOf(
          DecisionStep(1, "L'obligation est-elle générale/impersonnelle, sans sujet précis mis en avant ?", "-> Il faut que + subjonctif", "-> Passez à l'étape 2"),
          DecisionStep(2, "Voulez-vous nommer directement le sujet obligé ?", "-> Devoir + infinitif", "-> Passez à l'étape 3"),
          DecisionStep(3, "Voulez-vous insister sur une contrainte externe et la réticence du sujet ?", "-> Être obligé(e) de + infinitif", "-> Adoucissez avec le conditionnel pour une simple suggestion")
        ),
        comparisonTableTitle = "Tableau des Nuances",
        comparisonHeaders = listOf("Structure", "Nuance", "Exemple"),
        comparisonRows = listOf(
          listOf("Il faut que + subj.", "impersonnel", "Il faut qu'on parte maintenant."),
          listOf("Devoir + inf.", "personnalisé", "Je dois partir maintenant."),
          listOf("Être obligé de + inf.", "contrainte externe", "Je suis obligé de partir à cause du travail.")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Il faut que tu finisses tes devoirs avant le dîner.",
          highlightedSegment = "Il faut que tu finisses",
          englishSentence = "You need to finish your homework before dinner.",
          contextNote = "'Il faut que' + subjonctif dans une instruction courante à un enfant."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Il est impératif que chaque participant respecte le règlement intérieur.",
          highlightedSegment = "Il est impératif que",
          englishSentence = "It is imperative that each participant respect the internal rules.",
          contextNote = "Équivalent soutenu de 'il faut que', renforcé par 'impératif', registre administratif."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "Je suis obligé de bosser ce week-end, c'est chiant.",
          highlightedSegment = "Je suis obligé de bosser",
          englishSentence = "I have to work this weekend, it's annoying.",
          contextNote = "'Être obligé de' avec réticence explicite, ton familier avec 'bosser' et 'chiant'."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Tous les employés doivent soumettre leur rapport avant vendredi.",
          highlightedSegment = "doivent soumettre",
          englishSentence = "All employees must submit their report before Friday.",
          contextNote = "'Devoir' + infinitif pour une obligation professionnelle claire et personnalisée."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Les Règles du Bureau",
        fullTextFr = "Il faut que tous les employés arrivent avant neuf heures. Chacun doit porter son badge en permanence dans les locaux. Nous sommes obligés de respecter les procédures de sécurité, même si c'est parfois contraignant. Il faudrait peut-être assouplir certaines règles pour plus de flexibilité.",
        fullTextEn = "All employees need to arrive before nine. Everyone must wear their badge at all times on the premises. We are required to follow the security procedures, even if it's sometimes restrictive. Perhaps some rules should be relaxed for more flexibility.",
        annotations = listOf(
          InlineAnnotation(
            id = "no_ann_1",
            targetPhrase = "Il faut que tous les employés arrivent",
            explanationFr = "Obligation impersonnelle générale, sans sujet spécifique nommé directement (bien que 'les employés' soit précisé après 'que').",
            explanationEn = "General impersonal obligation, without a specific subject named directly before 'que'.",
            whyItApplies = "Il faut que + subjonctif pour une règle générale.",
            commonPitfall = "Le verbe après 'il faut que' est toujours au subjonctif, jamais à l'indicatif."
          ),
          InlineAnnotation(
            id = "no_ann_2",
            targetPhrase = "Chacun doit porter son badge",
            explanationFr = "'Devoir' personnalise l'obligation sur le sujet 'chacun', plus direct que 'il faut que chacun porte'.",
            explanationEn = "'Devoir' personalizes the obligation onto the subject 'chacun', more direct than 'il faut que chacun porte'.",
            whyItApplies = "Devoir + infinitif pour personnaliser l'obligation.",
            commonPitfall = "'Devoir' se construit directement avec l'infinitif, sans 'que' ni subjonctif."
          ),
          InlineAnnotation(
            id = "no_ann_3",
            targetPhrase = "Il faudrait peut-être assouplir",
            explanationFr = "Le conditionnel de 'il faut' (il faudrait) adoucit l'obligation en simple suggestion, renforcé par 'peut-être'.",
            explanationEn = "The conditional of 'il faut' (il faudrait) softens the obligation into a mere suggestion, reinforced by 'peut-être'.",
            whyItApplies = "Conditionnel pour adoucir une obligation en suggestion.",
            commonPitfall = "'Il faudrait' + infinitif est possible aussi (sans 'que') quand le sujet n'est pas exprimé séparément."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_no_1",
          instructionFr = "Complétez avec la structure d'obligation impersonnelle :",
          instructionEn = "Complete with the impersonal obligation structure:",
          basePrompt = "_____ (Il faut que/Il doit) nous partions avant midi.",
          targetAnswer = "Il faut que",
          acceptedAnswers = listOf("Il faut que"),
          hint = "Obligation impersonnelle + subjonctif.",
          explanation = "'Il faut que' + subjonctif est la structure impersonnelle correcte ; 'il doit' ne se construit pas ainsi."
        ),
        ProductionDrillItem(
          id = "prod_no_2",
          instructionFr = "Complétez avec 'devoir' personnalisé :",
          instructionEn = "Complete with personalized 'devoir':",
          basePrompt = "Tu _____ (devoir) rendre ce livre avant vendredi.",
          targetAnswer = "dois",
          acceptedAnswers = listOf("dois"),
          hint = "'Devoir' + infinitif, sujet direct.",
          explanation = "'Devoir' se conjugue directement avec le sujet 'tu' : tu dois rendre..."
        ),
        ProductionDrillItem(
          id = "prod_no_3",
          instructionFr = "Complétez avec 'être obligé de' :",
          instructionEn = "Complete with 'être obligé de':",
          basePrompt = "Je _____ (être obligé de) annuler mon rendez-vous, désolé.",
          targetAnswer = "suis obligé d'",
          acceptedAnswers = listOf("suis obligé d'", "suis obligée d'"),
          hint = "'Être obligé de' insiste sur une contrainte externe.",
          explanation = "'Être obligé(e) de' + infinitif souligne une contrainte externe forçant l'action, ici avec regret ('désolé')."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_no_1",
          instructionFr = "Touchez l'erreur de mode :",
          passage = "Il faut que tu finis ce travail avant ce soir absolument.",
          tokens = listOf("Il", "faut", "que", "tu", "finis", "ce", "travail", "avant", "ce", "soir", "absolument."),
          errorTokenIndex = 4,
          errorWord = "finis",
          correction = "finisses",
          ruleExplanation = "'Il faut que' exige toujours le subjonctif : 'que tu finisses', pas l'indicatif 'finis'."
        ),
        SpotErrorDrillItem(
          id = "spot_no_2",
          instructionFr = "Trouvez l'erreur de construction :",
          passage = "Nous devons que nous partions tôt demain matin.",
          tokens = listOf("Nous", "devons", "que", "nous", "partions", "tôt", "demain", "matin."),
          errorTokenIndex = 2,
          errorWord = "que",
          correction = "(supprimer 'que', utiliser l'infinitif : devons partir)",
          ruleExplanation = "'Devoir' se construit directement avec l'infinitif, sans 'que' ni subjonctif : nous devons PARTIR, pas 'devons que nous partions'."
        )
      )
    ),

    // 37. EXPRIMER SES SENTIMENTS
    StructuredRule(
      id = "exprimer-sentiments",
      categoryId = "cat-avance",
      categoryName = "Nuances Avancées",
      level = "B1",
      titleFr = "Exprimer ses Sentiments",
      titleEn = "Expressing Feelings",
      summaryFr = "De + infinitif quand le sentiment porte sur sa propre action ; que + subjonctif quand il porte sur l'action de quelqu'un d'autre.",
      summaryEn = "De + infinitif when the feeling is about your own action; que + subjonctif when it's about someone else's.",
      pillar = GrammarPillar.VERB_SYSTEM,
      contrastGroupId = "triad-sentiment-souhait",
      contrastRoleLabelFr = "Sentiment sur une action",
      contrastRoleLabelEn = "Feeling about an action",
      ruleExplanationEn = "This is the same same-subject/different-subject logic as purpose and the subjunctive: if the person feeling the emotion is also the one doing the action the emotion is about, use 'de + infinitif' (Je suis content de partir — I'm happy that I'm leaving). If the emotion is about someone ELSE's action, switch to 'que + subjonctif' (Je suis content qu'il parte — I'm happy that HE's leaving). Common feeling expressions: être content/heureux/triste/surpris/désolé + de/que, avoir peur + de/que, regretter + de/que.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Être + adjectif de sentiment", "content, triste, surpris...", TokenCategory.SUBJECT, "Exprime l'émotion ressentie."),
          FormulaToken("De + infinitif", "Même sujet", TokenCategory.CONNECTOR, "Le sentiment porte sur sa propre action."),
          FormulaToken("Que + subjonctif", "Sujets différents", TokenCategory.CONNECTOR, "Le sentiment porte sur l'action de quelqu'un d'autre.")
        ),
        diagramTitle = "Arbre de Décision : De + Infinitif ou Que + Subjonctif ?",
        diagramDescription = "Identifiez qui fait l'action qui provoque le sentiment :",
        decisionSteps = listOf(
          DecisionStep(1, "La personne qui ressent le sentiment est-elle aussi celle qui fait l'action concernée ?", "-> De + infinitif (Je suis content de partir.)", "-> Passez à l'étape 2"),
          DecisionStep(2, "L'action concerne-t-elle une autre personne que celle qui ressent le sentiment ?", "-> Que + subjonctif (Je suis content qu'il parte.)", "-> Revérifiez qui agit réellement"),
          DecisionStep(3, "Le sentiment est-il très fort (peur, indignation) ?", "-> Le subjonctif reste identique, seule l'intensité de l'adjectif change", "-> Adaptez l'adjectif au degré du sentiment")
        ),
        comparisonTableTitle = "Même Sujet vs Sujets Différents",
        comparisonHeaders = listOf("Même Sujet (de + inf.)", "Sujets Différents (que + subj.)", "Sens"),
        comparisonRows = listOf(
          listOf("Je suis triste de partir.", "Je suis triste qu'il parte.", "moi/moi vs moi/lui"),
          listOf("Elle a peur de rater le train.", "Elle a peur que nous rations le train.", "elle/elle vs elle/nous")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Je suis vraiment content de te revoir après tout ce temps.",
          highlightedSegment = "content de te revoir",
          englishSentence = "I'm really happy to see you again after all this time.",
          contextNote = "'De' + infinitif, même sujet ('je' ressent et 'je' revoit)."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Nous sommes profondément désolés que cet incident ait pu se produire.",
          highlightedSegment = "désolés que cet incident ait pu se produire",
          englishSentence = "We are deeply sorry that this incident could have happened.",
          contextNote = "'Que' + subjonctif passé, sujets différents, registre d'excuse formel/soutenu."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "J'ai trop peur que tu sois en retard, dépêche-toi !",
          highlightedSegment = "peur que tu sois en retard",
          englishSentence = "I'm so scared you'll be late, hurry up!",
          contextNote = "'Peur que' + subjonctif à l'oral familier, sujets différents (je/tu)."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Nous regrettons de ne pas pouvoir donner suite à votre candidature.",
          highlightedSegment = "regrettons de ne pas pouvoir donner suite",
          englishSentence = "We regret that we cannot proceed with your application.",
          contextNote = "'De' + infinitif négatif, même sujet ('nous' regrette et 'nous' ne peut pas), formule professionnelle standard."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Avant un Grand Départ",
        fullTextFr = "Je suis heureux de partir enfin en voyage après des mois d'attente. Ma mère a peur que je m'ennuie tout seul à l'étranger. Je suis triste de quitter mes amis pour six mois. Mais je suis aussi ravi qu'ils viennent me rendre visite pendant les vacances.",
        fullTextEn = "I'm happy to finally leave on my trip after months of waiting. My mother is afraid that I'll get bored alone abroad. I'm sad to leave my friends for six months. But I'm also thrilled that they'll come visit me during the holidays.",
        annotations = listOf(
          InlineAnnotation(
            id = "sent_ann_1",
            targetPhrase = "heureux de partir",
            explanationFr = "Même sujet ('je' est heureux et 'je' part) : on utilise 'de' + infinitif.",
            explanationEn = "Same subject ('je' is happy and 'je' leaves): 'de' + infinitive is used.",
            whyItApplies = "Sujet identique -> de + infinitif.",
            commonPitfall = "Ne dites pas 'heureux que je parte' ici, c'est redondant puisque le sujet est identique."
          ),
          InlineAnnotation(
            id = "sent_ann_2",
            targetPhrase = "a peur que je m'ennuie",
            explanationFr = "Sujets différents (ma mère a peur, je m'ennuie) : on utilise 'que' + subjonctif de 's'ennuyer'.",
            explanationEn = "Different subjects (my mother is afraid, I get bored): 'que' + subjunctive of 's'ennuyer' is used.",
            whyItApplies = "Sujets différents -> que + subjonctif.",
            commonPitfall = "Ne dites pas 'peur de m'ennuyer' ici : cela suggérerait que c'est la mère qui s'ennuie."
          ),
          InlineAnnotation(
            id = "sent_ann_3",
            targetPhrase = "ravi qu'ils viennent",
            explanationFr = "Sujets différents (je suis ravi, ils viennent) : 'que' + subjonctif de 'venir'.",
            explanationEn = "Different subjects (I am thrilled, they come): 'que' + subjunctive of 'venir'.",
            whyItApplies = "Sujets différents -> que + subjonctif.",
            commonPitfall = "'Venir' a un subjonctif irrégulier : que je vienne, qu'ils viennent."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_sent_1",
          instructionFr = "Complétez avec 'de' + infinitif ou 'que' + subjonctif :",
          instructionEn = "Complete with 'de' + infinitive or 'que' + subjunctive:",
          basePrompt = "Elle est contente _____ (de/que) réussir son examen.",
          targetAnswer = "de",
          acceptedAnswers = listOf("de"),
          hint = "Même sujet dans les deux actions.",
          explanation = "Le sujet est le même ('elle' est contente et 'elle' réussit), donc 'de' + infinitif."
        ),
        ProductionDrillItem(
          id = "prod_sent_2",
          instructionFr = "Complétez avec le subjonctif requis :",
          instructionEn = "Complete with the required subjunctive:",
          basePrompt = "J'ai peur que mon fils _____ (ne pas réussir) son entretien.",
          targetAnswer = "ne réussisse pas",
          acceptedAnswers = listOf("ne réussisse pas"),
          hint = "Sujets différents (je/mon fils) -> subjonctif.",
          explanation = "Sujets différents (moi/mon fils), donc 'que' + subjonctif : que mon fils ne réussisse pas."
        ),
        ProductionDrillItem(
          id = "prod_sent_3",
          instructionFr = "Complétez la structure correcte :",
          instructionEn = "Complete the correct structure:",
          basePrompt = "Nous sommes surpris _____ (de/que) vous soyez déjà arrivés.",
          targetAnswer = "que",
          acceptedAnswers = listOf("que"),
          hint = "Sujets différents (nous/vous).",
          explanation = "Sujets différents (nous sommes surpris, vous êtes arrivés), donc 'que' + subjonctif."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_sent_1",
          instructionFr = "Touchez l'erreur de construction :",
          passage = "Je suis triste que partir demain matin si tôt.",
          tokens = listOf("Je", "suis", "triste", "que", "partir", "demain", "matin", "si", "tôt."),
          errorTokenIndex = 3,
          errorWord = "que",
          correction = "de",
          ruleExplanation = "Le sujet est le même ('je' est triste et 'je' part) : il fallait 'de' + infinitif, pas 'que'."
        ),
        SpotErrorDrillItem(
          id = "spot_sent_2",
          instructionFr = "Trouvez l'erreur de mode :",
          passage = "Ils sont désolés que nous sommes en retard aujourd'hui.",
          tokens = listOf("Ils", "sont", "désolés", "que", "nous", "sommes", "en", "retard", "aujourd'hui."),
          errorTokenIndex = 5,
          errorWord = "sommes",
          correction = "soyons",
          ruleExplanation = "Après un verbe de sentiment + 'que' avec des sujets différents, il faut le subjonctif : que nous SOYONS, pas l'indicatif 'sommes'."
        )
      )
    ),

    // 38. EXPRIMER LA VOLONTÉ, LE SOUHAIT
    StructuredRule(
      id = "exprimer-volonte-souhait",
      categoryId = "cat-avance",
      categoryName = "Nuances Avancées",
      level = "B1",
      titleFr = "Exprimer la Volonté, le Souhait",
      titleEn = "Expressing Wishes and Will",
      summaryFr = "Espérer que + indicatif est l'exception ; tous les autres verbes de souhait/volonté (vouloir, souhaiter, désirer, aimer) prennent le subjonctif (ou l'infinitif si même sujet).",
      summaryEn = "Espérer que + indicatif is the exception — every other wish verb takes the subjonctif (or infinitif with same subject).",
      pillar = GrammarPillar.VERB_SYSTEM,
      contrastGroupId = "triad-sentiment-souhait",
      contrastRoleLabelFr = "Souhait sur une action",
      contrastRoleLabelEn = "Wish about an action",
      ruleExplanationEn = "Memorize 'espérer' as the one loud exception: 'espérer que' takes the indicative (usually future), never the subjunctive — J'espère qu'il viendra, not 'qu'il vienne'. Every other verb of wishing or wanting (vouloir que, souhaiter que, désirer que, aimer que, préférer que) follows the normal subjunctive-trigger pattern: same subject -> infinitive, different subjects -> que + subjonctif. This is a very common trap because 'espérer' and 'souhaiter' feel semantically identical but behave completely differently grammatically.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Espérer que", "+ INDICATIF (exception)", TokenCategory.TRIGGER, "Le seul verbe de souhait qui ne prend jamais le subjonctif."),
          FormulaToken("Vouloir/souhaiter/désirer que", "+ subjonctif", TokenCategory.TRIGGER, "Tous les autres verbes de volonté/souhait, sujets différents."),
          FormulaToken("Vouloir/souhaiter + infinitif", "Même sujet", TokenCategory.CONNECTOR, "Pas de 'que', juste l'infinitif direct.")
        ),
        diagramTitle = "Arbre de Décision : Espérer ou un Autre Verbe de Souhait ?",
        diagramDescription = "Identifiez le verbe utilisé avant de choisir le mode :",
        decisionSteps = listOf(
          DecisionStep(1, "Le verbe est-il 'espérer' ?", "-> Indicatif obligatoire, jamais le subjonctif (j'espère qu'il viendra)", "-> Passez à l'étape 2"),
          DecisionStep(2, "Le sujet du souhait et de l'action sont-ils identiques ?", "-> Infinitif direct, sans 'que' (je veux partir)", "-> Passez à l'étape 3"),
          DecisionStep(3, "Les sujets sont-ils différents ?", "-> Que + subjonctif (je veux qu'il parte)", "-> Vérifiez le verbe utilisé si le résultat semble étrange")
        ),
        comparisonTableTitle = "Espérer vs Autres Verbes de Souhait",
        comparisonHeaders = listOf("Verbe", "Mode", "Exemple"),
        comparisonRows = listOf(
          listOf("espérer que", "indicatif", "J'espère qu'il viendra."),
          listOf("souhaiter que", "subjonctif", "Je souhaite qu'il vienne."),
          listOf("vouloir que", "subjonctif", "Je veux qu'il vienne.")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "J'espère que tu passeras de bonnes vacances cet été.",
          highlightedSegment = "J'espère que tu passeras",
          englishSentence = "I hope you have a good vacation this summer.",
          contextNote = "'Espérer que' + futur simple (indicatif), exception à retenir absolument."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "La direction souhaite que chaque collaborateur s'investisse pleinement.",
          highlightedSegment = "souhaite que chaque collaborateur s'investisse",
          englishSentence = "Management wishes for each team member to fully commit.",
          contextNote = "'Souhaiter que' + subjonctif, registre professionnel soutenu."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "J'espère qu'il va pas pleuvoir demain, on a prévu un pique-nique !",
          highlightedSegment = "J'espère qu'il va pas pleuvoir",
          englishSentence = "I hope it doesn't rain tomorrow, we planned a picnic!",
          contextNote = "'Espérer que' + futur proche à l'oral familier, indicatif conservé malgré le registre."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Nous aimerions que vous puissiez nous confirmer votre présence rapidement.",
          highlightedSegment = "aimerions que vous puissiez",
          englishSentence = "We would like you to be able to confirm your attendance quickly.",
          contextNote = "'Aimer que' au conditionnel + subjonctif, formule professionnelle polie."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Avant les Résultats",
        fullTextFr = "J'espère que j'aurai de bonnes notes ce semestre. Mes parents souhaitent que je continue mes études à l'université. Moi, je veux surtout réussir par moi-même. J'espère aussi que mes amis obtiendront de bons résultats, on a beaucoup travaillé ensemble.",
        fullTextEn = "I hope I'll have good grades this semester. My parents wish for me to continue my studies at university. Personally, I mostly want to succeed on my own. I also hope my friends get good results, we worked a lot together.",
        annotations = listOf(
          InlineAnnotation(
            id = "vol_ann_1",
            targetPhrase = "J'espère que j'aurai",
            explanationFr = "'Espérer que' est toujours suivi de l'indicatif, ici le futur simple 'aurai', jamais le subjonctif.",
            explanationEn = "'Espérer que' is always followed by the indicative, here the futur simple 'aurai', never the subjunctive.",
            whyItApplies = "Exception : espérer que + indicatif.",
            commonPitfall = "Ne dites jamais 'j'espère que j'aie' — 'espérer' exclut totalement le subjonctif, contrairement à 'souhaiter'."
          ),
          InlineAnnotation(
            id = "vol_ann_2",
            targetPhrase = "souhaitent que je continue",
            explanationFr = "'Souhaiter que' avec des sujets différents (mes parents/je) exige le subjonctif de 'continuer'.",
            explanationEn = "'Souhaiter que' with different subjects (my parents/je) requires the subjunctive of 'continuer'.",
            whyItApplies = "Sujets différents -> que + subjonctif.",
            commonPitfall = "Ne confondez pas avec 'espérer' : 'souhaiter' suit la règle normale du subjonctif."
          ),
          InlineAnnotation(
            id = "vol_ann_3",
            targetPhrase = "j'espère aussi que mes amis obtiendront",
            explanationFr = "Même avec des sujets différents (je/mes amis), 'espérer que' reste toujours à l'indicatif (futur simple ici).",
            explanationEn = "Even with different subjects (je/mes amis), 'espérer que' always stays in the indicative (futur simple here).",
            whyItApplies = "Espérer que + indicatif, quels que soient les sujets.",
            commonPitfall = "Contrairement à tous les autres verbes de souhait, le nombre de sujets ne change rien pour 'espérer' : toujours l'indicatif."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_vol_1",
          instructionFr = "Complétez avec le mode correct :",
          instructionEn = "Complete with the correct mood:",
          basePrompt = "J'espère qu'il _____ (venir/vienne) à la fête ce soir.",
          targetAnswer = "viendra",
          acceptedAnswers = listOf("viendra"),
          hint = "'Espérer que' exige l'indicatif, jamais le subjonctif.",
          explanation = "'Espérer que' est toujours suivi de l'indicatif : 'il viendra' (futur simple), pas 'qu'il vienne'."
        ),
        ProductionDrillItem(
          id = "prod_vol_2",
          instructionFr = "Complétez avec le subjonctif requis :",
          instructionEn = "Complete with the required subjunctive:",
          basePrompt = "Je souhaite que vous _____ (être) heureux dans votre nouvelle maison.",
          targetAnswer = "soyez",
          acceptedAnswers = listOf("soyez"),
          hint = "'Souhaiter que' suit la règle normale du subjonctif.",
          explanation = "'Souhaiter que' (contrairement à 'espérer') exige le subjonctif : que vous soyez."
        ),
        ProductionDrillItem(
          id = "prod_vol_3",
          instructionFr = "Complétez avec l'infinitif (même sujet) :",
          instructionEn = "Complete with the infinitive (same subject):",
          basePrompt = "Elle veut _____ (partir/qu'elle parte) en vacances cet été.",
          targetAnswer = "partir",
          acceptedAnswers = listOf("partir"),
          hint = "Même sujet ('elle' veut et 'elle' part).",
          explanation = "Le sujet étant identique, on utilise l'infinitif direct sans 'que' : elle veut partir."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_vol_1",
          instructionFr = "Touchez l'erreur de mode :",
          passage = "J'espère que tu sois bientôt guéri de ton rhume.",
          tokens = listOf("J'espère", "que", "tu", "sois", "bientôt", "guéri", "de", "ton", "rhume."),
          errorTokenIndex = 3,
          errorWord = "sois",
          correction = "seras",
          ruleExplanation = "'Espérer que' exige toujours l'indicatif, jamais le subjonctif : il fallait 'tu seras', pas 'tu sois'."
        ),
        SpotErrorDrillItem(
          id = "spot_vol_2",
          instructionFr = "Trouvez l'erreur de mode :",
          passage = "Nous souhaitons que vous passez un excellent séjour parmi nous.",
          tokens = listOf("Nous", "souhaitons", "que", "vous", "passez", "un", "excellent", "séjour", "parmi", "nous."),
          errorTokenIndex = 4,
          errorWord = "passez",
          correction = "passiez",
          ruleExplanation = "'Souhaiter que' (contrairement à 'espérer') exige le subjonctif : que vous PASSIEZ, pas l'indicatif 'passez'."
        )
      )
    ),

    // 39. LES PRÉPOSITIONS À, DE, EN, PAR, POUR
    StructuredRule(
      id = "prepositions-a-de-en-par-pour",
      categoryId = "cat-avance",
      categoryName = "Nuances Avancées",
      level = "B1",
      titleFr = "Les Prépositions À, De, En, Par, Pour",
      titleEn = "The Prepositions À, De, En, Par, Pour",
      summaryFr = "Au-delà de leur usage géographique, ces cinq prépositions expriment l'attitude, la cause, le moyen, la manière, la matière, la mesure, l'agent, et le but.",
      summaryEn = "The non-locative uses of the five core prepositions: attitude, cause, means, manner, material, measure, agent, purpose — one at a time.",
      pillar = GrammarPillar.OTHER,
      ruleExplanationEn = "Beyond places, each preposition has a small set of fixed non-locative jobs worth memorizing as chunks rather than translating word-by-word: 'à' marks manner/means/characteristic (à pied, à la main, une tasse à café); 'de' marks cause, material, or manner in fixed phrases (mourir de faim, une table de bois, d'une voix douce); 'en' marks material or manner as a transformation/state (en bois, en colère, parler en anglais); 'par' marks the agent of a passive or a means/route (par le train, frappé par la foudre); 'pour' marks purpose, duration planned in advance, or exchange (pour deux jours, acheté pour dix euros).",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("À", "manière/moyen/caractéristique", TokenCategory.CONNECTOR, "à pied, à la main, tasse à café"),
          FormulaToken("De", "cause/matière/manière", TokenCategory.CONNECTOR, "mourir de faim, table de bois"),
          FormulaToken("En", "matière/état/transformation", TokenCategory.CONNECTOR, "en bois, en colère"),
          FormulaToken("Par", "agent/moyen/itinéraire", TokenCategory.CONNECTOR, "par le train, frappé par la foudre"),
          FormulaToken("Pour", "but/durée prévue/échange", TokenCategory.CONNECTOR, "pour deux jours, acheté pour dix euros")
        ),
        diagramTitle = "Arbre de Décision : Quelle Préposition Non-Locative ?",
        diagramDescription = "Identifiez la fonction exacte visée :",
        decisionSteps = listOf(
          DecisionStep(1, "Voulez-vous exprimer une manière, un moyen ou une caractéristique typique ?", "-> À (à pied, tasse à café)", "-> Passez à l'étape 2"),
          DecisionStep(2, "Voulez-vous exprimer une cause, une matière brute, ou une manière figée ?", "-> De (mourir de faim, table de bois)", "-> Passez à l'étape 3"),
          DecisionStep(3, "Voulez-vous exprimer un agent, un moyen de transport, ou un but/une durée prévue ?", "-> Par (agent/itinéraire) ou Pour (but/durée/échange)", "-> Vérifiez le sens précis recherché")
        ),
        comparisonTableTitle = "Emplois Non-Locatifs",
        comparisonHeaders = listOf("Préposition", "Fonction", "Exemple"),
        comparisonRows = listOf(
          listOf("à", "caractéristique/manière", "une glace à la vanille, écrire à la main"),
          listOf("de", "matière/cause", "une bague en or... non, de l'or (rare) ; mourir de peur"),
          listOf("en", "matière/état", "une table en bois, être en colère"),
          listOf("par", "agent/moyen", "envoyé par la poste, puni par le professeur"),
          listOf("pour", "but/durée/échange", "partir pour affaires, loué pour un mois")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Elle est partie à pied parce que sa voiture était en panne.",
          highlightedSegment = "à pied",
          englishSentence = "She left on foot because her car had broken down.",
          contextNote = "'À' pour exprimer le moyen de déplacement (sans véhicule), usage courant."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "La proposition a été rejetée par la majorité des membres du conseil.",
          highlightedSegment = "rejetée par la majorité",
          englishSentence = "The proposal was rejected by the majority of council members.",
          contextNote = "'Par' introduisant l'agent d'une action passive, registre institutionnel soutenu."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "Je tremblais de froid en sortant, il faisait un temps de chien !",
          highlightedSegment = "tremblais de froid",
          englishSentence = "I was shivering with cold going out, the weather was awful!",
          contextNote = "'De' exprimant la cause d'un état physique, usage courant/familier expressif."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Ce matériau est fabriqué en aluminium pour garantir sa légèreté.",
          highlightedSegment = "en aluminium pour garantir",
          englishSentence = "This material is made of aluminum to ensure its lightness.",
          contextNote = "'En' pour la matière, 'pour' pour le but, dans une description technique professionnelle."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Une Description d'Objet",
        fullTextFr = "Cette table en bois massif a été fabriquée par un artisan local. Elle tremblait de froid après cette longue marche à pied sous la pluie. Ce cadeau a été acheté pour son anniversaire. Le colis a été envoyé par la poste ce matin.",
        fullTextEn = "This solid wood table was made by a local craftsman. She was shivering with cold after that long walk on foot in the rain. This gift was bought for her birthday. The package was sent by post this morning.",
        annotations = listOf(
          InlineAnnotation(
            id = "prep_ann_1",
            targetPhrase = "table en bois massif",
            explanationFr = "'En' introduit la matière dont est fait l'objet, usage typique de cette préposition.",
            explanationEn = "'En' introduces the material an object is made of, a typical use of this preposition.",
            whyItApplies = "En + matière.",
            commonPitfall = "'En bois' est plus courant que 'de bois' pour désigner la matière d'un objet concret."
          ),
          InlineAnnotation(
            id = "prep_ann_2",
            targetPhrase = "tremblait de froid",
            explanationFr = "'De' introduit la cause d'un état physique ou émotionnel, ici le froid qui cause le tremblement.",
            explanationEn = "'De' introduces the cause of a physical or emotional state, here the cold causing the shivering.",
            whyItApplies = "De + cause d'un état.",
            commonPitfall = "Ne confondez pas avec 'par froid' qui n'existe pas dans ce sens ; 'de' est la préposition fixe ici."
          ),
          InlineAnnotation(
            id = "prep_ann_3",
            targetPhrase = "envoyé par la poste",
            explanationFr = "'Par' introduit ici le moyen/canal utilisé pour l'envoi, extension du sens d'agent/moyen.",
            explanationEn = "'Par' introduces here the means/channel used for sending, an extension of the agent/means meaning.",
            whyItApplies = "Par + moyen/canal.",
            commonPitfall = "'Par la poste' est une expression figée ; ne dites pas 'avec la poste' ou 'à la poste' dans ce sens précis."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_prep_1",
          instructionFr = "Choisissez la préposition correcte pour la matière :",
          instructionEn = "Choose the correct preposition for material:",
          basePrompt = "J'ai acheté une bague _____ (en/à) argent pour son anniversaire.",
          targetAnswer = "en",
          acceptedAnswers = listOf("en"),
          hint = "La matière d'un objet se marque généralement avec 'en'.",
          explanation = "'En' introduit la matière : une bague EN argent."
        ),
        ProductionDrillItem(
          id = "prod_prep_2",
          instructionFr = "Choisissez la préposition correcte pour la cause :",
          instructionEn = "Choose the correct preposition for cause:",
          basePrompt = "Il pleurait _____ (de/par) joie en apprenant la nouvelle.",
          targetAnswer = "de",
          acceptedAnswers = listOf("de"),
          hint = "La cause d'une émotion se marque avec 'de'.",
          explanation = "'De' introduit la cause d'un état émotionnel : pleurer DE joie."
        ),
        ProductionDrillItem(
          id = "prod_prep_3",
          instructionFr = "Choisissez la préposition correcte pour l'agent :",
          instructionEn = "Choose the correct preposition for the agent:",
          basePrompt = "Ce livre a été écrit _____ (par/pour) un auteur célèbre.",
          targetAnswer = "par",
          acceptedAnswers = listOf("par"),
          hint = "L'agent d'une action passive se marque avec 'par'.",
          explanation = "'Par' introduit l'agent d'une phrase passive : écrit PAR un auteur."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_prep_1",
          instructionFr = "Touchez l'erreur de préposition :",
          passage = "Cette maison est construite avec pierre depuis très longtemps.",
          tokens = listOf("Cette", "maison", "est", "construite", "avec", "pierre", "depuis", "très", "longtemps."),
          errorTokenIndex = 4,
          errorWord = "avec",
          correction = "en",
          ruleExplanation = "Pour indiquer la matière d'un objet/bâtiment, on utilise 'en', pas 'avec' : construite EN pierre."
        ),
        SpotErrorDrillItem(
          id = "spot_prep_2",
          instructionFr = "Trouvez l'erreur de préposition :",
          passage = "Il tremblait avec froid en attendant le bus sous la pluie.",
          tokens = listOf("Il", "tremblait", "avec", "froid", "en", "attendant", "le", "bus", "sous", "la", "pluie."),
          errorTokenIndex = 2,
          errorWord = "avec",
          correction = "de",
          ruleExplanation = "La cause d'un état physique se marque avec 'de', pas 'avec' : trembler DE froid."
        )
      )
    ),

    // 40. LE PASSÉ SIMPLE
    StructuredRule(
      id = "passe-simple",
      categoryId = "cat-verbes",
      categoryName = "Modes & Temps",
      level = "B2",
      titleFr = "Le Passé Simple",
      titleEn = "The Literary Past Tense — For Reading, Not Everyday Speech",
      summaryFr = "Le passé simple est le temps du récit littéraire écrit ; il ne s'emploie jamais à l'oral, où le passé composé le remplace systématiquement.",
      summaryEn = "The passé simple is the tense of written literary narrative; it is never used in speech, where the passé composé systematically replaces it.",
      pillar = GrammarPillar.VERB_SYSTEM,
      ruleExplanationEn = "You will never need to produce this tense in conversation — its only job is recognition when reading novels, fairy tales, and historical narration. Three families of endings cover almost everything: -er verbs take -ai/-as/-a/-âmes/-âtes/-èrent (il parla); most -ir/-re verbs take -is/-is/-it/-îmes/-îtes/-irent (il finit, il prit); a handful of -oir verbs and irregulars take -us/-us/-ut/-ûmes/-ûtes/-urent (il fut, il eut, il put). The 3rd person singular and plural (il/elle, ils/elles) are by far the most useful forms to recognize, since narration is almost always in the 3rd person.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Radical", "parl-, fin-, f- (être)...", TokenCategory.TARGET_VERB, "Souvent proche de l'infinitif, parfois irrégulier."),
          FormulaToken("-a / -it / -ut (il/elle)", "Terminaison de la 3e pers. sg.", TokenCategory.CONNECTOR, "La forme la plus fréquente à reconnaître en lecture.")
        ),
        diagramTitle = "Arbre de Décision : Reconnaître le Passé Simple",
        diagramDescription = "Identifiez la famille de terminaison pour comprendre le sens :",
        decisionSteps = listOf(
          DecisionStep(1, "Le verbe est-il en -er à l'infinitif ?", "-> Terminaisons en -a/-èrent (il parla, ils parlèrent)", "-> Passez à l'étape 2"),
          DecisionStep(2, "Le verbe est-il en -ir/-re régulier ?", "-> Terminaisons en -it/-irent (il finit, ils prirent)", "-> Passez à l'étape 3"),
          DecisionStep(3, "Le verbe est-il irrégulier (être, avoir, pouvoir, vouloir...) ?", "-> Terminaisons en -ut/-urent, radical à mémoriser (il fut, il eut)", "-> Consultez un tableau de conjugaison pour le radical exact")
        ),
        comparisonTableTitle = "Passé Composé vs Passé Simple (3e pers.)",
        comparisonHeaders = listOf("Passé Composé (oral)", "Passé Simple (écrit littéraire)", "Verbe"),
        comparisonRows = listOf(
          listOf("il a parlé", "il parla", "parler"),
          listOf("il a fini", "il finit", "finir"),
          listOf("il a été", "il fut", "être"),
          listOf("il a eu", "il eut", "avoir")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Il a fini son travail et il est rentré chez lui.",
          highlightedSegment = "a fini",
          englishSentence = "He finished his work and went home.",
          contextNote = "À l'oral, le passé composé remplace toujours le passé simple : jamais 'il finit' à l'oral pour ce sens."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Le roi entra dans la salle et s'assit sur son trône.",
          highlightedSegment = "entra... s'assit",
          englishSentence = "The king entered the room and sat on his throne.",
          contextNote = "Passé simple typique d'un récit littéraire écrit, jamais utilisé à l'oral."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "Il a été super content quand il l'a appris !",
          highlightedSegment = "a été",
          englishSentence = "He was super happy when he found out!",
          contextNote = "Le passé simple ('il fut') n'existe jamais à l'oral, même dans un contexte narratif informel."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "L'entreprise fut fondée en mille neuf cent cinquante par deux ingénieurs visionnaires.",
          highlightedSegment = "fut fondée",
          englishSentence = "The company was founded in 1950 by two visionary engineers.",
          contextNote = "Passé simple parfois utilisé à l'écrit dans des textes historiques/institutionnels formels."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Un Extrait de Conte",
        fullTextFr = "Il était une fois un jeune prince qui vivait dans un grand château. Un jour, il partit à la recherche d'un trésor caché. Il traversa la forêt et rencontra une vieille sorcière. Elle lui donna une carte magique, et il continua son voyage plein d'espoir.",
        fullTextEn = "Once upon a time there was a young prince who lived in a great castle. One day, he set off in search of a hidden treasure. He crossed the forest and met an old witch. She gave him a magic map, and he continued his journey full of hope.",
        annotations = listOf(
          InlineAnnotation(
            id = "ps_ann_1",
            targetPhrase = "il partit",
            explanationFr = "'Partit' est la forme au passé simple de 'partir' à la 3e personne du singulier, typique d'un conte écrit.",
            explanationEn = "'Partit' is the passé simple form of 'partir' in the 3rd person singular, typical of a written tale.",
            whyItApplies = "Passé simple, narration littéraire.",
            commonPitfall = "À l'oral, on dirait 'il est parti' (passé composé) ; 'il partit' ne s'entend jamais en conversation."
          ),
          InlineAnnotation(
            id = "ps_ann_2",
            targetPhrase = "il traversa la forêt et rencontra",
            explanationFr = "Deux verbes en -er au passé simple, terminaison -a à la 3e personne du singulier.",
            explanationEn = "Two -er verbs in the passé simple, ending -a in the 3rd person singular.",
            whyItApplies = "Terminaison -a pour les verbes en -er au passé simple.",
            commonPitfall = "Ne confondez pas avec le présent 'traverse/rencontre' : le passé simple utilise le même radical mais avec la terminaison -a."
          ),
          InlineAnnotation(
            id = "ps_ann_3",
            targetPhrase = "Elle lui donna",
            explanationFr = "'Donna' est le passé simple de 'donner', reconnaissable par sa terminaison -a très fréquente dans les contes.",
            explanationEn = "'Donna' is the passé simple of 'donner', recognizable by its very frequent -a ending in fairy tales.",
            whyItApplies = "Verbe en -er, terminaison -a au passé simple.",
            commonPitfall = "Ne le confondez pas avec l'article 'donna' d'une autre langue ; c'est bien un verbe conjugué ici."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_ps_1",
          instructionFr = "Identifiez le passé composé équivalent :",
          instructionEn = "Identify the equivalent passé composé:",
          basePrompt = "'Il alla à Paris' au passé simple équivaut à l'oral à : Il _____ (est allé/a allé) à Paris.",
          targetAnswer = "est allé",
          acceptedAnswers = listOf("est allé"),
          hint = "'Aller' se conjugue avec 'être' au passé composé.",
          explanation = "'Alla' (passé simple) correspond à 'est allé' (passé composé), 'aller' utilisant l'auxiliaire être."
        ),
        ProductionDrillItem(
          id = "prod_ps_2",
          instructionFr = "Reconnaissez le verbe au passé simple :",
          instructionEn = "Recognize the verb in the passé simple:",
          basePrompt = "'Elle fut heureuse' : identifiez l'infinitif du verbe conjugué 'fut'. Infinitif : _____",
          targetAnswer = "être",
          acceptedAnswers = listOf("être"),
          hint = "Verbe irrégulier très fréquent en narration.",
          explanation = "'Fut' est la forme au passé simple du verbe 'être' à la 3e personne du singulier."
        ),
        ProductionDrillItem(
          id = "prod_ps_3",
          instructionFr = "Convertissez au passé composé oral :",
          instructionEn = "Convert to the spoken passé composé:",
          basePrompt = "'Ils prirent le train' -> à l'oral : Ils _____ (ont pris/ont prendu) le train.",
          targetAnswer = "ont pris",
          acceptedAnswers = listOf("ont pris"),
          hint = "Le participe passé irrégulier de 'prendre' est 'pris'.",
          explanation = "'Prirent' (passé simple) correspond à 'ont pris' (passé composé) à l'oral."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_ps_1",
          instructionFr = "Touchez le mot qui ne devrait jamais s'employer à l'oral :",
          passage = "Hier, mon collègue arriva en retard à la réunion du matin.",
          tokens = listOf("Hier,", "mon", "collègue", "arriva", "en", "retard", "à", "la", "réunion", "du", "matin."),
          errorTokenIndex = 3,
          errorWord = "arriva",
          correction = "est arrivé",
          ruleExplanation = "Le passé simple ('arriva') ne s'utilise jamais à l'oral, même dans un registre narratif ; il faut le passé composé 'est arrivé'."
        ),
        SpotErrorDrillItem(
          id = "spot_ps_2",
          instructionFr = "Trouvez l'usage inapproprié du passé simple :",
          passage = "Je te raconte : hier soir, je sortis avec mes amis en boîte de nuit.",
          tokens = listOf("Je", "te", "raconte:", "hier", "soir,", "je", "sortis", "avec", "mes", "amis", "en", "boîte", "de", "nuit."),
          errorTokenIndex = 6,
          errorWord = "sortis",
          correction = "suis sorti(e)",
          ruleExplanation = "Dans une conversation orale, même en racontant une histoire, on utilise le passé composé, jamais le passé simple 'sortis'."
        )
      )
    ),

    // 41. INDICATIF, SUBJONCTIF OU INFINITIF ?
    StructuredRule(
      id = "indicatif-subjonctif-infinitif",
      categoryId = "cat-verbes",
      categoryName = "Modes & Temps",
      level = "B2",
      titleFr = "Indicatif, Subjonctif ou Infinitif ?",
      titleEn = "Choosing the Right Mood After a Verb or Expression",
      summaryFr = "Synthèse méthodique : classez le verbe/l'expression introducteur (certitude, doute/sentiment, volonté) et vérifiez si les sujets sont identiques pour choisir entre indicatif, subjonctif et infinitif.",
      summaryEn = "A methodical synthesis: classify the introducing verb/expression (certainty, doubt/feeling, will) and check whether the subjects match to choose between indicative, subjunctive, and infinitive.",
      pillar = GrammarPillar.VERB_SYSTEM,
      ruleExplanationEn = "This rule doesn't introduce anything new — it's the decision tree that ties together everything you've already learned about the subjunctive, purpose, feelings, and wishes into one reflex. Step one: are the two clauses' subjects the same? If yes, skip 'que' and use the infinitive, regardless of the verb. If the subjects differ, step two: classify the introducing expression. Certainty/declaration (savoir, dire, être sûr que, penser affirmatively) -> indicative. Doubt, emotion, necessity, will, judgment (douter, craindre, il faut, vouloir, il est possible, être content) -> subjunctive. The main trap is verbs of opinion (penser, croire, trouver) which take the indicative affirmatively but flip to subjunctive when negated or questioned, since negation introduces doubt.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Sujets identiques ?", "Test prioritaire", TokenCategory.SUBJECT, "Si oui : infinitif direct, sans 'que'."),
          FormulaToken("Certitude/déclaration", "savoir, dire, être sûr que", TokenCategory.TRIGGER, "-> Indicatif"),
          FormulaToken("Doute/sentiment/volonté", "douter, craindre, vouloir, il faut", TokenCategory.TRIGGER, "-> Subjonctif")
        ),
        diagramTitle = "Arbre de Décision Global",
        diagramDescription = "Suivez ces trois questions dans l'ordre :",
        decisionSteps = listOf(
          DecisionStep(1, "Les deux sujets (verbe principal et verbe subordonné) sont-ils identiques ?", "-> Infinitif direct, pas de 'que' (je veux partir)", "-> Passez à l'étape 2"),
          DecisionStep(2, "Le verbe/l'expression exprime-t-il une certitude affirmative (savoir, dire, être sûr, penser à l'affirmatif) ?", "-> Indicatif (je sais qu'il vient)", "-> Passez à l'étape 3"),
          DecisionStep(3, "Le verbe/l'expression exprime-t-il un doute, un sentiment, une volonté, une nécessité, ou une opinion niée/interrogée ?", "-> Subjonctif (je doute qu'il vienne / je ne pense pas qu'il vienne)", "-> Vérifiez si un verbe de déclaration à l'affirmatif convient mieux -> indicatif")
        ),
        comparisonTableTitle = "Synthèse des Trois Modes",
        comparisonHeaders = listOf("Situation", "Mode", "Exemple"),
        comparisonRows = listOf(
          listOf("Sujets identiques", "Infinitif", "Je veux réussir."),
          listOf("Certitude, sujets différents", "Indicatif", "Je sais qu'il réussira."),
          listOf("Doute/sentiment, sujets différents", "Subjonctif", "Je doute qu'il réussisse.")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Je pense qu'il a raison, mais je ne pense pas qu'il ait tout compris.",
          highlightedSegment = "je ne pense pas qu'il ait",
          englishSentence = "I think he's right, but I don't think he's understood everything.",
          contextNote = "Contraste direct : 'penser que' affirmatif = indicatif, 'ne pas penser que' négatif = subjonctif."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Il est essentiel que chacun prenne conscience de ses responsabilités.",
          highlightedSegment = "il est essentiel que chacun prenne",
          englishSentence = "It is essential that everyone become aware of their responsibilities.",
          contextNote = "Expression impersonnelle de nécessité, subjonctif obligatoire, registre soutenu."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "Je crois pas qu'il vienne, il avait l'air crevé hier.",
          highlightedSegment = "Je crois pas qu'il vienne",
          englishSentence = "I don't think he's coming, he looked exhausted yesterday.",
          contextNote = "'Croire' à la forme négative entraîne le subjonctif, même à l'oral familier."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Nous sommes convaincus que cette stratégie portera ses fruits.",
          highlightedSegment = "convaincus que cette stratégie portera",
          englishSentence = "We are convinced that this strategy will pay off.",
          contextNote = "'Être convaincu que' exprime une certitude affirmative -> indicatif, registre professionnel."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Une Discussion sur un Projet",
        fullTextFr = "Je sais que ce projet est ambitieux, mais je ne crois pas qu'il soit impossible. Il faut que toute l'équipe s'implique pleinement pour réussir. Nous voulons finir avant la date limite. Je doute cependant que nous ayons assez de ressources actuellement.",
        fullTextEn = "I know this project is ambitious, but I don't believe it's impossible. The whole team needs to fully get involved to succeed. We want to finish before the deadline. However, I doubt we have enough resources currently.",
        annotations = listOf(
          InlineAnnotation(
            id = "isi_ann_1",
            targetPhrase = "Je sais que ce projet est",
            explanationFr = "'Savoir que' exprime une certitude affirmative, imposant l'indicatif ('est').",
            explanationEn = "'Savoir que' expresses affirmative certainty, requiring the indicative ('est').",
            whyItApplies = "Certitude affirmative -> indicatif.",
            commonPitfall = "Ne mettez jamais le subjonctif après 'savoir que' à l'affirmatif."
          ),
          InlineAnnotation(
            id = "isi_ann_2",
            targetPhrase = "je ne crois pas qu'il soit",
            explanationFr = "'Croire' niée bascule vers le subjonctif ('soit'), car la négation introduit un doute.",
            explanationEn = "Negated 'croire' shifts to the subjunctive ('soit'), since the negation introduces doubt.",
            whyItApplies = "Verbe d'opinion nié -> subjonctif.",
            commonPitfall = "'Je crois qu'il est' (affirmatif) prendrait l'indicatif ; c'est la négation qui change tout ici."
          ),
          InlineAnnotation(
            id = "isi_ann_3",
            targetPhrase = "Nous voulons finir",
            explanationFr = "Sujets identiques ('nous' voulons et 'nous' finissons) : infinitif direct sans 'que', quel que soit le verbe de volonté utilisé.",
            explanationEn = "Same subjects ('nous' want and 'nous' finish): direct infinitive with no 'que', regardless of the will-verb used.",
            whyItApplies = "Sujets identiques -> infinitif, priorité sur la classification du verbe.",
            commonPitfall = "Ne dites jamais 'nous voulons que nous finissions' : c'est toujours l'infinitif quand les sujets sont identiques."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_isi_1",
          instructionFr = "Choisissez le mode correct :",
          instructionEn = "Choose the correct mood:",
          basePrompt = "Je suis certain qu'elle _____ (réussir/réussisse) son examen.",
          targetAnswer = "réussira",
          acceptedAnswers = listOf("réussira"),
          hint = "'Être certain que' à l'affirmatif exprime une certitude.",
          explanation = "'Être certain que' à l'affirmatif exige l'indicatif : elle réussira."
        ),
        ProductionDrillItem(
          id = "prod_isi_2",
          instructionFr = "Choisissez le mode correct (verbe d'opinion nié) :",
          instructionEn = "Choose the correct mood (negated opinion verb):",
          basePrompt = "Je ne pense pas qu'il _____ (avoir) raison sur ce point.",
          targetAnswer = "ait",
          acceptedAnswers = listOf("ait"),
          hint = "La négation d'un verbe d'opinion entraîne le subjonctif.",
          explanation = "'Ne pas penser que' entraîne le subjonctif car la négation introduit le doute : qu'il ait raison."
        ),
        ProductionDrillItem(
          id = "prod_isi_3",
          instructionFr = "Choisissez la bonne structure (sujets identiques) :",
          instructionEn = "Choose the correct structure (same subjects):",
          basePrompt = "Elle espère _____ (pouvoir/qu'elle puisse) partir plus tôt aujourd'hui.",
          targetAnswer = "pouvoir",
          acceptedAnswers = listOf("pouvoir"),
          hint = "Sujets identiques : pas besoin de 'que'.",
          explanation = "Les sujets sont identiques ('elle' espère et 'elle' part) : on utilise l'infinitif direct, sans 'que'."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_isi_1",
          instructionFr = "Touchez l'erreur de mode :",
          passage = "Je suis sûr qu'il vienne demain matin comme prévu.",
          tokens = listOf("Je", "suis", "sûr", "qu'il", "vienne", "demain", "matin", "comme", "prévu."),
          errorTokenIndex = 4,
          errorWord = "vienne",
          correction = "viendra",
          ruleExplanation = "'Être sûr que' à l'affirmatif exprime une certitude et exige l'indicatif : il viendra, pas le subjonctif."
        ),
        SpotErrorDrillItem(
          id = "spot_isi_2",
          instructionFr = "Trouvez l'erreur de mode :",
          passage = "Il est possible qu'elle a raison sur ce sujet délicat.",
          tokens = listOf("Il", "est", "possible", "qu'elle", "a", "raison", "sur", "ce", "sujet", "délicat."),
          errorTokenIndex = 4,
          errorWord = "a",
          correction = "ait",
          ruleExplanation = "'Il est possible que' exprime une possibilité incertaine et exige le subjonctif : qu'elle AIT raison, pas l'indicatif."
        )
      )
    ),

    // 42. L'OPPOSITION ET LA CONCESSION
    StructuredRule(
      id = "opposition-concession",
      categoryId = "cat-avance",
      categoryName = "Nuances Avancées",
      level = "B2",
      titleFr = "L'Opposition et la Concession",
      titleEn = "Opposition and Concession: \"Although\", \"Whereas\", \"Despite\"",
      summaryFr = "L'opposition (contraste simple, indicatif) et la concession (contre-attente malgré un obstacle, souvent subjonctif) se marquent par des connecteurs distincts selon la construction grammaticale requise.",
      summaryEn = "Opposition (simple contrast, indicative) and concession (unexpected result despite an obstacle, often subjunctive) are marked by distinct connectors depending on the required grammatical construction.",
      pillar = GrammarPillar.OTHER,
      ruleExplanationEn = "Opposition simply contrasts two facts side by side with no obstacle overcome ('alors que', 'tandis que' + indicative — Il aime le café, alors qu'elle préfère le thé). Concession says an expected obstacle failed to block a result, so it feels closer to 'despite' ('bien que', 'quoique' + subjunctive; 'malgré' + noun; 'même si' + indicative). Sort each connector by what follows it grammatically: a full clause with its own subject needs a conjunction (bien que, alors que); a noun phrase needs a preposition (malgré, en dépit de).",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Alors que / Tandis que", "+ indicatif, contraste simple", TokenCategory.CONNECTOR, "Deux faits opposés, sans obstacle vaincu."),
          FormulaToken("Bien que / Quoique", "+ subjonctif, concession", TokenCategory.CONNECTOR, "Un obstacle n'empêche pas le résultat."),
          FormulaToken("Malgré / En dépit de", "+ nom, concession", TokenCategory.CONNECTOR, "Préposition, jamais suivie d'une proposition complète.")
        ),
        diagramTitle = "Arbre de Décision : Opposition ou Concession ?",
        diagramDescription = "Identifiez la relation logique et la structure grammaticale :",
        decisionSteps = listOf(
          DecisionStep(1, "S'agit-il d'un simple contraste entre deux faits, sans obstacle vaincu ?", "-> Opposition : alors que/tandis que + indicatif", "-> Passez à l'étape 2"),
          DecisionStep(2, "Un obstacle empêche-t-il logiquement le résultat, qui se produit quand même ?", "-> Concession : bien que/quoique + subjonctif", "-> Passez à l'étape 3"),
          DecisionStep(3, "Le mot suivant est-il un nom (pas de sujet propre) ?", "-> Utilisez une préposition : malgré/en dépit de + nom", "-> Utilisez une conjonction + proposition complète")
        ),
        comparisonTableTitle = "Tableau Comparatif",
        comparisonHeaders = listOf("Connecteur", "Relation", "Construction"),
        comparisonRows = listOf(
          listOf("alors que / tandis que", "opposition", "+ indicatif"),
          listOf("bien que / quoique", "concession", "+ subjonctif"),
          listOf("malgré / en dépit de", "concession", "+ nom"),
          listOf("même si", "concession", "+ indicatif (exception)")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Il fait très froid, mais nous sortons quand même faire une promenade.",
          highlightedSegment = "mais nous sortons quand même",
          englishSentence = "It's very cold, but we're still going out for a walk.",
          contextNote = "Concession simple à l'oral avec 'mais... quand même', équivalent informel de 'bien que'."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Bien que la situation soit complexe, le comité est parvenu à un accord.",
          highlightedSegment = "Bien que la situation soit complexe",
          englishSentence = "Although the situation is complex, the committee reached an agreement.",
          contextNote = "'Bien que' + subjonctif dans un registre soutenu administratif."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "Malgré le prix, je me suis quand même acheté ces baskets.",
          highlightedSegment = "Malgré le prix",
          englishSentence = "Despite the price, I still bought these sneakers.",
          contextNote = "'Malgré' + nom à l'oral familier, très courant et naturel."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Alors que les ventes ont baissé en Europe, elles ont progressé en Asie.",
          highlightedSegment = "Alors que les ventes ont baissé",
          englishSentence = "While sales dropped in Europe, they grew in Asia.",
          contextNote = "'Alors que' pour une opposition factuelle dans un rapport professionnel, indicatif."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Un Choix Difficile",
        fullTextFr = "Bien qu'elle ait de bonnes notes, elle doute encore de ses capacités. Alors que ses amis semblent confiants, elle reste anxieuse avant l'examen. Malgré ses efforts, elle craint de ne pas réussir. Même si elle échoue, elle sait qu'elle pourra réessayer l'année prochaine.",
        fullTextEn = "Although she has good grades, she still doubts her abilities. While her friends seem confident, she remains anxious before the exam. Despite her efforts, she fears she won't succeed. Even if she fails, she knows she can try again next year.",
        annotations = listOf(
          InlineAnnotation(
            id = "opp_ann_1",
            targetPhrase = "Bien qu'elle ait de bonnes notes",
            explanationFr = "'Bien que' exprime une concession et exige le subjonctif ('ait'), un obstacle (les bonnes notes) n'empêchant pas le doute.",
            explanationEn = "'Bien que' expresses concession and requires the subjunctive ('ait'), an obstacle (good grades) not preventing the doubt.",
            whyItApplies = "Concession -> subjonctif.",
            commonPitfall = "Ne confondez pas avec 'alors que', qui prendrait l'indicatif pour un simple contraste."
          ),
          InlineAnnotation(
            id = "opp_ann_2",
            targetPhrase = "Alors que ses amis semblent confiants",
            explanationFr = "'Alors que' marque ici un contraste simple entre deux états, à l'indicatif, sans notion d'obstacle vaincu.",
            explanationEn = "'Alors que' marks a simple contrast between two states here, in the indicative, with no notion of an overcome obstacle.",
            whyItApplies = "Opposition simple -> indicatif.",
            commonPitfall = "Ne mettez jamais le subjonctif après 'alors que' dans son sens d'opposition simple."
          ),
          InlineAnnotation(
            id = "opp_ann_3",
            targetPhrase = "Même si elle échoue",
            explanationFr = "'Même si' est l'exception : il exprime une concession hypothétique mais prend toujours l'indicatif, jamais le subjonctif.",
            explanationEn = "'Même si' is the exception: it expresses a hypothetical concession but always takes the indicative, never the subjunctive.",
            whyItApplies = "Même si + indicatif (exception à la règle générale des concessions).",
            commonPitfall = "Ne confondez jamais 'même si' (indicatif) avec 'bien que' (subjonctif), une erreur très fréquente."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_opp_1",
          instructionFr = "Complétez avec le mode correct :",
          instructionEn = "Complete with the correct mood:",
          basePrompt = "Bien qu'il _____ (être) fatigué, il a terminé le marathon.",
          targetAnswer = "soit",
          acceptedAnswers = listOf("soit"),
          hint = "'Bien que' exige toujours le subjonctif.",
          explanation = "'Bien que' impose systématiquement le subjonctif : bien qu'il soit fatigué."
        ),
        ProductionDrillItem(
          id = "prod_opp_2",
          instructionFr = "Choisissez la préposition adaptée :",
          instructionEn = "Choose the appropriate preposition:",
          basePrompt = "_____ (Malgré/Bien que) la pluie, ils ont continué leur randonnée.",
          targetAnswer = "Malgré",
          acceptedAnswers = listOf("Malgré"),
          hint = "'La pluie' est un nom, pas une proposition.",
          explanation = "Devant un nom seul ('la pluie'), on utilise la préposition 'malgré', pas la conjonction 'bien que'."
        ),
        ProductionDrillItem(
          id = "prod_opp_3",
          instructionFr = "Choisissez le connecteur d'opposition simple :",
          instructionEn = "Choose the simple opposition connector:",
          basePrompt = "_____ (Alors que/Bien que) je préfère le café, mon frère préfère le thé.",
          targetAnswer = "Alors que",
          acceptedAnswers = listOf("Alors que"),
          hint = "Contraste simple entre deux préférences, sans obstacle vaincu.",
          explanation = "'Alors que' convient pour un simple contraste entre deux faits, avec l'indicatif."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_opp_1",
          instructionFr = "Touchez l'erreur de mode :",
          passage = "Bien qu'il est très occupé, il a trouvé le temps de m'aider.",
          tokens = listOf("Bien", "qu'il", "est", "très", "occupé,", "il", "a", "trouvé", "le", "temps", "de", "m'aider."),
          errorTokenIndex = 2,
          errorWord = "est",
          correction = "soit",
          ruleExplanation = "'Bien que' exige toujours le subjonctif : 'qu'il soit occupé', jamais l'indicatif 'est'."
        ),
        SpotErrorDrillItem(
          id = "spot_opp_2",
          instructionFr = "Trouvez l'erreur de construction :",
          passage = "Malgré qu'il pleuve, nous irons quand même à la plage.",
          tokens = listOf("Malgré", "qu'il", "pleuve,", "nous", "irons", "quand", "même", "à", "la", "plage."),
          errorTokenIndex = 0,
          errorWord = "Malgré",
          correction = "Bien que (ou Malgré la pluie)",
          ruleExplanation = "'Malgré' est une préposition et ne peut pas être suivi de 'que' + proposition ; il faut soit 'bien que' + subjonctif, soit 'malgré' + nom."
        )
      )
    ),

    // 43. LA MODALISATION
    StructuredRule(
      id = "modalisation",
      categoryId = "cat-avance",
      categoryName = "Nuances Avancées",
      level = "B2",
      titleFr = "La Modalisation",
      titleEn = "Marking How Certain, or How Personal, a Statement Is",
      summaryFr = "Des adverbes (probablement, sans doute), verbes (sembler, paraître) et structures (à mon avis, selon moi) permettent de nuancer le degré de certitude ou d'implication personnelle d'une affirmation.",
      summaryEn = "Adverbs (probablement, sans doute), verbs (sembler, paraître) and structures (à mon avis, selon moi) allow nuancing the degree of certainty or personal involvement of a statement.",
      pillar = GrammarPillar.OTHER,
      ruleExplanationEn = "Modalization is how a speaker signals they're not stating a bare fact but their degree of confidence in it, or that it's their opinion rather than an established truth. Certainty markers (certainement, sans doute, probablement, il paraît que) soften a claim along a scale from near-certain to speculative. Opinion markers (à mon avis, selon moi, il me semble que, je trouve que) explicitly flag a statement as subjective. Note the word-order trick: 'sans doute' and 'peut-être' at the very start of a sentence often trigger inversion in careful written French (Peut-être viendra-t-il), though this inversion is optional and less common in speech.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Adverbe de certitude", "probablement, sans doute, certainement", TokenCategory.CONNECTOR, "Nuance le degré de certitude d'une affirmation."),
          FormulaToken("Verbe de perception", "sembler, paraître + que/infinitif", TokenCategory.CONNECTOR, "Présente une impression plutôt qu'un fait affirmé."),
          FormulaToken("À mon avis / Selon moi", "Marqueur de subjectivité", TokenCategory.SUBJECT, "Signale explicitement une opinion personnelle.")
        ),
        diagramTitle = "Arbre de Décision : Comment Modaliser ?",
        diagramDescription = "Identifiez le type de nuance à exprimer :",
        decisionSteps = listOf(
          DecisionStep(1, "Voulez-vous indiquer un degré de certitude sur un fait ?", "-> Adverbe de certitude (probablement, sans doute, certainement)", "-> Passez à l'étape 2"),
          DecisionStep(2, "Voulez-vous présenter une impression plutôt qu'une affirmation directe ?", "-> Verbe de perception (sembler, paraître)", "-> Passez à l'étape 3"),
          DecisionStep(3, "Voulez-vous marquer explicitement que c'est votre opinion personnelle ?", "-> À mon avis / Selon moi / Il me semble que", "-> Utilisez une affirmation directe si aucune nuance n'est nécessaire")
        ),
        comparisonTableTitle = "Échelle de Certitude",
        comparisonHeaders = listOf("Marqueur", "Degré de Certitude", "Exemple"),
        comparisonRows = listOf(
          listOf("certainement / sûrement", "très élevé", "Il viendra certainement."),
          listOf("probablement / sans doute", "élevé mais pas garanti", "Il viendra probablement."),
          listOf("peut-être", "incertain", "Il viendra peut-être."),
          listOf("il paraît que / il semblerait que", "information rapportée, non vérifiée", "Il paraît qu'il a démissionné.")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Il va sans doute pleuvoir cet après-midi, prends un parapluie.",
          highlightedSegment = "sans doute pleuvoir",
          englishSentence = "It will probably rain this afternoon, take an umbrella.",
          contextNote = "'Sans doute' nuance une prédiction avec un degré de certitude élevé mais pas absolu."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Il semblerait que la situation économique se soit légèrement améliorée.",
          highlightedSegment = "Il semblerait que",
          englishSentence = "It would seem that the economic situation has slightly improved.",
          contextNote = "'Il semblerait que' + subjonctif, formule prudente et soutenue pour une information non confirmée."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "À mon avis, il ment, mais bon, je peux me tromper.",
          highlightedSegment = "À mon avis, il ment",
          englishSentence = "In my opinion, he's lying, but hey, I could be wrong.",
          contextNote = "'À mon avis' marquant explicitement une opinion personnelle, ton familier avec 'mais bon'."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Selon nos analystes, la croissance devrait ralentir au troisième trimestre.",
          highlightedSegment = "Selon nos analystes",
          englishSentence = "According to our analysts, growth should slow in the third quarter.",
          contextNote = "'Selon' attribue l'opinion à une source précise, registre professionnel/analytique."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Un Débat sur l'Avenir",
        fullTextFr = "À mon avis, les nouvelles technologies vont transformer notre façon de travailler. Il semble que de plus en plus d'entreprises adoptent le télétravail. Certains experts pensent probablement que cette tendance va s'accélérer. Selon moi, il faudra s'adapter rapidement à ces changements.",
        fullTextEn = "In my opinion, new technologies are going to transform the way we work. It seems that more and more companies are adopting remote work. Some experts probably think this trend will accelerate. In my view, we will need to adapt quickly to these changes.",
        annotations = listOf(
          InlineAnnotation(
            id = "mod_ann_1",
            targetPhrase = "À mon avis, les nouvelles technologies",
            explanationFr = "'À mon avis' marque explicitement que ce qui suit est une opinion personnelle, non un fait établi.",
            explanationEn = "'À mon avis' explicitly marks what follows as a personal opinion, not an established fact.",
            whyItApplies = "Marqueur de subjectivité.",
            commonPitfall = "N'utilisez pas 'à mon avis' pour présenter un fait objectif et vérifiable."
          ),
          InlineAnnotation(
            id = "mod_ann_2",
            targetPhrase = "Il semble que de plus en plus",
            explanationFr = "'Il semble que' présente une observation comme une impression plutôt qu'une certitude absolue.",
            explanationEn = "'Il semble que' presents an observation as an impression rather than absolute certainty.",
            whyItApplies = "Verbe de perception atténuant l'affirmation.",
            commonPitfall = "'Il semble que' peut prendre l'indicatif ou le subjonctif selon le degré de certitude visé ; à l'affirmatif courant, l'indicatif est fréquent."
          ),
          InlineAnnotation(
            id = "mod_ann_3",
            targetPhrase = "pensent probablement que",
            explanationFr = "'Probablement' nuance le verbe 'penser', indiquant un degré de certitude élevé mais pas absolu de la part du locuteur sur ce que pensent les experts.",
            explanationEn = "'Probablement' nuances the verb 'penser', indicating a high but not absolute degree of certainty about what experts think.",
            whyItApplies = "Adverbe de certitude modalisant une affirmation.",
            commonPitfall = "Placez l'adverbe de certitude près du verbe qu'il modifie pour éviter toute ambiguïté."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_mod_1",
          instructionFr = "Complétez avec un marqueur d'opinion personnelle :",
          instructionEn = "Complete with a personal opinion marker:",
          basePrompt = "_____ (À mon avis/Il est certain), cette décision est une erreur.",
          targetAnswer = "À mon avis",
          acceptedAnswers = listOf("À mon avis"),
          hint = "Marquez explicitement une opinion subjective.",
          explanation = "'À mon avis' marque clairement que la phrase exprime une opinion personnelle, pas un fait objectif."
        ),
        ProductionDrillItem(
          id = "prod_mod_2",
          instructionFr = "Complétez avec un adverbe de certitude modérée :",
          instructionEn = "Complete with a moderate certainty adverb:",
          basePrompt = "Il va _____ (probablement/certainement absolument) accepter cette offre, mais rien n'est sûr.",
          targetAnswer = "probablement",
          acceptedAnswers = listOf("probablement"),
          hint = "La suite de la phrase ('rien n'est sûr') indique une certitude modérée.",
          explanation = "'Probablement' convient car la phrase indique elle-même une incertitude résiduelle ('rien n'est sûr')."
        ),
        ProductionDrillItem(
          id = "prod_mod_3",
          instructionFr = "Complétez avec une expression d'information rapportée :",
          instructionEn = "Complete with a reported-information expression:",
          basePrompt = "_____ (Il paraît que/Je suis sûr que) le directeur va démissionner, mais rien n'est confirmé.",
          targetAnswer = "Il paraît que",
          acceptedAnswers = listOf("Il paraît que"),
          hint = "Information non confirmée, rapportée de source tierce.",
          explanation = "'Il paraît que' convient pour une information rapportée et non vérifiée, cohérent avec 'rien n'est confirmé'."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_mod_1",
          instructionFr = "Touchez l'incohérence de modalisation :",
          passage = "Je suis absolument certain qu'il viendra, mais je peux me tromper.",
          tokens = listOf("Je", "suis", "absolument", "certain", "qu'il", "viendra,", "mais", "je", "peux", "me", "tromper."),
          errorTokenIndex = 3,
          errorWord = "certain",
          correction = "presque sûr",
          ruleExplanation = "'Certain' exprime une certitude absolue, incompatible avec 'je peux me tromper' ; il faudrait un marqueur plus nuancé comme 'presque sûr' ou 'assez confiant'."
        ),
        SpotErrorDrillItem(
          id = "spot_mod_2",
          instructionFr = "Trouvez l'erreur de registre :",
          passage = "Selon des sources officielles confirmées, il paraît que le projet serait annulé.",
          tokens = listOf("Selon", "des", "sources", "officielles", "confirmées,", "il", "paraît", "que", "le", "projet", "serait", "annulé."),
          errorTokenIndex = 5,
          errorWord = "il paraît que",
          correction = "(incohérent avec 'sources officielles confirmées')",
          ruleExplanation = "'Il paraît que' marque une information non confirmée, ce qui contredit 'sources officielles confirmées' : il faudrait une formule de certitude, pas de rumeur."
        )
      )
    ),

    // 44. LE PARTICIPE PASSÉ COMPOSÉ
    StructuredRule(
      id = "participe-passe-compose",
      categoryId = "cat-avance",
      categoryName = "Nuances Avancées",
      level = "B2",
      titleFr = "Le Participe Passé Composé",
      titleEn = "\"Having Done\" — a Compact Way to Express a Prior Action",
      summaryFr = "'Ayant/Étant + participe passé' exprime une action achevée avant celle du verbe principal, dans un style condensé typique de l'écrit soutenu.",
      summaryEn = "'Ayant/Étant + past participle' expresses an action completed before the main verb's action, in a condensed style typical of formal writing.",
      pillar = GrammarPillar.VERB_SYSTEM,
      ruleExplanationEn = "This structure compresses a whole subordinate clause of prior cause or time into two words: 'ayant' (for avoir-verbs) or 'étant' (for être-verbs) plus the past participle, agreeing the same way it would in a normal compound tense. 'Ayant terminé son travail, elle est rentrée chez elle' means the same as 'Comme elle avait terminé son travail, elle est rentrée' — but much more compact. It belongs to written, fairly formal style; in speech, French prefers the full subordinate clause with 'quand' or 'comme' + plus-que-parfait.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Ayant / Étant", "Participe présent de l'auxiliaire", TokenCategory.CONNECTOR, "Ayant pour avoir, étant pour être, selon le verbe principal."),
          FormulaToken("+ Participe passé", "Accordé comme au passé composé", TokenCategory.TARGET_VERB, "terminé, arrivée, pris...")
        ),
        diagramTitle = "Arbre de Décision : Utiliser le Participe Passé Composé",
        diagramDescription = "Condensez une cause/temps antérieur en une forme compacte :",
        decisionSteps = listOf(
          DecisionStep(1, "Le verbe utiliserait-il 'avoir' au passé composé ?", "-> Ayant + participe passé (Ayant fini, elle est partie.)", "-> Passez à l'étape 2"),
          DecisionStep(2, "Le verbe utiliserait-il 'être' au passé composé ?", "-> Étant + participe passé, accordé avec le sujet (Étant arrivée en retard, elle s'est excusée.)", "-> Passez à l'étape 3"),
          DecisionStep(3, "Le contexte est-il un registre écrit soutenu ?", "-> Utilisez cette forme condensée", "-> Préférez une proposition complète avec 'quand/comme' à l'oral")
        ),
        comparisonTableTitle = "Forme Complète vs Condensée",
        comparisonHeaders = listOf("Forme Complète (oral)", "Participe Passé Composé (écrit)", "Verbe"),
        comparisonRows = listOf(
          listOf("Comme il avait fini son travail,", "Ayant fini son travail,", "finir (avoir)"),
          listOf("Comme elle était arrivée en retard,", "Étant arrivée en retard,", "arriver (être)")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "Comme elle avait déjà mangé, elle n'a pas eu faim au dîner.",
          highlightedSegment = "Comme elle avait déjà mangé",
          englishSentence = "Since she had already eaten, she wasn't hungry at dinner.",
          contextNote = "Forme complète à l'oral, équivalente au participe passé composé mais plus naturelle en conversation."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Ayant terminé ses études, elle s'est immédiatement lancée dans la vie active.",
          highlightedSegment = "Ayant terminé ses études",
          englishSentence = "Having finished her studies, she immediately threw herself into working life.",
          contextNote = "Participe passé composé typique de l'écrit soutenu/littéraire, condensant une cause antérieure."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "Vu qu'il avait déjà tout organisé, on n'a rien eu à faire.",
          highlightedSegment = "Vu qu'il avait déjà tout organisé",
          englishSentence = "Since he had already organized everything, we didn't have anything to do.",
          contextNote = "À l'oral familier, on préfère toujours 'vu que/comme' + plus-que-parfait à la forme condensée."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Étant donné les résultats obtenus, la direction a validé le budget supplémentaire.",
          highlightedSegment = "Étant donné les résultats obtenus",
          englishSentence = "Given the results obtained, management approved the additional budget.",
          contextNote = "'Étant donné' figé, forme condensée fréquente dans les rapports professionnels formels."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Un Rapport de Mission",
        fullTextFr = "Ayant analysé les données du dernier trimestre, l'équipe a proposé de nouvelles orientations stratégiques. Étant arrivés en avance sur le calendrier prévu, les développeurs ont pu ajouter des fonctionnalités supplémentaires. Ayant consulté tous les partenaires concernés, la direction a validé le projet final.",
        fullTextEn = "Having analyzed the last quarter's data, the team proposed new strategic directions. Having arrived ahead of the planned schedule, the developers were able to add extra features. Having consulted all relevant partners, management approved the final project.",
        annotations = listOf(
          InlineAnnotation(
            id = "ppc_ann_1",
            targetPhrase = "Ayant analysé les données",
            explanationFr = "'Ayant' + participe passé condense 'comme l'équipe avait analysé', car 'analyser' se conjugue avec 'avoir'.",
            explanationEn = "'Ayant' + past participle condenses 'as the team had analyzed', since 'analyser' takes 'avoir'.",
            whyItApplies = "Ayant + participe passé pour un verbe en 'avoir'.",
            commonPitfall = "N'utilisez pas 'étant' ici : 'analyser' n'est pas un verbe qui se conjugue avec 'être'."
          ),
          InlineAnnotation(
            id = "ppc_ann_2",
            targetPhrase = "Étant arrivés en avance",
            explanationFr = "'Étant' + participe passé accordé au masculin pluriel, car 'arriver' se conjugue avec 'être' et le sujet ('les développeurs') est masculin pluriel.",
            explanationEn = "'Étant' + past participle agreeing in masculine plural, since 'arriver' takes 'être' and the subject ('les développeurs') is masculine plural.",
            whyItApplies = "Étant + participe passé accordé pour un verbe en 'être'.",
            commonPitfall = "N'oubliez pas l'accord du participe passé avec le sujet quand l'auxiliaire est 'être' : arrivés, pas 'arrivé'."
          ),
          InlineAnnotation(
            id = "ppc_ann_3",
            targetPhrase = "Ayant consulté tous les partenaires",
            explanationFr = "Structure condensée typique de l'écrit professionnel, résumant une étape antérieure au résultat final.",
            explanationEn = "Condensed structure typical of professional writing, summarizing a step prior to the final result.",
            whyItApplies = "Registre écrit formel, condensation d'une cause/étape antérieure.",
            commonPitfall = "Cette forme reste réservée à l'écrit ; à l'oral, on dirait plutôt 'comme elle avait consulté...'."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_ppc_1",
          instructionFr = "Formez le participe passé composé :",
          instructionEn = "Form the compound past participle:",
          basePrompt = "_____ (Finir) son travail, il est rentré chez lui.",
          targetAnswer = "Ayant fini",
          acceptedAnswers = listOf("Ayant fini"),
          hint = "'Finir' se conjugue avec 'avoir'.",
          explanation = "'Ayant fini' = 'ayant' + participe passé de 'finir', verbe qui se conjugue avec 'avoir'."
        ),
        ProductionDrillItem(
          id = "prod_ppc_2",
          instructionFr = "Formez le participe passé composé avec accord :",
          instructionEn = "Form the compound past participle with agreement:",
          basePrompt = "_____ (Partir) très tôt, elles ont évité les embouteillages.",
          targetAnswer = "Étant parties",
          acceptedAnswers = listOf("Étant parties"),
          hint = "'Partir' se conjugue avec 'être', sujet féminin pluriel.",
          explanation = "'Étant parties' = 'étant' + participe passé accordé au féminin pluriel avec 'elles'."
        ),
        ProductionDrillItem(
          id = "prod_ppc_3",
          instructionFr = "Transformez la proposition complète en forme condensée :",
          instructionEn = "Transform the full clause into the condensed form:",
          basePrompt = "Comme il avait beaucoup travaillé, il a pu se reposer. -> _____ (Ayant/Étant) beaucoup travaillé, il a pu se reposer.",
          targetAnswer = "Ayant",
          acceptedAnswers = listOf("Ayant"),
          hint = "'Travailler' se conjugue avec 'avoir'.",
          explanation = "'Travailler' prend l'auxiliaire 'avoir', donc on utilise 'ayant' pour la forme condensée."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_ppc_1",
          instructionFr = "Touchez l'erreur d'auxiliaire :",
          passage = "Ayant arrivée en retard, elle a dû s'excuser devant tout le monde.",
          tokens = listOf("Ayant", "arrivée", "en", "retard,", "elle", "a", "dû", "s'excuser", "devant", "tout", "le", "monde."),
          errorTokenIndex = 0,
          errorWord = "Ayant",
          correction = "Étant",
          ruleExplanation = "'Arriver' se conjugue avec 'être', donc il fallait 'Étant arrivée', pas 'Ayant arrivée'."
        ),
        SpotErrorDrillItem(
          id = "spot_ppc_2",
          instructionFr = "Trouvez l'erreur d'accord :",
          passage = "Étant rentré tard, les enfants se sont couchés immédiatement.",
          tokens = listOf("Étant", "rentré", "tard,", "les", "enfants", "se", "sont", "couchés", "immédiatement."),
          errorTokenIndex = 1,
          errorWord = "rentré",
          correction = "rentrés",
          ruleExplanation = "Le participe passé doit s'accorder avec le sujet 'les enfants' (masculin pluriel) : rentrés, pas 'rentré'."
        )
      )
    ),

    // 45. ARGUMENTER : LES CONNECTEURS LOGIQUES
    StructuredRule(
      id = "connecteurs-argumentation",
      categoryId = "cat-avance",
      categoryName = "Nuances Avancées",
      level = "B2",
      titleFr = "Argumenter : Les Connecteurs Logiques",
      titleEn = "Connectors for Building and Structuring an Argument",
      summaryFr = "Des connecteurs organisent un raisonnement en étapes claires : addition (de plus, en outre), énumération (d'abord, ensuite, enfin), reformulation (autrement dit), et illustration (par exemple, notamment).",
      summaryEn = "Connectors organize reasoning into clear steps: addition (de plus, en outre), enumeration (d'abord, ensuite, enfin), reformulation (autrement dit), and illustration (par exemple, notamment).",
      pillar = GrammarPillar.OTHER,
      ruleExplanationEn = "Structure a written argument by function, not randomly: enumeration connectors ('d'abord', 'ensuite', 'enfin' or 'premièrement', 'deuxièmement') order your points sequentially; addition connectors ('de plus', 'en outre', 'par ailleurs') stack a new point onto the previous one without contradicting it; reformulation connectors ('autrement dit', 'c'est-à-dire') restate the same idea more clearly; illustration connectors ('par exemple', 'notamment', 'en particulier') back a claim with a concrete instance. Mixing categories at random (e.g. using 'en outre' where a contrast connector like 'cependant' is needed) makes an argument's logic hard to follow even when each sentence is grammatically correct.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("D'abord / Ensuite / Enfin", "Énumération", TokenCategory.CONNECTOR, "Ordonne les points d'un raisonnement dans le temps ou l'importance."),
          FormulaToken("De plus / En outre", "Addition", TokenCategory.CONNECTOR, "Ajoute un argument allant dans le même sens."),
          FormulaToken("Par exemple / Notamment", "Illustration", TokenCategory.CONNECTOR, "Appuie une affirmation par un cas concret.")
        ),
        diagramTitle = "Arbre de Décision : Quel Connecteur Logique ?",
        diagramDescription = "Identifiez la fonction du connecteur dans votre raisonnement :",
        decisionSteps = listOf(
          DecisionStep(1, "Voulez-vous ordonner vos points dans une séquence ?", "-> D'abord/Premièrement... Ensuite/Deuxièmement... Enfin", "-> Passez à l'étape 2"),
          DecisionStep(2, "Voulez-vous ajouter un argument qui va dans le même sens que le précédent ?", "-> De plus / En outre / Par ailleurs", "-> Passez à l'étape 3"),
          DecisionStep(3, "Voulez-vous illustrer ou reformuler une idée déjà énoncée ?", "-> Par exemple/notamment (illustration) ou autrement dit/c'est-à-dire (reformulation)", "-> Vérifiez si un connecteur de contraste (cependant) convient mieux")
        ),
        comparisonTableTitle = "Catégories de Connecteurs",
        comparisonHeaders = listOf("Fonction", "Connecteurs", "Usage"),
        comparisonRows = listOf(
          listOf("Énumération", "d'abord, ensuite, enfin", "Ordonner les étapes d'un raisonnement"),
          listOf("Addition", "de plus, en outre, par ailleurs", "Ajouter un argument complémentaire"),
          listOf("Illustration", "par exemple, notamment", "Appuyer avec un cas concret"),
          listOf("Reformulation", "autrement dit, c'est-à-dire", "Reformuler plus clairement")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "D'abord, il faut réserver les billets. Ensuite, on pourra organiser le reste du voyage.",
          highlightedSegment = "D'abord... Ensuite",
          englishSentence = "First, we need to book the tickets. Then, we can organize the rest of the trip.",
          contextNote = "Connecteurs d'énumération simples, usage courant pour organiser des étapes."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Cette mesure est coûteuse ; qui plus est, son efficacité reste à démontrer.",
          highlightedSegment = "qui plus est",
          englishSentence = "This measure is costly; moreover, its effectiveness remains to be proven.",
          contextNote = "'Qui plus est' est un connecteur d'addition soutenu, ajoutant un argument renforçant le précédent."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "Genre, d'abord c'est trop cher, et en plus c'est loin, donc non merci.",
          highlightedSegment = "d'abord... et en plus",
          englishSentence = "Like, first it's too expensive, and plus it's far, so no thanks.",
          contextNote = "Connecteurs simplifiés à l'oral familier ('en plus' au lieu de 'de plus'), avec 'genre' comme marqueur discursif."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Premièrement, nous devons réduire les coûts. Par ailleurs, il convient d'améliorer la qualité.",
          highlightedSegment = "Premièrement... Par ailleurs",
          englishSentence = "First, we need to reduce costs. Furthermore, quality should be improved.",
          contextNote = "Connecteurs formels structurant une présentation professionnelle en points clairs."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Un Essai Argumentatif",
        fullTextFr = "Premièrement, le télétravail permet de réduire le temps de trajet. De plus, il améliore souvent la concentration des employés. Par exemple, certaines entreprises ont constaté une hausse de productivité. Cependant, il présente aussi des inconvénients, notamment l'isolement social. Enfin, autrement dit, un équilibre entre présentiel et télétravail semble être la meilleure solution.",
        fullTextEn = "Firstly, remote work reduces commuting time. Moreover, it often improves employee concentration. For example, some companies have noted an increase in productivity. However, it also has drawbacks, notably social isolation. Finally, in other words, a balance between office and remote work seems to be the best solution.",
        annotations = listOf(
          InlineAnnotation(
            id = "arg_ann_1",
            targetPhrase = "Premièrement, le télétravail",
            explanationFr = "'Premièrement' ouvre l'énumération, annonçant le premier point d'une série d'arguments.",
            explanationEn = "'Premièrement' opens the enumeration, announcing the first point of a series of arguments.",
            whyItApplies = "Connecteur d'énumération en tête d'argumentation.",
            commonPitfall = "Veillez à la cohérence : 'premièrement' appelle généralement 'deuxièmement' puis 'enfin', pas un mélange incohérent."
          ),
          InlineAnnotation(
            id = "arg_ann_2",
            targetPhrase = "Par exemple, certaines entreprises",
            explanationFr = "'Par exemple' introduit un cas concret illustrant l'affirmation précédente sur la concentration.",
            explanationEn = "'Par exemple' introduces a concrete case illustrating the previous claim about concentration.",
            whyItApplies = "Connecteur d'illustration.",
            commonPitfall = "'Par exemple' doit toujours être suivi d'un cas concret, pas d'une généralité supplémentaire."
          ),
          InlineAnnotation(
            id = "arg_ann_3",
            targetPhrase = "notamment l'isolement social",
            explanationFr = "'Notamment' introduit un exemple particulier parmi d'autres inconvénients possibles, sans être exhaustif.",
            explanationEn = "'Notamment' introduces one particular example among other possible drawbacks, without being exhaustive.",
            whyItApplies = "Connecteur d'illustration partielle.",
            commonPitfall = "'Notamment' suggère qu'il existe d'autres exemples non cités, contrairement à une liste complète."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_arg_1",
          instructionFr = "Choisissez le connecteur d'addition correct :",
          instructionEn = "Choose the correct addition connector:",
          basePrompt = "Ce plan est ambitieux. _____ (De plus/Par exemple), il est réalisable avec notre budget actuel.",
          targetAnswer = "De plus",
          acceptedAnswers = listOf("De plus"),
          hint = "On ajoute un argument allant dans le même sens.",
          explanation = "'De plus' ajoute un argument complémentaire allant dans le même sens que le précédent."
        ),
        ProductionDrillItem(
          id = "prod_arg_2",
          instructionFr = "Choisissez le connecteur d'illustration :",
          instructionEn = "Choose the illustration connector:",
          basePrompt = "Certains pays ont adopté cette politique, _____ (par exemple/autrement dit) la Suède.",
          targetAnswer = "par exemple",
          acceptedAnswers = listOf("par exemple"),
          hint = "On introduit un cas concret.",
          explanation = "'Par exemple' introduit un cas concret (la Suède) illustrant l'affirmation générale."
        ),
        ProductionDrillItem(
          id = "prod_arg_3",
          instructionFr = "Choisissez le connecteur de reformulation :",
          instructionEn = "Choose the reformulation connector:",
          basePrompt = "Le projet est ajourné, _____ (autrement dit/par ailleurs) reporté à une date ultérieure.",
          targetAnswer = "autrement dit",
          acceptedAnswers = listOf("autrement dit"),
          hint = "On reformule la même idée plus clairement.",
          explanation = "'Autrement dit' reformule 'ajourné' de façon plus explicite avec 'reporté à une date ultérieure'."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_arg_1",
          instructionFr = "Touchez le connecteur mal choisi :",
          passage = "Ce produit est cher, autrement dit il est aussi très populaire auprès des clients.",
          tokens = listOf("Ce", "produit", "est", "cher,", "autrement", "dit", "il", "est", "aussi", "très", "populaire", "auprès", "des", "clients."),
          errorTokenIndex = 4,
          errorWord = "autrement",
          correction = "cependant (ou par ailleurs)",
          ruleExplanation = "'Autrement dit' sert à reformuler, pas à introduire une idée contrastante ou additionnelle ; il faudrait 'cependant' ou 'par ailleurs'."
        ),
        SpotErrorDrillItem(
          id = "spot_arg_2",
          instructionFr = "Trouvez l'incohérence logique :",
          passage = "Par exemple, tous les pays européens ont adopté cette réglementation sans exception.",
          tokens = listOf("Par", "exemple,", "tous", "les", "pays", "européens", "ont", "adopté", "cette", "réglementation", "sans", "exception."),
          errorTokenIndex = 0,
          errorWord = "Par exemple",
          correction = "En fait (ou De fait)",
          ruleExplanation = "'Par exemple' introduit normalement un cas particulier, pas une généralité totale ('tous les pays... sans exception') ; un autre connecteur serait plus cohérent."
        )
      )
    ),

    // 46. LES DONNÉES CHIFFRÉES ET LA PROPORTION
    StructuredRule(
      id = "chiffres-proportion",
      categoryId = "cat-avance",
      categoryName = "Nuances Avancées",
      level = "B2",
      titleFr = "Les Données Chiffrées et la Proportion",
      titleEn = "Talking About Statistics, Quantities, and Proportions",
      summaryFr = "Des structures comme 'la moitié de', 'un tiers des', 'de plus en plus de', et le vocabulaire de l'évolution (augmenter, chuter, stagner) permettent de commenter des statistiques avec précision.",
      summaryEn = "Structures like 'la moitié de', 'un tiers des', 'de plus en plus de', and evolution vocabulary (augmenter, chuter, stagner) allow precise commentary on statistics.",
      pillar = GrammarPillar.OTHER,
      ruleExplanationEn = "Fractions and proportions ('la moitié de', 'un tiers des', 'les trois quarts de') are always followed by 'de/des + noun', and the verb generally agrees with the noun that follows 'de', not with the fraction word itself ('la moitié des étudiants ont réussi', not 'a réussi', in standard usage — this is a nuance many learners get wrong, so both agreement patterns are defensible but plural agreement with the real quantity is what native speakers usually prefer). For trends, sort your verbs by direction and speed: augmenter/croître/progresser (up), diminuer/baisser/chuter (down, chuter = sharply), stagner/rester stable (flat) — pairing these with adverbs like 'légèrement', 'fortement', 'considérablement' gives precision.",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Fraction/proportion + de/des", "la moitié de, un tiers des...", TokenCategory.CONNECTOR, "Toujours suivi de 'de' + nom (singulier ou pluriel selon le sens)."),
          FormulaToken("Verbe d'évolution", "augmenter, chuter, stagner", TokenCategory.TARGET_VERB, "Choisi selon la direction et l'intensité du changement."),
          FormulaToken("Adverbe d'intensité", "légèrement, fortement, considérablement", TokenCategory.CONNECTOR, "Précise l'ampleur du changement.")
        ),
        diagramTitle = "Arbre de Décision : Décrire une Statistique",
        diagramDescription = "Choisissez la structure adaptée à la donnée chiffrée :",
        decisionSteps = listOf(
          DecisionStep(1, "Voulez-vous exprimer une fraction ou une proportion précise ?", "-> La moitié de / Un tiers des / Les trois quarts de + nom", "-> Passez à l'étape 2"),
          DecisionStep(2, "Voulez-vous décrire une évolution dans le temps ?", "-> Choisissez le verbe selon la direction : augmenter (hausse), chuter (forte baisse), stagner (stable)", "-> Passez à l'étape 3"),
          DecisionStep(3, "Voulez-vous préciser l'intensité du changement ?", "-> Ajoutez un adverbe : légèrement, fortement, considérablement", "-> Utilisez un chiffre précis (de X% à Y%) pour plus de précision")
        ),
        comparisonTableTitle = "Vocabulaire de l'Évolution",
        comparisonHeaders = listOf("Direction", "Verbes", "Intensité"),
        comparisonRows = listOf(
          listOf("Hausse", "augmenter, croître, progresser", "fortement, considérablement (forte hausse)"),
          listOf("Baisse", "diminuer, baisser, chuter", "légèrement (faible baisse), chuter (forte baisse)"),
          listOf("Stabilité", "stagner, rester stable", "à peine, quasiment pas")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "La moitié de mes amis ont déjà voyagé en Asie.",
          highlightedSegment = "La moitié de mes amis",
          englishSentence = "Half of my friends have already traveled to Asia.",
          contextNote = "'La moitié de' + nom pluriel, verbe accordé au pluriel selon l'usage courant."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Le chiffre d'affaires a considérablement progressé au cours du dernier exercice.",
          highlightedSegment = "a considérablement progressé",
          englishSentence = "Revenue has considerably increased over the last fiscal year.",
          contextNote = "Vocabulaire précis de l'évolution avec adverbe d'intensité, registre financier soutenu."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "Y a de plus en plus de monde qui bosse à distance maintenant.",
          highlightedSegment = "de plus en plus de monde",
          englishSentence = "There are more and more people working remotely now.",
          contextNote = "'De plus en plus de' à l'oral familier, structure figée pour une tendance croissante."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Les ventes ont chuté de vingt pour cent au deuxième trimestre.",
          highlightedSegment = "ont chuté de vingt pour cent",
          englishSentence = "Sales dropped by twenty percent in the second quarter.",
          contextNote = "'Chuter' pour une forte baisse précise, avec pourcentage exact, registre professionnel."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Une Analyse de Marché",
        fullTextFr = "Un tiers des consommateurs préfèrent désormais acheter en ligne. Les ventes en magasin ont légèrement diminué cette année. En revanche, le commerce électronique a fortement progressé, avec une hausse de trente pour cent. De plus en plus d'entreprises investissent dans ce secteur en pleine croissance.",
        fullTextEn = "A third of consumers now prefer to buy online. In-store sales have slightly decreased this year. On the other hand, e-commerce has strongly increased, with a thirty percent rise. More and more companies are investing in this fast-growing sector.",
        annotations = listOf(
          InlineAnnotation(
            id = "chi_ann_1",
            targetPhrase = "Un tiers des consommateurs préfèrent",
            explanationFr = "'Un tiers des' est suivi du pluriel 'consommateurs', et le verbe s'accorde au pluriel selon l'usage courant préféré.",
            explanationEn = "'Un tiers des' is followed by the plural 'consommateurs', and the verb agrees in the plural per the preferred common usage.",
            whyItApplies = "Fraction + de/des + nom pluriel, accord au pluriel.",
            commonPitfall = "L'accord singulier ('préfère') est aussi techniquement possible, mais l'accord pluriel est largement privilégié à l'oral et à l'écrit courant."
          ),
          InlineAnnotation(
            id = "chi_ann_2",
            targetPhrase = "ont légèrement diminué",
            explanationFr = "'Légèrement' précise que la baisse ('diminué') est faible, nuançant l'intensité du changement.",
            explanationEn = "'Légèrement' specifies that the decrease ('diminué') is small, nuancing the intensity of the change.",
            whyItApplies = "Adverbe d'intensité + verbe de baisse.",
            commonPitfall = "Ne confondez pas 'diminuer légèrement' (faible baisse) avec 'chuter' (forte baisse soudaine)."
          ),
          InlineAnnotation(
            id = "chi_ann_3",
            targetPhrase = "De plus en plus d'entreprises investissent",
            explanationFr = "'De plus en plus de' est une structure figée exprimant une augmentation progressive et continue, suivie d'un nom pluriel.",
            explanationEn = "'De plus en plus de' is a fixed structure expressing a progressive, continuous increase, followed by a plural noun.",
            whyItApplies = "Structure figée de tendance croissante.",
            commonPitfall = "'De plus en plus de' est invariable dans sa forme, quel que soit le nom qui suit."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_chi_1",
          instructionFr = "Complétez avec la structure de fraction correcte :",
          instructionEn = "Complete with the correct fraction structure:",
          basePrompt = "_____ (Les trois quarts des/Le trois quart de) employés sont satisfaits de leur travail.",
          targetAnswer = "Les trois quarts des",
          acceptedAnswers = listOf("Les trois quarts des"),
          hint = "'Trois quarts' est toujours au pluriel.",
          explanation = "'Les trois quarts des' est la forme correcte, toujours au pluriel avec 's' à 'quarts'."
        ),
        ProductionDrillItem(
          id = "prod_chi_2",
          instructionFr = "Choisissez le verbe d'évolution adapté à une forte baisse :",
          instructionEn = "Choose the evolution verb suited to a sharp decrease:",
          basePrompt = "Les bénéfices ont _____ (chuté/légèrement diminué) de cinquante pour cent en un an.",
          targetAnswer = "chuté",
          acceptedAnswers = listOf("chuté"),
          hint = "Cinquante pour cent en un an est une baisse importante.",
          explanation = "'Chuter' convient pour une baisse importante et rapide, cohérent avec 'cinquante pour cent en un an'."
        ),
        ProductionDrillItem(
          id = "prod_chi_3",
          instructionFr = "Complétez avec la structure de tendance croissante :",
          instructionEn = "Complete with the increasing-trend structure:",
          basePrompt = "_____ (De plus en plus de/De moins en moins de) gens utilisent les vélos électriques.",
          targetAnswer = "De plus en plus de",
          acceptedAnswers = listOf("De plus en plus de"),
          hint = "Le contexte suggère une tendance croissante.",
          explanation = "'De plus en plus de' exprime une augmentation progressive, adaptée à la popularité croissante des vélos électriques."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_chi_1",
          instructionFr = "Touchez l'erreur d'accord de fraction :",
          passage = "La moitié des étudiants a réussi leur examen haut la main cette année.",
          tokens = listOf("La", "moitié", "des", "étudiants", "a", "réussi", "leur", "examen", "haut", "la", "main", "cette", "année."),
          errorTokenIndex = 4,
          errorWord = "a",
          correction = "ont",
          ruleExplanation = "Avec 'la moitié des + nom pluriel', l'accord au pluriel ('ont réussi') est largement préféré à l'accord singulier."
        ),
        SpotErrorDrillItem(
          id = "spot_chi_2",
          instructionFr = "Trouvez l'incohérence de verbe d'évolution :",
          passage = "Les ventes ont chuté légèrement de deux pour cent seulement ce mois-ci.",
          tokens = listOf("Les", "ventes", "ont", "chuté", "légèrement", "de", "deux", "pour", "cent", "seulement", "ce", "mois-ci."),
          errorTokenIndex = 3,
          errorWord = "chuté",
          correction = "diminué",
          ruleExplanation = "'Chuter' implique une forte baisse soudaine, incohérent avec 'légèrement' et 'seulement deux pour cent' ; il faudrait 'diminué' pour une faible baisse."
        )
      )
    ),

    // 47. L'ACCORD DU VERBE AVEC LE SUJET
    StructuredRule(
      id = "accord-sujet-verbe",
      categoryId = "cat-avance",
      categoryName = "Nuances Avancées",
      level = "B2",
      titleFr = "L'Accord du Verbe avec le Sujet : Cas Particuliers",
      titleEn = "Special Cases: Collective Nouns, Multiple Subjects, Coordination",
      summaryFr = "Au-delà de l'accord simple, des cas particuliers se posent : noms collectifs (la plupart, la foule), sujets multiples reliés par 'et'/'ou', et sujets de personnes différentes.",
      summaryEn = "Beyond simple agreement, special cases arise: collective nouns (la plupart, la foule), multiple subjects joined by 'et'/'ou', and subjects of different persons.",
      pillar = GrammarPillar.AGREEMENT_ENGINE,
      ruleExplanationEn = "Several tricky patterns worth memorizing: a singular collective noun followed by 'de + plural noun' (la plupart des gens, un groupe d'étudiants) usually takes a PLURAL verb when the collective is understood as many individuals ('la plupart des gens pensent'), though 'un groupe de' more often stays singular. Two or more subjects joined by 'et' take a plural verb (Paul et Marie sont partis). Subjects joined by 'ou' or 'ni... ni' usually take a singular verb if the choice is exclusive (Paul ou Marie viendra), though plural is also accepted when both could act together. When subjects of different persons are joined, the verb takes the highest-priority person: 'nous' beats 'vous' beats 'il/elle' (toi et moi sommes d'accord = nous sommes d'accord).",
      visualStructure = VisualStructure(
        formulaTokens = listOf(
          FormulaToken("Nom collectif + de + pluriel", "la plupart de, un groupe de", TokenCategory.SUBJECT, "Accord souvent au pluriel si le sens est distributif."),
          FormulaToken("Sujet 1 et Sujet 2", "Sujets multiples reliés par 'et'", TokenCategory.SUBJECT, "Verbe toujours au pluriel."),
          FormulaToken("Sujet 1 ou Sujet 2", "Sujets reliés par 'ou'", TokenCategory.SUBJECT, "Verbe souvent au singulier si le choix est exclusif.")
        ),
        diagramTitle = "Arbre de Décision : Quel Accord pour ce Sujet Complexe ?",
        diagramDescription = "Identifiez la structure du sujet avant d'accorder le verbe :",
        decisionSteps = listOf(
          DecisionStep(1, "Le sujet est-il un nom collectif suivi de 'de' + nom pluriel ?", "-> Accordez généralement au pluriel si le sens est distributif (la plupart des gens pensent)", "-> Passez à l'étape 2"),
          DecisionStep(2, "Le sujet contient-il plusieurs éléments reliés par 'et' ?", "-> Verbe toujours au pluriel", "-> Passez à l'étape 3"),
          DecisionStep(3, "Le sujet contient-il des personnes différentes (toi et moi, lui et vous) ?", "-> Priorité : nous > vous > il/elle (accordez au pronom prioritaire)", "-> Sujets reliés par 'ou' : accordez au singulier si le choix est exclusif")
        ),
        comparisonTableTitle = "Cas Particuliers d'Accord",
        comparisonHeaders = listOf("Structure", "Accord", "Exemple"),
        comparisonRows = listOf(
          listOf("La plupart des + pluriel", "pluriel", "La plupart des invités sont arrivés."),
          listOf("Sujet1 et Sujet2", "pluriel", "Le chat et le chien dorment."),
          listOf("Toi et moi", "1re pers. pluriel (nous)", "Toi et moi sommes d'accord.")
        )
      ),
      registerExamples = listOf(
        RegisterExample(
          register = RegisterType.COURANT,
          frenchSentence = "La plupart des élèves ont bien réussi le contrôle de mathématiques.",
          highlightedSegment = "La plupart des élèves ont",
          englishSentence = "Most of the students did well on the math test.",
          contextNote = "'La plupart des' + pluriel, accord pluriel du verbe, usage courant standard."
        ),
        RegisterExample(
          register = RegisterType.SOUTENU,
          frenchSentence = "Ni le directeur ni son adjoint n'ont été informés de cette décision.",
          highlightedSegment = "n'ont été informés",
          englishSentence = "Neither the director nor his deputy were informed of this decision.",
          contextNote = "'Ni... ni' avec accord pluriel car les deux personnes sont concernées, registre soutenu."
        ),
        RegisterExample(
          register = RegisterType.FAMILIER,
          frenchSentence = "Toi et moi, on est vraiment faits pour s'entendre !",
          highlightedSegment = "Toi et moi, on est",
          englishSentence = "You and me, we're really meant to get along!",
          contextNote = "'On' remplace souvent 'nous' à l'oral familier, même après 'toi et moi'."
        ),
        RegisterExample(
          register = RegisterType.PROFESSIONNEL,
          frenchSentence = "Le conseil d'administration ainsi que les actionnaires ont validé la fusion.",
          highlightedSegment = "ainsi que les actionnaires ont validé",
          englishSentence = "The board of directors as well as the shareholders approved the merger.",
          contextNote = "'Ainsi que' reliant deux groupes distincts, accord pluriel dans un contexte professionnel/juridique."
        )
      ),
      contextualPassage = ContextualPassage(
        title = "Un Compte-rendu de Réunion",
        fullTextFr = "La plupart des participants ont approuvé la nouvelle proposition. Le directeur et son assistante ont présenté le projet ensemble. Ni les investisseurs ni le conseil n'ont exprimé d'objection majeure. Toi et moi devrons finaliser les derniers détails avant vendredi.",
        fullTextEn = "Most of the participants approved the new proposal. The director and his assistant presented the project together. Neither the investors nor the board raised any major objection. You and I will need to finalize the last details before Friday.",
        annotations = listOf(
          InlineAnnotation(
            id = "asv_ann_1",
            targetPhrase = "La plupart des participants ont approuvé",
            explanationFr = "'La plupart des' + pluriel entraîne un verbe au pluriel, le sens étant distributif (chaque participant individuellement).",
            explanationEn = "'La plupart des' + plural triggers a plural verb, the meaning being distributive (each participant individually).",
            whyItApplies = "Nom collectif distributif -> accord pluriel.",
            commonPitfall = "Ne mettez jamais le singulier après 'la plupart des + nom pluriel', contrairement à ce que la structure pourrait suggérer."
          ),
          InlineAnnotation(
            id = "asv_ann_2",
            targetPhrase = "Ni les investisseurs ni le conseil n'ont exprimé",
            explanationFr = "'Ni... ni' reliant deux groupes de personnes prend ici l'accord pluriel, les deux étant concernés par l'absence d'objection.",
            explanationEn = "'Ni... ni' linking two groups of people takes the plural agreement here, both being concerned by the lack of objection.",
            whyItApplies = "Ni... ni + accord pluriel (les deux sujets sont impliqués).",
            commonPitfall = "L'accord singulier est aussi parfois toléré avec 'ni... ni', mais le pluriel est généralement préféré quand les deux sujets sont pluriels."
          ),
          InlineAnnotation(
            id = "asv_ann_3",
            targetPhrase = "Toi et moi devrons",
            explanationFr = "'Toi et moi' combine 2e et 1re personne : la priorité va à la 1re personne du pluriel ('nous'), d'où 'devrons'.",
            explanationEn = "'Toi et moi' combines 2nd and 1st person: priority goes to 1st person plural ('nous'), hence 'devrons'.",
            whyItApplies = "Priorité de personne : nous > vous > il/elle.",
            commonPitfall = "Ne dites jamais 'devrez' (2e personne) ici ; la 1re personne du pluriel a toujours priorité sur la 2e."
          )
        )
      ),
      productionDrills = listOf(
        ProductionDrillItem(
          id = "prod_asv_1",
          instructionFr = "Accordez le verbe avec le nom collectif :",
          instructionEn = "Agree the verb with the collective noun:",
          basePrompt = "La plupart des clients _____ (être) satisfaits de notre service.",
          targetAnswer = "sont",
          acceptedAnswers = listOf("sont"),
          hint = "'La plupart des' + pluriel -> verbe au pluriel.",
          explanation = "'La plupart des clients' entraîne l'accord pluriel : sont satisfaits."
        ),
        ProductionDrillItem(
          id = "prod_asv_2",
          instructionFr = "Accordez le verbe avec les sujets multiples :",
          instructionEn = "Agree the verb with the multiple subjects:",
          basePrompt = "Mon frère et ma sœur _____ (venir) nous rendre visite ce week-end.",
          targetAnswer = "viennent",
          acceptedAnswers = listOf("viennent"),
          hint = "Deux sujets reliés par 'et' -> pluriel.",
          explanation = "Deux sujets reliés par 'et' entraînent toujours l'accord pluriel : viennent."
        ),
        ProductionDrillItem(
          id = "prod_asv_3",
          instructionFr = "Accordez avec la priorité de personne correcte :",
          instructionEn = "Agree with the correct person priority:",
          basePrompt = "Toi et lui _____ (devoir) apprendre à mieux communiquer.",
          targetAnswer = "devez",
          acceptedAnswers = listOf("devez"),
          hint = "2e personne + 3e personne -> priorité à la 2e personne du pluriel (vous).",
          explanation = "'Toi' (2e pers.) et 'lui' (3e pers.) combinés donnent la priorité à la 2e personne du pluriel : vous devez."
        )
      ),
      spotErrorDrills = listOf(
        SpotErrorDrillItem(
          id = "spot_asv_1",
          instructionFr = "Touchez l'erreur d'accord :",
          passage = "La plupart des employés est en télétravail cette semaine.",
          tokens = listOf("La", "plupart", "des", "employés", "est", "en", "télétravail", "cette", "semaine."),
          errorTokenIndex = 4,
          errorWord = "est",
          correction = "sont",
          ruleExplanation = "'La plupart des' + nom pluriel entraîne l'accord pluriel : 'sont en télétravail', pas le singulier 'est'."
        ),
        SpotErrorDrillItem(
          id = "spot_asv_2",
          instructionFr = "Trouvez l'erreur de priorité de personne :",
          passage = "Toi et moi partira ensemble en vacances cet été.",
          tokens = listOf("Toi", "et", "moi", "partira", "ensemble", "en", "vacances", "cet", "été."),
          errorTokenIndex = 3,
          errorWord = "partira",
          correction = "partirons",
          ruleExplanation = "'Toi et moi' combine 2e et 1re personne : la priorité va à 'nous', donc 'partirons', pas 'partira' (3e personne)."
        )
      )
    )
  )

  fun getAllRules(): List<StructuredRule> = RULES

  fun getRuleById(id: String): StructuredRule? = RULES.find { it.id == id }

  fun getRulesByLevel(level: String): List<StructuredRule> {
    if (level.equals("TOUS", ignoreCase = true)) return RULES
    return RULES.filter { it.level.equals(level, ignoreCase = true) }
  }

  fun getRulesByPillar(pillar: GrammarPillar): List<StructuredRule> =
    RULES.filter { it.pillar == pillar }

  /** All rules sharing a Mental Model Triad / contrast group with [rule], excluding itself. */
  fun getContrastGroup(rule: StructuredRule): List<StructuredRule> {
    val groupId = rule.contrastGroupId ?: return emptyList()
    return RULES.filter { it.id != rule.id && it.contrastGroupId == groupId }
  }

  fun searchRules(query: String): List<StructuredRule> {
    val q = query.trim().lowercase()
    if (q.isEmpty()) return RULES
    return RULES.filter {
      it.titleFr.lowercase().contains(q) ||
      it.titleEn.lowercase().contains(q) ||
      it.summaryFr.lowercase().contains(q) ||
      it.categoryName.lowercase().contains(q)
    }
  }
}
