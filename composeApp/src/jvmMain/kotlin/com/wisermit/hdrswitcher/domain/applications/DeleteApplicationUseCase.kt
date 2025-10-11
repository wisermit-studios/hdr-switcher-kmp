package com.wisermit.hdrswitcher.domain.applications

import com.wisermit.hdrswitcher.data.applications.ApplicationsStorage
import com.wisermit.hdrswitcher.domain.UseCase
import com.wisermit.hdrswitcher.model.Application

class DeleteApplicationUseCase(
    private val applicationsStorage: ApplicationsStorage,
) : UseCase<Application, Unit>() {

    override suspend fun execute(parameters: Application) =
        applicationsStorage.delete(parameters)
}