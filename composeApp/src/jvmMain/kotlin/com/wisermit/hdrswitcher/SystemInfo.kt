package com.wisermit.hdrswitcher

import com.sun.jna.platform.win32.KnownFolders
import com.sun.jna.platform.win32.Shell32Util
import com.wisermit.hdrswitcher.utils.add
import java.nio.file.Path

internal val OS_NAME: String = System.getProperty("os.name")

enum class Platform { Windows, MacOs }

abstract class SystemInfo {
    abstract val platform: Platform

    private val osName: String = OS_NAME

    abstract val systemDrive: Path

    val userHomePath: Path get() = Path.of(System.getProperty("user.home"))
    abstract val appSettingsPath: Path

    abstract val applicationExtension: String
}

class WindowsSystemInfo : SystemInfo() {
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
class MacOsSystemInfo : SystemInfo() {
    override val platform: Platform = Platform.MacOs

    override val systemDrive: Path get() = Path.of("/")

    override val appSettingsPath: Path
        get() = userHomePath
            .add("Library")
            .add("Application Support")

    override val applicationExtension = "app"
}