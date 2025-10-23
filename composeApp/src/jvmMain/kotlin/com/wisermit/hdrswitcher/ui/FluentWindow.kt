package com.wisermit.hdrswitcher.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.MaterialTheme.typography
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
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberDialogState
import androidx.compose.ui.window.rememberWindowState
import com.wisermit.hdrswitcher.resources.Res
import com.wisermit.hdrswitcher.resources.app_icon
import com.wisermit.hdrswitcher.resources.close
import com.wisermit.hdrswitcher.resources.maximize
import com.wisermit.hdrswitcher.resources.minimize
import com.wisermit.hdrswitcher.resources.restore_down
import com.wisermit.hdrswitcher.ui.theme.ThemeDefaults
import com.wisermit.hdrswitcher.util.surface
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import java.awt.Dimension
import java.awt.GraphicsEnvironment

object FluentWindowDefaults {
    val margin = 4.dp
    val cornerRadius = 8.dp
    val TitleBarHeight = 44.dp
    val TitleBarFontSize = 14.sp
    val TitleBarIconSize = 28.dp
    val ControlButtonIconSize = 20.dp
    val ControlButtonWidth = 47.dp
    val TooltipShadowElevation = 8.dp
    val TooltipPadding = 8.dp
}

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
fun FluentDialogWindow(
    state: DialogState = rememberDialogState(),
    visible: Boolean = true,
    content: @Composable WindowScope.() -> Unit,
) {
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
        border = BorderStroke(ThemeDefaults.BorderStrokeWidth, colorScheme.outlineVariant),
        shape = RoundedCornerShape(FluentWindowDefaults.cornerRadius),
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
                .height(FluentWindowDefaults.TitleBarHeight)
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(FluentWindowDefaults.TitleBarIconSize)
                    .padding(end = 8.dp),
                painter = painterResource(Res.drawable.app_icon),
                tint = Color.Unspecified,
                contentDescription = null,
            )

            Text(
                modifier = Modifier.weight(1f),
                text = title,
                fontSize = FluentWindowDefaults.TitleBarFontSize,
                color = colorScheme.onSurfaceVariant
            )

            ControlButton(
                Icons.Default.HorizontalRule,
                contentDescription = stringResource(Res.string.minimize),
                onClick = { state.isMinimized = true },
            )

            if (window.isResizable) {
                if (state.placement == WindowPlacement.Maximized) {
                    ControlButton(
                        Icons.Default.FilterNone,
                        contentDescription = stringResource(Res.string.restore_down),
                        onClick = { state.placement = WindowPlacement.Floating },
                    )
                } else {
                    ControlButton(
                        Icons.Default.CropSquare,
                        contentDescription = stringResource(Res.string.maximize),
                        onClick = { state.placement = WindowPlacement.Maximized },
                    )
                }
            }

            ControlButton(
                Icons.Default.Close,
                contentDescription = stringResource(Res.string.close),
                onClick = onCloseRequest,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ControlButton(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    TooltipArea(
        tooltipPlacement = TooltipPlacement.ComponentRect(),
        delayMillis = 1000,
        tooltip = {
            Text(
                text = contentDescription,
                modifier = Modifier
                    .surface(
                        shadowElevation = FluentWindowDefaults.TooltipShadowElevation,
                    )
                    .padding(FluentWindowDefaults.TooltipPadding),
                style = typography.bodySmall
            )
        },
    ) {
        TextButton(
            modifier = Modifier
                .width(width = FluentWindowDefaults.ControlButtonWidth),
            shape = RoundedCornerShape(0.dp),
            colors = ButtonDefaults.textButtonColors(
                contentColor = colorScheme.onSurface,
            ),
            onClick = onClick,
        ) {
            Icon(
                imageVector,
                modifier = Modifier.size(FluentWindowDefaults.ControlButtonIconSize),
                contentDescription = contentDescription,
            )
        }
    }
}

fun safeWindowPosition(
    position: DpOffset,
    windowSize: DpSize,
    screenMargin: Dp = FluentWindowDefaults.margin,
): WindowPosition {
    val safeScreenBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().maximumWindowBounds
    val safePositionArea = with(safeScreenBounds) {
        object {
            val left = x.dp + screenMargin
            val top = y.dp + screenMargin
            val right = (maxX.dp - windowSize.width - screenMargin).coerceAtLeast(left)
            val bottom = (maxY.dp - windowSize.height - screenMargin).coerceAtLeast(top)
        }
    }
    return WindowPosition(
        x = position.x.coerceIn(safePositionArea.left, safePositionArea.right),
        y = position.y.coerceIn(safePositionArea.top, safePositionArea.bottom),
    )
}