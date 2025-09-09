package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.infrastructure.MacOsSystemInfo
import com.wisermit.hdrswitcher.infrastructure.MacOsSystemManager
import com.wisermit.hdrswitcher.infrastructure.OS_NAME
import com.wisermit.hdrswitcher.infrastructure.SystemInfo
import com.wisermit.hdrswitcher.infrastructure.SystemManager
import com.wisermit.hdrswitcher.infrastructure.WindowsSystemInfo
import com.wisermit.hdrswitcher.infrastructure.WindowsSystemManager
import org.koin.dsl.module

val platformModule = when {
    OS_NAME.startsWith("Windows") -> windowsPlatformModule
    OS_NAME.startsWith("Mac") -> macOsPlatformModule
    else -> throw UnsupportedOperationException("Unsupported OS: $OS_NAME")
}

private val windowsPlatformModule
    get() = module {
        single<SystemInfo> { WindowsSystemInfo() }
        single<SystemManager> { WindowsSystemManager() }
    }

private val macOsPlatformModule
    get() = module {
        single<SystemInfo> { MacOsSystemInfo() }
        single<SystemManager> { MacOsSystemManager() }
    }