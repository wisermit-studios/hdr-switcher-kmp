package com.wisermit.hdrswitcher.data.hdr

import kotlinx.coroutines.flow.Flow

internal expect fun hdrManager(): HdrManager

interface HdrManager {
    fun getHdrStatus(): Flow<Boolean?>
    suspend fun refreshHdrStatus()
    suspend fun setHdrStatus(enabled: Boolean)
}