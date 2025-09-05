package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.Config
import com.wisermit.hdrswitcher.MacOsSystemInfo
import com.wisermit.hdrswitcher.OS_NAME
import com.wisermit.hdrswitcher.SystemInfo
import com.wisermit.hdrswitcher.WindowsSystemInfo
import com.wisermit.hdrswitcher.ui.main.MainViewModel
import org.koin.dsl.module

object AppModule {

    private val appModule = module {
        single<SystemInfo> {
            when {
                OS_NAME.startsWith("Windows") -> WindowsSystemInfo()
                OS_NAME.startsWith("Mac") -> MacOsSystemInfo()
                else -> throw Exception("Unsupported operating system: $OS_NAME.")
            }
        }
        single { Config(get()) }
    }

    private val uiModule = module {
        factory { MainViewModel(get(), get()) }
    }

    val modules = listOf(
        appModule,
        dataModule,
        uiModule
    )
}