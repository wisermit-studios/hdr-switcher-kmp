package com.wisermit.hdrswitcher.infrastructure

import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File

internal class MacOsSystemManager : SystemManager {

    override fun isHdrEnabled() = MutableStateFlow(null)

    override suspend fun setSystemHdr(enabled: Boolean) = Unit

    override suspend fun getFileDescription(file: File) = null
}