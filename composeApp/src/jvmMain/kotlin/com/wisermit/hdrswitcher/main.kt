package com.wisermit.hdrswitcher

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.wisermit.hdrswitcher.designsystem.components.PopupMenuItem
import com.wisermit.hdrswitcher.designsystem.theme.FluentTheme
import com.wisermit.hdrswitcher.di.AppModule
import com.wisermit.hdrswitcher.domain.storage.InitializeStoragesUseCase
import com.wisermit.hdrswitcher.resources.Res
import com.wisermit.hdrswitcher.resources.app_icon
import com.wisermit.hdrswitcher.resources.app_name
import com.wisermit.hdrswitcher.resources.exit
import com.wisermit.hdrswitcher.service.ApplicationsWatcherService
import com.wisermit.hdrswitcher.ui.ErrorDialogWindow
import com.wisermit.hdrswitcher.ui.FluentTray
import com.wisermit.hdrswitcher.ui.FluentWindow
import com.wisermit.hdrswitcher.ui.UiErrorData
import com.wisermit.hdrswitcher.ui.main.MainScreen
import com.wisermit.hdrswitcher.ui.safeWindowPosition
import com.wisermit.hdrswitcher.util.Log
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import java.awt.GraphicsEnvironment

private val WINDOW_SIZE = DpSize(
    width = 436.dp,
    height = 600.dp,
)

fun main() = application {

    Log.level = Log.Level.Test

    KoinApplication(
        application = {
            modules(AppModule.modules)
        },
    ) {
        val initializeStoragesUseCase = koinInject<InitializeStoragesUseCase>()
        val applicationsWatcherService = koinInject<ApplicationsWatcherService>()

        var initializationFailure by remember { mutableStateOf<Throwable?>(null) }

        LaunchedEffect(Unit) {
            initializeStoragesUseCase(Unit)
                .onFailure {
                    initializationFailure = it
                }
        }

        DisposableEffect(Unit) {
            applicationsWatcherService.start()
            onDispose {
                applicationsWatcherService.destroy()
            }
        }

        FluentTheme {
            val requestWindowFocus = remember { Channel<Unit>() }
            var isVisible by remember { mutableStateOf(true) }

            val state = rememberWindowState(
                size = WINDOW_SIZE,
                position = safeWindowPosition(screenBottomRight, WINDOW_SIZE)
            )

            FluentTray(
                icon = painterResource(Res.drawable.app_icon),
                tooltip = stringResource(Res.string.app_name),
                onAction = {
                    if (isVisible) {
                        requestWindowFocus.trySend(Unit)
                    } else {
                        isVisible = true
                    }
                },
            ) {
                PopupMenuItem(
                    icon = Icons.Default.Close,
                    title = stringResource(Res.string.exit),
                    onClick = ::exitApplication,
                )
            }

            initializationFailure?.let {
                ErrorDialogWindow(
                    data = UiErrorData.from(it),
                    onCloseRequest = ::exitApplication
                )
            } ?: run {
                FluentWindow(
                    state = state,
                    visible = isVisible,
                    title = stringResource(Res.string.app_name),
                    icon = painterResource(Res.drawable.app_icon),
                    minimumSize = WINDOW_SIZE,
                    resizable = false,
                    onCloseRequest = {
                        // TODO: Create config to close or minimize to systray.
//                    isVisible = false
                        exitApplication()
                    },
                ) {
                    val coroutineScope = rememberCoroutineScope()

                    LaunchedEffect(Unit) {
                        requestWindowFocus.receiveAsFlow()
                            .onEach { window.requestFocus() }
                            .launchIn(coroutineScope)
                    }

                    MainScreen()
                }
            }
        }
    }
}

private val screenBottomRight: DpOffset
    get() = GraphicsEnvironment.getLocalGraphicsEnvironment().maximumWindowBounds
        .run { DpOffset(width.dp, height.dp) }