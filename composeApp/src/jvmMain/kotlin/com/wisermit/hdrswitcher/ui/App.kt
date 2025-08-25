package com.wisermit.hdrswitcher.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragData
import androidx.compose.ui.draganddrop.dragData
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.wisermit.hdrswitcher.model.Application
import com.wisermit.hdrswitcher.resources.Res
import org.jetbrains.compose.resources.stringResource
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
                .padding(all = 16.dp)
                .dragAndDropTarget(
                    shouldStartDragAndDrop = { true },
                    target = dragAndDropTarget
                ),
        ) {
            // TODO: Check HDR support.
            ConfigItem(
                headline = "HDR",
                trailing = {
                    Switch(
                        checked = hdrEnabled.value,
                        onCheckedChange = {
                            // TODO: Toggle system HDR.
                            hdrEnabled.value = !hdrEnabled.value
                        },
                    )
                }
            )

            Text(
                modifier = Modifier.padding(top = 32.dp, bottom = 8.dp),
                style = MaterialTheme.typography.labelLarge,
                text = "Custom settings for applications"
            )

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(appList.value.size) { index ->
                    val item = appList.value[index]
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
                                Text(item.pathString)
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
    onClick: (() -> Unit)? = null,
) {
    var modifier = modifier.clip(shape = shapes.extraSmall)
    onClick?.let {
        modifier = modifier.clickable(onClick = onClick)
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