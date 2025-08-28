package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.di.DataModule.dataModule
import com.wisermit.hdrswitcher.ui.main.MainViewModel
import org.koin.dsl.module

object AppModule {
    val uiModule = module {
        factory { MainViewModel(get(), get()) }
    }

    val modules = listOf(
        dataModule,
        uiModule
    )
}