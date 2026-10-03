package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LighterYellowColorScheme = darkColorScheme(
    primary = LightYellowMain,
    onPrimary = TextOnYellow,
    primaryContainer = LightYellowWarm,
    onPrimaryContainer = TextOnYellow,
    secondary = CyberBlue,
    onSecondary = TextOnYellow,
    secondaryContainer = SpaceCardElevated,
    onSecondaryContainer = NeonCyanLight,
    tertiary = NayzakOrange,
    onTertiary = TextPrimary,
    background = SpaceBackground,
    onBackground = TextOnYellow,
    surface = SpaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SpaceCardElevated,
    onSurfaceVariant = TextSecondary,
    outline = SpaceBorder,
    error = GameWrong,
    onError = TextPrimary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = LighterYellowColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = LightYellowWarm.toArgb()
                window.navigationBarColor = LightYellowWarm.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
