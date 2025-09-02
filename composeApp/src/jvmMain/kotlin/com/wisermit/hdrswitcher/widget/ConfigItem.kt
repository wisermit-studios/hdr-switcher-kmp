package com.wisermit.hdrswitcher.widget

import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ConfigItem(
    modifier: Modifier = Modifier,
    padding: PaddingValues = ConfigItemDefaults.padding(),
    headline: String,
    trailing: @Composable (() -> Unit)? = null,
) {

    Row(
        modifier.fillMaxWidth()
            .padding(padding)
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

object ConfigItemDefaults {

    val HorizontalPadding = 16.dp
    val VerticalPadding = 4.dp

    fun padding(
        horizontal: Dp = HorizontalPadding,
        vertical: Dp = VerticalPadding,
        start: Dp? = null,
        top: Dp? = null,
        end: Dp? = null,
        bottom: Dp? = null,
    ) = PaddingValues(
        start = start ?: horizontal,
        top = top ?: vertical,
        end = end ?: horizontal,
        bottom = bottom ?: vertical,
    )
}