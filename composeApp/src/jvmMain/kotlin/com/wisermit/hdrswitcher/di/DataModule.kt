package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.Config
import com.wisermit.hdrswitcher.data.SystemRepository
import com.wisermit.hdrswitcher.data.application.ApplicationLocalDataSource
import com.wisermit.hdrswitcher.data.application.ApplicationRepository
import com.wisermit.hdrswitcher.data.application.ApplicationsStorage
import org.koin.dsl.module

object DataModule {
    val dataModule = module {
        single { SystemRepository() }

        factory { ApplicationsStorage(Config.applicationsFile) }
        factory { ApplicationLocalDataSource(get()) }
        factory { ApplicationRepository(get()) }
    }
}