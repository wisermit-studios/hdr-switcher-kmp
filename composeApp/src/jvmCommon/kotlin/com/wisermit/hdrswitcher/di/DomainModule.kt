package com.wisermit.hdrswitcher.di

import com.wisermit.hdrswitcher.domain.applications.AddApplicationUseCase
import com.wisermit.hdrswitcher.domain.applications.DeleteApplicationUseCase
import com.wisermit.hdrswitcher.domain.applications.GetApplicationsUseCase
import com.wisermit.hdrswitcher.domain.applications.SaveApplicationUseCase
import com.wisermit.hdrswitcher.domain.hdr.GetHdrStatusUseCase
import com.wisermit.hdrswitcher.domain.hdr.RefreshHdrStatusUseCase
import com.wisermit.hdrswitcher.domain.hdr.SetHdrEnabledUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::RefreshHdrStatusUseCase)
    factoryOf(::GetHdrStatusUseCase)
    factoryOf(::SetHdrEnabledUseCase)

    factoryOf(::GetApplicationsUseCase)
    factoryOf(::SaveApplicationUseCase)
    factoryOf(::DeleteApplicationUseCase)
    factoryOf(::AddApplicationUseCase)
}