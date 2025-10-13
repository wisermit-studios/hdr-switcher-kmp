package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.ui.main.MainViewModel
import org.koin.dsl.module

val uiModule = module {
    factory { MainViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
}