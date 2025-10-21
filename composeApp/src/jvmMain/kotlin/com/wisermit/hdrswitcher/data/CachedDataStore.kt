package com.wisermit.hdrswitcher.data

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.IOException
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import com.wisermit.hdrswitcher.core.WiseError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import java.io.File

interface CachedDataStore<T> : DataStore<T> {

    suspend fun initialize()

    companion object {
        fun <T> create(
            produceFile: () -> File,
            serializer: VersionedJsonSerializer<T>,
        ): CachedDataStore<T> {
            val dataStore = DataStoreFactory.create(
                produceFile = produceFile,
                serializer = serializer,
                corruptionHandler = handleCorruption { e ->
                    throw WiseError.InvalidFile(produceFile().name, e.cause)
                }
            )
            return CachedDataStoreImpl(dataStore)
        }
    }
}

private fun <T> handleCorruption(handleCorruption: (CorruptionException) -> T) =
    ReplaceFileCorruptionHandler(produceNewData = { e -> handleCorruption(e) })

/**
 * Unlike the standard DataStore, this implementation ignores external changes
 * and does not throw exceptions for the `data` Flow.
 */
private class CachedDataStoreImpl<T>(
    private val dataStore: DataStore<T>,
) : CachedDataStore<T> {

    private val _data = MutableStateFlow<T?>(null)
    override val data: Flow<T>
        get() = _data.filterNotNull()

    @Throws(IOException::class)
    override suspend fun initialize() {
        _data.value = dataStore.data.first()
    }

    override suspend fun updateData(transform: suspend (T) -> T): T =
        dataStore.updateData(transform).also { _data.value = it }
}