package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.service.ApplicationsWatcherService
import com.wisermit.hdrswitcher.service.ApplicationsWatcherServiceImpl
import org.koin.dsl.module

val platformServiceModule = module {
    single<ApplicationsWatcherService> { ApplicationsWatcherServiceImpl() }
}