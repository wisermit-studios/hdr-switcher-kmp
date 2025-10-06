package com.wisermit.hdrswitcher.domain.hdrsettings

import com.wisermit.hdrswitcher.domain.UnsafeFlowUseCase
import com.wisermit.hdrswitcher.service.SystemManagerService
import kotlinx.coroutines.flow.Flow

class GetHdrStatusUseCase(
    private val systemManagerService: SystemManagerService,
) : UnsafeFlowUseCase<Unit, Boolean?>() {

    override fun execute(parameters: Unit): Flow<Boolean?> =
        systemManagerService.getHdrStatus()
}