package com.wisermit.hdrswitcher.domain.application

import com.wisermit.hdrswitcher.data.application.ApplicationStorage
import com.wisermit.hdrswitcher.domain.UseCase
import com.wisermit.hdrswitcher.framework.AppError
import com.wisermit.hdrswitcher.infrastructure.SystemInfo
import com.wisermit.hdrswitcher.infrastructure.systemmanager.SystemManager
import com.wisermit.hdrswitcher.model.Application
import java.io.File

class AddApplicationFileUseCase(
    private val systemInfo: SystemInfo,
    private val systemManager: SystemManager,
    private val applicationStorage: ApplicationStorage,
) : UseCase<File, Unit>() {

    override suspend fun execute(parameters: File) {
        val file = parameters
        if (file.extension != systemInfo.applicationExtension) {
            throw AppError.UnsupportedFile(file.name)
        }
        val application = Application(
            path = file.toPath(),
            description = systemManager.getFileDescription(file)
        )
        return applicationStorage.add(application)
    }
}