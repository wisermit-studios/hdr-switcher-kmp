package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.domain.applications.AddApplicationUseCase
import com.wisermit.hdrswitcher.domain.applications.DeleteApplicationUseCase
import com.wisermit.hdrswitcher.domain.applications.GetApplicationsUseCase
import com.wisermit.hdrswitcher.domain.applications.SaveApplicationUseCase
import com.wisermit.hdrswitcher.domain.hdr.GetHdrStatusUseCase
import com.wisermit.hdrswitcher.domain.hdr.RefreshHdrStatusUseCase
import com.wisermit.hdrswitcher.domain.hdr.SetHdrEnabledUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { RefreshHdrStatusUseCase(get()) }
    factory { GetHdrStatusUseCase(get()) }
    factory { SetHdrEnabledUseCase(get()) }

    factory { GetApplicationsUseCase(get()) }
    factory { SaveApplicationUseCase(get()) }
    factory { DeleteApplicationUseCase(get()) }
    factory { AddApplicationUseCase(get(), get(), get()) }
}