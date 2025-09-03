package com.wisermit.hdrswitcher

import java.nio.file.Path

enum class Platform { Windows, MacOs }

object SystemInfo {
    val osName: String = System.getProperty("os.name")

    val platform: Platform =
        when {
            osName.startsWith("Windows") -> Platform.Windows
            // The app is not functional on macOS. This is for development purposes only.
            osName.startsWith("mac") -> Platform.MacOs
            else -> throw Exception("Unsupported operating system: $osName.")
        }

    val systemDrive: Path get() = Path.of(System.getenv("SystemDrive"))
    val userHomePath: Path get() = Path.of(System.getProperty("user.home"))
}