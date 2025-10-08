package com.wisermit.hdrswitcher.system

import com.wisermit.hdrswitcher.system.process.PowerShell
import java.io.File

class SystemToolsImpl : SystemTools {

    // FIXME: Fix charset (™).
    override suspend fun getFileDescription(file: File) =
        PowerShell
            .run("(Get-Item \\\"$file\\\").VersionInfo.FileDescription")
            .firstOrNull()
}