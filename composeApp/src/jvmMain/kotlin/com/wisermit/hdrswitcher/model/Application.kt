package com.wisermit.hdrswitcher.model

import kotlinx.serialization.Serializable
import java.nio.file.Path
import kotlin.io.path.name
import kotlin.io.path.pathString

enum class HdrMode {
    Default, On, Off;
}

@Serializable
data class Application(
    val path: String,
    val description: String,
    val hdr: HdrMode = HdrMode.Default,
) {
    constructor(path: Path, description: String?) : this(
        path = path.pathString,
        description = description ?: path.name.substringBeforeLast("."),
    )
}