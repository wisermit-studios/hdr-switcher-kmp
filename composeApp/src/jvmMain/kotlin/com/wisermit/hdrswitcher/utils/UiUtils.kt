package com.wisermit.hdrswitcher.utils

import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.wisermit.hdrswitcher.ui.theme.Theme

@Composable
@Stable
fun Modifier.outline(
    width: Dp = Theme.BORDER_STROKE_WIDTH,
    color: Color = colorScheme.outlineVariant,
    shape: Shape = shapes.extraSmall,
): Modifier =
    clip(shape).border(width, SolidColor(color), shape)

@Composable
fun Int.toDp(): Dp = with(LocalDensity.current) { toDp() }

@Stable
@Composable
fun Modifier.applyIf(
    condition: Boolean,
    block: @Composable Modifier.() -> Modifier,
): Modifier {
    return if (condition) block() else this
}

@Stable
@Composable
fun <T> Modifier.withNotNull(
    value: T?,
    block: @Composable Modifier.(T) -> Modifier,
): Modifier {
    return if (value != null) block(value) else this
}