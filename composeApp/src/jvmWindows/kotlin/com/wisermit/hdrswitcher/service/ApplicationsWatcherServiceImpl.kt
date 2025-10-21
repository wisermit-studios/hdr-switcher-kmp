package com.wisermit.hdrswitcher.service

import com.wisermit.hdrswitcher.domain.applications.GetApplicationsUseCase
import com.wisermit.hdrswitcher.model.Application
import com.wisermit.hdrswitcher.system.process.SystemManagerProcess
import com.wisermit.hdrswitcher.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

private val TAG = ApplicationsWatcherService::class.java.simpleName

class ApplicationsWatcherServiceImpl(
    getApplicationsUseCase: GetApplicationsUseCase,
    private val scope: CoroutineScope = CoroutineScope(Job() + Dispatchers.Default)
) : ApplicationsWatcherService {

    private val applications = getApplicationsUseCase(Unit)

    private var systemManagerExe: SystemManagerProcess? = null

    override fun start() {
        scope.launch {
            applications.collect {
                systemManagerExe?.destroy()
                if (it.isNotEmpty()) startProcess(it)
            }
        }
    }

    private suspend fun startProcess(list: List<Application>) {
        try {
            systemManagerExe = SystemManagerProcess.start {
                val args = list.map { "${it.file.name}|${it.file.path}" }
                setArgs(*args.toTypedArray())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error while trying to start Application Watcher Service", e)
        }
    }

    override fun destroy() {
        scope.cancel()
        systemManagerExe?.destroy()
    }
}