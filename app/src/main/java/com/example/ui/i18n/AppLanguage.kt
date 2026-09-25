package com.example.ui.i18n

import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

/**
 * The app is French-first. [EN] is an on-demand help mode: the interface strings switch to
 * `values-en` and rule content shows its English explanations/translations.
 */
enum class AppLanguage(val locale: Locale) {
  FR(Locale.FRENCH),
  EN(Locale.ENGLISH)
}

val LocalAppLanguage = staticCompositionLocalOf { AppLanguage.FR }

/**
 * Applies [language] to everything below it: `stringResource` resolves against resources whose
 * locale is forced to [AppLanguage.locale], whatever the device language is. The context stays a
 * wrapper around the Activity, so starting activities (share sheet, links) keeps working.
 */
@Composable
fun ProvideAppLanguage(language: AppLanguage, content: @Composable () -> Unit) {
  val baseContext = LocalContext.current
  val baseConfiguration = LocalConfiguration.current
  val localizedConfiguration = remember(baseConfiguration, language) {
    Configuration(baseConfiguration).apply { setLocale(language.locale) }
  }
  val localizedContext = remember(baseContext, localizedConfiguration) {
    val localizedResources = baseContext.createConfigurationContext(localizedConfiguration).resources
    object : ContextWrapper(baseContext) {
      override fun getResources(): Resources = localizedResources
    }
  }
  CompositionLocalProvider(
    LocalAppLanguage provides language,
    LocalContext provides localizedContext,
    LocalConfiguration provides localizedConfiguration,
    content = content
  )
}

/** True when the learner asked for English help. */
val isEnglish: Boolean
  @Composable
  @ReadOnlyComposable
  get() = LocalAppLanguage.current == AppLanguage.EN

/** Picks the English variant of a content field when English help is on and one exists. */
@Composable
@ReadOnlyComposable
fun localized(fr: String, en: String?): String =
  if (isEnglish && !en.isNullOrBlank()) en else fr
