package com.wisermit.hdrswitcher.service

import com.wisermit.hdrswitcher.domain.applications.GetApplicationsUseCase
import com.wisermit.hdrswitcher.model.Application
import com.wisermit.hdrswitcher.process.SystemManagerProcess
import com.wisermit.hdrswitcher.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

private val TAG = HdrSwitcherService::class.java.simpleName

class HdrSwitcherServiceImpl(
    getApplicationsUseCase: GetApplicationsUseCase,
    private val scope: CoroutineScope = CoroutineScope(Job() + Dispatchers.Default)
) : HdrSwitcherService {

    private var job: Job? = null

    private val applications = getApplicationsUseCase(Unit)

    private var systemManager: SystemManagerProcess? = null

    override fun start() {
        if (job?.isActive == true) return

        job = scope.launch {
            applications.collect { result ->
                systemManager?.destroy()

                result.getOrNull()
                    ?.takeIf { it.isNotEmpty() }
                    ?.let { startProcess(it) }
            }
        }
    }

    private suspend fun startProcess(list: List<Application>) {
        try {
            systemManager = SystemManagerProcess.start {
                val args = list.map { "${it.file.name}|${it.file.path}" }
                setArgs(*args.toTypedArray())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error while trying to start Application Watcher Service", e)
        }
    }

    override fun stop() {
        scope.cancel()
        systemManager?.destroy()
    }
}