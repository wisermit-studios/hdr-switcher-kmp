package com.wisermit.hdrswitcher.ui.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragData
import androidx.compose.ui.draganddrop.dragData
import androidx.lifecycle.ViewModel
import com.wisermit.hdrswitcher.data.ApplicationsSettingsRepository
import com.wisermit.hdrswitcher.data.SystemRepository
import com.wisermit.hdrswitcher.model.ApplicationSettings
import java.net.URI

class MainViewModel(
    private val systemRepository: SystemRepository,
    private val applicationsSettingsRepository: ApplicationsSettingsRepository,
) : ViewModel() {
    private val _isHdrEnabled = mutableStateOf<Boolean?>(null)
    val isHdrEnabled: State<Boolean?> = _isHdrEnabled

    private val _applicationsSettings = mutableStateOf<List<ApplicationSettings>>(emptyList())
    val applicationsSettings: State<List<ApplicationSettings>> = _applicationsSettings

    init {
        refresh()
    }

    fun refresh() {
        // TODO: Return Flow from repository.
        refreshHdrStatus()
        refreshApplicationsSettings()

    }

    private fun refreshHdrStatus() {
        _isHdrEnabled.value = systemRepository.isHdrEnabled()
    }

    private fun refreshApplicationsSettings() {
        _applicationsSettings.value = applicationsSettingsRepository.fetch().toList()
    }

    fun setHdrEnabled(enabled: Boolean) {
        systemRepository.setSystemHdr(enabled)
        refreshHdrStatus()
    }

    fun save(settings: ApplicationSettings) {
        applicationsSettingsRepository.save(settings)
            .onFailure {
                // TODO: Display error.
            }
    }

    fun dropFile(event: DragAndDropEvent): Boolean {
        (event.dragData() as? DragData.FilesList)
            ?.readFiles()
            ?.firstOrNull()
            ?.let(::URI)
            ?.let {
                applicationsSettingsRepository.save(it)
                    .onSuccess {
                        refreshApplicationsSettings()
                        return true
                    }.onFailure {
                        // TODO: Display error.
                    }
            }
        return false
    }
}