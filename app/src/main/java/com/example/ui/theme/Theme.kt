package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val AuraColorScheme = darkColorScheme(
    primary = AuraPurplePrimary,
    onPrimary = AuraWhite,
    primaryContainer = AuraPurpleDark,
    onPrimaryContainer = AuraPurpleLight,
    secondary = AuraCyanSecondary,
    onSecondary = AuraDeepNavy,
    secondaryContainer = AuraElevatedSurface,
    onSecondaryContainer = AuraCyanLight,
    tertiary = AuraPinkAccent,
    onTertiary = AuraWhite,
    background = AuraDeepNavy,
    onBackground = AuraSilver,
    surface = AuraDarkSurface,
    onSurface = AuraSilver,
    surfaceVariant = AuraCardSurface,
    onSurfaceVariant = AuraMutedSilver,
    outline = AuraBorderGlow
)

@Composable
fun AuraverseTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = AuraDeepNavy.toArgb()
                window.navigationBarColor = AuraDeepNavy.toArgb()
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = false
                controller.isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = AuraColorScheme,
        typography = Typography,
        content = content
    )
}
