package com.wisermit.hdrswitcher.domain.storage

import com.wisermit.hdrswitcher.data.applications.ApplicationsStorage
import com.wisermit.hdrswitcher.domain.UseCase

class InitializeStoragesUseCase(
    private val applicationsStorage: ApplicationsStorage,
) : UseCase<Unit, Unit>() {

    override suspend fun execute(parameters: Unit) {
        applicationsStorage.initialize()
    }
}