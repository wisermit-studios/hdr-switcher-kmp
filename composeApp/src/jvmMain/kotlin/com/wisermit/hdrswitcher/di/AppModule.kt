package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.Configuration
import com.wisermit.hdrswitcher.ConfigurationImpl
import com.wisermit.hdrswitcher.SystemInfo
import com.wisermit.hdrswitcher.SystemInfoImpl
import com.wisermit.hdrswitcher.data.FilePropertiesProvider
import com.wisermit.hdrswitcher.data.FilePropertiesProviderImpl
import com.wisermit.hdrswitcher.data.hdr.HdrManager
import com.wisermit.hdrswitcher.data.hdr.HdrManagerImpl
import org.koin.dsl.module

object AppModule {

    private val systemModule = module {
        single<SystemInfo> { SystemInfoImpl() }
        single<FilePropertiesProvider> { FilePropertiesProviderImpl() }
        single<HdrManager> { HdrManagerImpl() }
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