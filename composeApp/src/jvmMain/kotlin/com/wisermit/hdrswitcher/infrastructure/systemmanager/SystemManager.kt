package com.wisermit.hdrswitcher.infrastructure.systemmanager

import kotlinx.coroutines.flow.StateFlow
import java.io.File

interface SystemManager {

    fun getHdrStatus(): StateFlow<Boolean?>

    fun refreshHdrStatus()

    fun toggleHdr()

    suspend fun getFileDescription(file: File): String?
}