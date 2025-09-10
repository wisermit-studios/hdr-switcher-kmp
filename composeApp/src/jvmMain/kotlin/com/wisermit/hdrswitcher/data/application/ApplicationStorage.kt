package com.wisermit.hdrswitcher.data.application

import com.wisermit.hdrswitcher.model.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ApplicationStorage(
    private val dataStore: ApplicationsDataStore
) {
    fun getApplications(
        currentScope: CoroutineScope,
        onFailure: (Throwable) -> Unit,
    ): StateFlow<List<Application>> {
        currentScope.launch {
            withContext(Dispatchers.IO) {
                dataStore.read {}
            }.onFailure(onFailure)
        }
        return dataStore.applications
    }

    suspend fun add(app: Application) = withContext(Dispatchers.IO) {
        dataStore.edit {
            val index = indexOfFirst { it.path == app.path }
            if (index == -1) add(app)
        }
    }

    suspend fun save(app: Application) = withContext(Dispatchers.IO) {
        dataStore.edit {
            val index = indexOfFirst { it.path == app.path }
            if (index == -1) {
                add(app)
            } else {
                set(index, app)
            }
        }
    }

    suspend fun delete(app: Application) = withContext(Dispatchers.IO) {
        dataStore.edit {
            removeIf { it.path == app.path }
        }
    }
}