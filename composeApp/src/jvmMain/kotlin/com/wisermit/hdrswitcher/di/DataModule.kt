package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.Config
import com.wisermit.hdrswitcher.data.CachedDataStore
import com.wisermit.hdrswitcher.data.applications.ApplicationsJsonSerializer
import com.wisermit.hdrswitcher.data.applications.ApplicationsStorage
import com.wisermit.hdrswitcher.data.applications.ApplicationsStorageImpl
import com.wisermit.hdrswitcher.model.Application
import org.koin.dsl.module

val dataModule = module {
    single<CachedDataStore<List<Application>>> {
        val config = get<Config>()

        CachedDataStore.create(
            produceFile = { config.applicationsFile },
            serializer = ApplicationsJsonSerializer,
        )
    }

    single<ApplicationsStorage> {
        ApplicationsStorageImpl(get())
    }
}