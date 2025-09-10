package com.wisermit.hdrswitcher.data.application

import com.wisermit.hdrswitcher.framework.AppError
import com.wisermit.hdrswitcher.framework.toFailure
import com.wisermit.hdrswitcher.infrastructure.SystemInfo
import com.wisermit.hdrswitcher.infrastructure.SystemManager
import com.wisermit.hdrswitcher.model.Application
import kotlinx.coroutines.CoroutineScope
import java.io.File

class ApplicationRepository(
    val localDataSource: ApplicationStorage,
    val systemInfo: SystemInfo,
    val systemManager: SystemManager,
) {
    fun getApplications(
        currentScope: CoroutineScope,
        onFailure: (Throwable) -> Unit,
    ) = localDataSource.getApplications(currentScope, onFailure)

    suspend fun add(file: File): Result<Unit> {
        if (file.extension != systemInfo.applicationExtension) {
            return AppError.UnsupportedFile(file.name).toFailure()
        }
        val application = Application(
            path = file.toPath(),
            description = systemManager.getFileDescription(file)
        )
        return localDataSource.add(application)
    }

    suspend fun save(app: Application) = localDataSource.save(app)

    suspend fun delete(app: Application) = localDataSource.delete(app)
}