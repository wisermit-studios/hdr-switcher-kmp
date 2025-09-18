package com.wisermit.hdrswitcher.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.FilterNone
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberDialogState
import androidx.compose.ui.window.rememberWindowState
import com.wisermit.hdrswitcher.ui.theme.ThemeDefaults
import hdrswitcher.composeapp.generated.resources.Res
import hdrswitcher.composeapp.generated.resources.app_icon
import hdrswitcher.composeapp.generated.resources.close
import hdrswitcher.composeapp.generated.resources.maximize
import hdrswitcher.composeapp.generated.resources.minimize
import hdrswitcher.composeapp.generated.resources.restore_down
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import java.awt.Dimension
import java.awt.GraphicsEnvironment

private val WINDOW_MARGIN_DP = 4.dp
private val WINDOW_CORNER_RADIUS = 8.dp
private val TITLE_BAR_HEIGHT = 44.dp
private val TITLE_BAR_FONT_SIZE = 14.sp
private val TITLE_BAR_ICON_SIZE = 28.dp
private val WINDOW_CONTROL_BUTTON_ICON_SIZE = 20.dp
private val WINDOW_CONTROL_BUTTON_WIDTH = 47.dp
private val WINDOW_CONTROL_BUTTON_HEIGHT = TITLE_BAR_HEIGHT

@Composable
fun FluentWindow(
    controller: Boolean = true,
    visible: Boolean = true,
    title: String,
    icon: Painter?,
    size: DpSize,
    minimumSize: DpSize? = null,
    resizable: Boolean = true,
    onCloseRequest: () -> Unit,
    content: @Composable FrameWindowScope.() -> Unit,
) {
    val state = rememberWindowState(
        size = size,
        position = with(GraphicsEnvironment.getLocalGraphicsEnvironment()) {
            val anchorOffset = DpOffset(
                x = maximumWindowBounds.width.dp,
                y = maximumWindowBounds.height.dp,
            )
            calculateWindowPosition(anchorOffset, size)
        }
    )

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
            Column {
                TitleBar(
                    state = state,
                    title = title,
                    onCloseRequest = onCloseRequest,
                )
                content()
            }
        }
    }
}

@Composable
fun FluentPopupWindow(
    visible: Boolean = true,
    size: DpSize,
    anchor: DpOffset,
    content: @Composable WindowScope.() -> Unit,
) {
    val state = rememberDialogState(
        width = size.width,
        height = size.height,
    )

    LaunchedEffect(size, anchor) {
        state.size = size
        state.position = calculateWindowPosition(anchor, size)
    }

    DialogWindow(
        visible = visible,
        state = state,
        title = "",
        undecorated = true,
        transparent = true,
        resizable = false,
        onCloseRequest = { },
    ) {
        WindowSurface {
            content()
        }
    }
}

@Composable
private fun WindowSurface(
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorScheme.background,
        border = BorderStroke(ThemeDefaults.BORDER_STROKE_WIDTH, colorScheme.outlineVariant),
        shape = RoundedCornerShape(WINDOW_CORNER_RADIUS),
        content = content,
    )
}

@Composable
private fun FrameWindowScope.TitleBar(
    state: WindowState,
    title: String,
    onCloseRequest: () -> Unit,
) {
    WindowDraggableArea {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(TITLE_BAR_HEIGHT)
                .padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(TITLE_BAR_ICON_SIZE).padding(end = 8.dp),
                painter = painterResource(Res.drawable.app_icon),
                tint = Color.Unspecified,
                contentDescription = null,
            )

            Text(
                modifier = Modifier.weight(1f),
                text = title,
                fontSize = TITLE_BAR_FONT_SIZE,
                color = colorScheme.onSurfaceVariant
            )

            WindowControlButton(
                Icons.Default.HorizontalRule,
                contentDescription = stringResource(Res.string.minimize),
                onClick = { state.isMinimized = true },
            )

            if (window.isResizable) {
                if (state.placement == WindowPlacement.Maximized) {
                    WindowControlButton(
                        Icons.Default.FilterNone,
                        contentDescription = stringResource(Res.string.restore_down),
                        onClick = { state.placement = WindowPlacement.Floating },
                    )
                } else {
                    WindowControlButton(
                        Icons.Default.CropSquare,
                        contentDescription = stringResource(Res.string.maximize),
                        onClick = { state.placement = WindowPlacement.Maximized },
                    )
                }
            }

            WindowControlButton(
                Icons.Default.Close,
                contentDescription = stringResource(Res.string.close),
                onClick = onCloseRequest,
            )
        }
    }
}

@Composable
private fun WindowControlButton(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    TextButton(
        modifier = Modifier.size(
            width = WINDOW_CONTROL_BUTTON_WIDTH,
            height = WINDOW_CONTROL_BUTTON_HEIGHT,
        ),
        shape = RoundedCornerShape(0.dp),
        contentPadding = PaddingValues(all = 0.dp),
        colors = ButtonDefaults.textButtonColors(
            contentColor = colorScheme.onSurface,
        ),
        onClick = onClick,
    ) {
        Icon(
            imageVector,
            modifier = Modifier.size(WINDOW_CONTROL_BUTTON_ICON_SIZE),
            contentDescription = contentDescription,
        )
    }
}

private fun calculateWindowPosition(
    offset: DpOffset,
    size: DpSize,
    screenMargin: Dp = WINDOW_MARGIN_DP,
): WindowPosition {
    val screen = GraphicsEnvironment.getLocalGraphicsEnvironment().maximumWindowBounds
    val bounds = object {
        val start = screen.x.dp + screenMargin
        val top = screen.y.dp + screenMargin
        val end = (screen.width.dp - size.width - screenMargin).run { max(this, start) }
        val bottom = (screen.height.dp - size.height - screenMargin).run { max(this, top) }
    }
    return WindowPosition(
        x = offset.x.coerceIn(bounds.start, bounds.end),
        y = offset.y.coerceIn(bounds.top, bounds.bottom),
    )
}
