package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.Config
import com.wisermit.hdrswitcher.ConfigMacOs
import com.wisermit.hdrswitcher.ConfigWindows
import com.wisermit.hdrswitcher.Platform.MacOs
import com.wisermit.hdrswitcher.Platform.Windows
import com.wisermit.hdrswitcher.SystemInfo
import com.wisermit.hdrswitcher.ui.main.MainViewModel
import org.koin.dsl.module

object AppModule {

    private val appModule = module {
        single<Config> {
            when (SystemInfo.platform) {
                Windows -> ConfigWindows()
                MacOs -> ConfigMacOs()
            }
        }
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