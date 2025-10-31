package com.wisermit.hdrswitcher.ui.main

import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragData
import androidx.compose.ui.draganddrop.dragData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wisermit.hdrswitcher.domain.applications.AddApplicationUseCase
import com.wisermit.hdrswitcher.domain.applications.DeleteApplicationUseCase
import com.wisermit.hdrswitcher.domain.applications.GetApplicationsUseCase
import com.wisermit.hdrswitcher.domain.applications.SaveApplicationUseCase
import com.wisermit.hdrswitcher.domain.system.GetHdrStatusUseCase
import com.wisermit.hdrswitcher.domain.system.RefreshHdrStatusUseCase
import com.wisermit.hdrswitcher.domain.system.SetHdrEnabledUseCase
import com.wisermit.hdrswitcher.model.Application
import com.wisermit.hdrswitcher.model.HdrMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.net.URI

class MainViewModel(
    getHdrStatus: GetHdrStatusUseCase,
    getApplicationsUseCase: GetApplicationsUseCase,
    private val refreshHdrStatusUseCase: RefreshHdrStatusUseCase,
    private val setHdrEnabledUseCase: SetHdrEnabledUseCase,
    private val addApplicationUseCase: AddApplicationUseCase,
    private val saveApplicationUseCase: SaveApplicationUseCase,
    private val deleteApplicationUseCase: DeleteApplicationUseCase,
) : ViewModel() {

    private val _error = MutableStateFlow<Throwable?>(null)
    val error: StateFlow<Throwable?> = _error

    val hdrStatus: StateFlow<Boolean?> = getHdrStatus(Unit)
        .stateIn(viewModelScope, Lazily, null)

    val applications: StateFlow<List<Application>> = getApplicationsUseCase(Unit)
        .stateIn(viewModelScope, Lazily, emptyList())

    fun refreshData() {
        viewModelScope.launch {
            refreshHdrStatusUseCase(Unit)
        }
    }

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
            addApplicationUseCase(file)
                .onFailure(_error::tryEmit)
        }
    }

    fun setHdrEnabled(enabled: Boolean) {
        viewModelScope.launch {
            setHdrEnabledUseCase(enabled)
        }
    }

    fun setApplicationHdr(app: Application, hdrMode: HdrMode) {
        save(app.copy(hdr = hdrMode))
    }

    fun save(app: Application) {
        viewModelScope.launch {
            saveApplicationUseCase(app)
                .onFailure(_error::tryEmit)
        }
    }

    fun delete(app: Application) {
        viewModelScope.launch {
            deleteApplicationUseCase(app)
                .onFailure(_error::tryEmit)
        }
    }

    fun clearError() {
        _error.value = null
    }
}