package com.wisermit.hdrswitcher.ui.main

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HdrOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.wisermit.hdrswitcher.designsystem.components.Button
import com.wisermit.hdrswitcher.designsystem.components.ConfigurationItem
import com.wisermit.hdrswitcher.designsystem.components.ScrollViewer
import com.wisermit.hdrswitcher.designsystem.theme.ThemeDefaults
import com.wisermit.hdrswitcher.resources.Res
import com.wisermit.hdrswitcher.resources.add_application
import com.wisermit.hdrswitcher.resources.drag_and_drop_application
import com.wisermit.hdrswitcher.resources.hdr
import com.wisermit.hdrswitcher.resources.hdr_switcher_service_error
import com.wisermit.hdrswitcher.resources.main_applications_label
import com.wisermit.hdrswitcher.resources.no_hdr_message
import com.wisermit.hdrswitcher.resources.off
import com.wisermit.hdrswitcher.resources.on
import com.wisermit.hdrswitcher.resources.open
import com.wisermit.hdrswitcher.resources.or
import com.wisermit.hdrswitcher.service.HdrSwitcherService
import com.wisermit.hdrswitcher.ui.ErrorDialog
import com.wisermit.hdrswitcher.ui.ErrorDialogData
import com.wisermit.hdrswitcher.util.FilePicker
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import java.io.File

@Composable
fun MainScreen(
    onClose: () -> Unit,
    viewModel: MainViewModel = koinInject(),
    hdrService: HdrSwitcherService = koinInject(),
) {
    val error by viewModel.error.collectAsState()
    val listState = rememberLazyListState()
    val scrollAdapter = rememberScrollbarAdapter(listState)
    val hdrServiceStatus = hdrService.status.collectAsState()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.refreshData()
    }

    when (val error = error) {
        is MainError.Error -> ErrorDialog(
            data = ErrorDialogData.from(error.cause),
            onClose = viewModel::clearError
        )

        is MainError.FatalError -> ErrorDialog(
            data = ErrorDialogData.from(error.cause),
            onClose = onClose
        )

        else -> Unit
    }

    if (hdrServiceStatus.value == HdrSwitcherService.Status.Error) {
        Text(
            stringResource(Res.string.hdr_switcher_service_error),
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.error)
                .padding(16.dp),
            style = typography.bodySmall
        )
    }

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
        adapter = scrollAdapter,
    ) {
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

                HdrConfigItem(
                    hdrStatus = hdrStatus,
                    onCheckedChange = viewModel::setHdrEnabled,
                )
            }

            val applications = applications
            when {
                applications == null -> {
                    item {
                        CircularProgressIndicator(
                            Modifier
                                .fillMaxWidth()
                                .padding(32.dp)
                                .wrapContentSize(),
                        )
                    }
                }

                applications.isEmpty() -> {
                    item {
                        EmptyView(onApplicationAdded = viewModel::addApplication)
                    }
                }

                else -> {
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
                    ) { item ->
                        ApplicationItem(
                            item = item,
                            onHdrChange = { hdrMode ->
                                viewModel.setApplicationHdr(item, hdrMode)
                            },
                            onDelete = {
                                viewModel.delete(item)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HdrConfigItem(
    hdrStatus: Boolean?,
    onCheckedChange: (Boolean) -> Unit,
) {
    ConfigurationItem(
        headlineContent = { Text(stringResource(Res.string.hdr)) },
        supportingContent = {
            if (hdrStatus == null) {
                Text(stringResource(Res.string.no_hdr_message))
            }
        },
        leadingContent = {
            Icon(
                Icons.Default.HdrOn,
                contentDescription = null,
            )
        },
        trailingContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(
                        if (hdrStatus == true) Res.string.on else Res.string.off
                    ),
                    modifier = Modifier.alpha(
                        if (hdrStatus == null) ThemeDefaults.DisabledStateLayerOpacity else 1f,
                    ),
                )
                Spacer(Modifier.width(16.dp))
                Switch(
                    enabled = hdrStatus != null,
                    checked = hdrStatus == true,
                    onCheckedChange = onCheckedChange,
                )
            }
        },
    )
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