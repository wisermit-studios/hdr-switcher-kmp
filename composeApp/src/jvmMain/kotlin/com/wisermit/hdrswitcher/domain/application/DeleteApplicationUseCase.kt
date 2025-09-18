package com.wisermit.hdrswitcher.domain.application

import com.wisermit.hdrswitcher.data.application.ApplicationStorage
import com.wisermit.hdrswitcher.domain.UseCase
import com.wisermit.hdrswitcher.model.Application

class DeleteApplicationUseCase(
    private val applicationStorage: ApplicationStorage,
) : UseCase<Application, Unit>() {

    override suspend fun execute(parameters: Application) =
        applicationStorage.delete(parameters)
}