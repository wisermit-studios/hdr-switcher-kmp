package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.data.ApplicationsSettingsRepository
import com.wisermit.hdrswitcher.data.SystemRepository
import com.wisermit.hdrswitcher.ui.main.MainViewModel
import org.koin.dsl.module

object AppModule {
    val dataModule = module {
        single { SystemRepository() }
        single { ApplicationsSettingsRepository() }
    }

    val uiModule = module {
        single { MainViewModel(get(), get()) }
    }

    val modules = listOf(
        dataModule,
        uiModule
    )
}