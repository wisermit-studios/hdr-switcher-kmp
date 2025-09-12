package com.wisermit.hdrswitcher.domain.application

import com.wisermit.hdrswitcher.data.application.ApplicationStorage
import com.wisermit.hdrswitcher.domain.FlowUseCase
import com.wisermit.hdrswitcher.model.Application
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retry

class GetApplicationsUseCase(
    private val applicationStorage: ApplicationStorage,
) : FlowUseCase<Unit, List<Application>>() {

    override fun execute(parameters: Unit): Flow<Result<List<Application>>> {
        var failure: Result<List<Application>>? = null

        return applicationStorage.getApplications()
            // Emit error from 'flow.onStart', but keep collecting changes.
            .retry(1) { e ->
                logFailure(e)
                failure = Result.failure(e)
                true
            }
            .map { value ->
                failure?.also { failure = null } ?: Result.success(value)
            }
    }
}