package com.wisermit.hdrswitcher

import java.io.File

enum class Platform {
    Windows, MacOS;

    companion object {
        val current: Platform = platform
    }
}

abstract class SystemInfo {
    abstract val systemDrive: File

    val userHomeDir: File get() = File(System.getProperty("user.home"))

    abstract val appSettingsDir: File

    abstract val applicationExtension: String
}