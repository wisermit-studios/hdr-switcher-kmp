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
    val MinimumInteractiveComponentSize = 44.dp
    val BorderStrokeWidth = 1.dp
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
        surfaceContainerLowest = layer2Dark,
        surfaceContainer = layer3Dark,
        surfaceContainerHighest = layer4Dark,
        surfaceBright = layer5Dark,
        surfaceDim = outlineShadowDark,
        outline = outline1Dark,
        outlineVariant = outline2Dark,
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
        surfaceBright = layer5,
        surfaceDim = outlineShadow,
        outline = outline1,
        outlineVariant = outline2,
    )
}

@Composable
fun FluentTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentSize provides
                ThemeDefaults.MinimumInteractiveComponentSize
    ) {
        MaterialTheme(
            colorScheme = colorScheme(isDarkTheme),
            content = content,
        )
    }
}