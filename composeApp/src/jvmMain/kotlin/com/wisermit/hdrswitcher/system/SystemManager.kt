package com.wisermit.hdrswitcher.system

interface SystemManager {

    fun getHdrStatus(): Boolean?

    fun toggleHdr()
}