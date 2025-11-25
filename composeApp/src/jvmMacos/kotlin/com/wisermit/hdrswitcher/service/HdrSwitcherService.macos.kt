package com.wisermit.hdrswitcher.service

import org.koin.core.scope.Scope

internal actual fun Scope.hdrSwitcherService(): HdrSwitcherService = MacOsHdrSwitcherService()

internal class MacOsHdrSwitcherService : HdrSwitcherService {
    override fun start() = Unit
    override fun stop() = Unit
}