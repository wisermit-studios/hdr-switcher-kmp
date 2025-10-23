package com.wisermit.hdrswitcher.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wisermit.hdrswitcher.util.ProvideContentColorTextStyle
import com.wisermit.hdrswitcher.util.surface
import com.wisermit.hdrswitcher.widget.ConfigItemDefaults.TrailingStartPadding

@Composable
fun ConfigItem(
    modifier: Modifier = Modifier,
    padding: PaddingValues = ConfigItemDefaults.Padding,
    headlineContent: @Composable () -> Unit,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    backgroundEnabled: Boolean = true,
) {
    Row(
        modifier.fillMaxWidth()
            .then(if (backgroundEnabled) Modifier.surface() else Modifier)
            .padding(padding)
            .defaultMinSize(minHeight = LocalMinimumInteractiveComponentSize.current),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leadingContent?.let {
            Box(Modifier.padding(end = ConfigItemDefaults.LeadingEndPadding)) {
                it()
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            ProvideTextStyle(
                typography.bodyMedium,
                content = headlineContent
            )

            supportingContent?.let {
                ProvideContentColorTextStyle(
                    colorScheme.onSurfaceVariant,
                    typography.bodySmall,
                    content = it
                )
            }
        }
        trailingContent?.let {
            Box(Modifier.padding(start = TrailingStartPadding)) {
                ProvideTextStyle(
                    typography.bodySmall,
                    content = it
                )
            }
        }
    }
}

object ConfigItemDefaults {
    val HorizontalPadding = 16.dp
    val VerticalPadding = 12.dp
    val LeadingEndPadding = 16.dp
    val TrailingStartPadding = 16.dp

    val Padding = PaddingValues(
        horizontal = HorizontalPadding,
        vertical = VerticalPadding,
    )
}