package com.wisermit.hdrswitcher.data.application

import com.wisermit.hdrswitcher.model.Application
import com.wisermit.hdrswitcher.utils.FileUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import java.net.URI
import kotlin.io.path.toPath

class ApplicationRepository(
    val localDataSource: ApplicationStorage,
) {
    fun getApplications(
        onFailureScope: CoroutineScope,
        onFailure: (Throwable) -> Unit,
    ): StateFlow<List<Application>> {
        return localDataSource.getApplications(onFailureScope, onFailure)
    }

    suspend fun add(appUri: URI): Result<Unit> {
        // TODO: Check uri and file type.

        val path = appUri.toPath()
        val application = Application(
            path = path,
            description = FileUtils.getExeDescription(path).getOrNull()
        )
        return localDataSource.add(application)
    }

    suspend fun save(app: Application): Result<Unit> {
        return localDataSource.save(app)
    }
}