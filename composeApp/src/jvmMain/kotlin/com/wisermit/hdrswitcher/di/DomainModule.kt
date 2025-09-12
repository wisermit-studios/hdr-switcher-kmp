package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.domain.application.AddApplicationFileUseCase
import com.wisermit.hdrswitcher.domain.application.DeleteApplicationUseCase
import com.wisermit.hdrswitcher.domain.application.GetApplicationsUseCase
import com.wisermit.hdrswitcher.domain.application.SaveApplicationUseCase
import com.wisermit.hdrswitcher.domain.hdrsettings.GetHdrStatusUseCase
import com.wisermit.hdrswitcher.domain.hdrsettings.RefreshHdrStatusUseCase
import com.wisermit.hdrswitcher.domain.hdrsettings.SetHdrEnabledUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetHdrStatusUseCase(get()) }
    factory { RefreshHdrStatusUseCase(get()) }
    factory { SetHdrEnabledUseCase(get()) }

    factory { GetApplicationsUseCase(get()) }
    factory { SaveApplicationUseCase(get()) }
    factory { DeleteApplicationUseCase(get()) }
    factory { AddApplicationFileUseCase(get(), get(), get()) }
}