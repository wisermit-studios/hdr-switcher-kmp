package com.wisermit.hdrswitcher.ui.main

import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WebAsset
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wisermit.hdrswitcher.model.Application
import com.wisermit.hdrswitcher.model.HdrMode
import com.wisermit.hdrswitcher.resources.Res
import com.wisermit.hdrswitcher.resources.error
import com.wisermit.hdrswitcher.resources.main_empty_application_list
import com.wisermit.hdrswitcher.ui.components.ConfigItem
import com.wisermit.hdrswitcher.ui.components.WindowsScrollBar
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import javax.swing.JOptionPane

@Composable
@Preview
fun MainScreen(
    viewModel: MainViewModel = koinInject(),
) {
    // TODO: Refresh on Focus changed.

    val event by viewModel.event.collectAsState(null)
    event?.pickValue()?.let { exception ->
        val title = stringResource(Res.string.error)

        LaunchedEffect(exception) {
            // TODO: Beautify error messages.
            JOptionPane.showMessageDialog(
                null,
                exception.message,
                title,
                JOptionPane.ERROR_MESSAGE
            )
        }
    }

    Scaffold {
        WindowsScrollBar(
            modifier = Modifier
                .fillMaxHeight()
                .dragAndDropTarget(
                    shouldStartDragAndDrop = { true },
                    target = remember {
                        object : DragAndDropTarget {
                            override fun onDrop(event: DragAndDropEvent) = true.also {
                                viewModel.dropFile(event)
                            }
                        }
                    }
                ),
        ) { listState ->
            val isHdrEnabled by viewModel.isHdrEnabled.collectAsState()
            val applications by viewModel.applications.collectAsState()

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                contentPadding = PaddingValues(
                    top = 18.dp,
                    start = 18.dp,
                    end = 18.dp,
                    bottom = 32.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                item {
                    // TODO: Display no HDR message.

                    ConfigItem(
                        enabled = isHdrEnabled != null,
                        headline = "HDR",
                        trailing = {
                            Switch(
                                enabled = isHdrEnabled != null,
                                checked = isHdrEnabled == true,
                                onCheckedChange = viewModel::setHdrEnabled,
                            )
                        },
                    )
                }

                if (applications.isEmpty()) {
                    // TODO: Display add applications button.

                    item {
                        Text(
                            stringResource(Res.string.main_empty_application_list),
                            modifier = Modifier
                                .padding(vertical = 48.dp, horizontal = 32.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    item {
                        Text(
                            modifier = Modifier.padding(top = 12.dp, bottom = 0.dp),
                            style = MaterialTheme.typography.labelLarge,
                            text = "Custom settings for applications"
                        )
                    }
                    items(applications.size) { index ->
                        ApplicationItem(
                            item = applications[index],
                            onApplicationHdrChange = { app, enabled ->
                                viewModel.setApplicationHdr(app, enabled)
                            },
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun ApplicationItem(
    item: Application,
    onApplicationHdrChange: (Application, Boolean) -> Unit,
) {
    ConfigItem(
        leading = {
            Icon(
                modifier = Modifier.height(44.dp),
                imageVector = Icons.Default.WebAsset,
                contentDescription = null,
            )
        },
        headline = item.description,
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
                        checked = item.hdr == HdrMode.On,
                        onCheckedChange = {
                            onApplicationHdrChange(item, it)
                        },
                    )
                }
            }
        }
    )
}