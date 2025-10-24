package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.Configuration
import com.wisermit.hdrswitcher.ConfigurationImpl
import com.wisermit.hdrswitcher.system.SystemInfo
import com.wisermit.hdrswitcher.system.SystemInfoImpl
import com.wisermit.hdrswitcher.system.SystemManager
import com.wisermit.hdrswitcher.system.SystemManagerImpl
import com.wisermit.hdrswitcher.system.SystemTools
import com.wisermit.hdrswitcher.system.SystemToolsImpl
import org.koin.dsl.module

object AppModule {

    private val systemModule = module {
        single<SystemInfo> { SystemInfoImpl() }
        single<SystemTools> { SystemToolsImpl() }
        single<SystemManager> { SystemManagerImpl() }
    }

    private val configurationModule = module {
        single<Configuration> { ConfigurationImpl(get()) }
    }

    val modules = listOf(
        systemModule,
        configurationModule,
        serviceModule,
        dataModule,
        domainModule,
        uiModule,
    )
}