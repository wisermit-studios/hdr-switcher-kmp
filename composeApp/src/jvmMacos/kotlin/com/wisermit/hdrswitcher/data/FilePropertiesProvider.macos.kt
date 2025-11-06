package com.wisermit.hdrswitcher.data

import java.io.File

internal actual fun filePropertiesProvider(): FilePropertiesProvider =
    MacOsFilePropertiesProvider()

class MacOsFilePropertiesProvider : FilePropertiesProvider {
    override suspend fun getFileDescription(file: File) = null
}