package com.wisermit.hdrswitcher.domain.system

import com.wisermit.hdrswitcher.domain.UseCase
import com.wisermit.hdrswitcher.system.SystemManager

class SetHdrEnabledUseCase(
    private val systemManagerService: SystemManager,
) : UseCase<Boolean, Unit>() {

    override suspend fun execute(parameters: Boolean) =
        systemManagerService.setHdrStatus(parameters)
}