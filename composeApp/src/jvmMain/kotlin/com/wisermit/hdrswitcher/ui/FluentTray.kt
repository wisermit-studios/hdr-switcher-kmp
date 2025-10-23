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
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.rememberDialogState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.wisermit.hdrswitcher.resources.Res
import com.wisermit.hdrswitcher.resources.app_icon
import com.wisermit.hdrswitcher.resources.app_name
import com.wisermit.hdrswitcher.resources.exit
import com.wisermit.hdrswitcher.system.Platform
import com.wisermit.hdrswitcher.widget.PopupMenuItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import java.awt.GraphicsEnvironment
import java.awt.MouseInfo
import java.awt.SystemTray
import java.awt.TrayIcon
import java.awt.event.MouseEvent
import java.awt.event.MouseListener

private val POPUP_SIZE = DpSize(width = 108.dp, height = 44.dp)
private val POPUP_PADDING = 4.dp

@Suppress("UnusedReceiverParameter")
@Composable
fun ApplicationScope.FluentTray(
    onAction: () -> Unit,
    onExit: () -> Unit,
) {
    var isOpen by remember { mutableStateOf(false) }
    val popupState = rememberDialogState(size = POPUP_SIZE)

    FluentTrayIcon(
        icon = painterResource(Res.drawable.app_icon),
        tooltip = stringResource(Res.string.app_name),
        onAction = onAction,
        onPopupMenuRequest = { position ->
            isOpen = true
            popupState.position = safeWindowPosition(position, POPUP_SIZE)
        },
    )

    FluentDialogWindow(
        state = popupState,
        visible = isOpen,
    ) {
        LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
            isOpen = false
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
private fun FluentTrayIcon(
    icon: Painter,
    tooltip: String,
    onAction: () -> Unit = {},
    onPopupMenuRequest: (position: DpOffset) -> Unit,
) {
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
                override fun mouseClicked(p0: MouseEvent) = Unit
                override fun mousePressed(p0: MouseEvent) = Unit
                override fun mouseEntered(p0: MouseEvent) = Unit
                override fun mouseExited(p0: MouseEvent) = Unit
                override fun mouseReleased(event: MouseEvent) {
                    if (event.isPopupTrigger) {
                        // Get Point from MouseInfo, since MouseEvent is inconsistent
                        // across Windows and macOS.
                        val position = MouseInfo.getPointerInfo().location
                            .run { DpOffset(x = x.dp, y = y.dp) }

                        onPopupMenuRequest(position)
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