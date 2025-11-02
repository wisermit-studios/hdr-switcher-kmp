package com.wisermit.hdrswitcher

import java.io.File

internal val platform = Platform.MacOS

internal class SystemInfoImpl : SystemInfo() {
    override val systemDrive: File = File("/")

    override val appSettingsDir: File
        get() = File(userHomeDir, "Library/Application Support")

    override val applicationExtension = "app"
}