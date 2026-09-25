package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

/** User's theme preference. [SYSTEM] follows the device's dark-mode setting. */
enum class ThemeMode {
  SYSTEM,
  LIGHT,
  DARK
}

/** The resolved theme actually applied: always [ThemeMode.LIGHT] or [ThemeMode.DARK], never SYSTEM. */
val LocalThemeMode = staticCompositionLocalOf { ThemeMode.DARK }

@Composable
fun ThemeMode.isDark(): Boolean = when (this) {
  ThemeMode.SYSTEM -> isSystemInDarkTheme()
  ThemeMode.LIGHT -> false
  ThemeMode.DARK -> true
}

private val DarkColorScheme = darkColorScheme(
  primary = DarkPrimary,
  onPrimary = DarkOnPrimary,
  primaryContainer = DarkPrimaryContainer,
  onPrimaryContainer = DarkOnPrimaryContainer,
  secondary = DarkSecondary,
  onSecondary = DarkOnSecondary,
  secondaryContainer = DarkSecondaryContainer,
  onSecondaryContainer = DarkOnSecondaryContainer,
  tertiary = DarkTertiary,
  onTertiary = DarkOnTertiary,
  tertiaryContainer = DarkTertiaryContainer,
  onTertiaryContainer = DarkOnTertiaryContainer,
  background = DarkBackground,
  onBackground = DarkOnBackground,
  surface = DarkSurface,
  onSurface = DarkOnSurface,
  surfaceVariant = DarkSurfaceVariant,
  onSurfaceVariant = DarkOnSurfaceVariant,
  outline = DarkOutline,
  outlineVariant = DarkOutline,
  surfaceContainerLowest = DarkBackground,
  surfaceContainerLow = Color(0xFF111A2C),
  surfaceContainer = DarkSurface,
  surfaceContainerHigh = DarkSurfaceVariant,
  surfaceContainerHighest = Color(0xFF273449)
)

private val LightColorScheme = lightColorScheme(
  primary = LightPrimary,
  onPrimary = LightOnPrimary,
  primaryContainer = LightPrimaryContainer,
  onPrimaryContainer = LightOnPrimaryContainer,
  secondary = LightSecondary,
  onSecondary = LightOnSecondary,
  secondaryContainer = LightSecondaryContainer,
  onSecondaryContainer = LightOnSecondaryContainer,
  tertiary = LightTertiary,
  onTertiary = LightOnTertiary,
  tertiaryContainer = LightTertiaryContainer,
  onTertiaryContainer = LightOnTertiaryContainer,
  background = LightBackground,
  onBackground = LightOnBackground,
  surface = LightSurface,
  onSurface = LightOnSurface,
  surfaceVariant = LightSurfaceVariant,
  onSurfaceVariant = LightOnSurfaceVariant,
  outline = LightOutline,
  outlineVariant = LightOutline,
  surfaceContainerLowest = LightSurface,
  surfaceContainerLow = LightBackground,
  surfaceContainer = LightSurfaceVariant,
  surfaceContainerHigh = Color(0xFFEEF2F6),
  surfaceContainerHighest = LightOutline
)

@Composable
fun MyApplicationTheme(
  themeMode: ThemeMode = ThemeMode.SYSTEM,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit
) {
  val resolvedMode = if (themeMode.isDark()) ThemeMode.DARK else ThemeMode.LIGHT
  val colorScheme = when (resolvedMode) {
    ThemeMode.LIGHT -> {
      if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        dynamicLightColorScheme(LocalContext.current)
      } else {
        LightColorScheme
      }
    }
    else -> {
      if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        dynamicDarkColorScheme(LocalContext.current)
      } else {
        DarkColorScheme
      }
    }
  }

  CompositionLocalProvider(LocalThemeMode provides resolvedMode) {
    MaterialTheme(
      colorScheme = colorScheme,
      typography = Typography,
      content = content
    )
  }
}
