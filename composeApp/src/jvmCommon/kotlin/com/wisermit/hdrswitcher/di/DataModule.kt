package com.wisermit.hdrswitcher.di

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import com.wisermit.hdrswitcher.Configuration
import com.wisermit.hdrswitcher.core.WiseError
import com.wisermit.hdrswitcher.data.applications.ApplicationsJsonSerializer
import com.wisermit.hdrswitcher.data.applications.ApplicationsStorage
import com.wisermit.hdrswitcher.data.applications.ApplicationsStorageImpl
import com.wisermit.hdrswitcher.model.Application
import org.koin.dsl.module

val dataModule = module {
    single<DataStore<List<Application>>> {
        val configuration = get<Configuration>()
        val file = configuration.applicationsFile

        DataStoreFactory.create(
            produceFile = { file },
            serializer = ApplicationsJsonSerializer,
            corruptionHandler = handleCorruption { e ->
                throw WiseError.InvalidFile(file.name, e.cause)
            }
        )
    }

    single<ApplicationsStorage> {
        ApplicationsStorageImpl(get())
    }
}

private fun <T> handleCorruption(handleCorruption: (CorruptionException) -> T) =
    ReplaceFileCorruptionHandler(produceNewData = { e -> handleCorruption(e) })
