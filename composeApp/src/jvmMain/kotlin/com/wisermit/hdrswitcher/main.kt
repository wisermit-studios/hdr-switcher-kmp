package com.wisermit.hdrswitcher

import androidx.compose.ui.window.application
import com.wisermit.hdrswitcher.resources.Res
import com.wisermit.hdrswitcher.resources.app_name
import com.wisermit.hdrswitcher.resources.hdr_switcher
import com.wisermit.hdrswitcher.ui.App
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