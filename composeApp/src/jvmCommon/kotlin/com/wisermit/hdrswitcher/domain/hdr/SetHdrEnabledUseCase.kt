package com.wisermit.hdrswitcher.domain.hdr

import com.wisermit.hdrswitcher.data.hdr.HdrManager
import com.wisermit.hdrswitcher.domain.UseCase

class SetHdrEnabledUseCase(
    private val hdrManager: HdrManager,
) : UseCase<Boolean, Unit>() {

    override suspend fun execute(parameters: Boolean) =
        hdrManager.setHdrStatus(parameters)
}