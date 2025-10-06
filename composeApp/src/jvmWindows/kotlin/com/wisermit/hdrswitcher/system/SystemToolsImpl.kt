package com.wisermit.hdrswitcher.system

import com.wisermit.hdrswitcher.utils.PowerShell
import java.io.File

class SystemToolsImpl : SystemTools {

    // FIXME: Fix charset (™).
    override suspend fun getFileDescription(file: File) =
        PowerShell
            .exec("(Get-Item \\\"$file\\\").VersionInfo.FileDescription")
            .firstOrNull()
}