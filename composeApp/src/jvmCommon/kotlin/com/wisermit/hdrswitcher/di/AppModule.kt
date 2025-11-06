package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.Configuration
import com.wisermit.hdrswitcher.ConfigurationImpl
import com.wisermit.hdrswitcher.SystemInfo
import com.wisermit.hdrswitcher.data.FilePropertiesProvider
import com.wisermit.hdrswitcher.data.filePropertiesProvider
import com.wisermit.hdrswitcher.data.hdr.HdrManager
import com.wisermit.hdrswitcher.data.hdr.hdrManager
import com.wisermit.hdrswitcher.systemInfo
import org.koin.dsl.module

object AppModule {

    private val systemModule = module {
        single<SystemInfo> { systemInfo() }
        single<FilePropertiesProvider> { filePropertiesProvider() }
        single<HdrManager> { hdrManager() }
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