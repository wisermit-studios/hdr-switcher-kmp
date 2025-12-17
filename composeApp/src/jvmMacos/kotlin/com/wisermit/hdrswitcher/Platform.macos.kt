package com.wisermit.hdrswitcher

import java.io.File

actual val platform = Platform.MacOS

internal actual fun systemInfo(): SystemInfo = MacOsSystemInfo()

internal class MacOsSystemInfo : SystemInfo {
    override val systemDrive: File = File("/")

    override val applicationDataDir: File
        get() = File(userHomeDir, "Library/Application Support")

    override val applicationExtension = "app"
}