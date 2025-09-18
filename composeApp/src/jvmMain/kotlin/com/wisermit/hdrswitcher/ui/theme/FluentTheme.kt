package com.wisermit.hdrswitcher.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp

object ThemeDefaults {
    const val DISABLED_OPACITY = 0.38f
    val MINIMUM_INTERACTIVE_COMPONENT_SIZE = 44.dp
    val BORDER_STROKE_WIDTH = 0.5.dp

    // Fix for incorrect horizontal alignment.
    val TEXT_BASELINE_BOTTOM_FIX = 6.dp
}

private fun colorScheme(darkTheme: Boolean) = when {
    darkTheme -> darkColorScheme(
        primary = brandDark,
        onPrimary = onBrandDark,
        error = errorDark,
        background = layer1Dark,
        onBackground = layerForegroundDark,
        surface = layer3Dark,
        onSurface = layerForegroundDark,
        onSurfaceVariant = layerForegroundVariantDark,
        surfaceBright = layer5Dark,
        surfaceContainerLowest = layer2Dark,
        surfaceContainer = layer3Dark,
        surfaceContainerHighest = layer4Dark,
        outline = outlineDark,
        outlineVariant = outlineVariantDark,
    )
    else -> lightColorScheme(
        primary = brand,
        onPrimary = brandForeground,
        error = error,
        background = layer1,
        onBackground = layerForeground,
        surface = layer3,
        onSurface = layerForeground,
        onSurfaceVariant = layerForegroundVariant,
        surfaceContainerLowest = layer2,
        surfaceContainer = layer3,
        surfaceContainerHighest = layer4,
        outline = outline,
        outlineVariant = outlineVariant,
    )
}

@Composable
fun FluentTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentSize provides
                ThemeDefaults.MINIMUM_INTERACTIVE_COMPONENT_SIZE
    ) {
        MaterialTheme(
            colorScheme = colorScheme(isDarkTheme),
            content = content,
        )
    }
}