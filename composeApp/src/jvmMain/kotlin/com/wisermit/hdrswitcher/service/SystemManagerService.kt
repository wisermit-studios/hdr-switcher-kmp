package com.wisermit.hdrswitcher.service

import kotlinx.coroutines.flow.Flow

interface SystemManagerService {
    fun getHdrStatus(): Flow<Boolean?>

    fun refreshHdrStatus()

    suspend fun setHdrStatus(enabled: Boolean)
}
