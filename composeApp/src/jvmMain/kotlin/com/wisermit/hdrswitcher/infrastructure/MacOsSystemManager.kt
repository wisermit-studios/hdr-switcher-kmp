package com.wisermit.hdrswitcher.infrastructure

import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File

internal class MacOsSystemManager : SystemManager {

    override fun getHdrStatus() = MutableStateFlow(null)

    override suspend fun setSystemHdr(enabled: Boolean) = Unit

    override suspend fun getFileDescription(file: File) = null
}