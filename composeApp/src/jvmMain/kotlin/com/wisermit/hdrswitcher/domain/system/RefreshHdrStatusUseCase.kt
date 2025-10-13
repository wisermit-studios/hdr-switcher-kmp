package com.wisermit.hdrswitcher.domain.system

import com.wisermit.hdrswitcher.domain.UseCase
import com.wisermit.hdrswitcher.system.SystemManager

class RefreshHdrStatusUseCase(
    private val systemManagerService: SystemManager,
) : UseCase<Unit, Unit>() {

    override suspend fun execute(parameters: Unit) =
        systemManagerService.refreshHdrStatus()
}