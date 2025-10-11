package com.wisermit.hdrswitcher.service

import com.wisermit.hdrswitcher.model.Application
import kotlinx.coroutines.flow.Flow

interface SystemManagerService {
    fun getHdrStatus(): Flow<Boolean?>

    suspend fun refreshHdrStatus()

    suspend fun setHdrStatus(enabled: Boolean)

    suspend fun registerApplicationsSettings(applications: List<Application>)
}