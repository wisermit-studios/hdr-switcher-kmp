package com.wisermit.hdrswitcher.ui.main

import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragData
import androidx.compose.ui.draganddrop.dragData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wisermit.hdrswitcher.domain.application.AddApplicationFileUseCase
import com.wisermit.hdrswitcher.domain.application.DeleteApplicationUseCase
import com.wisermit.hdrswitcher.domain.application.GetApplicationsUseCase
import com.wisermit.hdrswitcher.domain.application.RefreshApplicationUseCase
import com.wisermit.hdrswitcher.domain.application.SaveApplicationUseCase
import com.wisermit.hdrswitcher.domain.hdrsettings.GetHdrStatusUseCase
import com.wisermit.hdrswitcher.domain.hdrsettings.RefreshHdrStatusUseCase
import com.wisermit.hdrswitcher.domain.hdrsettings.SetHdrEnabledUseCase
import com.wisermit.hdrswitcher.model.Application
import com.wisermit.hdrswitcher.model.HdrMode
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.net.URI

class MainViewModel(
    getHdrStatus: GetHdrStatusUseCase,
    getApplicationsUseCase: GetApplicationsUseCase,
    private val refreshHdrStatusUseCase: RefreshHdrStatusUseCase,
    private val setHdrEnabledUseCase: SetHdrEnabledUseCase,
    private val refreshApplicationUseCase: RefreshApplicationUseCase,
    private val addApplicationFileUseCase: AddApplicationFileUseCase,
    private val saveApplicationUseCase: SaveApplicationUseCase,
    private val deleteApplicationUseCase: DeleteApplicationUseCase,
) : ViewModel() {

    private val _showErrorDialog = Channel<Throwable>(Channel.CONFLATED)
    val showErrorDialog = _showErrorDialog.receiveAsFlow()

    val applications: StateFlow<List<Application>> = getApplicationsUseCase(Unit)
        .stateIn(viewModelScope, Lazily, emptyList())

    val hdrStatus: StateFlow<Boolean?> = getHdrStatus(Unit)
        .stateIn(viewModelScope, Lazily, null)

    fun refreshData() {
        viewModelScope.launch {
            launch {
                refreshHdrStatusUseCase(Unit)
                    .onFailure(_showErrorDialog::trySend)
            }
            launch {
                refreshApplicationUseCase(Unit)
                    .onFailure(_showErrorDialog::trySend)
            }
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
            addApplicationFileUseCase(file)
                .onFailure(_showErrorDialog::trySend)
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
                .onFailure(_showErrorDialog::trySend)
        }
    }

    fun delete(app: Application) {
        viewModelScope.launch {
            deleteApplicationUseCase(app)
                .onFailure(_showErrorDialog::trySend)
        }
    }
}