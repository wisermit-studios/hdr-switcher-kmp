package com.wisermit.hdrswitcher.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.wisermit.hdrswitcher.ui.theme.ThemeDefaults
import com.wisermit.hdrswitcher.utils.fluentSurface
import com.wisermit.hdrswitcher.utils.toDp

@Composable
fun <T> ComboBox(
    value: T?,
    entries: Map<T, String>,
    onSelected: (T) -> Unit,
    enabled: Boolean = true,
) {
    var expanded by remember { mutableStateOf(false) }

    // TODO: Improve.
    var menuSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = Modifier.onGloballyPositioned { coordinates ->
            menuSize = coordinates.size
        },
    ) {
        InteractiveBox(
            modifier = Modifier
                .defaultMinSize(minHeight = ComboBoxDefaults.MinHeight)
                .fluentSurface(
                    backgroundColor = colorScheme.surfaceBright,
                    borderColor = colorScheme.outlineVariant,
                    borderWidth = ComboBoxDefaults.OutlineWidth
                ),
            role = Role.DropdownList,
            onClick = { expanded = !expanded },
            enabled = enabled,
        ) {
            Row(
                modifier = Modifier.padding(
                    horizontal = ComboBoxDefaults.HorizontalPadding,
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    entries[value] ?: "",
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
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(0.dp, -menuSize.height.toDp()),
            shape = shapes.small,
            border = BorderStroke(
                width = ThemeDefaults.BorderStrokeWidth,
                color = colorScheme.background,
            ),
            modifier = Modifier
                .defaultMinSize(
                    minWidth = menuSize.width.toDp(),
                ),
            content = {
                entries.forEach { entry ->
                    val isSelected = entry.key == value

                    DropdownMenuItem(
                        modifier = Modifier
                            .height(ComboBoxDefaults.MinHeight)
                            .padding(
                                horizontal = ComboBoxDefaults.MenuItem.HorizontalPadding,
                                vertical = ComboBoxDefaults.MenuItem.VerticalPadding,
                            )
                            .clip(shapes.extraSmall)
                            .background(
                                if (isSelected) {
                                    ComboBoxDefaults.MenuItem.selectedBackgroundColor
                                } else {
                                    Color.Unspecified
                                }
                            ),
                        contentPadding = PaddingValues(0.dp),
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
                                        .padding(
                                            ComboBoxDefaults.MenuItem.ContentPadding,
                                        ),
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
        )
    }
}

object ComboBoxDefaults {
    val MinHeight = ButtonDefaults.MinHeight
    val HorizontalPadding = 12.dp
    val IconSize = 16.dp
    val OutlineWidth = 0.5.dp

    val foregroundColor: Color
        @Composable @ReadOnlyComposable get() = colorScheme.onSurface

    object MenuItem {
        val HorizontalPadding = 6.dp
        val VerticalPadding = 2.dp
        val ContentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
        val IndicatorWidth = 3.dp
        val IndicatorHeight = 16.dp

        val selectedBackgroundColor: Color
            @Composable @ReadOnlyComposable get() = colorScheme.onSurface.copy(alpha = 0.08f)
    }
}
