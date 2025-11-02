package com.wisermit.hdrswitcher.util

import java.io.File

abstract class PlatformAppResources : CommonAppResources() {
    val systemManagerExe: File = binDir.resolve("system_manager.exe")
}