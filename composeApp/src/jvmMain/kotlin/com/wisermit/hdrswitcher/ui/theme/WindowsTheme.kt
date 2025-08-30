package com.wisermit.hdrswitcher.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object Theme {
    const val DISABLED_OPACITY = 0.38f
    val OUTLINE_WIDTH = 0.5.dp
    val BUTTON_HEIGHT = 32.dp
    val TEXT_BASELINE_PADDING = 6.dp
}

// Switch = outline: 8C8C8C, thumb: (off: 5E5E5E, on: White)
private val brand = Color(0xFF0067C0)
private val brandForeground = Color.White
private val error = Color(0xFFC42B1C)
private val layer1 = Color(0xFFF3F3F3)
private val layer2 = Color(0xFFF9F9F9)
private val layer3 = Color(0xFFFBFBFB)
private val layer4 = Color(0xFFFDFDFD)
//private val layer5 =
private val layerForeground = Color(0xFF1B1B1B)
private val layerForegroundVariant = Color(0xFF606060)
private val outline = Color(0xFF8C8C8C)
private val outlineVariant = Color(0xFFD6D6D6)

// Switch = outline: A0A0A0, thumb: (off: #CFCFCF, on: Black)
private val brandDark = Color(0xFF4CC2FF)
private val onBrandDark = Color(0xFF08151B)
private val errorDark = Color(0xFFE81123)
private val layer1Dark = Color(0xFF202020)
private val layer2Dark = Color(0xFF272727)
private val layer3Dark = Color(0xFF2B2B2B)
private val layer4Dark = Color(0xFF323232)
private val layer5Dark = Color(0xFF373737)
private val layerForegroundDark = Color.White
private val layerForegroundVariantDark = Color(0xFFD0D0D0)
private val outlineDark = Color(0xFFA0A0A0)
private val outlineVariantDark = Color(0xFF383838)

fun colorScheme(darkTheme: Boolean) = when {
    darkTheme -> darkColorScheme(
        primary = brandDark,
        onPrimary = onBrandDark,
        error = errorDark,
        background = layer1Dark,
        surface = layer3Dark,
        onSurface = layerForegroundDark,
        onSurfaceVariant = layerForegroundVariantDark,
        surfaceBright = layer5Dark,
        surfaceContainerLowest = layer2Dark,
        surfaceContainerHighest = layer4Dark,
        outline = outlineDark,
        outlineVariant = outlineVariantDark,
    )
    else -> lightColorScheme(
        primary = brand,
        onPrimary = brandForeground,
        error = error,
        background = layer1,
        surface = layer3,
        onSurface = layerForeground,
        onSurfaceVariant = layerForegroundVariant,
        surfaceContainerLowest = layer2,
        surfaceContainerHighest = layer4,
        outline = outline,
        outlineVariant = outlineVariant,
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