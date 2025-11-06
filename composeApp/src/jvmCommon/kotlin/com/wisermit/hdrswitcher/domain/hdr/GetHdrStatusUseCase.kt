package com.wisermit.hdrswitcher.domain.hdr

import com.wisermit.hdrswitcher.data.hdr.HdrManager
import com.wisermit.hdrswitcher.domain.FlowUseCase
import kotlinx.coroutines.flow.Flow

class GetHdrStatusUseCase(
    private val hdrManager: HdrManager,
) : FlowUseCase<Unit, Boolean?>() {

    override fun execute(parameters: Unit): Flow<Boolean?> =
        hdrManager.getHdrStatus()
}