package com.wisermit.hdrswitcher.data

import java.io.File

interface FilePropertiesProvider {
    suspend fun getFileDescription(file: File): String?
}