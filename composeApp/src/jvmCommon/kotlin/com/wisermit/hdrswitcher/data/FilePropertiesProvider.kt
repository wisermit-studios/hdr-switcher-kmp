package com.wisermit.hdrswitcher.data

import java.io.File

internal expect fun filePropertiesProvider(): FilePropertiesProvider

interface FilePropertiesProvider {
    suspend fun getFileDescription(file: File): String?
}