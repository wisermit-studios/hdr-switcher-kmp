package com.wisermit.hdrswitcher.widget

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val PADDING_VERTICAL = 4.dp

@Composable
fun ConfigSubItem(
    headline: String,
    trailing: @Composable (() -> Unit)? = null,
) {
    Row(
        Modifier.fillMaxWidth()
            .padding(vertical = PADDING_VERTICAL)
            .defaultMinSize(minHeight = LocalMinimumInteractiveComponentSize.current),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            headline,
            color = colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        trailing?.invoke()
    }
}