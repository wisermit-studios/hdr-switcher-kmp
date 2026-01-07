package com.wisermit.hdrswitcher.di


import com.wisermit.hdrswitcher.ui.main.MainViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val uiModule = module {
    factoryOf(::MainViewModel)
}