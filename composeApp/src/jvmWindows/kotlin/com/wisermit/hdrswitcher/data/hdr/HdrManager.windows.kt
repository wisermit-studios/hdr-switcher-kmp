package com.wisermit.hdrswitcher.data.hdr

import com.wisermit.hdrswitcher.process.HdrManagerPowerShell
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal actual fun hdrManager(): HdrManager = WindowsHdrManager()

private class WindowsHdrManager : HdrManager {

    private val hdrStatus = MutableStateFlow<Boolean?>(null)

    override fun getHdrStatus(): Flow<Boolean?> = hdrStatus.asStateFlow()

    override suspend fun refreshHdrStatus() {
        hdrStatus.value = HdrManagerPowerShell.getHdrStatus()
    }

    override suspend fun setHdrStatus(enabled: Boolean) {
        refreshHdrStatus()
        if (hdrStatus.value != enabled) {
            HdrManagerPowerShell.toggleHdr()
            refreshHdrStatus()
        }
    }
}