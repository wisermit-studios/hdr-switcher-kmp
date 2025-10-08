package com.wisermit.hdrswitcher.service

import kotlinx.coroutines.flow.Flow

interface SystemManagerService {
    fun getHdrStatus(): Flow<Boolean?>

    suspend fun refreshHdrStatus()

    suspend fun setHdrStatus(enabled: Boolean)
}
