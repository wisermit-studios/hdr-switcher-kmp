package com.wisermit.hdrswitcher.domain.applications

import com.wisermit.hdrswitcher.core.WiseError
import com.wisermit.hdrswitcher.data.applications.ApplicationsStorage
import com.wisermit.hdrswitcher.domain.UseCase
import com.wisermit.hdrswitcher.model.Application
import com.wisermit.hdrswitcher.system.SystemInfo
import com.wisermit.hdrswitcher.system.SystemTools
import java.io.File

class AddApplicationUseCase(
    private val systemInfo: SystemInfo,
    private val systemTools: SystemTools,
    private val applicationsStorage: ApplicationsStorage,
) : UseCase<File, Unit>() {

    override suspend fun execute(parameters: File) {
        val file = parameters
        if (file.extension != systemInfo.applicationExtension) {
            throw WiseError.UnsupportedFile(file.name)
        }
        val application = Application(
            file = file,
            description = systemTools.getFileDescription(file)
                ?: file.name.substringBeforeLast(".")
        )
        return applicationsStorage.add(application)
    }
}