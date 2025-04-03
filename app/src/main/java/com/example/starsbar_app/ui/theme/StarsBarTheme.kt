package com.example.starsbar_app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = ButtonBlue,
    onPrimary = White,
    secondary = Yellow,
    onSecondary = ColorPrimaryDark,
    background = ColorPrimary,
    onBackground = White,
    surface = ColorPrimaryDark,
    onSurface = White,
    error = Color(0xFFFF3B30),
    onError = White
)

private val LightColorScheme = lightColorScheme(
    primary = ButtonBlue,
    onPrimary = White,
    secondary = Yellow,
    onSecondary = ColorPrimaryDark,
    background = White,
    onBackground = ColorPrimary,
    surface = Color(0xFFF5F5F5),
    onSurface = ColorPrimary,
    error = Color(0xFFFF3B30),
    onError = White
)

@Composable
fun StarsBarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}