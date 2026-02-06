package com.wisermit.hdrswitcher.ui.main

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WebAsset
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.wisermit.hdrswitcher.designsystem.components.Button
import com.wisermit.hdrswitcher.designsystem.components.ComboBox
import com.wisermit.hdrswitcher.designsystem.components.ComboBoxItem
import com.wisermit.hdrswitcher.designsystem.components.ConfigurationItem
import com.wisermit.hdrswitcher.designsystem.components.SubItem
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource

private val HdrEntries = mapOf(
    HdrMode.Default to Res.string.default,
    HdrMode.On to Res.string.on,
    HdrMode.Off to Res.string.off,
)

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
                var expanded by remember { mutableStateOf(false) }

                ComboBox(
                    expanded = expanded,
                    onClick = { expanded = !expanded },
                    onDismissRequest = { expanded = false },
                    content = {
                        HdrEntries[item.hdr]?.let {
                            Text(stringResource(it))
                        }
                    }
                ) {
                    HdrEntries.forEach { entry ->
                        ComboBoxItem(
                            selected = entry.key == item.hdr,
                            onClick = {
                                expanded = false
                                onHdrChange(entry.key)
                            }
                        ) {
                            Text(stringResource(entry.value))
                        }
                    }
                }
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