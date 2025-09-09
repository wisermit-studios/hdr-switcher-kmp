package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.data.application.ApplicationRepository
import com.wisermit.hdrswitcher.data.application.ApplicationStorage
import com.wisermit.hdrswitcher.data.application.ApplicationsDataStore
import org.koin.dsl.module

val dataModule = module {
    factory { ApplicationsDataStore(get()) }
    factory { ApplicationStorage(get()) }
    factory { ApplicationRepository(get(), get(), get()) }
}