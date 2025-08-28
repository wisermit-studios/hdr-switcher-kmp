package com.wisermit.hdrswitcher.data.application

import com.wisermit.hdrswitcher.AppConfig
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

class ApplicationsStorage {

    private val mutex = Mutex()

    private val file: File by lazy {
        File(AppConfig.APPLICATIONS_PATH).also {
            if (!it.exists()) {
                it.parentFile.mkdirs()
                it.write(Data())
            }
        }
    }

    private val data: Data by lazy {
        val jsonString = file.readText()
        if (jsonString.isBlank()) {
            Data()
        } else {
            json.decodeFromString(jsonString)
        }.also {
            _applications.value = it.applications.toList()
        }
    }

    private val _applications = MutableStateFlow(emptyList<Application>())
    val applications = _applications.asStateFlow()

    suspend fun <T> read(
        block: MutableList<Application>.() -> T
    ) = runCatching {
        mutex.withLock {
            block(data.applications)
        }
    }

    suspend fun write(
        block: MutableList<Application>.() -> Unit
    ) = runCatching {
        mutex.withLock {
            block(data.applications)
            file.write(data)
            _applications.value = data.applications.toList()
        }
    }
}

private fun File.write(data: Data) {
    val jsonString = json.encodeToString(data)
    writeText(jsonString)
}

@Serializable
private data class Data(
    var version: Int = 1,
    var applications: MutableList<Application> = mutableListOf(),
)