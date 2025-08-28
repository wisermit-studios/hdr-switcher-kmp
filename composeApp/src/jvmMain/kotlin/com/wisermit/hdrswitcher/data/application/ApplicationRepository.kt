package com.wisermit.hdrswitcher.data.application

import com.wisermit.hdrswitcher.model.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URI
import kotlin.io.path.toPath

class ApplicationRepository(
    val localDataSource: ApplicationLocalDataSource,
) {
    fun getApplications(
        onFailureScope: CoroutineScope,
        onFailure: (Throwable) -> Unit,
    ): StateFlow<List<Application>> {
        return localDataSource.getApplications(onFailureScope, onFailure)
    }

    suspend fun save(appUri: URI): Result<Unit> {
        // TODO: Check uri and file type.
        return save(Application(appUri.toPath()))
    }

    suspend fun save(appSettings: Application): Result<Unit> {
        return localDataSource.save(appSettings)
    }
}