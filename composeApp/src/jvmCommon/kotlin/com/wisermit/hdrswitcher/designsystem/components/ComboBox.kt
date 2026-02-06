package com.wisermit.hdrswitcher.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.wisermit.hdrswitcher.designsystem.theme.FluentTheme
import com.wisermit.hdrswitcher.util.surface

@Composable
fun ComboBox(
    content: @Composable () -> Unit,
    expanded: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit,
    onDismissRequest: () -> Unit,
    menu: @Composable ColumnScope.() -> Unit,
) {
    CompositionLocalProvider(
        LocalTextStyle provides typography.bodyMedium,
    ) {
        Box(Modifier.width(IntrinsicSize.Max)) {
            val interactionSource = remember { MutableInteractionSource() }

            Row(
                modifier = Modifier
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        role = Role.DropdownList,
                        enabled = enabled,
                        onClick = onClick,
                    )
                    .minimumInteractiveComponentSize()
                    .surface(
                        backgroundColor = colorScheme.surfaceContainerHighest,
                        borderColor = FluentTheme.colors.outlineVariant2,
                        borderWidth = ComboBoxDefaults.BorderWidth,
                        shadowElevation = ComboBoxDefaults.ShadowElevation
                    )
                    .indication(interactionSource, ripple())
                    .defaultMinSize(minHeight = ComboBoxDefaults.MinHeight)
                    .padding(horizontal = ComboBoxDefaults.HorizontalPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                content()

                Icon(
                    modifier = Modifier
                        .padding(start = ComboBoxDefaults.HorizontalPadding)
                        .size(ComboBoxDefaults.IconSize),
                    imageVector = Icons.Default.ExpandMore,
                    tint = ComboBoxDefaults.foregroundColor,
                    contentDescription = null,
                )
            }

            Box(Modifier.fillMaxWidth()) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = onDismissRequest,
                    shape = shapes.medium,
                    border = BorderStroke(
                        width = ComboBoxDefaults.DropdownBorderWidth,
                        color = colorScheme.background,
                    ),
                    content = menu,
                )
            }
        }
    }
}

@Composable
fun ComboBoxItem(
    selected: Boolean,
    onClick: () -> Unit,
    text: @Composable () -> Unit,
) {
    val indicatorColor = colorScheme.primary

    DropdownMenuItem(
        modifier = Modifier
            .height(ComboBoxDefaults.MinHeight)
            .padding(ComboBoxDefaults.MenuItem.Padding)
            .clip(shapes.small)
            .background(
                if (selected)
                    ComboBoxDefaults.MenuItem.selectedBackgroundColor else Color.Unspecified
            )
            .then(
                if (selected) {
                    Modifier.drawBehind {
                        val indicatorSize = ComboBoxDefaults.MenuItem.IndicatorSize.toSize()
                        val yOffset = (size.height - indicatorSize.height) / 2f

                        drawRoundRect(
                            color = indicatorColor,
                            topLeft = Offset(0f, yOffset),
                            size = indicatorSize,
                            cornerRadius = CornerRadius(indicatorSize.width / 2f)
                        )
                    }
                } else Modifier
            ),
        contentPadding = ComboBoxDefaults.MenuItem.ContentPadding,
        onClick = onClick,
        text = text,
    )
}

private object ComboBoxDefaults {
    val MinHeight = 32.dp
    val HorizontalPadding = 12.dp

    val IconSize = 16.dp

    val BorderWidth = 1.dp
    val ShadowElevation = 1.dp

    val DropdownBorderWidth = 1.dp

    val foregroundColor: Color
        @Composable @ReadOnlyComposable get() = colorScheme.onSurface

    object MenuItem {
        val Padding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
        val ContentPadding = PaddingValues(start = 12.dp, end = 36.dp)
        val IndicatorSize = DpSize(3.dp, 16.dp)

        val selectedBackgroundColor: Color
            @Composable @ReadOnlyComposable get() = colorScheme.onSurface.copy(alpha = 0.08f)
    }
}
