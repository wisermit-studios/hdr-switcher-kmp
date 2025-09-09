package com.wisermit.hdrswitcher.ui.main

import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragData
import androidx.compose.ui.draganddrop.dragData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wisermit.hdrswitcher.data.application.ApplicationRepository
import com.wisermit.hdrswitcher.infrastructure.SystemManager
import com.wisermit.hdrswitcher.model.Application
import com.wisermit.hdrswitcher.model.HdrMode
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.File
import java.net.URI

class MainViewModel(
    private val systemManager: SystemManager,
    private val applicationRepository: ApplicationRepository,
) : ViewModel() {
    val hdrStatus: StateFlow<Boolean?> = systemManager.getHdrStatus()

    val applications: StateFlow<List<Application>> = applicationRepository.getApplications(
        currentScope = viewModelScope,
        onFailure = { _showErrorDialog.trySend(it) },
    )

    private val _showErrorDialog = Channel<Throwable>()
    val showErrorDialog = _showErrorDialog.receiveAsFlow()

    fun dropFile(event: DragAndDropEvent) {
        (event.dragData() as? DragData.FilesList)
            ?.readFiles()
            ?.firstOrNull()
            ?.let(::URI)
            ?.let(::File)
            ?.let(::addApplication)
    }

    fun addApplication(file: File) {
        viewModelScope.launch {
            applicationRepository.add(file)
                .onFailure(_showErrorDialog::trySend)
        }
    }

    fun setHdrEnabled(enabled: Boolean) {
        viewModelScope.launch {
            systemManager.setSystemHdr(enabled)
        }
    }

    fun setApplicationHdr(app: Application, hdrMode: HdrMode) {
        save(app.copy(hdr = hdrMode))
    }

    fun save(app: Application) {
        viewModelScope.launch {
            applicationRepository.save(app)
                .onFailure(_showErrorDialog::trySend)
        }
    }

    fun delete(app: Application) {
        viewModelScope.launch {
            applicationRepository.delete(app)
                .onFailure(_showErrorDialog::trySend)
        }
    }
}