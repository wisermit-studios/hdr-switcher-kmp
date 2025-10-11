package com.wisermit.hdrswitcher.system

import com.wisermit.hdrswitcher.system.process.SystemManagerPowerShell
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SystemManagerImpl : SystemManager {

    private val hdrStatus = MutableStateFlow<Boolean?>(null)

    override fun getHdrStatus(): Flow<Boolean?> = hdrStatus.asStateFlow()

    override suspend fun refreshHdrStatus() {
        hdrStatus.value = SystemManagerPowerShell.getHdrStatus()
    }

    override suspend fun setHdrStatus(enabled: Boolean) {
        refreshHdrStatus()
        if (hdrStatus.value != enabled) {
            SystemManagerPowerShell.toggleHdr()
            refreshHdrStatus()
        }
    }
}