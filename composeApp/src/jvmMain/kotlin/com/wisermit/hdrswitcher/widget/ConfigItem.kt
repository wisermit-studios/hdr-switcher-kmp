package com.wisermit.hdrswitcher.widget

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.wisermit.hdrswitcher.utils.fluentSurface

@Composable
fun ConfigItem(
    modifier: Modifier = Modifier,
    padding: PaddingValues = ConfigItemDefaults.Padding,
    headline: String,
    supporting: String? = null,
    icon: ImageVector? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    backgroundEnabled: Boolean = true,
) {
    Row(
        modifier.fillMaxWidth()
            .then(
                if (backgroundEnabled) {
                    Modifier.fluentSurface(
                        borderColor = ConfigItemDefaults.outlineColor,
                        backgroundColor = ListItemDefaults.containerColor,
                    )
                } else {
                    Modifier
                }
            )
            .padding(padding)
            .defaultMinSize(minHeight = LocalMinimumInteractiveComponentSize.current),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon?.let {
            Icon(
                it,
                modifier = Modifier.padding(end = 16.dp),
                contentDescription = null,
            )
        }
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                headline,
                color = colorScheme.onSurface,
            )
            supporting?.let {
                Text(
                    it,
                    style = typography.bodySmall,
                    color = colorScheme.onSurfaceVariant,
                )
            }
        }
        trailingContent?.invoke()
    }
}

object ConfigItemDefaults {

    private const val OUTLINE_OPACITY = 0.09f
    private const val OUTLINE_OPACITY_DARK = 0.32f

    val HorizontalPadding = 16.dp
    val VerticalPadding = 12.dp

    val Padding = PaddingValues(
        horizontal = HorizontalPadding,
        vertical = VerticalPadding,
    )

    val outlineColor: Color
        @Composable get() = colorScheme.scrim.copy(
            alpha = if (isSystemInDarkTheme()) OUTLINE_OPACITY_DARK else OUTLINE_OPACITY,
        )
}