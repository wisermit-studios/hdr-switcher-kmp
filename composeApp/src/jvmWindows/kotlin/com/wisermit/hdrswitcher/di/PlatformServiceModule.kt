package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.service.ApplicationsWatcherService
import com.wisermit.hdrswitcher.service.ApplicationsWatcherServiceImpl
import com.wisermit.hdrswitcher.system.SystemManager
import com.wisermit.hdrswitcher.system.SystemManagerImpl
import org.koin.dsl.module

val platformServiceModule = module {
    single<ApplicationsWatcherService> { ApplicationsWatcherServiceImpl(get()) }
}