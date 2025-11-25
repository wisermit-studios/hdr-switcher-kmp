package com.wisermit.hdrswitcher.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.dp

@Composable
fun FluentTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalFluentColors provides fluentColors(isDarkTheme),
        LocalMinimumInteractiveComponentSize provides ThemeDefaults.MinimumInteractiveComponentSize,
        LocalRippleConfiguration provides ThemeDefaults.RippleConfiguration
    ) {
        MaterialTheme(
            colorScheme = fluentColorScheme(isDarkTheme),
            shapes = fluentShapes(),
            typography = fluentTypography(),
            content = content,
        )
    }
}

object FluentTheme {
    val colors: FluentColors
        @Composable @ReadOnlyComposable get() = LocalFluentColors.current
}

@Suppress("ConstPropertyName")
object ThemeDefaults {
    const val DisabledStateLayerOpacity = StateTokens.DISABLED_STATE_LAYER_OPACITY

    internal val MinimumInteractiveComponentSize = 44.dp

    private object StateTokens {
        const val DRAGGED_STATE_LAYER_OPACITY = 0.16f
        const val FOCUS_STATE_LAYER_OPACITY = 0.1f
        const val HOVER_STATE_LAYER_OPACITY = 0.02f
        const val PRESSED_STATE_LAYER_OPACITY = 0.08f
        const val DISABLED_STATE_LAYER_OPACITY = 0.38f
    }

    private val RippleAlpha = RippleAlpha(
        StateTokens.DRAGGED_STATE_LAYER_OPACITY,
        StateTokens.FOCUS_STATE_LAYER_OPACITY,
        StateTokens.HOVER_STATE_LAYER_OPACITY,
        StateTokens.PRESSED_STATE_LAYER_OPACITY,
    )
    internal val RippleConfiguration = RippleConfiguration(rippleAlpha = RippleAlpha)
}
