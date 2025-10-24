package com.wisermit.hdrswitcher

import com.wisermit.hdrswitcher.system.SystemInfo
import java.io.File

private const val APP_SETTINGS_FOLDER_NAME = "HDR Switcher"
private const val APPLICATIONS_FILE_NAME = "applications.json"

interface Configuration {
    val userDataDir: File
    val applicationsFile: File
}

class ConfigurationImpl(val systemInfo: SystemInfo) : Configuration {

    override val userDataDir: File
        get() = File(systemInfo.appSettingsDir, APP_SETTINGS_FOLDER_NAME)

    override val applicationsFile: File
        get() = File(userDataDir, APPLICATIONS_FILE_NAME)
}
