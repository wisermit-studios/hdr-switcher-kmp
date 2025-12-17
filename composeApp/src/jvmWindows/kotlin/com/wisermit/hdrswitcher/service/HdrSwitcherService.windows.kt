package com.wisermit.hdrswitcher.service

import com.wisermit.hdrswitcher.Configuration
import com.wisermit.hdrswitcher.domain.applications.GetApplicationsUseCase
import com.wisermit.hdrswitcher.process.SystemManagerProcess
import com.wisermit.hdrswitcher.service.HdrSwitcherService.Status
import com.wisermit.hdrswitcher.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import org.koin.core.scope.Scope

private val SERVICE_NAME = HdrSwitcherService::class.java.simpleName
private val TAG = SERVICE_NAME

internal actual fun Scope.hdrSwitcherService(): HdrSwitcherService =
    WindowsHdrSwitcherService(get(), get())

private class WindowsHdrSwitcherService(
    getApplicationsUseCase: GetApplicationsUseCase,
    private val configuration: Configuration,
    private val scope: CoroutineScope = CoroutineScope(Job() + Dispatchers.Default),
) : HdrSwitcherService {

    private var job: Job? = null

    private val applicationsUseCase = getApplicationsUseCase(Unit)

    private var systemManager: SystemManagerProcess? = null

    private val _status = MutableStateFlow(Status.Stopped)
    override val status: StateFlow<Status> = _status

    init {
        scope.launch {
            status.drop(1).collect {
                Log.i(TAG, "$SERVICE_NAME $it")
            }
        }
    }

    override fun start() {
        if (job?.isActive == true) return

        Log.i(TAG, "Starting $SERVICE_NAME...")

        job = scope.launch {
            applicationsUseCase.collect { result ->
                systemManager?.destroy()

                val applications = result.getOrNull()

                if (applications.isNullOrEmpty()) {
                    _status.emit(Status.Suspended)
                } else {
                    startProcess()
                }
            }
        }
    }

    private suspend fun startProcess() {
        try {
            systemManager = SystemManagerProcess.start {
                data = configuration.applicationsFile.path

                onExit = {
                    Log.i(TAG, "Process exited: ${it.hexCode}.")
                    val status = if (it.code == 0) Status.Suspended else Status.Error
                    _status.tryEmit(status)
                }
            }
            _status.emit(Status.Active)
        } catch (e: Exception) {
            Log.e(TAG, "Error while trying to start $SERVICE_NAME.", e)
            _status.emit(Status.Error)
        }
    }

    override fun stop() {
        scope.cancel()
        systemManager?.destroy()
        _status.tryEmit(Status.Stopped)
    }
}