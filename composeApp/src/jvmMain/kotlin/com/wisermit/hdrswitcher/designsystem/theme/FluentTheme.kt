package com.wisermit.hdrswitcher.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

object ThemeDefaults {
    const val DISABLED_STATE_LAYER_OPACITY = StateTokens.DISABLED_STATE_LAYER_OPACITY

    internal val MinimumInteractiveComponentSize = 44.dp

    private val RippleAlpha = RippleAlpha(
        StateTokens.DRAGGED_STATE_LAYER_OPACITY,
        StateTokens.FOCUS_STATE_LAYER_OPACITY,
        StateTokens.HOVER_STATE_LAYER_OPACITY,
        StateTokens.PRESSED_STATE_LAYER_OPACITY,
    )

    internal val RippleConfiguration = RippleConfiguration(rippleAlpha = RippleAlpha)
}

object FluentTheme {
    val colors: FluentColors
        @Composable @ReadOnlyComposable get() = LocalFluentColors.current
}

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
            colorScheme = colorScheme(isDarkTheme),
            typography = typography(),
            content = content,
        )
    }
}

@Composable
private fun typography() = Typography(
    titleLarge = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.W500
    )
)
