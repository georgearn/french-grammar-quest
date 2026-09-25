package com.example.data.prefs

import android.content.Context
import com.example.ui.theme.ThemeMode

/** The two ways of using the app; chosen once at onboarding and editable in Settings. */
enum class StartMode { EXPLORE, TRAIN }

/** Which drill type the Training path runs. */
enum class DrillMode { PRODUCTION, SPOT_ERROR }

/**
 * Small persisted settings and "where was I" state, kept in SharedPreferences.
 *
 * The theme and API-key files keep their historical names so existing installs keep their values.
 */
class AppPreferences(context: Context) {

  private val app = context.applicationContext
  private val themePrefs = app.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
  private val apiKeyPrefs = app.getSharedPreferences("gemini_api_prefs", Context.MODE_PRIVATE)
  private val prefs = app.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

  var themeMode: ThemeMode
    get() = runCatching {
      ThemeMode.valueOf(themePrefs.getString(KEY_THEME, ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name)
    }.getOrDefault(ThemeMode.SYSTEM)
    set(value) = themePrefs.edit().putString(KEY_THEME, value.name).apply()

  var geminiApiKey: String
    get() = apiKeyPrefs.getString(KEY_API_KEY, "") ?: ""
    set(value) = apiKeyPrefs.edit().putString(KEY_API_KEY, value).apply()

  var onboardingDone: Boolean
    get() = prefs.getBoolean(KEY_ONBOARDING_DONE, false)
    set(value) = prefs.edit().putBoolean(KEY_ONBOARDING_DONE, value).apply()

  var startMode: StartMode
    get() = enumOrDefault(prefs.getString(KEY_START_MODE, null), StartMode.EXPLORE)
    set(value) = prefs.edit().putString(KEY_START_MODE, value.name).apply()

  var exploreRuleId: String?
    get() = prefs.getString(KEY_EXPLORE_RULE, null)
    set(value) = prefs.edit().putString(KEY_EXPLORE_RULE, value).apply()

  var trainRuleId: String?
    get() = prefs.getString(KEY_TRAIN_RULE, null)
    set(value) = prefs.edit().putString(KEY_TRAIN_RULE, value).apply()

  var drillMode: DrillMode
    get() = enumOrDefault(prefs.getString(KEY_DRILL_MODE, null), DrillMode.PRODUCTION)
    set(value) = prefs.edit().putString(KEY_DRILL_MODE, value.name).apply()

  /** CEFR level filter shared by the rule pickers and the Index; null means all levels. */
  var levelFilter: String?
    get() = prefs.getString(KEY_LEVEL_FILTER, null)
    set(value) = prefs.edit().putString(KEY_LEVEL_FILTER, value).apply()

  /** Rule the grammar map is focused on (shows its same-family neighbours). */
  var mapFocusRuleId: String?
    get() = prefs.getString(KEY_MAP_FOCUS, null)
    set(value) = prefs.edit().putString(KEY_MAP_FOCUS, value).apply()

  /** Whether rules trained before the map existed were already added to it. */
  var mapSeeded: Boolean
    get() = prefs.getBoolean(KEY_MAP_SEEDED, false)
    set(value) = prefs.edit().putBoolean(KEY_MAP_SEEDED, value).apply()

  private inline fun <reified E : Enum<E>> enumOrDefault(raw: String?, default: E): E =
    raw?.let { name -> enumValues<E>().firstOrNull { it.name == name } } ?: default

  private companion object {
    const val KEY_THEME = "saved_theme_mode"
    const val KEY_API_KEY = "gemini_api_key"
    const val KEY_ONBOARDING_DONE = "onboarding_done"
    const val KEY_START_MODE = "start_mode"
    const val KEY_EXPLORE_RULE = "explore_rule_id"
    const val KEY_TRAIN_RULE = "train_rule_id"
    const val KEY_DRILL_MODE = "drill_mode"
    const val KEY_LEVEL_FILTER = "level_filter"
    const val KEY_MAP_FOCUS = "map_focus_rule_id"
    const val KEY_MAP_SEEDED = "map_seeded"
  }
}
