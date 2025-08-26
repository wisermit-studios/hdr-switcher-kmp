package com.wisermit.hdrswitcher.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WebAsset
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.wisermit.hdrswitcher.ui.theme.DISABLED_ALPHA
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject


@Composable
@Preview
fun MainScreen(
    viewModel: MainViewModel = koinInject(),
) {
    val dragAndDropTarget = remember {
        object : DragAndDropTarget {
            override fun onDrop(event: DragAndDropEvent): Boolean {
                return viewModel.dropFile(event)
            }
        }
    }

    // TODO: Refresh on Focus changed.

    Scaffold {
        Column(
            modifier = Modifier
                .padding(all = 16.dp)
                .dragAndDropTarget(
                    shouldStartDragAndDrop = { true },
                    target = dragAndDropTarget
                ),
        ) {
            // TODO: Display no HDR message.

            ConfigItem(
                enabled = viewModel.isHdrEnabled.value != null,
                headline = "HDR",
                trailing = {
                    Switch(
                        enabled = viewModel.isHdrEnabled.value != null,
                        checked = viewModel.isHdrEnabled.value == true,
                        onCheckedChange = viewModel::setHdrEnabled,
                    )
                },
            )

            Text(
                modifier = Modifier.padding(top = 32.dp, bottom = 8.dp),
                style = MaterialTheme.typography.labelLarge,
                text = "Custom settings for applications"
            )

            // TODO: Display no applications.

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(viewModel.applicationsSettings.value.size) { index ->
                    val item = viewModel.applicationsSettings.value[index]
                    ConfigItem(
                        leading = {
                            Icon(
                                modifier = Modifier.height(44.dp),
                                imageVector = Icons.Default.WebAsset,
                                contentDescription = null,
                            )
                        },
                        headline = item.name,
                        supportingContent = {
                            Column {
                                Text(item.path)
                                Row(
                                    Modifier.fillMaxWidth().padding(top = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        "HDR",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.weight(1f),
                                    )
                                    Switch(
                                        checked = false,
                                        onCheckedChange = {
                                            // TODO: Toggle app HDR.
                                        },
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

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