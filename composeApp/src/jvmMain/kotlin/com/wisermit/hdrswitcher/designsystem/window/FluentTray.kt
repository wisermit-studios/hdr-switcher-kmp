package com.wisermit.hdrswitcher.designsystem.window

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.wisermit.hdrswitcher.system.Platform
import java.awt.GraphicsEnvironment
import java.awt.MouseInfo
import java.awt.SystemTray
import java.awt.TrayIcon
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

private val PopupSize = DpSize(width = 108.dp, height = 44.dp)
private val PopupPadding = 4.dp

@Suppress("UnusedReceiverParameter")
@Composable
fun ApplicationScope.FluentTray(
    icon: Painter,
    tooltip: String,
    onAction: () -> Unit = {},
    menu: @Composable () -> Unit = {},
) {
    var isOpen by remember { mutableStateOf(false) }
    val popupState = rememberDialogState(size = PopupSize)

    FluentTrayIcon(
        icon = icon,
        tooltip = tooltip,
        onAction = onAction,
        onPopupMenuRequest = { position ->
            isOpen = true
            popupState.position = safeWindowPosition(position, popupState.size)
        },
    )

    FluentDialogWindow(
        state = popupState,
        visible = isOpen,
        titleBarEnabled = false,
        onCloseRequest = {}
    ) {
        LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
            isOpen = false
        }

        Column(
            Modifier
                .width(PopupSize.width)
                .padding(PopupPadding)
        ) {
            menu()
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
    val currentOnPopupMenuRequest by rememberUpdatedState(onPopupMenuRequest)

    val awtIcon = remember(icon) {
        icon.toAwtImage(GlobalDensity, LayoutDirection.Rtl, iconSize)
    }

    val tray = remember {
        TrayIcon(awtIcon).apply {
            isImageAutoSize = true

            addActionListener {
                currentOnAction()
            }

            addMouseListener { event ->
                if (event.isPopupTrigger) {
                    // Get Point from MouseInfo, since MouseEvent is inconsistent
                    // across Windows and macOS.
                    val position = MouseInfo.getPointerInfo().location
                        .run { DpOffset(x = x.dp, y = y.dp) }

                    currentOnPopupMenuRequest(position)
                }
            }
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

private fun TrayIcon.addMouseListener(block: (MouseEvent) -> Unit) {
    addMouseListener(object : MouseAdapter() {
        override fun mousePressed(e: MouseEvent) = block(e)
        override fun mouseReleased(e: MouseEvent) = block(e)
    })
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