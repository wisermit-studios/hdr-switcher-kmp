package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.service.SystemManagerService
import com.wisermit.hdrswitcher.service.SystemManagerServiceImpl
import org.koin.dsl.module

val platformServiceModule = module {
    single<SystemManagerService> { SystemManagerServiceImpl(get()) }
}