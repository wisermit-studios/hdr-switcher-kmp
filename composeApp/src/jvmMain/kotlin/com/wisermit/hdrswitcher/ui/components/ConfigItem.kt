package com.wisermit.hdrswitcher.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import com.wisermit.hdrswitcher.ui.theme.DISABLED_ALPHA

@Composable
fun ConfigItem(
    modifier: Modifier = Modifier,
    headline: String,
    overline: String? = null,
    supporting: String? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    leading: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
) {
    val modifier = modifier
        .clip(shape = shapes.extraSmall)
        .let {
            if (!enabled) it.alpha(DISABLED_ALPHA) else it
        }.let { m ->
            onClick?.let {
                m.clickable(enabled = enabled, onClick = onClick)
            } ?: m
        }

    ListItem(
        modifier = modifier,
        headlineContent = { Text(headline) },
        overlineContent = overline?.let { { Text(it) } },
        supportingContent = supportingContent ?: supporting?.let { { Text(it) } },
        leadingContent = leading,
        trailingContent = trailing,
    )
}