package com.wisermit.hdrswitcher.system

import java.io.File

interface SystemTools {
    suspend fun getFileDescription(file: File): String?
}