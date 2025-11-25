package com.wisermit.hdrswitcher.util

import java.io.File

object AppResources : CommonAppResources()

abstract class CommonAppResources {
    protected val resourcesDir = File(System.getProperty("compose.application.resources.dir"))
    protected val binDir = resourcesDir.resolve("bin")
}