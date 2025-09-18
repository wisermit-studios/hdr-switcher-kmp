package com.wisermit.hdrswitcher.infrastructure.systemmanager

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.io.File

internal class MacOsSystemManager : SystemManager {

    override fun getHdrStatus(): Flow<Boolean?> = emptyFlow()

    override fun refreshHdrStatus() = Unit

    override fun toggleHdr() = Unit

    override suspend fun getFileDescription(file: File): String? = null
}