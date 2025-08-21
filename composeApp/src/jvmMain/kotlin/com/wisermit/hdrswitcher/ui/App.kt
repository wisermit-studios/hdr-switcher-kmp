package com.wisermit.hdrswitcher.ui

import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragData
import androidx.compose.ui.draganddrop.dragData
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.net.URI
import java.nio.file.Path
import kotlin.io.path.toPath

@Composable
@Preview
fun App() {
    var exePath: Path? by remember { mutableStateOf(null) }

    val dragAndDropTarget = remember {
        object : DragAndDropTarget {
            override fun onDrop(event: DragAndDropEvent): Boolean {
                (event.dragData() as? DragData.FilesList)
                    ?.readFiles()
                    ?.firstOrNull()
                    ?.let(::URI)
                    ?.let(URI::toPath)
                    ?.let {
                        exePath = it
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
                headlineContent = {
                    Text("Dark Mode")
                },
                trailingContent = {
                    Switch(
                        checked = isSystemInDarkTheme(),
                        onCheckedChange = {},
                    )
                }
            )
            Spacer(Modifier.weight(1f))
            Text(exePath?.toString().orEmpty())
            Spacer(Modifier.height(16.dp))
            Button(onClick = { }) {
                Text("Button")
            }
            Spacer(Modifier.weight(1f))
        }
    }
}