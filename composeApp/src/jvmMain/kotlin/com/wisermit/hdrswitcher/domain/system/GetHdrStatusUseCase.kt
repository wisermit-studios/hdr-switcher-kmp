package com.wisermit.hdrswitcher.domain.system

import com.wisermit.hdrswitcher.domain.FlowUseCase
import com.wisermit.hdrswitcher.system.SystemManager
import kotlinx.coroutines.flow.Flow

class GetHdrStatusUseCase(
    private val systemManagerService: SystemManager,
) : FlowUseCase<Unit, Boolean?>() {

    override fun execute(parameters: Unit): Flow<Boolean?> =
        systemManagerService.getHdrStatus()
}