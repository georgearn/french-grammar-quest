package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AppThemeColors
import com.example.ui.theme.ThemeMode

@Composable
fun TopStatusBar(
  themeMode: ThemeMode = ThemeMode.DARK,
  onCycleTheme: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 3.dp,
    shadowElevation = 2.dp
  ) {
    Column {
      // Scrim strip directly under the status bar: on the Light theme, system clock/icons
      // are drawn white by the OS and become unreadable over a light background. A slightly
      // darker band exactly the height of the status bar keeps them legible without having
      // to fight the platform's status-bar icon-color API per theme.
      if (themeMode == ThemeMode.LIGHT) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .windowInsetsTopHeight(WindowInsets.statusBars)
            .background(Color.Black.copy(alpha = 0.35f))
        )
      } else {
        Box(modifier = Modifier.fillMaxWidth().windowInsetsTopHeight(WindowInsets.statusBars))
      }

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // App Identity
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .weight(1f)
          .padding(end = 8.dp)
          .testTag("app_identity_header")
      ) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(AppThemeColors.primaryContainer),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.AutoStories,
            contentDescription = null,
            tint = AppThemeColors.onPrimaryContainer,
            modifier = Modifier.size(20.dp)
          )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f, fill = false)) {
          Text(
            text = "Grammar Study Tool",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
          Text(
            text = "Grammaire française",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }

      // Theme Switcher Button replacing unclickable badges
      Surface(
        onClick = onCycleTheme,
        shape = RoundedCornerShape(20.dp),
        color = when (themeMode) {
          ThemeMode.LIGHT -> Color(0xFFFEF3C7)
          ThemeMode.DARK -> MaterialTheme.colorScheme.surfaceVariant
        },
        modifier = Modifier.testTag("theme_toggle_button")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = when (themeMode) {
              ThemeMode.LIGHT -> Icons.Default.LightMode
              ThemeMode.DARK -> Icons.Default.DarkMode
            },
            contentDescription = "Thème: ${themeMode.title}",
            tint = when (themeMode) {
              ThemeMode.LIGHT -> Color(0xFFD97706)
              ThemeMode.DARK -> Color(0xFF93C5FD)
            },
            modifier = Modifier.size(15.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = themeMode.title,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold,
            color = when (themeMode) {
              ThemeMode.LIGHT -> Color(0xFF78350F)
              ThemeMode.DARK -> MaterialTheme.colorScheme.onSurfaceVariant
            },
            maxLines = 1,
            softWrap = false
          )
        }
      }
    }
    }
  }
}

