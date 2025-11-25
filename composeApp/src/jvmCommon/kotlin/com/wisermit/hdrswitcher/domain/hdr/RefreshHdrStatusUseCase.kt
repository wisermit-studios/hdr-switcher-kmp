package com.wisermit.hdrswitcher.domain.hdr

import com.wisermit.hdrswitcher.data.hdr.HdrManager
import com.wisermit.hdrswitcher.domain.UseCase

class RefreshHdrStatusUseCase(
    private val hdrManager: HdrManager,
) : UseCase<Unit, Unit>() {

    override suspend fun execute(parameters: Unit) =
        hdrManager.refreshHdrStatus()
}