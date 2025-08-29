package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.Config
import com.wisermit.hdrswitcher.data.SystemRepository
import com.wisermit.hdrswitcher.data.application.ApplicationStorage
import com.wisermit.hdrswitcher.data.application.ApplicationRepository
import com.wisermit.hdrswitcher.data.application.ApplicationsDataStore
import org.koin.dsl.module

object DataModule {
    val dataModule = module {
        single { SystemRepository() }

        factory { ApplicationsDataStore(Config.applicationsFile) }
        factory { ApplicationStorage(get()) }
        factory { ApplicationRepository(get()) }
    }
}