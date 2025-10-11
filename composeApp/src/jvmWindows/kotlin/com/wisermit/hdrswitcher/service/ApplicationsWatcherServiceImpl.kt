package com.wisermit.hdrswitcher.service

import com.wisermit.hdrswitcher.domain.applications.GetApplicationsUseCase
import com.wisermit.hdrswitcher.system.process.SystemManagerPowerShell
import com.wisermit.hdrswitcher.system.process.SystemManagerProcess
import com.wisermit.hdrswitcher.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

private val TAG = SystemManagerPowerShell::class.java.simpleName

class ApplicationsWatcherServiceImpl(
    getApplicationsUseCase: GetApplicationsUseCase
) : ApplicationsWatcherService {

    private val serviceScope = CoroutineScope(Job() + Dispatchers.Default)

    private val applications = getApplicationsUseCase(Unit)

    private var systemManagerExe: SystemManagerProcess? = null

    override fun start() {
        serviceScope.launch {
            applications.collect { list ->
                systemManagerExe?.destroy()
                if (list.isNotEmpty()) {
                    try {
                        systemManagerExe = SystemManagerProcess.start {
                            val args = list.map { "${it.file.name}|${it.file.path}" }
                            setArgs(*args.toTypedArray())
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error while trying to start Application Watcher Service", e)
                    }
                }
            }
        }
    }

    override fun destroy() {
        serviceScope.cancel()
        systemManagerExe?.destroy()
    }
}