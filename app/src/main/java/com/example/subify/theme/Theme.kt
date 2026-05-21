package com.example.subify.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val SubifyColorScheme = darkColorScheme(
    primary = NeonPurple,
    secondary = NeonCyan,
    tertiary = NeonEmerald,
    background = DeepDarkNavy,
    surface = GlassSurface,
    onPrimary = TextPrimary,
    onSecondary = DarkBackground,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun SubifyTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SubifyColorScheme,
        typography = Typography,
        content = content
    )
}
