@file:Suppress("UnusedReceiverParameter")

package com.wisermit.hdrswitcher.util

import java.io.File

private object WindowsAppResources : CommonAppResources() {
    val systemManagerExe: File = binDir.resolve("system_manager.exe")
}

val AppResources.systemManagerExe get() = WindowsAppResources.systemManagerExe