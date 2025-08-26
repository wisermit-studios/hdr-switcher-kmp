package com.wisermit.hdrswitcher.model

import java.nio.file.Path
import kotlin.io.path.name
import kotlin.io.path.pathString

enum class HdrMode {
    Default, On, Off;

    companion object {
        fun fromString(string: String) = entries.find { it.name == string }
    }
}

data class ApplicationSettings(
    val path: String,
    val name: String,
    val hdr: HdrMode = HdrMode.Default,
) {
    constructor(params: List<String>) : this(
        path = params[0],
        name = params[1],
        hdr = HdrMode.fromString(params[2]) ?: HdrMode.Default,
    )

    constructor(path: Path) : this(
        path = path.pathString,
        name = path.name.substringBeforeLast("."),
    )

    fun toLine() = "$path|$name|$hdr"
}