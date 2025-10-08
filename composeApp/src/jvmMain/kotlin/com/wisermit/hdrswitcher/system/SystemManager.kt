package com.wisermit.hdrswitcher.system

interface SystemManager {

    suspend fun getHdrStatus(): Boolean?

    suspend fun toggleHdr()
}