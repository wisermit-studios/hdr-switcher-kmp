package com.wisermit.hdrswitcher.service

import com.wisermit.hdrswitcher.model.Application
import com.wisermit.hdrswitcher.system.process.SystemManagerPowerShell
import com.wisermit.hdrswitcher.system.process.SystemManagerProcess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SystemManagerServiceImpl : SystemManagerService {

    private val hdrStatus = MutableStateFlow<Boolean?>(null)

    private var systemManagerExe: SystemManagerProcess? = null

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

    override suspend fun registerApplicationsSettings(applications: List<Application>) {
        systemManagerExe?.destroy()
        systemManagerExe = SystemManagerProcess.start {
            val args = applications.map { "${it.file.name}|${it.file.path}" }
            setArgs(*args.toTypedArray())
        }
    }
}