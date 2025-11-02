package com.wisermit.hdrswitcher

import com.sun.jna.platform.win32.KnownFolders
import com.sun.jna.platform.win32.Shell32Util
import java.io.File

internal val platform = Platform.Windows

internal class SystemInfoImpl : SystemInfo() {
    override val systemDrive: File = File(System.getenv("SystemDrive"))

    override val appSettingsDir: File
        get() = Shell32Util.getKnownFolderPath(KnownFolders.FOLDERID_Documents)
            ?.let(::File)
            ?: File(userHomeDir, "Documents")

    override val applicationExtension = "exe"
}