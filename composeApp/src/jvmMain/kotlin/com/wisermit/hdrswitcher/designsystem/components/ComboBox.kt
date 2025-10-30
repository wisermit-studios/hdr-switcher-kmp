package com.wisermit.hdrswitcher.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wisermit.hdrswitcher.designsystem.theme.FluentTheme
import com.wisermit.hdrswitcher.util.surface

@Immutable
data class ComboBoxData<T>(
    val value: T?,
    val entries: Map<T, String>,
) {
    val text = entries[value]
}

@Composable
fun <T> ComboBox(
    data: ComboBoxData<T>,
    onSelected: (T) -> Unit,
    enabled: Boolean = true,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(Modifier.width(IntrinsicSize.Max)) {
        val interactionSource = remember { MutableInteractionSource() }

        Row(
            modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    role = Role.DropdownList,
                    enabled = enabled,
                    onClick = { expanded = !expanded },
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
            Text(
                data.text ?: "",
                style = typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = ComboBoxDefaults.foregroundColor
            )
            Spacer(Modifier.width(ComboBoxDefaults.HorizontalPadding))
            Icon(
                modifier = Modifier.size(ComboBoxDefaults.IconSize),
                imageVector = Icons.Default.ExpandMore,
                tint = ComboBoxDefaults.foregroundColor,
                contentDescription = null,
            )
        }

        Box(Modifier.fillMaxWidth()) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                shape = shapes.small,
                border = BorderStroke(
                    width = ComboBoxDefaults.DropdownBorderWidth,
                    color = colorScheme.background,
                ),
            ) {
                data.entries.forEach { entry ->
                    val isSelected = entry.key == data.value

                    DropdownMenuItem(
                        modifier = Modifier
                            .height(ComboBoxDefaults.MinHeight)
                            .padding(ComboBoxDefaults.MenuItem.Padding)
                            .clip(shapes.extraSmall)
                            .background(
                                if (isSelected) {
                                    ComboBoxDefaults.MenuItem.selectedBackgroundColor
                                } else {
                                    Color.Unspecified
                                }
                            ),
                        contentPadding = ComboBoxDefaults.MenuItem.ContentPadding,
                        text = {
                            Box(contentAlignment = Alignment.CenterStart) {
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(
                                                width = ComboBoxDefaults.MenuItem.IndicatorWidth,
                                                height = ComboBoxDefaults.MenuItem.IndicatorHeight,
                                            )
                                            .background(
                                                color = colorScheme.primary,
                                                shape = CircleShape,
                                            ),
                                    )
                                }
                                Text(
                                    entry.value,
                                    modifier = Modifier
                                        .padding(ComboBoxDefaults.MenuItem.TextPadding),
                                    style = typography.bodyMedium,
                                )
                            }
                        },
                        onClick = {
                            expanded = false
                            onSelected(entry.key)
                        },
                    )
                }
            }
        }
    }
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
        val ContentPadding = PaddingValues(end = 24.dp)
        val TextPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
        val IndicatorWidth = 3.dp
        val IndicatorHeight = 16.dp

        val selectedBackgroundColor: Color
            @Composable @ReadOnlyComposable get() = colorScheme.onSurface.copy(alpha = 0.08f)
    }
}
