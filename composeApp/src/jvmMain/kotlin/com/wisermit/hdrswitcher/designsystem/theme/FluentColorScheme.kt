package com.wisermit.hdrswitcher.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

internal fun colorScheme(darkTheme: Boolean) = when {
    darkTheme -> with(ColorDarkTokens) {
        darkColorScheme(
            primary = brand,
            onPrimary = onBrand,
            secondary = secondary,
            onSecondary = onSecondary,
            error = error,
            background = layer1,
            onBackground = layerForeground,
            surface = layer3,
            onSurface = layerForeground,
            onSurfaceVariant = layerForegroundVariant,
            surfaceContainerLowest = layer2,
            surfaceContainer = layer3,
            surfaceContainerHigh = layer4,
            surfaceContainerHighest = layer5,
            outline = outline1,
            outlineVariant = outline3,
        )
    }
    else -> with(ColorTokens) {
        lightColorScheme(
            primary = brand,
            onPrimary = brandForeground,
            secondary = secondary,
            onSecondary = onSecondary,
            error = error,
            background = layer1,
            onBackground = layerForeground,
            surface = layer3,
            onSurface = layerForeground,
            onSurfaceVariant = layerForegroundVariant,
            surfaceContainerLowest = layer2,
            surfaceContainer = layer3,
            surfaceContainerHigh = layer4,
            surfaceContainerHighest = layer5,
            outline = outline1,
            outlineVariant = outline3,
        )
    }
}

internal fun fluentColors(darkTheme: Boolean): FluentColors = when {
    darkTheme -> with(ColorDarkTokens) {
        FluentColors(
            dialogSurface = dialogLayer,
            outlineLow = outline2,
            outlineVariant2 = outlineComboBox,
        )
    }
    else -> with(ColorTokens) {
        FluentColors(
            dialogSurface = dialogLayer,
            outlineLow = outline2,
            outlineVariant2 = outlineComboBox,
        )
    }
}

@Immutable
data class FluentColors(
    val dialogSurface: Color,
    val outlineLow: Color,
    val outlineVariant2: Color,
)

internal val LocalFluentColors = staticCompositionLocalOf { fluentColors(darkTheme = false) }
