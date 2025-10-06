package com.wisermit.hdrswitcher.domain.application

import com.wisermit.hdrswitcher.data.application.ApplicationStorage
import com.wisermit.hdrswitcher.domain.UseCase
import com.wisermit.hdrswitcher.framework.AppError
import com.wisermit.hdrswitcher.model.Application
import com.wisermit.hdrswitcher.system.SystemInfo
import com.wisermit.hdrswitcher.system.SystemTools
import java.io.File

class AddApplicationFileUseCase(
    private val systemInfo: SystemInfo,
    private val systemTools: SystemTools,
    private val applicationStorage: ApplicationStorage,
) : UseCase<File, Unit>() {

    override suspend fun execute(parameters: File) {
        val file = parameters
        if (file.extension != systemInfo.applicationExtension) {
            throw AppError.UnsupportedFile(file.name)
        }
        val application = Application(
            file = file,
            description = systemTools.getFileDescription(file)
                ?: file.name.substringBeforeLast(".")
        )
        return applicationStorage.add(application)
    }
}