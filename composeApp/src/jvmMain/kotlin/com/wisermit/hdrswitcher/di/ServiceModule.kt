package com.wisermit.hdrswitcher.di

import org.koin.dsl.module

val serviceModule = module {
    includes(platformServiceModule)
}