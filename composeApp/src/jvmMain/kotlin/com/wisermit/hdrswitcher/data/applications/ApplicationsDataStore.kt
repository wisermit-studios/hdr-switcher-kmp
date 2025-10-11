package com.wisermit.hdrswitcher.data.applications

import com.wisermit.hdrswitcher.Config
import com.wisermit.hdrswitcher.core.WiseError
import com.wisermit.hdrswitcher.model.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream

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
            mutableList.sortBy { it.description }
            write(mutableList.toList())

            applications.value = data.applications
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private suspend fun ensureData() {
        if (_data == null) {
            withContext(Dispatchers.IO) {
                _data = if (!file.exists() || file.length() == 0L) {
                    Data()
                } else {
                    file.inputStream().use {
                        try {
                            json.decodeFromStream(it)
                        } catch (e: SerializationException) {
                            throw WiseError.InvalidFile(file.name, e)
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private suspend fun write(applications: List<Application>) {
        withContext(Dispatchers.IO) {
            if (!file.exists()) {
                file.parentFile.mkdirs()
            }
            _data = data.copy(applications = applications)
            file.outputStream().use {
                json.encodeToStream(data, it)
            }
        }
    }
}

@Serializable
private data class Data(
    val version: Int = 1,
    val applications: List<Application> = emptyList(),
)