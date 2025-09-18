package com.wisermit.hdrswitcher.domain.application

import com.wisermit.hdrswitcher.data.application.ApplicationStorage
import com.wisermit.hdrswitcher.domain.UseCase

class RefreshApplicationUseCase(
    private val applicationStorage: ApplicationStorage,
) : UseCase<Unit, Unit>() {

    override suspend fun execute(parameters: Unit) =
        applicationStorage.refresh()
}