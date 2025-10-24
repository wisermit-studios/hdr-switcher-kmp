package com.wisermit.hdrswitcher.util

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wisermit.hdrswitcher.ui.theme.ThemeDefaults

@Composable
fun Modifier.surface(
    backgroundColor: Color = colorScheme.surface,
    borderWidth: Dp = ThemeDefaults.BorderStrokeWidth,
    borderColor: Color = colorScheme.surfaceDim,
    shape: Shape = shapes.extraSmall,
    shadowElevation: Dp = 0.dp,
) =
    then(
        if (shadowElevation > 0.dp) {
            Modifier.graphicsLayer(
                shadowElevation = with(LocalDensity.current) { shadowElevation.toPx() },
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
        .padding(borderWidth)
        .clip(shape)

@Composable
internal fun ProvideContentColorTextStyle(
    contentColor: Color,
    textStyle: TextStyle,
    content: @Composable () -> Unit
) {
    val mergedStyle = LocalTextStyle.current.merge(textStyle)
    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalTextStyle provides mergedStyle,
        content = content
    )
}