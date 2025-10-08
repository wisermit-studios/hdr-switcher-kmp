package com.wisermit.hdrswitcher.service

import com.wisermit.hdrswitcher.system.SystemManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SystemManagerServiceImpl(
    private val systemManager: SystemManager,
) : SystemManagerService {
    private val hdrStatus = MutableStateFlow<Boolean?>(null)

    override fun getHdrStatus(): Flow<Boolean?> = hdrStatus.asStateFlow()

    override suspend fun refreshHdrStatus() {
        hdrStatus.value = systemManager.getHdrStatus()
    }

    override suspend fun setHdrStatus(enabled: Boolean) {
        refreshHdrStatus()
        if (hdrStatus.value != enabled) {
            systemManager.toggleHdr()
            refreshHdrStatus()
        }
    }
}