package com.wisermit.hdrswitcher.data.application

import com.wisermit.hdrswitcher.model.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ApplicationLocalDataSource(
    private val storage: ApplicationsStorage
) {
    fun getApplications(
        onFailureScope: CoroutineScope,
        onFailure: (Throwable) -> Unit,
    ): StateFlow<List<Application>> {
        onFailureScope.launch {
            withContext(Dispatchers.IO) {
                storage.read {}
            }.onFailure(onFailure)
        }
        return storage.applications
    }

    suspend fun save(app: Application) = withContext(Dispatchers.IO) {
        storage.write {
            val index = indexOfFirst { it.path == app.path }
            if (index == -1) {
                add(app)
            } else {
                set(index, app)
            }
        }
    }

    suspend fun delete(app: Application) = withContext(Dispatchers.IO) {
        runCatching {
            storage.write {
                removeIf { it.path == app.path }
            }
        }
    }
}