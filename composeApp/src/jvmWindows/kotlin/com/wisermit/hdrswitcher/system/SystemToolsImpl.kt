package com.wisermit.hdrswitcher.system

import com.wisermit.hdrswitcher.system.process.PowerShell
import java.io.File

class SystemToolsImpl : SystemTools {

    // FIXME: Wrong charset (™).
    override suspend fun getFileDescription(file: File) =
        PowerShell
            .execute("(Get-Item \\\"$file\\\").VersionInfo.FileDescription")
            .firstOrNull()
}