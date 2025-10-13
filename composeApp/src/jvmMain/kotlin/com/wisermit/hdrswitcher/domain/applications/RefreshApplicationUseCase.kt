package com.wisermit.hdrswitcher.domain.applications

import com.wisermit.hdrswitcher.data.applications.ApplicationsStorage
import com.wisermit.hdrswitcher.domain.UseCase

class RefreshApplicationUseCase(
    private val applicationsStorage: ApplicationsStorage,
) : UseCase<Unit, Unit>() {

    override suspend fun execute(parameters: Unit) =
        applicationsStorage.refresh()
}