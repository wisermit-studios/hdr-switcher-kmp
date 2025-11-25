package com.wisermit.hdrswitcher.domain.applications

import com.wisermit.hdrswitcher.SystemInfo
import com.wisermit.hdrswitcher.core.WiseError
import com.wisermit.hdrswitcher.data.FilePropertiesProvider
import com.wisermit.hdrswitcher.data.applications.ApplicationsStorage
import com.wisermit.hdrswitcher.domain.UseCase
import com.wisermit.hdrswitcher.model.Application
import java.io.File

class AddApplicationUseCase(
    private val systemInfo: SystemInfo,
    private val filePropertiesProvider: FilePropertiesProvider,
    private val applicationsStorage: ApplicationsStorage,
) : UseCase<File, Unit>() {

    override suspend fun execute(parameters: File) {
        val file = parameters
        if (file.extension != systemInfo.applicationExtension) {
            throw WiseError.UnsupportedFile(file.name)
        }
        val application = Application(
            file = file,
            description = filePropertiesProvider.getFileDescription(file)
                ?: file.name.substringBeforeLast(".")
        )
        return applicationsStorage.add(application)
    }
}