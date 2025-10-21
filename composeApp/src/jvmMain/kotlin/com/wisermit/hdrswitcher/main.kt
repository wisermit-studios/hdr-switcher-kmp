package com.wisermit.hdrswitcher

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import com.wisermit.hdrswitcher.di.AppModule
import com.wisermit.hdrswitcher.domain.storage.InitializeStoragesUseCase
import com.wisermit.hdrswitcher.resources.Res
import com.wisermit.hdrswitcher.resources.app_icon
import com.wisermit.hdrswitcher.resources.app_name
import com.wisermit.hdrswitcher.service.ApplicationsWatcherService
import com.wisermit.hdrswitcher.ui.FluentWindow
import com.wisermit.hdrswitcher.ui.SystemTray
import com.wisermit.hdrswitcher.ui.main.MainScreen
import com.wisermit.hdrswitcher.ui.theme.FluentTheme
import com.wisermit.hdrswitcher.util.DialogUtils
import com.wisermit.hdrswitcher.util.Log
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

private val WINDOW_SIZE = DpSize(
    width = 440.dp,
    height = 600.dp,
)

fun main() = application {

    Log.level = Log.Level.Test

    KoinApplication(
        application = {
            modules(AppModule.modules)
        },
    ) {
        val initializeStorages = koinInject<InitializeStoragesUseCase>()
        val applicationsWatcherService = koinInject<ApplicationsWatcherService>()

        LaunchedEffect(Unit) {
            initializeStorages(Unit)
                .onFailure {
                    DialogUtils.showErrorDialogFor(it)
                    exitApplication()
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

            SystemTray(
                onAction = {
                    if (isVisible) {
                        requestWindowFocus.trySend(Unit)
                    } else {
                        isVisible = true
                    }
                },
                onExit = ::exitApplication,
            )

            FluentWindow(
                visible = isVisible,
                title = stringResource(Res.string.app_name),
                icon = painterResource(Res.drawable.app_icon),
                size = WINDOW_SIZE,
                minimumSize = WINDOW_SIZE,
                resizable = false,
                onCloseRequest = {
                    // TODO: Create config to hide instead of closing.
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