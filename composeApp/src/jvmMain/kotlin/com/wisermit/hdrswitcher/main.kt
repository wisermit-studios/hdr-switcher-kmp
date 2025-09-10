package com.wisermit.hdrswitcher

import androidx.compose.ui.window.application
import com.wisermit.hdrswitcher.di.AppModule
import com.wisermit.hdrswitcher.ui.FluentWindow
import com.wisermit.hdrswitcher.ui.main.MainScreen
import com.wisermit.hdrswitcher.ui.theme.FluentTheme
import hdrswitcher.composeapp.generated.resources.Res
import hdrswitcher.composeapp.generated.resources.app_name
import hdrswitcher.composeapp.generated.resources.hdr_switcher
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.context.startKoin

fun main() = application {
    startKoin {
        modules(AppModule.modules)
    }
    FluentTheme {
        FluentWindow(
            onCloseRequest = ::exitApplication,
            title = stringResource(Res.string.app_name),
            icon = painterResource(Res.drawable.hdr_switcher),
        ) {
            MainScreen()
        }
    }
}