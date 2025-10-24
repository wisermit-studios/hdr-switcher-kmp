package com.wisermit.hdrswitcher.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wisermit.hdrswitcher.util.ProvideContentColorTextStyle
import com.wisermit.hdrswitcher.util.surface
import com.wisermit.hdrswitcher.widget.ConfigurationItemDefaults.TrailingStartPadding

class ConfigurationItemScope()

@Composable
fun ConfigurationItem(
    modifier: Modifier = Modifier,
    expanded: Boolean = true,
    headlineContent: @Composable () -> Unit,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    content: @Composable (ConfigurationItemScope.() -> Unit)? = null,
) {
    Column(Modifier.surface()) {
        ConfigurationItem(
            modifier = modifier,
            headlineContent = headlineContent,
            supportingContent = supportingContent,
            leadingContent = leadingContent,
            trailingContent = trailingContent,
        )

        if (expanded) {
            content?.invoke(ConfigurationItemScope())
        }
    }
}

@Suppress("UnusedReceiverParameter")
@Composable
fun ConfigurationItemScope.SubItem(
    headlineContent: @Composable () -> Unit,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
) {
    HorizontalDivider(color = colorScheme.background)
    ConfigurationItem(
        modifier = Modifier.defaultMinSize(minHeight = ConfigurationItemDefaults.SubItemMinHeight),
        contentPadding = ConfigurationItemDefaults.SubItemContentPadding,
        headlineContent = headlineContent,
        supportingContent = supportingContent,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
    )
}

@Composable
private fun ConfigurationItem(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = ConfigurationItemDefaults.Padding,
    headlineContent: @Composable () -> Unit,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = ConfigurationItemDefaults.MinHeight)
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leadingContent?.let {
            Box(Modifier.padding(end = ConfigurationItemDefaults.LeadingEndPadding)) {
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

object ConfigurationItemDefaults {
    val MinHeight = 68.dp

    val HorizontalPadding = 16.dp
    val VerticalPadding = 12.dp
    val LeadingEndPadding = 16.dp
    val TrailingStartPadding = 16.dp

    val Padding = PaddingValues(
        horizontal = HorizontalPadding,
        vertical = VerticalPadding,
    )

    val SubItemMinHeight = 56.dp

    val SubItemContentPadding = PaddingValues(
        start = 56.dp,
        top = 4.dp,
        end = HorizontalPadding,
        bottom = 4.dp,
    )
}