package com.wisermit.hdrswitcher.data.hdr

import kotlinx.coroutines.flow.emptyFlow

class HdrManagerImpl : HdrManager {

    override fun getHdrStatus() = emptyFlow<Boolean?>()

    override suspend fun refreshHdrStatus() = Unit

    override suspend fun setHdrStatus(enabled: Boolean) = Unit
}