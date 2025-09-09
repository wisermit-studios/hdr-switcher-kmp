package com.wisermit.hdrswitcher.infrastructure

import kotlinx.coroutines.flow.StateFlow
import java.io.File

interface SystemManager {

    fun getHdrStatus(): StateFlow<Boolean?>

    suspend fun setSystemHdr(enabled: Boolean)

    suspend fun getFileDescription(file: File): String?
}

