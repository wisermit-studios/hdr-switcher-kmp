package com.wisermit.hdrswitcher.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.io.File

enum class HdrMode {
    Default, On, Off;
}

@Immutable
@Serializable
data class Application(

    @SerialName("file")
    @Contextual
    val file: File,

    @SerialName("description")
    val description: String,

    @SerialName("hdr")
    val hdr: HdrMode = HdrMode.Default,
) {
    @Transient
    val id: String = file.path
}