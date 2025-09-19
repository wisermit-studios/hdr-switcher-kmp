package com.wisermit.hdrswitcher.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.nio.file.Path
import kotlin.io.path.name
import kotlin.io.path.pathString

enum class HdrMode {
    Default, On, Off;
}

@Immutable
@Serializable
data class Application(
    val path: String,
    val description: String,
    val hdr: HdrMode = HdrMode.Default,
) {
    @Transient
    val id: String = path

    constructor(path: Path, description: String?) : this(
        path = path.pathString,
        description = description ?: path.name.substringBeforeLast("."),
    )
}