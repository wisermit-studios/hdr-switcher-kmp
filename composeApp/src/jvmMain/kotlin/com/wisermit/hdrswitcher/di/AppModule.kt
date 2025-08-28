package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.data.application.ApplicationRepository
import com.wisermit.hdrswitcher.data.SystemRepository
import com.wisermit.hdrswitcher.data.application.ApplicationLocalDataSource
import com.wisermit.hdrswitcher.data.application.ApplicationsStorage
import com.wisermit.hdrswitcher.ui.main.MainViewModel
import org.koin.dsl.module

object AppModule {
    val dataModule = module {
        single { SystemRepository() }

        single { ApplicationsStorage() }
        single { ApplicationLocalDataSource(get()) }
        single { ApplicationRepository(get()) }
    }

    val uiModule = module {
        factory { MainViewModel(get(), get()) }
    }

    val modules = listOf(
        dataModule,
        uiModule
    )
}