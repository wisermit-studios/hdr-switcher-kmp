package com.wisermit.hdrswitcher.widget

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ButtonDefaults as MaterialButtonDefaults

@Composable
fun Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    ButtonInternal(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
    ) {
        Text(text)
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    ButtonInternal(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = MaterialButtonDefaults.elevatedButtonColors(
            containerColor = colorScheme.secondary,
            contentColor = colorScheme.onSecondary,
        ),
    ) {
        Text(text)
    }
}

@Composable
private fun ButtonInternal(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = MaterialButtonDefaults.buttonColors(),
    content: @Composable RowScope.() -> Unit
) {
    ElevatedButton(
        modifier = modifier.defaultMinSize(minHeight = ButtonDefaults.MinHeight),
        enabled = enabled,
        colors = colors,
        contentPadding = PaddingValues(
            start = ButtonDefaults.HorizontalPadding,
            end = ButtonDefaults.HorizontalPadding,
            bottom = 2.dp,
        ),
        elevation = MaterialButtonDefaults.elevatedButtonElevation(
            defaultElevation = ButtonDefaults.Elevation,
            hoveredElevation = ButtonDefaults.Elevation,
            focusedElevation = ButtonDefaults.Elevation,
            pressedElevation = ButtonDefaults.PressedElevation,
        ),
        shape = shapes.extraSmall,
        onClick = onClick,
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides typography.labelLarge.copy(fontWeight = FontWeight.Normal),
        ) {
            content()
        }
    }
}

object ButtonDefaults {
    val MinHeight = 32.dp
    val HorizontalPadding = 24.dp

    val Elevation = 1.dp
    val PressedElevation = 0.dp
}