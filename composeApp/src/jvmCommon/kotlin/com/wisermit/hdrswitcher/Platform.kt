package com.wisermit.hdrswitcher

import java.io.File

enum class Platform {
    Windows, MacOS;
}

expect val platform: Platform

internal expect fun systemInfo(): SystemInfo

interface SystemInfo {
    val systemDrive: File

    val userHomeDir: File get() = File(System.getProperty("user.home"))

    val appSettingsDir: File

    val applicationExtension: String
}