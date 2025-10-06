package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.system.SystemManager
import com.wisermit.hdrswitcher.system.SystemManagerImpl
import org.koin.dsl.module

val platformSystemModule = module {
    single<SystemManager> { SystemManagerImpl() }
}