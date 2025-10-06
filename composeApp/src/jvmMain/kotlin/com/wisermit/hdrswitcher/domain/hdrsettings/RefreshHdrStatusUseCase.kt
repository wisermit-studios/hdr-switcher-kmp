package com.wisermit.hdrswitcher.domain.hdrsettings

import com.wisermit.hdrswitcher.domain.UseCase
import com.wisermit.hdrswitcher.service.SystemManagerService

class RefreshHdrStatusUseCase(
    private val systemManagerService: SystemManagerService,
) : UseCase<Unit, Unit>() {

    override suspend fun execute(parameters: Unit) =
        systemManagerService.refreshHdrStatus()
}