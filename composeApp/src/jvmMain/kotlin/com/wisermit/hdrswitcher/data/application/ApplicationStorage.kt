package com.wisermit.hdrswitcher.data.application

import com.wisermit.hdrswitcher.Config
import com.wisermit.hdrswitcher.model.Application
import com.wisermit.hdrswitcher.utils.FileManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private val json = Json {
    prettyPrint = true
    encodeDefaults = true
    ignoreUnknownKeys = true
}

class ApplicationsStorage() {

    private val _applications = MutableStateFlow(emptyList<Application>())
    val applications = _applications.asStateFlow()

    private val fileManager = FileManager(
        file = Config.applicationsFile,
        encode = { json.encodeToString(it) },
        decode = { json.decodeFromString(it) },
        newData = ::Data,
        onDataChanged = {
            _applications.value = it.applications.toList()
        },
    )

    private val mutex = Mutex()

    suspend fun <T> read(
        block: MutableList<Application>.() -> T
    ) = runCatching {
        mutex.withLock {
            block(fileManager.data.applications)
        }
    }

    suspend fun write(
        block: MutableList<Application>.() -> Unit
    ) = runCatching {
        mutex.withLock {
            block(fileManager.data.applications)
            fileManager.write()
        }
    }
}

@Serializable
private data class Data(
    val version: Int = 1,
    val applications: MutableList<Application> = mutableListOf(),
)