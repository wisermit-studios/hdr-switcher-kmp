package com.wisermit.hdrswitcher.widget

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.wisermit.hdrswitcher.ui.theme.Theme

private const val OUTLINE_OPACITY = 0.09f
private const val OUTLINE_OPACITY_DARK = 0.32f

@Composable
fun ConfigItem(
    modifier: Modifier = Modifier,
    headline: String,
    overline: String? = null,
    supporting: String? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    leading: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    val shape = shapes.extraSmall
    val outlineOpacity = if (isSystemInDarkTheme()) OUTLINE_OPACITY_DARK else OUTLINE_OPACITY

    val modifier = modifier
        .clip(shape = shape)
        .let { modifier ->
            onClick?.let {
                modifier.clickable(onClick = onClick)
            } ?: modifier
        }

    ListItem(
        modifier = modifier.border(
            width = Theme.OUTLINE_WIDTH,
            color = colorScheme.scrim.copy(alpha = outlineOpacity),
            shape = shape
        ),
        headlineContent = { Text(headline) },
        overlineContent = overline?.let { { Text(it) } },
        supportingContent = supportingContent ?: supporting?.let { { Text(it) } },
        leadingContent = leading,
        trailingContent = trailing,
    )
}