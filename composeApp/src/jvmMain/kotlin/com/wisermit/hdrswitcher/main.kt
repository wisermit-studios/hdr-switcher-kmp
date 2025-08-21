package com.wisermit.hdrswitcher

import androidx.compose.ui.window.application
import com.wisermit.hdrswitcher.ui.App
import hdrswitcher.composeapp.generated.resources.Res
import hdrswitcher.composeapp.generated.resources.app_name
import hdrswitcher.composeapp.generated.resources.hdr_switcher
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

fun main() = application {
    FluentWindow(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.app_name),
        icon = painterResource(Res.drawable.hdr_switcher),
    ) {
        App()
    }
}