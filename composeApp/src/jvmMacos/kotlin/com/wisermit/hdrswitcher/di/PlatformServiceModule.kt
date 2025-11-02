package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.service.HdrSwitcherService
import com.wisermit.hdrswitcher.service.HdrSwitcherServiceImpl
import org.koin.dsl.module

val platformServiceModule = module {
    single<HdrSwitcherService> { HdrSwitcherServiceImpl() }
}