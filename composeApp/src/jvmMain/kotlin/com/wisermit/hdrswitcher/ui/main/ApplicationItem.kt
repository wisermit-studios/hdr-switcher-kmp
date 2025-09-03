package com.wisermit.hdrswitcher.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WebAsset
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wisermit.hdrswitcher.model.Application
import com.wisermit.hdrswitcher.model.HdrMode
import com.wisermit.hdrswitcher.utils.outline
import com.wisermit.hdrswitcher.widget.Button
import com.wisermit.hdrswitcher.widget.ComboBox
import com.wisermit.hdrswitcher.widget.ConfigItem
import com.wisermit.hdrswitcher.widget.ConfigItemDefaults
import hdrswitcher.composeapp.generated.resources.Res
import hdrswitcher.composeapp.generated.resources.default
import hdrswitcher.composeapp.generated.resources.hdr
import hdrswitcher.composeapp.generated.resources.off
import hdrswitcher.composeapp.generated.resources.on
import hdrswitcher.composeapp.generated.resources.remove
import hdrswitcher.composeapp.generated.resources.remove_from_list
import org.jetbrains.compose.resources.stringResource

@Composable
fun ApplicationItem(
    item: Application,
    onApplicationHdrChange: (Application, HdrMode) -> Unit,
    onDelete: (Application) -> Unit,
) {
    Column(
        modifier = Modifier
            .outline(color = ApplicationItemDefaults.outlineColor)
            .background(color = ListItemDefaults.containerColor),

        ) {
        ListItem(
            colors = ListItemDefaults.colors(
                containerColor = Color.Unspecified,
            ),
            leadingContent = {
                Icon(
                    // TODO: Icon.
                    imageVector = Icons.Default.WebAsset,
                    modifier = Modifier.height(44.dp),
                    contentDescription = null,
                )
            },
            headlineContent = {
                Text(item.description)
            },
            supportingContent = {
                Text(item.path)
            }
        )

        HorizontalDivider(color = colorScheme.background)

        ConfigItem(
            padding = ApplicationItemDefaults.Padding,
            headline = stringResource(Res.string.hdr),
            trailing = {
                ComboBox(
                    value = item.hdr,
                    entries = mapOf(
                        HdrMode.Default to stringResource(Res.string.default),
                        HdrMode.On to stringResource(Res.string.on),
                        HdrMode.Off to stringResource(Res.string.off),
                    ),
                    onSelected = {
                        onApplicationHdrChange(item, it)
                    },
                )
            }
        )

        HorizontalDivider(color = colorScheme.background)

        ConfigItem(
            padding = ApplicationItemDefaults.Padding,
            headline = stringResource(Res.string.remove_from_list),
            trailing = {
                Button(
                    stringResource(Res.string.remove),
                    onClick = { onDelete(item) },
                )
            }
        )
    }
}

object ApplicationItemDefaults {

    private const val OUTLINE_OPACITY = 0.09f
    private const val OUTLINE_OPACITY_DARK = 0.32f

    val Padding = ConfigItemDefaults.padding(start = 56.dp)

    val outlineColor: Color
        @Composable get() = colorScheme.scrim.copy(
            alpha = if (isSystemInDarkTheme()) OUTLINE_OPACITY_DARK else OUTLINE_OPACITY,
        )
}