package com.wisermit.hdrswitcher.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import com.wisermit.hdrswitcher.infrastructure.Platform
import com.wisermit.hdrswitcher.widget.PopupMenuItem
import hdrswitcher.composeapp.generated.resources.Res
import hdrswitcher.composeapp.generated.resources.app_icon
import hdrswitcher.composeapp.generated.resources.app_name
import hdrswitcher.composeapp.generated.resources.exit
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import java.awt.GraphicsEnvironment
import java.awt.SystemTray
import java.awt.TrayIcon
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.WindowEvent
import java.awt.event.WindowFocusListener

private val POPUP_SIZE = DpSize(width = 108.dp, height = 44.dp)
private val POPUP_PADDING = 4.dp

@Suppress("UnusedReceiverParameter")
@Composable
fun ApplicationScope.SystemTray(
    onAction: () -> Unit,
    onExit: () -> Unit,
) {
    var isOpen by remember { mutableStateOf(false) }
    var popupAnchor by remember { mutableStateOf(DpOffset.Zero) }

    FluentTray(
        icon = painterResource(Res.drawable.app_icon),
        tooltip = stringResource(Res.string.app_name),
        onAction = onAction,
        onPopupMenuRequest = { offset ->
            popupAnchor = offset
            isOpen = true
        },
    )

    FluentPopupWindow(
        visible = isOpen,
        size = POPUP_SIZE,
        anchor = popupAnchor,
    ) {
        DisposableEffect(Unit) {
            val listener = object : WindowFocusListener {
                override fun windowGainedFocus(e: WindowEvent?) {}
                override fun windowLostFocus(e: WindowEvent?) {
                    isOpen = false
                }
            }

            window.addWindowFocusListener(listener)
            onDispose {
                window.removeWindowFocusListener(listener)
            }
        }

        Column(Modifier.padding(POPUP_PADDING)) {
            PopupMenuItem(
                icon = Icons.Default.Close,
                title = stringResource(Res.string.exit),
                onClick = onExit,
            )
        }
    }
}

@Composable
private fun FluentTray(
    icon: Painter,
    tooltip: String,
    onAction: () -> Unit = {},
    onPopupMenuRequest: (offset: DpOffset) -> Unit,
) {
    val density = LocalDensity.current
    val currentOnAction by rememberUpdatedState(onAction)

    val awtIcon = remember(icon) {
        icon.toAwtImage(GlobalDensity, LayoutDirection.Rtl, iconSize)
    }

    val tray = remember {
        TrayIcon(awtIcon).apply {
            isImageAutoSize = true

            addActionListener {
                currentOnAction()
            }

            addMouseListener(object : MouseListener {
                override fun mouseClicked(p0: MouseEvent?) {}
                override fun mousePressed(p0: MouseEvent?) {}
                override fun mouseEntered(p0: MouseEvent?) {}
                override fun mouseExited(p0: MouseEvent?) {}
                override fun mouseReleased(event: MouseEvent?) {
                    event?.let {
                        if (event.button == 3) {
                            val offset = with(density) {
                                DpOffset(
                                    x = event.x.toDp(),
                                    y = event.y.toDp(),
                                )
                            }
                            onPopupMenuRequest(offset)
                        }
                    }
                }
            })
        }
    }

    SideEffect {
        if (tray.image != awtIcon) tray.image = awtIcon
        if (tray.toolTip != tooltip) tray.toolTip = tooltip
    }

    DisposableEffect(Unit) {
        SystemTray.getSystemTray().add(tray)

        onDispose {
            SystemTray.getSystemTray().remove(tray)
        }
    }
}

private val GlobalDensity: Density
    get() = GraphicsEnvironment.getLocalGraphicsEnvironment()
        .defaultScreenDevice
        .defaultConfiguration
        .defaultTransform
        .run { Density(scaleX.toFloat(), fontScale = 1f) }

private val iconSize: Size
    get() {
        return when (Platform.Current) {
            Platform.Windows -> Size(16f, 16f)
            Platform.MacOS -> Size(22f, 22f)
        }
    }