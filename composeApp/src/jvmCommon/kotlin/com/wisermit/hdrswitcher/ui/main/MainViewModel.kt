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
import com.wisermit.hdrswitcher.domain.hdr.GetHdrStatusUseCase
import com.wisermit.hdrswitcher.domain.hdr.RefreshHdrStatusUseCase
import com.wisermit.hdrswitcher.domain.hdr.SetHdrEnabledUseCase
import com.wisermit.hdrswitcher.model.Application
import com.wisermit.hdrswitcher.model.HdrMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.net.URI

sealed class MainError() {
    class Error(val cause: Throwable) : MainError()
    class FatalError(val cause: Throwable) : MainError()
}

class MainViewModel(
    getHdrStatusUseCase: GetHdrStatusUseCase,
    getApplicationsUseCase: GetApplicationsUseCase,
    private val refreshHdrStatusUseCase: RefreshHdrStatusUseCase,
    private val setHdrEnabledUseCase: SetHdrEnabledUseCase,
    private val addApplicationUseCase: AddApplicationUseCase,
    private val saveApplicationUseCase: SaveApplicationUseCase,
    private val deleteApplicationUseCase: DeleteApplicationUseCase,
) : ViewModel() {

    private val _error = MutableStateFlow<MainError?>(null)
    val error: StateFlow<MainError?> = _error

    val hdrStatus: StateFlow<Boolean?> = getHdrStatusUseCase(Unit)
        .stateIn(viewModelScope, Lazily, null)

    private val _applications = MutableStateFlow<List<Application>?>(null)
    val applications: StateFlow<List<Application>?> = _applications

    init {
        viewModelScope.launch {
            getApplicationsUseCase(Unit).collect { result ->
                result.onSuccess {
                    _applications.value = it
                }.onFailure {
                    _error.value = MainError.FatalError(it)
                }
            }
        }
    }

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
                .onFailure {
                    _error.value = MainError.Error(it)
                }
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
                .onFailure {
                    _error.value = MainError.Error(it)
                }
        }
    }

    fun delete(app: Application) {
        viewModelScope.launch {
            deleteApplicationUseCase(app)
                .onFailure {
                    _error.value = MainError.Error(it)
                }
        }
    }

    fun clearError() {
        _error.value = null
    }
}