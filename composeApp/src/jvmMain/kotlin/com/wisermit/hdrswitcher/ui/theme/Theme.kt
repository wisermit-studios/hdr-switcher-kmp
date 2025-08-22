package com.wisermit.hdrswitcher.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Win11Blue = Color(0xFF0078D4)
val Win11BlueDark = Color(0xFF0063B1)
val Win11Gray = Color(0xFF2B2B2B)
val Win11GrayLight = Color(0xFFF3F3F3)
val Win11Accent = Color(0xFF005A9E)
val Win11Red = Color(0xFFE81123)
val Win11BackgroundDark = Color(0xFF1E1E1E)
val Win11SurfaceVariantLight = Color(0xFFE5E5E5)
val Win11SurfaceVariantDark = Color(0xFF808080)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content,
    )
}