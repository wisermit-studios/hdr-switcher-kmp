package com.wisermit.hdrswitcher

import com.sun.jna.platform.win32.KnownFolders
import com.sun.jna.platform.win32.Shell32Util
import com.wisermit.hdrswitcher.utils.add
import java.io.File
import java.nio.file.Path

const val APP_SETTINGS_FOLDER_NAME = "HDR Switcher"
const val APPLICATIONS_FILE_NAME = "applications.json"

abstract class Config() {

    abstract val userDataRoot: Path

    val userDataPath: Path
        get() = userDataRoot.add(APP_SETTINGS_FOLDER_NAME)

    val applicationsFile: File
        get() = userDataPath.add(APPLICATIONS_FILE_NAME).toFile()
}

class ConfigWindows : Config() {
    override val userDataRoot: Path
        get() = Shell32Util.getKnownFolderPath(KnownFolders.FOLDERID_Documents)
            ?.let { Path.of(it) }
            ?: SystemInfo.userHomePath.add("Documents")
}

class ConfigMacOs : Config() {
    override val userDataRoot: Path
        get() = SystemInfo.userHomePath
            .add("Library")
            .add("Application Support")
}


