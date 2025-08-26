package com.wisermit.hdrswitcher

import com.sun.jna.platform.win32.KnownFolders
import com.sun.jna.platform.win32.Shell32Util

class AppConfig {

    companion object {
        const val SETTINGS_FOLDER_NAME = "HDR Switcher"

        val USER_DOCUMENTS_PATH =
            Shell32Util.getKnownFolderPath(KnownFolders.FOLDERID_Documents)
                ?: "${System.getProperty("user.home")}\\Documents"

        val SETTINGS_PATH = "$USER_DOCUMENTS_PATH\\$SETTINGS_FOLDER_NAME"

        const val APPLICATIONS_SETTINGS_FILE_NAME = "ApplicationsSettings.ini"
        val APPLICATIONS_SETTINGS_PATH = "${SETTINGS_PATH}\\${APPLICATIONS_SETTINGS_FILE_NAME}"
    }

}