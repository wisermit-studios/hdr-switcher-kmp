package com.wisermit.hdrswitcher.domain.hdrsettings

import com.wisermit.hdrswitcher.domain.UseCase
import com.wisermit.hdrswitcher.service.SystemManagerService

class SetHdrEnabledUseCase(
    private val systemManagerService: SystemManagerService,
) : UseCase<Boolean, Unit>() {

    override suspend fun execute(parameters: Boolean) =
        systemManagerService.setHdrStatus(parameters)
}