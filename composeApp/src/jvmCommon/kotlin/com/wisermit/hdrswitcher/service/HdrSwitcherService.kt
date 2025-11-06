package com.wisermit.hdrswitcher.service

import org.koin.core.scope.Scope

internal expect fun Scope.hdrSwitcherService(): HdrSwitcherService

interface HdrSwitcherService {
    fun start()
    fun stop()
}