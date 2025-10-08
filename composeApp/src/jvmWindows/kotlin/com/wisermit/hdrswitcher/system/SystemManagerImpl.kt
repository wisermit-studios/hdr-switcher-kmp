package com.wisermit.hdrswitcher.system

private val TAG = SystemManagerImpl::class.java.simpleName

class SystemManagerImpl() : SystemManager {

    // TODO: Lock exe file.

    override suspend fun getHdrStatus(): Boolean? {
        return null
    }

    override suspend fun toggleHdr() {
    }
}