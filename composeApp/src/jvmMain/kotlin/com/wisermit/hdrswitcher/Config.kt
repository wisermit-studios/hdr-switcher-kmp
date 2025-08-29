package com.wisermit.hdrswitcher

import com.sun.jna.platform.win32.KnownFolders
import com.sun.jna.platform.win32.Shell32Util
import com.wisermit.hdrswitcher.utils.SystemInfo
import java.io.File

class Config {

    companion object {
        private const val USER_DATA_FOLDER_NAME = "HDR Switcher"

        private val userDocumentsPath
            get() = Shell32Util.getKnownFolderPath(KnownFolders.FOLDERID_Documents)
                ?: "${SystemInfo.userHomePath}\\Documents"

        private val userDataPath get() = "$userDocumentsPath\\$USER_DATA_FOLDER_NAME"

        val applicationsFile get() = File("${userDataPath}\\applications.json")
    }
}