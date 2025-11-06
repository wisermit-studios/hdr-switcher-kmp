package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.service.HdrSwitcherService
import com.wisermit.hdrswitcher.service.hdrSwitcherService
import org.koin.core.module.Module
import org.koin.dsl.module

val serviceModule: Module = module {
    single<HdrSwitcherService> { hdrSwitcherService() }
}