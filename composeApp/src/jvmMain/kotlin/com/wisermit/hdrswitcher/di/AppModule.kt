package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.Config
import com.wisermit.hdrswitcher.ui.main.MainViewModel
import org.koin.dsl.module

object AppModule {

    private val configModule = module {
        single { Config(get()) }
    }

    private val uiModule = module {
        factory { MainViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    }

    val modules = listOf(
        platformModule,
        configModule,
        dataModule,
        domainModule,
        uiModule
    )
}