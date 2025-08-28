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

class ApplicationsStorage(private val file: File) {

    private val _applications = MutableStateFlow(emptyList<Application>())
    val applications = _applications.asStateFlow()

    private val mutex = Mutex()

    private val data: Data by lazy {
        val jsonString = file
            .takeIf { it.exists() }
            ?.readText()

        if (jsonString.isNullOrBlank()) {
            Data()
        } else {
            json.decodeFromString(jsonString)
        }.also {
            _applications.value = it.applications.toList()
        }
    }

    suspend fun <T> read(
        block: List<Application>.() -> T
    ) = runCatching {
        mutex.withLock {
            block(data.applications)
        }
    }

    suspend fun write(
        block: MutableList<Application>.() -> Unit
    ) = runCatching {
        mutex.withLock {
            val mutableList = data.applications.toMutableList()
            block(mutableList)
            write(mutableList.toList())
        }
    }

    private fun write(applications: List<Application>) {
        if (!file.exists()) {
            file.parentFile.mkdirs()
        }
        val jsonString = json.encodeToString(data)
        file.writeText(jsonString)
        data.applications = applications
        _applications.value = applications
    }
}

@Serializable
private data class Data(
    val version: Int = 1,
    var applications: List<Application> = emptyList(),
)