package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

// French Grammar Quest Harmonious Palette

// 1. Primary French Saphir (Accessible Royal Blue)
val FrenchBlue = Color(0xFF2563EB)
val FrenchBlueDark = Color(0xFF1E3A8A)
val FrenchBlueLight = Color(0xFFDBEAFE)

// 2. Secondary French Rose/Crimson
val FrenchRed = Color(0xFFE11D48)
val FrenchRedDark = Color(0xFF9F1239)
val FrenchRedLight = Color(0xFFFFE4E6)

// 3. Soleil Gold (Star & Gems)
val GoldStar = Color(0xFFF59E0B)
val GoldStarLight = Color(0xFFFEF3C7)

// 4. French Emerald (Success & Mastery)
val EmeraldSuccess = Color(0xFF10B981)
val EmeraldSuccessLight = Color(0xFFD1FAE5)

// 5. Flambeau Orange (Streaks & Fast combos)
val FlameOrange = Color(0xFFEA580C)

// Theme Specific Tokens for High Contrast & WCAG Readability
val FrenchRedDarkTheme = Color(0xFFFDA4AF)
val FrenchRedContainerDark = Color(0xFF4C0519)
val FrenchRedContainerLight = Color(0xFFFFE4E6)
val FrenchRedOnContainerLight = Color(0xFF9F1239)
val FrenchRedOnContainerDark = Color(0xFFFFE4E6)

val EmeraldSuccessLightAccessible = Color(0xFF059669)
val EmeraldSuccessDarkTheme = Color(0xFF34D399)
val EmeraldSuccessContainerLight = Color(0xFFD1FAE5)
val EmeraldSuccessContainerDark = Color(0xFF064E3B)
val EmeraldSuccessOnContainerLight = Color(0xFF065F46)
val EmeraldSuccessOnContainerDark = Color(0xFFD1FAE5)

val GoldStarLightAccessible = Color(0xFFD97706)
val GoldStarDarkTheme = Color(0xFFFBBF24)
val GoldStarContainerLight = Color(0xFFFEF3C7)
val GoldStarContainerDark = Color(0xFF78350F)
val GoldStarOnContainerLight = Color(0xFF78350F)
val GoldStarOnContainerDark = Color(0xFFFEF3C7)

val FlameOrangeLightAccessible = Color(0xFFC2410C)
val FlameOrangeDarkTheme = Color(0xFFFB923C)
val FlameOrangeContainerLight = Color(0xFFFFEDD5)
val FlameOrangeContainerDark = Color(0xFF7C2D12)
val FlameOrangeOnContainerLight = Color(0xFF7C2D12)
val FlameOrangeOnContainerDark = Color(0xFFFFEDD5)

// Material 3 Light Color Scheme
val LightPrimary = Color(0xFF1D4ED8)
val LightOnPrimary = Color(0xFFFFFFFF)
val LightPrimaryContainer = Color(0xFFEFF6FF)
val LightOnPrimaryContainer = Color(0xFF1E40AF)

val LightSecondary = Color(0xFFE11D48)
val LightOnSecondary = Color(0xFFFFFFFF)
val LightSecondaryContainer = Color(0xFFFFF1F2)
val LightOnSecondaryContainer = Color(0xFF9F1239)

val LightTertiary = Color(0xFFD97706)
val LightOnTertiary = Color(0xFFFFFFFF)
val LightTertiaryContainer = Color(0xFFFEF3C7)
val LightOnTertiaryContainer = Color(0xFF78350F)

val LightBackground = Color(0xFFF8FAFC)
val LightOnBackground = Color(0xFF0F172A)
val LightSurface = Color(0xFFFFFFFF)
val LightOnSurface = Color(0xFF0F172A)
val LightSurfaceVariant = Color(0xFFF1F5F9)
val LightOnSurfaceVariant = Color(0xFF475569)
val LightOutline = Color(0xFFE2E8F0)

// Material 3 Dark Color Scheme (High-Contrast for pristine readability)
val DarkPrimary = Color(0xFF93C5FD)
val DarkOnPrimary = Color(0xFF0F172A)
val DarkPrimaryContainer = Color(0xFF1E3A8A)
val DarkOnPrimaryContainer = Color(0xFFDBEAFE)

val DarkSecondary = Color(0xFFFDA4AF)
val DarkOnSecondary = Color(0xFF4C0519)
val DarkSecondaryContainer = Color(0xFF881337)
val DarkOnSecondaryContainer = Color(0xFFFFE4E6)

val DarkTertiary = Color(0xFFFBBF24)
val DarkOnTertiary = Color(0xFF451A03)
val DarkTertiaryContainer = Color(0xFF78350F)
val DarkOnTertiaryContainer = Color(0xFFFEF3C7)

val DarkBackground = Color(0xFF0B1120)
val DarkOnBackground = Color(0xFFF8FAFC)
val DarkSurface = Color(0xFF131D31)
val DarkOnSurface = Color(0xFFF8FAFC)
val DarkSurfaceVariant = Color(0xFF1E293B)
val DarkOnSurfaceVariant = Color(0xFF94A3B8)
val DarkOutline = Color(0xFF334155)

/**
 * Semantic theme-aware colors that adapt between Light and Dark themes
 * providing guaranteed contrast and visual harmony.
 */
object AppThemeColors {
  val primary: Color
    @Composable
    @ReadOnlyComposable
    get() = when (LocalThemeMode.current) {
      ThemeMode.LIGHT -> LightPrimary
      ThemeMode.DARK -> DarkPrimary
    }

  val primaryContainer: Color
    @Composable
    @ReadOnlyComposable
    get() = when (LocalThemeMode.current) {
      ThemeMode.LIGHT -> LightPrimaryContainer
      ThemeMode.DARK -> DarkPrimaryContainer
    }

  val onPrimaryContainer: Color
    @Composable
    @ReadOnlyComposable
    get() = when (LocalThemeMode.current) {
      ThemeMode.LIGHT -> LightOnPrimaryContainer
      ThemeMode.DARK -> DarkOnPrimaryContainer
    }

  val red: Color
    @Composable
    @ReadOnlyComposable
    get() = when (LocalThemeMode.current) {
      ThemeMode.LIGHT -> FrenchRed
      ThemeMode.DARK -> FrenchRedDarkTheme
    }

  val redContainer: Color
    @Composable
    @ReadOnlyComposable
    get() = when (LocalThemeMode.current) {
      ThemeMode.LIGHT -> FrenchRedContainerLight
      ThemeMode.DARK -> FrenchRedContainerDark
    }

  val onRedContainer: Color
    @Composable
    @ReadOnlyComposable
    get() = when (LocalThemeMode.current) {
      ThemeMode.LIGHT -> FrenchRedOnContainerLight
      ThemeMode.DARK -> FrenchRedOnContainerDark
    }

  val success: Color
    @Composable
    @ReadOnlyComposable
    get() = when (LocalThemeMode.current) {
      ThemeMode.LIGHT -> EmeraldSuccessLightAccessible
      ThemeMode.DARK -> EmeraldSuccessDarkTheme
    }

  val successContainer: Color
    @Composable
    @ReadOnlyComposable
    get() = when (LocalThemeMode.current) {
      ThemeMode.LIGHT -> EmeraldSuccessContainerLight
      ThemeMode.DARK -> EmeraldSuccessContainerDark
    }

  val onSuccessContainer: Color
    @Composable
    @ReadOnlyComposable
    get() = when (LocalThemeMode.current) {
      ThemeMode.LIGHT -> EmeraldSuccessOnContainerLight
      ThemeMode.DARK -> EmeraldSuccessOnContainerDark
    }

  val gold: Color
    @Composable
    @ReadOnlyComposable
    get() = when (LocalThemeMode.current) {
      ThemeMode.LIGHT -> GoldStarLightAccessible
      ThemeMode.DARK -> GoldStarDarkTheme
    }

  val goldContainer: Color
    @Composable
    @ReadOnlyComposable
    get() = when (LocalThemeMode.current) {
      ThemeMode.LIGHT -> GoldStarContainerLight
      ThemeMode.DARK -> GoldStarContainerDark
    }

  val onGoldContainer: Color
    @Composable
    @ReadOnlyComposable
    get() = when (LocalThemeMode.current) {
      ThemeMode.LIGHT -> GoldStarOnContainerLight
      ThemeMode.DARK -> GoldStarOnContainerDark
    }

  val flame: Color
    @Composable
    @ReadOnlyComposable
    get() = when (LocalThemeMode.current) {
      ThemeMode.LIGHT -> FlameOrangeLightAccessible
      ThemeMode.DARK -> FlameOrangeDarkTheme
    }

  val flameContainer: Color
    @Composable
    @ReadOnlyComposable
    get() = when (LocalThemeMode.current) {
      ThemeMode.LIGHT -> FlameOrangeContainerLight
      ThemeMode.DARK -> FlameOrangeContainerDark
    }

  val onFlameContainer: Color
    @Composable
    @ReadOnlyComposable
    get() = when (LocalThemeMode.current) {
      ThemeMode.LIGHT -> FlameOrangeOnContainerLight
      ThemeMode.DARK -> FlameOrangeOnContainerDark
    }
}
