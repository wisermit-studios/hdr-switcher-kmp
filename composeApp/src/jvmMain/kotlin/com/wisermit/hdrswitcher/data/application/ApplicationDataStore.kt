package com.wisermit.hdrswitcher.data.application

import com.wisermit.hdrswitcher.model.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

private val json = Json {
    prettyPrint = true
    encodeDefaults = true
    ignoreUnknownKeys = true
}

class ApplicationsDataStore(private val file: File) {

    private val _applications = MutableStateFlow(emptyList<Application>())
    val applications = _applications.asStateFlow()

    private var _data: Data? = null
    private val data: Data
        get() = _data ?: run {
            val jsonString = file
                .takeIf { it.exists() }
                ?.readText()

            if (jsonString.isNullOrBlank()) {
                Data()
            } else {
                json.decodeFromString(jsonString)
            }.also {
                _data = it
                _applications.value = it.applications.toList()
            }
        }

    private val mutex = Mutex()

    suspend fun <T> read(
        block: List<Application>.() -> T
    ) = runCatching {
        mutex.withLock {
            block(data.applications)
        }
    }

    suspend fun edit(
        block: MutableList<Application>.() -> Unit
    ) = runCatching {
        mutex.withLock {
            val mutableList = data.applications.toMutableList()
            block(mutableList)
            mutableList.sortBy { it.path }
            write(mutableList.toList())
        }
    }

    private fun write(applications: List<Application>) {
        _data = data.copy(applications = applications)

        if (!file.exists()) {
            file.parentFile.mkdirs()
        }
        val jsonString = json.encodeToString(data)
        file.writeText(jsonString)

        _applications.value = data.applications
    }
}

@Serializable
private data class Data(
    val version: Int = 1,
    val applications: List<Application> = emptyList(),
)