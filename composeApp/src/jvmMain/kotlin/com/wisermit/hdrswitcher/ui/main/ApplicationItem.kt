package com.wisermit.hdrswitcher.ui.main

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WebAsset
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.wisermit.hdrswitcher.model.Application
import com.wisermit.hdrswitcher.model.HdrMode
import com.wisermit.hdrswitcher.resources.Res
import com.wisermit.hdrswitcher.resources.default
import com.wisermit.hdrswitcher.resources.hdr
import com.wisermit.hdrswitcher.resources.off
import com.wisermit.hdrswitcher.resources.on
import com.wisermit.hdrswitcher.resources.remove
import com.wisermit.hdrswitcher.resources.remove_from_list
import com.wisermit.hdrswitcher.util.FileUtils
import com.wisermit.hdrswitcher.widget.Button
import com.wisermit.hdrswitcher.widget.ComboBox
import com.wisermit.hdrswitcher.widget.ConfigurationItem
import com.wisermit.hdrswitcher.widget.SubItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource

@Composable
fun ApplicationItem(
    item: Application,
    onHdrChange: (HdrMode) -> Unit,
    onDelete: () -> Unit,
) {
    ConfigurationItem(
        headlineContent = { Text(item.description) },
        supportingContent = { Text(item.file.path) },
        leadingContent = {
            val icon by produceState<ImageBitmap?>(initialValue = null, item.file) {
                value = withContext(Dispatchers.IO) {
                    FileUtils.getIcon(item.file)
                }
            }

            icon?.let {
                Icon(
                    it,
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified,
                    contentDescription = null
                )
            } ?: run {
                Icon(
                    Icons.Default.WebAsset,
                    contentDescription = null
                )
            }
        }
    ) {
        SubItem(
            headlineContent = {
                Text(stringResource(Res.string.hdr))
            },
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

        SubItem(
            headlineContent = {
                Text(stringResource(Res.string.remove_from_list))
            },
            trailingContent = {
                Button(
                    stringResource(Res.string.remove),
                    onClick = onDelete,
                )
            }
        )
    }
}