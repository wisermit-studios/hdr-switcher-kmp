package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.data.application.ApplicationStorage
import com.wisermit.hdrswitcher.data.application.ApplicationsDataStore
import org.koin.dsl.module

val dataModule = module {
    single { ApplicationsDataStore(get()) }
    single { ApplicationStorage(get()) }
}