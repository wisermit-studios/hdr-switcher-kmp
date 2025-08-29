package com.wisermit.hdrswitcher.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

const val DISABLED_ALPHA = 0.38f

private val Win11Blue = Color(0xFF0078D4)
private val Win11BlueDark = Color(0xFF0063B1)
private val Win11Gray = Color(0xFF2B2B2B)
private val Win11GrayLight = Color(0xFFF3F3F3)
private val Win11Red = Color(0xFFE81123)
private val Win11Accent = Color(0xFF005A9E)
private val Win11BackgroundDark = Color(0xFF1E1E1E)
private val Win11SurfaceVariantLight = Color(0xFFE5E5E5)
private val Win11SurfaceVariantDark = Color(0xFF808080)

fun colorScheme(darkTheme: Boolean) = when {
    darkTheme -> darkColorScheme(
        primary = Win11BlueDark,
        onPrimary = Color.White,
        secondary = Win11Accent,
        background = Win11BackgroundDark,
        surface = Win11Gray,
        onSurfaceVariant = Win11SurfaceVariantDark,
        error = Win11Red,
    )
    else -> lightColorScheme(
        primary = Win11Blue,
        onPrimary = Color.White,
        secondary = Win11Accent,
        background = Win11GrayLight,
        onSurfaceVariant = Win11SurfaceVariantLight,
        error = Win11Red,
    )
}

@Composable
fun WindowsTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = colorScheme(isDarkTheme),
        content = content,
    )
}