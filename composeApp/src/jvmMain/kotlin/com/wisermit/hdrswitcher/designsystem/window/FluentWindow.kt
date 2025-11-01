package com.wisermit.hdrswitcher.designsystem.window

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import com.wisermit.hdrswitcher.resources.Res
import com.wisermit.hdrswitcher.resources.app_icon
import org.jetbrains.compose.resources.painterResource
import java.awt.Dimension

private val TitleBarHeight = 44.dp

@Composable
fun FluentWindow(
    onCloseRequest: () -> Unit,
    title: String,
    state: WindowState = rememberWindowState(),
    visible: Boolean = true,
    icon: Painter? = null,
    minimumSize: DpSize? = null,
    resizable: Boolean = true,
    content: @Composable FrameWindowScope.() -> Unit,
) {
    Window(
        state = state,
        visible = visible,
        title = title,
        icon = icon,
        undecorated = true,
        transparent = true,
        resizable = resizable,
        onCloseRequest = onCloseRequest,
    ) {
        LaunchedEffect(minimumSize) {
            window.minimumSize = minimumSize?.run {
                Dimension(width.value.toInt(), height.value.toInt())
            }
        }

        WindowSurface {
            Column(Modifier.fillMaxSize()) {
                WindowTitleBar(
                    windowState = state,
                    titleBarHeight = TitleBarHeight,
                    title = title,
                    icon = painterResource(Res.drawable.app_icon),
                    onCloseRequest = onCloseRequest,
                )
                content()
            }
        }
    }
}