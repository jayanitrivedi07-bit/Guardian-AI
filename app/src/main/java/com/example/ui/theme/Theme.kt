package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GuardianSoftPinkScheme = lightColorScheme(
  primary = SoftPink,
  onPrimary = Color.White,
  primaryContainer = LightPinkSurface,
  onPrimaryContainer = CharcoalPrimary,
  secondary = PaleBlush,
  onSecondary = CharcoalPrimary,
  secondaryContainer = LightPinkSurface,
  onSecondaryContainer = CharcoalPrimary,
  tertiary = WarningAmber,
  onTertiary = Color.White,
  background = WarmBackground,
  onBackground = CharcoalPrimary,
  surface = WarmSurface,
  onSurface = CharcoalPrimary,
  surfaceVariant = PaleBlush,
  onSurfaceVariant = CharcoalSecondary,
  error = EmergencyRed,
  onError = Color.White
)

@Composable
fun GuardianAiTheme(content: @Composable () -> Unit) {
  MaterialTheme(
    colorScheme = GuardianSoftPinkScheme,
    typography = Typography,
    content = content
  )
}
