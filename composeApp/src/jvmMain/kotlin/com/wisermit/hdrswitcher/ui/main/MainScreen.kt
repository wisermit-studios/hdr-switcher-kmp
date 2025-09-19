package com.wisermit.hdrswitcher.ui.main

import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HdrOn
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.wisermit.hdrswitcher.ui.theme.ThemeDefaults
import com.wisermit.hdrswitcher.utils.DialogUtils
import com.wisermit.hdrswitcher.utils.FilePicker
import com.wisermit.hdrswitcher.widget.Button
import com.wisermit.hdrswitcher.widget.ConfigItem
import com.wisermit.hdrswitcher.widget.ScrollViewer
import hdrswitcher.composeapp.generated.resources.Res
import hdrswitcher.composeapp.generated.resources.add_application
import hdrswitcher.composeapp.generated.resources.drag_and_drop_application
import hdrswitcher.composeapp.generated.resources.main_applications_label
import hdrswitcher.composeapp.generated.resources.no_hdr_message
import hdrswitcher.composeapp.generated.resources.off
import hdrswitcher.composeapp.generated.resources.on
import hdrswitcher.composeapp.generated.resources.open
import hdrswitcher.composeapp.generated.resources.or
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import java.io.File

@Composable
@Preview
fun MainScreen(
    viewModel: MainViewModel = koinInject(),
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(coroutineScope) {
        viewModel.showErrorDialog.onEach {
            DialogUtils.showErrorDialogFor(it)
        }.launchIn(coroutineScope)
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.refreshHdrStatus()
    }

    Scaffold {
        ScrollViewer(
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
            val applications by viewModel.applications.collectAsState()

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                contentPadding = PaddingValues(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 32.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item {
                    val hdrStatus by viewModel.hdrStatus.collectAsState()

                    ConfigItem(
                        headline = "HDR",
                        icon = Icons.Default.HdrOn,
                        supporting = if (hdrStatus == null)
                            stringResource(Res.string.no_hdr_message) else null,
                        trailingContent = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    stringResource(
                                        if (hdrStatus == true) Res.string.on else Res.string.off
                                    ),
                                    modifier = Modifier.alpha(
                                        if (hdrStatus == null)
                                            ThemeDefaults.DISABLED_OPACITY else 1f,
                                    ),
                                )
                                Spacer(Modifier.width(16.dp))
                                Switch(
                                    enabled = hdrStatus != null,
                                    checked = hdrStatus == true,
                                    onCheckedChange = viewModel::setHdrEnabled,
                                )
                            }
                        },
                    )
                }

                if (applications.isEmpty()) {
                    item {
                        EmptyView(onApplicationAdded = viewModel::addApplication)
                    }
                } else {
                    item {
                        Text(
                            stringResource(Res.string.main_applications_label),
                            modifier = Modifier.padding(top = 12.dp, bottom = 0.dp),
                            style = typography.labelLarge,
                        )
                    }
                    items(
                        applications,
                        key = { it.id },
                    ) { app ->
                        ApplicationItem(
                            item = app,
                            onHdrChange = { hdrMode ->
                                viewModel.setApplicationHdr(app, hdrMode)
                            },
                            onDelete = {
                                viewModel.delete(app)
                            }
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun EmptyView(onApplicationAdded: (File) -> Unit) {
    Column(
        modifier = Modifier
            .padding(vertical = 48.dp, horizontal = 32.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(Res.string.drag_and_drop_application))

        Spacer(Modifier.height(16.dp))

        Text(
            stringResource(Res.string.or).uppercase(),
            style = typography.bodySmall,
            color = colorScheme.onSurfaceVariant,
        )

        Spacer(Modifier.height(16.dp))

        val filePickerTitle = stringResource(Res.string.open)
        Button(
            text = stringResource(Res.string.add_application),
            onClick = {
                FilePicker.show(
                    title = filePickerTitle,
                    fileFilter = FilePicker.applicationFilter,
                    onPick = onApplicationAdded
                )
            },
        )
    }
}