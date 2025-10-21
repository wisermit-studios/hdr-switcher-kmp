package com.wisermit.hdrswitcher.data.applications

import com.wisermit.hdrswitcher.data.CachedDataStore
import com.wisermit.hdrswitcher.model.Application
import kotlinx.coroutines.flow.Flow

interface ApplicationsStorage {
    suspend fun initialize()
    fun getApplications(): Flow<List<Application>>
    suspend fun add(app: Application)
    suspend fun save(app: Application)
    suspend fun delete(app: Application)
}

class ApplicationsStorageImpl(
    private val dataStore: CachedDataStore<List<Application>>,
) : ApplicationsStorage {

    override suspend fun initialize() = dataStore.initialize()

    override fun getApplications() = dataStore.data

    override suspend fun add(app: Application) {
        editData {
            val index = indexOfFirst { it.id == app.id }
            if (index == -1) add(app)
        }
    }

    override suspend fun save(app: Application) {
        editData {
            val index = indexOfFirst { it.id == app.id }
            if (index == -1) {
                add(app)
            } else {
                set(index, app)
            }
        }
    }

    override suspend fun delete(app: Application) {
        editData {
            removeIf { it.id == app.id }
        }
    }

    private suspend fun editData(transform: suspend MutableList<Application>.() -> Unit) {
        dataStore.updateData { data ->
            data.toMutableList().apply {
                transform(this)
                sortBy { it.description }
            }
        }
    }
}