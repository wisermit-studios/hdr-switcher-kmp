package com.wisermit.hdrswitcher.domain.applications

import com.wisermit.hdrswitcher.data.applications.ApplicationsStorage
import com.wisermit.hdrswitcher.domain.ResultFlowUseCase
import com.wisermit.hdrswitcher.model.Application
import kotlinx.coroutines.flow.map

class GetApplicationsUseCase(
    private val applicationsStorage: ApplicationsStorage,
) : ResultFlowUseCase<Unit, List<Application>>() {

    override fun execute(parameters: Unit) = applicationsStorage
        .getApplications()
        .map { Result.success(it) }
}