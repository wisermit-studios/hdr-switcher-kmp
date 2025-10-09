import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem
import java.util.Properties

class LocalProperties(project: Project) {

    private val file = project.file("local.properties")
    private val properties = Properties().apply {
        file.inputStream().use { load(it) }
    }

    val buildType: BuildType
        get() = getBuildProperty(
            BUILD_TYPE,
            BuildType.entries,
            defaultProperty = { BuildType.Debug },
        )

    val buildDesktopTarget: BuildDesktopTarget
        get() = getBuildProperty(
            BUILD_DESKTOP_TARGET,
            BuildDesktopTarget.entries,
            defaultProperty = {
                when (OperatingSystem.current()) {
                    OperatingSystem.WINDOWS -> BuildDesktopTarget.Windows
                    OperatingSystem.MAC_OS -> BuildDesktopTarget.Macos
                    else -> BuildDesktopTarget.Windows
                }
            },
        )

    private fun <T> getBuildProperty(
        propertyName: String,
        entries: List<T>,
        defaultProperty: () -> T,
    ): T {
        val value = properties[propertyName] as String?

        return value?.let {
            entries.find { it.toString() == value }
                ?: throw Exception("Invalid $propertyName '$value'. Expected values: $entries.")
        } ?: defaultProperty().also {
            properties[propertyName] = value
            file.appendText("\n$propertyName=$value")
        }
    }

    private companion object {
        const val BUILD_TYPE = "buildType"
        const val BUILD_DESKTOP_TARGET = "buildDesktopTarget"
    }
}

val Project.localProperties get() = LocalProperties(rootProject)

