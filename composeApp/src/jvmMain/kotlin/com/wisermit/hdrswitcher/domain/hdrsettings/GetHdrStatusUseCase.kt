package com.wisermit.hdrswitcher.domain.hdrsettings

import com.wisermit.hdrswitcher.domain.UnsafeFlowUseCase
import com.wisermit.hdrswitcher.infrastructure.systemmanager.SystemManager
import kotlinx.coroutines.flow.Flow

class GetHdrStatusUseCase(
    private val systemManager: SystemManager,
) : UnsafeFlowUseCase<Unit, Boolean?>() {

    override fun execute(parameters: Unit): Flow<Boolean?> =
        systemManager.getHdrStatus()
}