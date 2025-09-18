package com.wisermit.hdrswitcher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import com.wisermit.hdrswitcher.di.AppModule
import com.wisermit.hdrswitcher.ui.FluentWindow
import com.wisermit.hdrswitcher.ui.SystemTray
import com.wisermit.hdrswitcher.ui.main.MainScreen
import com.wisermit.hdrswitcher.ui.theme.FluentTheme
import hdrswitcher.composeapp.generated.resources.Res
import hdrswitcher.composeapp.generated.resources.app_icon
import hdrswitcher.composeapp.generated.resources.app_name
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinApplication

private val WINDOW_SIZE = DpSize(
    width = 440.dp,
    height = 600.dp,
)

fun main() = application {
    KoinApplication(
        application = {
            modules(AppModule.modules)
        },
    ) {
        FluentTheme {
            var isVisible by remember { mutableStateOf(true) }

            SystemTray(
                onAction = {
                    isVisible = true
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
                    isVisible = false
                },
            ) {
                MainScreen()
            }
        }
    }
}