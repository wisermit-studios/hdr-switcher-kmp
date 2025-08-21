package com.wisermit.hdrswitcher

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import com.wisermit.hdrswitcher.ui.theme.AppTheme
import hdrswitcher.composeapp.generated.resources.Res
import hdrswitcher.composeapp.generated.resources.app_name
import hdrswitcher.composeapp.generated.resources.close
import hdrswitcher.composeapp.generated.resources.hdr_switcher
import hdrswitcher.composeapp.generated.resources.minimize
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import java.awt.Dimension
import java.awt.GraphicsEnvironment
import java.awt.Point

private val WINDOW_CORNER_RADIUS = 8.dp
private val WINDOW_BORDER_WIDTH = 0.5.dp
private val TITLE_BAR_HEIGHT = 44.dp
private val TITLE_BAR_FONT_SIZE = 14.sp
private val TITLE_BAR_ICON_SIZE = 28.dp
private val WINDOW_CONTROL_BUTTON_ICON_SIZE = 20.dp
private val WINDOW_CONTROL_BUTTON_WIDTH = 47.dp
private val WINDOW_CONTROL_BUTTON_HEIGHT = TITLE_BAR_HEIGHT

private const val WINDOW_WIDTH = 440
private const val WINDOW_HEIGHT = 600
private const val WINDOW_MARGIN = 4

@Composable
fun FluentWindow(
    onCloseRequest: () -> Unit,
    title: String,
    icon: Painter,
    content: @Composable FrameWindowScope.() -> Unit,
) {
    val windowPosition = getWindowPosition();

    Window(
        onCloseRequest = onCloseRequest,
        title = title,
        icon = icon,
        undecorated = true,
        transparent = true,
        resizable = false,
        state = rememberWindowState(
            width = WINDOW_WIDTH.dp,
            height = WINDOW_HEIGHT.dp,
            position = WindowPosition(windowPosition.x.dp, windowPosition.y.dp)
        ),
    ) {
        window.minimumSize = Dimension(WINDOW_WIDTH, WINDOW_HEIGHT)

        AppTheme {
            Column(
                Modifier.fillMaxSize()
                    .clip(RoundedCornerShape(WINDOW_CORNER_RADIUS))
                    .border(
                        WINDOW_BORDER_WIDTH,
                        colorScheme.outlineVariant,
                        RoundedCornerShape(WINDOW_CORNER_RADIUS),
                    )
                    .background(colorScheme.background),
            ) {
                TitleBar(
                    title = stringResource(Res.string.app_name),
                    onCloseRequest = onCloseRequest,
                )

                Box(
                    Modifier.weight(1f)
                ) {
                    content()
                }
            }
        }
    }
}

private fun getWindowPosition(): Point {
    val screenBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().maximumWindowBounds
    val x = screenBounds.width - WINDOW_WIDTH - WINDOW_MARGIN
    val y = screenBounds.height - WINDOW_HEIGHT - WINDOW_MARGIN
    return Point(x, y)
}

@Composable
private fun FrameWindowScope.TitleBar(
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
                painter = painterResource(Res.drawable.hdr_switcher),
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
                Icons.Filled.HorizontalRule,
                contentDescription = stringResource(Res.string.minimize),
                onClick = { window.isMinimized = true },
            )

//            if (window.placement == WindowPlacement.Maximized) {
//                WindowControlButton(
//                    Icons.Filled.FilterNone,
//                    contentDescription = stringResource(Res.string.restore_down),
//                    onClick = { window.placement = WindowPlacement.Floating },
//                )
//            } else {
//                WindowControlButton(
//                    Icons.Filled.CropSquare,
//                    contentDescription = stringResource(Res.string.maximize),
//                    onClick = { window.placement = WindowPlacement.Maximized },
//                )
//            }

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