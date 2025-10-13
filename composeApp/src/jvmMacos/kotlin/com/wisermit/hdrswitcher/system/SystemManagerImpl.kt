package com.wisermit.hdrswitcher.system

import kotlinx.coroutines.flow.emptyFlow

class SystemManagerImpl : SystemManager {

    override fun getHdrStatus() = emptyFlow<Boolean?>()

    override suspend fun refreshHdrStatus() = Unit

    override suspend fun setHdrStatus(enabled: Boolean) = Unit
}