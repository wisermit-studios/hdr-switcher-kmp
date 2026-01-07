package com.wisermit.hdrswitcher.service

import kotlinx.coroutines.flow.StateFlow
import org.koin.core.scope.Scope

internal expect fun Scope.hdrSwitcherService(): HdrSwitcherService

interface HdrSwitcherService {
    val status: StateFlow<Status>

    fun start()
    fun stop()

    enum class Status {
        Active, Suspended, Stopped, Error
    }
}