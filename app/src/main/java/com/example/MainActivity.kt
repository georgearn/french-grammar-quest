package com.example

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.GrammarViewModel
import com.example.ui.i18n.ProvideAppLanguage
import com.example.ui.navigation.FrenchGrammarQuestApp
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.isDark

class MainActivity : ComponentActivity() {

  private val viewModel: GrammarViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
      val language by viewModel.language.collectAsStateWithLifecycle()
      val darkTheme = themeMode.isDark()

      // Status/navigation bar icons follow the app theme (not only the system one), so the clock
      // and icons stay readable when the user forces Light or Dark.
      DisposableEffect(darkTheme) {
        enableEdgeToEdge(
          statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { darkTheme },
          navigationBarStyle = SystemBarStyle.auto(LIGHT_SCRIM, DARK_SCRIM) { darkTheme }
        )
        onDispose {}
      }

      MyApplicationTheme(themeMode = themeMode) {
        ProvideAppLanguage(language) {
          FrenchGrammarQuestApp(viewModel = viewModel)
        }
      }
    }
  }

  private companion object {
    // Same scrims as the androidx.activity defaults for button navigation.
    val LIGHT_SCRIM = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
    val DARK_SCRIM = Color.argb(0x80, 0x1b, 0x1b, 0x1b)
  }
}
