package com.wisermit.hdrswitcher.designsystem.window

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.rememberDialogState

private val TitleBarHeight = 28.dp

@Composable
fun FluentDialogWindow(
    onCloseRequest: () -> Unit,
    state: DialogState = rememberDialogState(size = DpSize.Unspecified),
    visible: Boolean = true,
    title: String = "",
    titleBarEnabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    val currentOnCloseRequest by rememberUpdatedState(onCloseRequest)

    DialogWindow(
        visible = visible,
        state = state,
        title = "",
        undecorated = true,
        transparent = true,
        resizable = false,
        onKeyEvent = { event ->
            val isDismissRequest = event.run { type == KeyEventType.KeyDown && key == Key.Escape }

            if (isDismissRequest) {
                currentOnCloseRequest()
                true
            } else {
                false
            }
        },
        onCloseRequest = onCloseRequest,
    ) {
        WindowSurface {
            Column(Modifier.width(IntrinsicSize.Max)) {
                if (titleBarEnabled) {
                    WindowTitleBar(
                        title = title,
                        titleBarHeight = TitleBarHeight,
                        onCloseRequest = onCloseRequest,
                    )
                }
                content()
            }
        }
    }
}