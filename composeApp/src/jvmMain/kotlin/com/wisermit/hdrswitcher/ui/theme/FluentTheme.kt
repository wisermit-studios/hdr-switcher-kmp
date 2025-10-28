package com.wisermit.hdrswitcher.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

object ThemeDefaults {
    const val DISABLED_OPACITY = 0.38f
    val MinimumInteractiveComponentSize = 44.dp
    val BorderStrokeWidth = 1.dp
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
            typography = typography(),
            content = content,
        )
    }
}

private fun colorScheme(darkTheme: Boolean) = when {
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
            surfaceContainerHighest = layer4,
            surfaceBright = layer5,
            surfaceDim = outlineShadow,
            outline = outline1,
            outlineVariant = outline2,
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
            surfaceContainerHighest = layer4,
            surfaceBright = layer5,
            surfaceDim = outlineShadow,
            outline = outline1,
            outlineVariant = outline2,
        )
    }
}

@Composable
private fun typography(): Typography {
    val materialTypography = MaterialTheme.typography

    return Typography(
        titleLarge = materialTypography.titleLarge.copy(
            fontWeight = FontWeight.W500
        )
    )
}