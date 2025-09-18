package com.wisermit.hdrswitcher.domain.application

import com.wisermit.hdrswitcher.data.application.ApplicationStorage
import com.wisermit.hdrswitcher.domain.UnsafeFlowUseCase
import com.wisermit.hdrswitcher.model.Application
import kotlinx.coroutines.flow.Flow

class GetApplicationsUseCase(
    private val applicationStorage: ApplicationStorage,
) : UnsafeFlowUseCase<Unit, List<Application>>() {

    override fun execute(parameters: Unit): Flow<List<Application>> =
        applicationStorage.getApplications()
}