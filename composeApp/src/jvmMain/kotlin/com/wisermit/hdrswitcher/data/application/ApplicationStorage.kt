package com.wisermit.hdrswitcher.data.application

import com.wisermit.hdrswitcher.model.Application
import kotlinx.coroutines.flow.Flow

class ApplicationStorage(
    private val dataStore: ApplicationsDataStore
) {
    fun getApplications(): Flow<List<Application>> = dataStore.getApplications()

    suspend fun refresh() = dataStore.refresh()

    suspend fun add(app: Application) {
        dataStore.edit {
            val index = indexOfFirst { it.id == app.id }
            if (index == -1)
                add(app)
        }
    }

    suspend fun save(app: Application) {
        dataStore.edit {
            val index = indexOfFirst { it.id == app.id }
            if (index == -1) {
                add(app)
            } else {
                set(index, app)
            }
        }
    }

    suspend fun delete(app: Application) {
        dataStore.edit {
            removeIf { it.id == app.id }
        }
    }
}