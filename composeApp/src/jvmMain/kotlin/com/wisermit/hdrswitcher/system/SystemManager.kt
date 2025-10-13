package com.wisermit.hdrswitcher.system

import kotlinx.coroutines.flow.Flow

interface SystemManager {
    fun getHdrStatus(): Flow<Boolean?>
    suspend fun refreshHdrStatus()
    suspend fun setHdrStatus(enabled: Boolean)
}