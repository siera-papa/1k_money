package com.project1k.money.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkScheme = darkColorScheme(
    primary = Color(0xFFFF4000),
    onPrimary = Color(0xFF21124D),
    primaryContainer = Color(0xFF342A49),
    onPrimaryContainer = Color(0xFFF0E9FF),
    inversePrimary = Color(0xFFFF4000),
    // Preserve the original elevated backgrounds instead of tinting them with the accent.
    surfaceTint = Color(0xFFB9A7FF),
    secondary = Color(0xFF8FD8E5),
    secondaryContainer = Color(0xFF15383F),
    onSecondaryContainer = Color(0xFFD7F7FC),
    tertiary = Color(0xFF9ED7B6),
    tertiaryContainer = Color(0xFF1C3A2A),
    onTertiaryContainer = Color(0xFFDDF7E7),
    background = Color(0xFF101114),
    onBackground = Color(0xFFE9E9EE),
    surface = Color(0xFF15161A),
    onSurface = Color(0xFFE9E9EE),
    surfaceVariant = Color(0xFF25262C),
    onSurfaceVariant = Color(0xFFC8C7CF),
    outline = Color(0xFF777780)
)

@Composable
fun Project1KMoneyTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = DarkScheme, content = content)
}
