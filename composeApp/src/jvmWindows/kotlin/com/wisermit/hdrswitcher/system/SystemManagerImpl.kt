package com.wisermit.hdrswitcher.system

import com.wisermit.hdrswitcher.utils.SystemManagerExe

private val TAG = SystemManagerImpl::class.java.simpleName

internal class SystemManagerImpl() : SystemManager {

    private val exe = SystemManagerExe()

    override fun getHdrStatus(): Boolean? {
        return null
    }

    override fun toggleHdr() {
    }
}