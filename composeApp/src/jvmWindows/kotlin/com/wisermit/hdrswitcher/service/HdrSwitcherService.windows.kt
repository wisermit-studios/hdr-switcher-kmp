package com.wisermit.hdrswitcher.service

import com.wisermit.hdrswitcher.domain.applications.GetApplicationsUseCase
import com.wisermit.hdrswitcher.model.Application
import com.wisermit.hdrswitcher.process.SystemManagerProcess
import com.wisermit.hdrswitcher.service.HdrSwitcherService.Status
import com.wisermit.hdrswitcher.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.scope.Scope

private val TAG = HdrSwitcherService::class.java.simpleName

internal actual fun Scope.hdrSwitcherService(): HdrSwitcherService =
    WindowsHdrSwitcherService(get())

private class WindowsHdrSwitcherService(
    getApplicationsUseCase: GetApplicationsUseCase,
    private val scope: CoroutineScope = CoroutineScope(Job() + Dispatchers.Default),
) : HdrSwitcherService {

    private var job: Job? = null

    private val applications = getApplicationsUseCase(Unit)

    private var systemManager: SystemManagerProcess? = null

    private val _status = MutableStateFlow(Status.Stopped)
    override val status: StateFlow<Status> = _status

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
            _status.emit(Status.Active)

            systemManager = SystemManagerProcess.start {
                val args = list.map { "${it.file.name}|${it.file.path}" }
                setArgs(*args.toTypedArray())

                onExit = {
                    val status = if (it.code == 0) Status.Stopped else Status.Error
                    _status.tryEmit(status)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error while trying to start $TAG", e)
            _status.emit(Status.Error)
        }
    }

    override fun stop() {
        scope.cancel()
        systemManager?.destroy()
        _status.tryEmit(Status.Stopped)
    }
}