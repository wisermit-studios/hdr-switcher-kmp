package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.infrastructure.MacOsSystemInfo
import com.wisermit.hdrswitcher.infrastructure.Platform
import com.wisermit.hdrswitcher.infrastructure.SystemInfo
import com.wisermit.hdrswitcher.infrastructure.WindowsSystemInfo
import com.wisermit.hdrswitcher.infrastructure.systemmanager.MacOsSystemManager
import com.wisermit.hdrswitcher.infrastructure.systemmanager.SystemManager
import com.wisermit.hdrswitcher.infrastructure.systemmanager.WindowsSystemManager
import org.koin.dsl.module

val platformModule = when (Platform.Current) {
    Platform.Windows -> windowsPlatformModule
    Platform.MacOS -> macOsPlatformModule
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