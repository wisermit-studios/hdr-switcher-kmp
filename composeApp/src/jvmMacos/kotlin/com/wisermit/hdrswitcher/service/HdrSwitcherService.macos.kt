package com.wisermit.hdrswitcher.service

import com.wisermit.hdrswitcher.service.HdrSwitcherService.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.scope.Scope

internal actual fun Scope.hdrSwitcherService(): HdrSwitcherService = MacOsHdrSwitcherService()

internal class MacOsHdrSwitcherService : HdrSwitcherService {
    override fun start() = Unit
    override fun stop() = Unit
    override val status: StateFlow<Status> = MutableStateFlow(Status.Stopped)
}