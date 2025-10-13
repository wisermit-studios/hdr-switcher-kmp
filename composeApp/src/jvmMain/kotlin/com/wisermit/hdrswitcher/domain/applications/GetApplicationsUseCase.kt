package com.wisermit.hdrswitcher.domain.applications

import com.wisermit.hdrswitcher.data.applications.ApplicationsStorage
import com.wisermit.hdrswitcher.domain.FlowUseCase
import com.wisermit.hdrswitcher.model.Application
import kotlinx.coroutines.flow.Flow

class GetApplicationsUseCase(
    private val applicationsStorage: ApplicationsStorage,
) : FlowUseCase<Unit, List<Application>>() {

    override fun execute(parameters: Unit): Flow<List<Application>> =
        applicationsStorage.getApplications()
}