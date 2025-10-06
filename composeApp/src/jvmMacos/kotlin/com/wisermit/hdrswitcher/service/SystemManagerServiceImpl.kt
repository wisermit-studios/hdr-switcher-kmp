package com.wisermit.hdrswitcher.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class SystemManagerServiceImpl : SystemManagerService {

    override fun getHdrStatus(): Flow<Boolean?> = emptyFlow()

    override fun refreshHdrStatus() = Unit

    override suspend fun setHdrStatus(enabled: Boolean) = Unit
}