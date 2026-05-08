package com.example.mindfuel.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = Primary.copy(alpha = 0.1f),
    secondary = Secondary,
    onSecondary = Color.White,
    background = Background,
    surface = Color.White,
    onSurface = TextPrimary,
    onBackground = TextPrimary,
)

@Composable
fun MindFuelTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // We are focusing on light theme for now. 
    // If you want to support dark theme, you would define a DarkColorScheme and use:
    // val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
