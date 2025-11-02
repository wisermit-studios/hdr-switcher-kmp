package com.wisermit.hdrswitcher.data

import com.wisermit.hdrswitcher.process.PowerShell
import java.io.File

class FilePropertiesProviderImpl : FilePropertiesProvider {

    // FIXME: Wrong charset (™).
    override suspend fun getFileDescription(file: File) =
        PowerShell
            .execute("(Get-Item \\\"$file\\\").VersionInfo.FileDescription")
            .firstOrNull()
}