package com.wisermit.hdrswitcher.model

import java.nio.file.Path
import kotlin.io.path.name
import kotlin.io.path.pathString

data class Application(
    private val path: Path,
    val hdr: HdrMode = HdrMode.Default,
) {
    val name = path.name
    val pathString = path.pathString
}

enum class HdrMode {
    Default, On, Off,
}