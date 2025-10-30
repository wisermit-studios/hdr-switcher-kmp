package com.wisermit.hdrswitcher.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun PopupMenuItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .defaultMinSize(minHeight = MenuItemDefaults.MinHeight)
            .fillMaxWidth()
            .padding(MenuItemDefaults.Padding)
            .clip(shape = shapes.extraSmall)
            .clickable(onClick = onClick)
            .padding(MenuItemDefaults.ContentPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            modifier = Modifier
                .size(MenuItemDefaults.IconSize)
                .padding(MenuItemDefaults.IconPadding),
            contentDescription = null,
        )

        Text(title, style = typography.bodyMedium)
    }
}

private object MenuItemDefaults {
    val MinHeight = 32.dp

    val Padding = PaddingValues(vertical = 4.dp)

    val ContentPadding = PaddingValues(
        start = 8.dp,
        top = 4.dp,
        end = 16.dp,
        bottom = 4.dp,
    )

    val IconSize = 16.dp
    val IconPadding = PaddingValues(end = 16.dp)
}