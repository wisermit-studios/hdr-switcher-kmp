package com.wisermit.hdrswitcher.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// TODO: Change to Service.
class SystemRepository {

    private val _isHdrEnabled = MutableStateFlow<Boolean?>(null)

    fun isHdrEnabled(): StateFlow<Boolean?> {
        // TODO: Check HDR support.
        return _isHdrEnabled.asStateFlow()
    }

    fun setSystemHdr(enabled: Boolean) {
        // TODO: Change HDR.
        _isHdrEnabled.tryEmit(enabled)
    }
}