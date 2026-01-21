package com.smartsorovnoma.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// SmartSorovnoma Light Theme - Turquoise & Gold
private val LightColorScheme = lightColorScheme(
    // Primary - Turquoise
    primary = Turquoise500,
    onPrimary = White,
    primaryContainer = Turquoise100,
    onPrimaryContainer = Turquoise900,
    
    // Secondary - Gold
    secondary = Gold500,
    onSecondary = Gray900,
    secondaryContainer = Gold100,
    onSecondaryContainer = Gold900,
    
    // Tertiary
    tertiary = Turquoise700,
    onTertiary = White,
    tertiaryContainer = Turquoise50,
    onTertiaryContainer = Turquoise900,
    
    // Error
    error = Error,
    errorContainer = ErrorLight,
    onError = White,
    onErrorContainer = Error,
    
    // Background & Surface
    background = Gray50,
    onBackground = Gray900,
    surface = White,
    onSurface = Gray900,
    surfaceVariant = Gray100,
    onSurfaceVariant = Gray700,
    
    // Outline
    outline = Gray400,
    outlineVariant = Gray200,
)

// SmartSorovnoma Dark Theme
private val DarkColorScheme = darkColorScheme(
    // Primary - Turquoise (lighter for dark theme)
    primary = Turquoise300,
    onPrimary = Turquoise900,
    primaryContainer = Turquoise800,
    onPrimaryContainer = Turquoise100,
    
    // Secondary - Gold
    secondary = Gold300,
    onSecondary = Gray900,
    secondaryContainer = Gold800,
    onSecondaryContainer = Gold100,
    
    // Tertiary
    tertiary = Turquoise400,
    onTertiary = Turquoise900,
    tertiaryContainer = Turquoise700,
    onTertiaryContainer = Turquoise100,
    
    // Error
    error = Color(0xFFFF6B6B),
    errorContainer = Color(0xFF93000A),
    onError = Color(0xFF690005),
    onErrorContainer = Color(0xFFFFDAD6),
    
    // Background & Surface
    background = DarkBackground,
    onBackground = Gray100,
    surface = DarkSurface,
    onSurface = Gray100,
    surfaceVariant = DarkCard,
    onSurfaceVariant = Gray300,
    
    // Outline
    outline = Gray600,
    outlineVariant = Gray700,
)

@Composable
fun SmartSorovnomaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}
