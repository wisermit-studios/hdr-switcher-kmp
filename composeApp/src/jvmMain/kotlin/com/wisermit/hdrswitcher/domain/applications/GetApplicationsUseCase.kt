package com.wisermit.hdrswitcher.domain.applications

import com.wisermit.hdrswitcher.data.applications.ApplicationsStorage
import com.wisermit.hdrswitcher.domain.FlowUseCase
import com.wisermit.hdrswitcher.model.Application

class GetApplicationsUseCase(
    private val applicationsStorage: ApplicationsStorage,
) : FlowUseCase<Unit, List<Application>>() {

    override fun execute(parameters: Unit) = applicationsStorage.getApplications()
}