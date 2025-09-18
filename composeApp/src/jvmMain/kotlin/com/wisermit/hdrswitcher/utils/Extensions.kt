package com.wisermit.hdrswitcher.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wisermit.hdrswitcher.ui.theme.ThemeDefaults
import java.nio.file.Path

fun Path.add(path: String): Path = resolve(path)

@Composable
@Stable
fun Modifier.fluentSurface(
    backgroundColor: Color = colorScheme.surfaceContainer,
    borderWidth: Dp = ThemeDefaults.BORDER_STROKE_WIDTH,
    borderColor: Color = colorScheme.outlineVariant,
    shape: Shape = shapes.extraSmall,
    shadowElevation: Dp = 0.dp,
) =
    then(
        if (shadowElevation > 0.dp) {
            Modifier.graphicsLayer(
                shadowElevation = shadowElevation.toPx(),
                shape = shape,
                clip = false
            )
        } else {
            Modifier
        }
    )
        .border(
            BorderStroke(width = borderWidth, color = borderColor),
            shape,
        )
        .background(color = backgroundColor, shape = shape)
        .clip(shape)

@Composable
fun Int.toDp(): Dp = with(LocalDensity.current) { toDp() }

@Composable
fun Dp.toPx(): Float = with(LocalDensity.current) { toPx() }