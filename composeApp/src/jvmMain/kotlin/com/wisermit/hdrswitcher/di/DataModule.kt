package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.data.applications.ApplicationsDataStore
import com.wisermit.hdrswitcher.data.applications.ApplicationsStorage
import org.koin.dsl.module

val dataModule = module {
    single { ApplicationsDataStore(get()) }
    single { ApplicationsStorage(get()) }
}