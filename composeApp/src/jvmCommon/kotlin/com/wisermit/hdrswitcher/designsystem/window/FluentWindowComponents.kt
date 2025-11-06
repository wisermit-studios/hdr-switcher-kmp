package com.wisermit.hdrswitcher.designsystem.window

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.FilterNone
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import com.wisermit.hdrswitcher.designsystem.theme.FluentTheme
import com.wisermit.hdrswitcher.resources.Res
import com.wisermit.hdrswitcher.resources.close
import com.wisermit.hdrswitcher.resources.maximize
import com.wisermit.hdrswitcher.resources.minimize
import com.wisermit.hdrswitcher.resources.restore_down
import com.wisermit.hdrswitcher.util.surface
import org.jetbrains.compose.resources.stringResource
import java.awt.GraphicsEnvironment

@Composable
internal fun WindowSurface(content: @Composable () -> Unit) {
    Surface(
        color = colorScheme.background,
        border = BorderStroke(
            width = WindowDefaults.BorderStrokeWidth,
            color = FluentTheme.colors.outlineLow
        ),
        shape = RoundedCornerShape(WindowDefaults.CornerRadius),
        content = content,
    )
}

@Composable
internal fun WindowScope.WindowTitleBar(
    windowState: WindowState? = null,
    titleBarHeight: Dp,
    title: String = "",
    icon: Painter? = null,
    onCloseRequest: () -> Unit,
) {
    WindowDraggableArea {
        Row(
            modifier = Modifier
                .height(titleBarHeight)
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    modifier = Modifier
                        .size(WindowDefaults.TitleBarIconSize)
                        .padding(end = 8.dp),
                    painter = icon,
                    tint = Color.Unspecified,
                    contentDescription = null,
                )
            }

            Text(
                modifier = Modifier.weight(1f),
                text = title,
                fontSize = WindowDefaults.TitleBarFontSize,
                color = colorScheme.onSurfaceVariant
            )

            windowState?.let {
                WindowControlButton(
                    Icons.Default.HorizontalRule,
                    contentDescription = stringResource(Res.string.minimize),
                    onClick = { windowState.isMinimized = true },
                )


                if ((this as? FrameWindowScope)?.window?.isResizable == true) {
                    if (windowState.placement == WindowPlacement.Maximized) {
                        WindowControlButton(
                            Icons.Default.FilterNone,
                            contentDescription = stringResource(Res.string.restore_down),
                            onClick = { windowState.placement = WindowPlacement.Floating },
                        )
                    } else {
                        WindowControlButton(
                            Icons.Default.CropSquare,
                            contentDescription = stringResource(Res.string.maximize),
                            onClick = { windowState.placement = WindowPlacement.Maximized },
                        )
                    }
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun WindowControlButton(
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
                    .surface(shadowElevation = 8.dp)
                    .padding(8.dp),
                style = typography.bodySmall
            )
        },
    ) {
        Icon(
            imageVector,
            modifier = Modifier
                .aspectRatio(1f)
                .clickable(
                    role = Role.Button,
                    onClick = onClick
                )
                .wrapContentSize()
                .size(WindowDefaults.ControlButtonIconSize),
            contentDescription = contentDescription,
        )
    }
}

internal fun safeWindowPosition(
    position: DpOffset,
    windowSize: DpSize,
    screenMargin: Dp = WindowDefaults.Margin,
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

private object WindowDefaults {
    val Margin = 6.dp
    val CornerRadius = 8.dp
    val BorderStrokeWidth = 1.dp

    val TitleBarFontSize = 14.sp
    val TitleBarIconSize = 28.dp
    val ControlButtonIconSize = 20.dp
}