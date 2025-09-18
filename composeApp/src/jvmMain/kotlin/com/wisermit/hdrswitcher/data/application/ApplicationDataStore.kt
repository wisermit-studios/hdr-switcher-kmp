package com.wisermit.hdrswitcher.data.application

import com.wisermit.hdrswitcher.Config
import com.wisermit.hdrswitcher.framework.AppError
import com.wisermit.hdrswitcher.model.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private val json = Json {
    prettyPrint = true
    encodeDefaults = true
    ignoreUnknownKeys = true
}

class ApplicationsDataStore(config: Config) {

    private val mutex = Mutex()

    private val file = config.applicationsFile

    private var _data: Data? = null
        set(value) {
            value?.let { applications.value = it.applications }
            field = value
        }
    private val data: Data get() = _data!!

    private val applications = MutableStateFlow(emptyList<Application>())

    fun getApplications(): Flow<List<Application>> = applications
        .onStart {
            runCatching { refresh() }
        }

    suspend fun refresh() {
        mutex.withLock {
            ensureData()
        }
    }

    suspend fun edit(block: MutableList<Application>.() -> Unit) {
        mutex.withLock {
            ensureData()
            val mutableList = data.applications.toMutableList()
            block(mutableList)
            mutableList.sortBy { it.path }
            write(mutableList.toList())
            applications.value = data.applications
        }
    }

    private suspend fun ensureData() {
        if (_data == null) {
            withContext(Dispatchers.IO) {
                val jsonString = file
                    .takeIf { it.exists() }
                    ?.readText()

                _data = if (jsonString.isNullOrBlank()) {
                    Data()
                } else {
                    try {
                        json.decodeFromString(jsonString)
                    } catch (e: Exception) {
                        throw AppError.InvalidFile(file.name, e)
                    }
                }
            }
        }
    }

    private suspend fun write(applications: List<Application>) {
        withContext(Dispatchers.IO) {
            if (!file.exists()) {
                file.parentFile.mkdirs()
            }
            _data = data.copy(applications = applications)
            val jsonString = json.encodeToString(data)
            file.writeText(jsonString)
        }
    }
}

@Serializable
private data class Data(
    val version: Int = 1,
    val applications: List<Application> = emptyList(),
)