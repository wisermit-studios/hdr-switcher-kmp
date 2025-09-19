package com.wisermit.hdrswitcher.infrastructure

import com.sun.jna.platform.win32.KnownFolders
import com.sun.jna.platform.win32.Shell32Util
import com.wisermit.hdrswitcher.utils.add
import java.nio.file.Path

private val OS_NAME: String = System.getProperty("os.name")

enum class Platform {
    Windows, MacOS;

    companion object {
        val Current: Platform by lazy {
            when {
                OS_NAME.startsWith("Win") -> Windows
                OS_NAME.startsWith("Mac") -> MacOS
                else -> throw UnsupportedOperationException("Unsupported OS: $OS_NAME")
            }
        }
    }
}

abstract class SystemInfo {
    abstract val platform: Platform

    @Suppress("unused")
    val osName: String = OS_NAME

    abstract val systemDrive: Path

    val userHomePath: Path get() = Path.of(System.getProperty("user.home"))
    abstract val appSettingsPath: Path

    abstract val applicationExtension: String
}

internal class WindowsSystemInfo : SystemInfo() {
    override val platform: Platform = Platform.Windows

    override val systemDrive: Path get() = Path.of(System.getenv("SystemDrive"))

    override val appSettingsPath: Path
        get() = Shell32Util.getKnownFolderPath(KnownFolders.FOLDERID_Documents)
            ?.let { Path.of(it) }
            ?: userHomePath.add("Documents")

    override val applicationExtension = "exe"
}

/*
 * The app is not functional on macOS. This is for development purposes only.
 */
internal class MacOsSystemInfo : SystemInfo() {
    override val platform: Platform = Platform.MacOS

    override val systemDrive: Path get() = Path.of("/")

    override val appSettingsPath: Path
        get() = userHomePath
            .add("Library")
            .add("Application Support")

    override val applicationExtension = "app"
}