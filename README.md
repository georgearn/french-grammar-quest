# French Grammar Quest

An Android app for learning French grammar, one rule at a time. It is built with Kotlin and Jetpack Compose (Material 3).

The interface is in French. An **EN** button in the top bar switches the interface and the rule explanations to English whenever something is unclear. Tap it again to go back to French. The app always starts in French.

## Features

The app has four tabs. On first launch it asks which mode to open on: *Comprendre* or *S'entraîner*. You can change this later in Settings.

**Comprendre (Learn)**
- Pick a rule from a searchable list with CEFR level filters, or step through rules in order.
- Each rule has an overview, its trigger phrase and a contrasting rule when one exists.
- Collapsible sections show the sentence structure (formula, decision steps, comparison table), examples by register and an annotated text you can listen to.
- "Règles liées" lists related rules and links to the grammar map.

**S'entraîner (Practice)**
- Two drill types: typing the answer (*Saisie*) and finding the wrong word (*Chasse à l'erreur*).
- Answer checking ignores case, apostrophe style, extra spaces and final punctuation. Missing or wrong accents get their own message.
- Accent keys insert at the cursor.
- The end-of-session summary lists your mistakes, lets you replay only those, and links to the next rule.
- The best score for each rule is saved.

**Créer (Create)**
- Gemini writes a French text (about one page) or a two-voice podcast script, built around a chosen rule, CEFR level and theme.
- Podcast scripts can be turned into audio with two natural voices, then exported to *Downloads/FrenchGrammarQuest*.
- Results can be listened to, saved, copied, shared or regenerated. Saved items are listed under "Mes créations".

**Carte (Map)**
- A mind map of the rules you have covered, inspired by the WikiLinks app.
- Explored rules are filled nodes, coloured by family. Mastered rules (best score of 70% or more) have a green ring.
- Rules linked to the ones you explored appear as dashed "ghost" nodes you can open next.
- The **+** button adds any rule, even one that no link leads to.
- Tap a node for its details, pinch to zoom, and long-press to move a node.
- The *List* view shows the same rules as a searchable index.

**Settings:** theme (System, Light, Dark), start screen, and your Gemini API key.

## Run locally

**Prerequisites:** [Android Studio](https://developer.android.com/studio) with JDK 11 or newer. The app targets Android 12 (API 31) and later.

1. In Android Studio, select **Open** and choose this directory. Let Android Studio sync Gradle.
2. Run the `app` configuration on an emulator or a device.
3. To use the **Créer** tab, get a free key at [aistudio.google.com/apikey](https://aistudio.google.com/apikey) and paste it in **Settings → Clé API Gemini** inside the app.

The key is stored only on the device, in SharedPreferences. It is sent only to `generativelanguage.googleapis.com`, and no key is bundled with the app. The `.env` / `GEMINI_API_KEY` setup from the original AI Studio template is not needed.

Release builds are signed with `my-upload-key.jks` at the repository root, or with the file set in `KEYSTORE_PATH`. Pass the passwords in `STORE_PASSWORD` and `KEY_PASSWORD`.

## Tests

```
./gradlew test
```

The unit tests in `app/src/test` cover answer checking (`AnswerCheckerTest`) and the construction and layout of the grammar map (`GrammarMapTest`).

## Project structure

```
app/src/main/java/com/example/
├── MainActivity.kt              Edge-to-edge setup, theme and language
├── data/
│   ├── engine/                  Rule content and pure logic
│   │   ├── GrammarRuleRepository.kt   All grammar rules (StructuredRule)
│   │   ├── RuleGraph.kt               Cross-references between rules
│   │   ├── GrammarMap.kt              Map graph builder and force-directed layout
│   │   ├── AnswerChecker.kt           Typed-answer normalisation
│   │   └── Gemini*Repository.kt       Text generation and text-to-speech calls
│   ├── local/                   Room database (training progress, saved creations, map)
│   ├── prefs/AppPreferences.kt  Settings and "where was I" state
│   └── GrammarRepository.kt
├── ui/
│   ├── GrammarViewModel.kt      Single ViewModel holding all screen state
│   ├── navigation/              NavHost, bottom bar, top bar
│   ├── onboarding/  exploratory/  training/  generation/  map/  screens/  settings/
│   ├── components/              Shared rule picker, level filters, collapsible sections
│   ├── i18n/                    French/English switch
│   └── theme/
└── util/FrenchAudioHelper.kt    Device French text-to-speech
```

Interface text lives in `res/values/strings.xml` (French, the default) and `res/values-en/strings.xml` (English).

### Database

Room database `french_grammar_quest.db`, version 5. Explicit migrations keep user data:
- 3 → 4 drops the tables of the old quest, quiz and speed-drill flow.
- 4 → 5 adds `rule_visit` and `map_node_position` for the grammar map.
