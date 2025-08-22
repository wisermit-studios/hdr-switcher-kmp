package com.wisermit.hdrswitcher.ui

import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragData
import androidx.compose.ui.draganddrop.dragData
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wisermit.hdrswitcher.model.Application
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.net.URI
import java.nio.file.Path
import kotlin.io.path.toPath

private val TEST = Application(Path.of("F:\\Downloads\\Test.exe"))

@Composable
@Preview
fun App() {
    val hdrEnabled = remember { mutableStateOf(false) }
    val appList = remember { mutableStateOf<List<Application>>(mutableListOf(TEST)) }

    val dragAndDropTarget = remember {
        object : DragAndDropTarget {
            override fun onDrop(event: DragAndDropEvent): Boolean {
                (event.dragData() as? DragData.FilesList)
                    ?.readFiles()
                    ?.firstOrNull()
                    ?.let(::URI)
                    ?.let(URI::toPath)
                    ?.let {
                        println(".. dropped: $it")
                        appList.value += Application(it)
                    }
                    ?.let(::println)
                    ?: return false
                return true
            }
        }
    }

    Scaffold {
        Column(
            modifier = Modifier
                .safeContentPadding()
                .padding(all = 16.dp)
                .fillMaxSize()
                .dragAndDropTarget(
                    shouldStartDragAndDrop = { true },
                    target = dragAndDropTarget
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ListItem(
                // TODO: Remove on Theme.
                colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent
                ),
                headlineContent = {
                    Text("HDR")
                },
                trailingContent = {
                    Switch(
                        checked = hdrEnabled.value,
                        onCheckedChange = {
                            hdrEnabled.value = !hdrEnabled.value
                        },
                    )
                }
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(appList.value.size) { index ->
                    val item = appList.value[index]
                    ListItem(
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent
                        ),
                        headlineContent = {
                            Text(item.name)
                        },
                        supportingContent = {
                            Text(item.pathString)
                        }
                    )
                }
            }
        }
    }
}