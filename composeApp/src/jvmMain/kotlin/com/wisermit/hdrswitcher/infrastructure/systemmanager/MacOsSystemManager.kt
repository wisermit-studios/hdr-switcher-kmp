package com.wisermit.hdrswitcher.infrastructure.systemmanager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

internal class MacOsSystemManager : SystemManager {

    override fun getHdrStatus(): StateFlow<Boolean?> = MutableStateFlow(null)

    override fun refreshHdrStatus() = Unit

    override fun toggleHdr() = Unit

    override suspend fun getFileDescription(file: File): String? = null
}