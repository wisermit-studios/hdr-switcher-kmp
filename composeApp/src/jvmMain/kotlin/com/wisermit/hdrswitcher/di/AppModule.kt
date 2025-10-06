package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.Config
import com.wisermit.hdrswitcher.system.SystemInfo
import com.wisermit.hdrswitcher.system.SystemInfoImpl
import com.wisermit.hdrswitcher.system.SystemTools
import com.wisermit.hdrswitcher.system.SystemToolsImpl
import org.koin.dsl.module

object AppModule {

    private val systemModule = module {
        includes(platformSystemModule)

        single<SystemInfo> { SystemInfoImpl() }
        single<SystemTools> { SystemToolsImpl() }
    }

    private val configModule = module {
        single { Config(get()) }
    }

    val modules = listOf(
        systemModule,
        configModule,
        serviceModule,
        dataModule,
        domainModule,
        uiModule,
    )
}