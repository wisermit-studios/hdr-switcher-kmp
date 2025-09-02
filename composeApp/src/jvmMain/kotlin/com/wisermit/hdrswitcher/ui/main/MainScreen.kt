package com.wisermit.hdrswitcher.ui.main

import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WebAsset
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.unit.dp
import com.wisermit.hdrswitcher.model.Application
import com.wisermit.hdrswitcher.model.HdrMode
import com.wisermit.hdrswitcher.resources.Res
import com.wisermit.hdrswitcher.resources.add_application
import com.wisermit.hdrswitcher.resources.default
import com.wisermit.hdrswitcher.resources.drag_and_drop_application
import com.wisermit.hdrswitcher.resources.error
import com.wisermit.hdrswitcher.resources.hdr
import com.wisermit.hdrswitcher.resources.main_applications_label
import com.wisermit.hdrswitcher.resources.off
import com.wisermit.hdrswitcher.resources.on
import com.wisermit.hdrswitcher.resources.open
import com.wisermit.hdrswitcher.resources.or
import com.wisermit.hdrswitcher.resources.remove
import com.wisermit.hdrswitcher.resources.remove_from_list
import com.wisermit.hdrswitcher.utils.SystemInfo
import com.wisermit.hdrswitcher.widget.Button
import com.wisermit.hdrswitcher.widget.ComboBox
import com.wisermit.hdrswitcher.widget.ConfigItem
import com.wisermit.hdrswitcher.widget.ConfigSubItem
import com.wisermit.hdrswitcher.widget.ScrollViewer
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import java.io.File
import java.net.URI
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.filechooser.FileNameExtensionFilter

private val FILE_PICKER_EXTENSION_FILTER = FileNameExtensionFilter("*.exe", "exe")

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
            val isHdrEnabled by viewModel.isHdrEnabled.collectAsState()
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
                    // TODO: Display no HDR message / Text On/Off.

                    ConfigItem(
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
                    item {
                        EmptyView(onAddApplication = viewModel::addApplication)
                    }
                } else {
                    item {
                        Text(
                            stringResource(Res.string.main_applications_label),
                            modifier = Modifier.padding(top = 12.dp, bottom = 0.dp),
                            style = typography.labelLarge,
                        )
                    }
                    items(applications.size) { index ->
                        ApplicationItem(
                            item = applications[index],
                            onApplicationHdrChange = { app, hdrMode ->
                                viewModel.setApplicationHdr(app, hdrMode)
                            },
                            onDelete = viewModel::delete
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
    onApplicationHdrChange: (Application, HdrMode) -> Unit,
    onDelete: (Application) -> Unit,
) {
    ConfigItem(
        leading = {
            Icon(
                // TODO: Icon.
                imageVector = Icons.Default.WebAsset,
                modifier = Modifier.height(44.dp),
                contentDescription = null,
            )
        },
        headline = item.description,
        supportingContent = {
            Column {
                Text(item.path)

                ConfigSubItem(
                    stringResource(Res.string.hdr),
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

                ConfigSubItem(
                    stringResource(Res.string.remove_from_list),
                    trailing = {
                        Button(
                            stringResource(Res.string.remove),
                            onClick = { onDelete(item) },
                        )
                    }
                )
            }
        }
    )
}

@Composable
fun EmptyView(onAddApplication: (URI) -> Unit) {
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

        val title = stringResource(Res.string.open)
        Button(
            text = stringResource(Res.string.add_application),
            onClick = {
                // TODO: Improve FileChoose.
                val chooser = JFileChooser().apply {
                    dialogTitle = title
                    fileFilter = FILE_PICKER_EXTENSION_FILTER
                    isAcceptAllFileFilterUsed = false
                    currentDirectory = File(SystemInfo.systemDrive)
                }
                val result = chooser.showOpenDialog(null)
                if (result == JFileChooser.APPROVE_OPTION) {
                    onAddApplication(chooser.selectedFile.toURI())
                }
            },
        )
    }
}
