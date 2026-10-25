package com.komikta.app.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val KomiktaColors = darkColorScheme(
    primary = Color(0xFFE63846),
    onPrimary = Color(0xFFFFFFFF),
    background = Color(0xFF080808),
    onBackground = Color(0xFFF5F5F7),
    surface = Color(0xFF141414),
    onSurface = Color(0xFFF5F5F7),
    surfaceVariant = Color(0xFF202024),
    onSurfaceVariant = Color(0xFFA1A1AA),
    outline = Color(0xFF29292F)
)

@Composable
fun KomiktaTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = KomiktaColors, content = content)
}
