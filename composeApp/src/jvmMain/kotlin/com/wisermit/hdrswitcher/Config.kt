package com.wisermit.hdrswitcher

import com.wisermit.hdrswitcher.infrastructure.SystemInfo
import com.wisermit.hdrswitcher.utils.add
import java.io.File
import java.nio.file.Path

const val APP_SETTINGS_FOLDER_NAME = "HDR Switcher"
const val APPLICATIONS_FILE_NAME = "applications.json"

class Config(val systemInfo: SystemInfo) {

    val userDataPath: Path
        get() = systemInfo.appSettingsPath.add(APP_SETTINGS_FOLDER_NAME)

    val applicationsFile: File
        get() = userDataPath.add(APPLICATIONS_FILE_NAME).toFile()
}


