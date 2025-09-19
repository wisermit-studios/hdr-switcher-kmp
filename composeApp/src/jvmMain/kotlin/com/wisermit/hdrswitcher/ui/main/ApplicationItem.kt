package com.wisermit.hdrswitcher.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WebAsset
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wisermit.hdrswitcher.model.Application
import com.wisermit.hdrswitcher.model.HdrMode
import com.wisermit.hdrswitcher.utils.fluentSurface
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
    onHdrChange: (HdrMode) -> Unit,
    onDelete: () -> Unit,
) {
    Column(
        modifier = Modifier.fluentSurface()
    ) {
        ConfigItem(
            // TODO: Application icon.
            backgroundEnabled = false,
            icon = Icons.Default.WebAsset,
            headline = item.description,
            supporting = item.path,
        )

        HorizontalDivider(color = colorScheme.background)

        ConfigItem(
            backgroundEnabled = false,
            padding = ApplicationItemDefaults.SubItemPadding,
            headline = stringResource(Res.string.hdr),
            trailingContent = {
                ComboBox(
                    value = item.hdr,
                    entries = mapOf(
                        HdrMode.Default to stringResource(Res.string.default),
                        HdrMode.On to stringResource(Res.string.on),
                        HdrMode.Off to stringResource(Res.string.off),
                    ),
                    onSelected = onHdrChange,
                )
            }
        )

        HorizontalDivider(color = colorScheme.background)

        ConfigItem(
            backgroundEnabled = false,
            padding = ApplicationItemDefaults.SubItemPadding,
            headline = stringResource(Res.string.remove_from_list),
            trailingContent = {
                Button(
                    stringResource(Res.string.remove),
                    onClick = onDelete,
                )
            }
        )
    }
}

object ApplicationItemDefaults {
    val SubItemPadding = PaddingValues(
        start = 56.dp,
        top = 4.dp,
        end = ConfigItemDefaults.HorizontalPadding,
        bottom = 4.dp,
    )
}