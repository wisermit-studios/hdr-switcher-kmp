package com.wisermit.hdrswitcher.domain.hdrsettings

import com.wisermit.hdrswitcher.domain.UseCase
import com.wisermit.hdrswitcher.infrastructure.systemmanager.SystemManager

class SetHdrEnabledUseCase(
    private val systemManager: SystemManager,
) : UseCase<Boolean, Unit>() {

    override suspend fun execute(parameters: Boolean) =
        with(systemManager) {
            refreshHdrStatus()
            if (getHdrStatus().value != parameters) {
                toggleHdr()
                refreshHdrStatus()
            }
        }
}