package com.wisermit.hdrswitcher.data.hdr

import kotlinx.coroutines.flow.emptyFlow

internal actual fun hdrManager(): HdrManager = MacOsHdrManager()

class MacOsHdrManager : HdrManager {

    override fun getHdrStatus() = emptyFlow<Boolean?>()

    override suspend fun refreshHdrStatus() = Unit

    override suspend fun setHdrStatus(enabled: Boolean) = Unit
}