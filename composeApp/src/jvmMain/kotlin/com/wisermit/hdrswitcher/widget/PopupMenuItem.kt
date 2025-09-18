package com.wisermit.hdrswitcher.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
            .padding(vertical = MenuItemDefaults.VerticalPadding)
            .clip(shape = shapes.extraSmall)
            .clickable(onClick = onClick)
            .padding(
                start = MenuItemDefaults.ContentStartPadding,
                top = MenuItemDefaults.ContentVerticalPadding,
                end = MenuItemDefaults.ContentEndPadding,
                bottom = MenuItemDefaults.ContentVerticalPadding,
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            modifier = Modifier.size(MenuItemDefaults.IconSize),
            contentDescription = null,
        )

        Spacer(Modifier.width(MenuItemDefaults.IconPadding))

        Text(
            title,
            style = typography.bodyMedium
        )
    }
}

object MenuItemDefaults {
    val MinHeight = ButtonDefaults.MinHeight

    val VerticalPadding = 4.dp

    val ContentStartPadding = 8.dp
    val ContentEndPadding = 16.dp
    val ContentVerticalPadding = 4.dp

    val IconSize = 16.dp
    val IconPadding = 16.dp
}