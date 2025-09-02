package com.wisermit.hdrswitcher.ui.main

import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragData
import androidx.compose.ui.draganddrop.dragData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wisermit.hdrswitcher.data.SystemRepository
import com.wisermit.hdrswitcher.data.application.ApplicationRepository
import com.wisermit.hdrswitcher.framework.Event
import com.wisermit.hdrswitcher.framework.trySend
import com.wisermit.hdrswitcher.model.Application
import com.wisermit.hdrswitcher.model.HdrMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.URI

class MainViewModel(
    private val systemRepository: SystemRepository,
    private val applicationRepository: ApplicationRepository,
) : ViewModel() {
    val isHdrEnabled: StateFlow<Boolean?> = systemRepository.isHdrEnabled()

    val applications: StateFlow<List<Application>> = applicationRepository.getApplications(
        onFailureScope = viewModelScope,
        onFailure = { _event.trySend(it) },
    )

    private val _event = MutableStateFlow<Event<Throwable?>?>(null)
    val event = _event.asStateFlow()

    fun addApplication(uri: URI) {
        viewModelScope.launch {
            applicationRepository.add(uri)
                .onFailure(_event::trySend)
        }
    }

    fun setHdrEnabled(enabled: Boolean) {
        systemRepository.setSystemHdr(enabled)
    }

    fun setApplicationHdr(app: Application, hdrMode: HdrMode) {
        save(app.copy(hdr = hdrMode))
    }

    fun save(app: Application) {
        viewModelScope.launch {
            applicationRepository.save(app)
                .onFailure(_event::trySend)
        }
    }

    fun delete(app: Application) {
        viewModelScope.launch {
            applicationRepository.delete(app)
                .onFailure(_event::trySend)
        }
    }

    fun dropFile(event: DragAndDropEvent) {
        (event.dragData() as? DragData.FilesList)
            ?.readFiles()
            ?.firstOrNull()
            ?.let(::URI)
            ?.let(::addApplication)
    }
}