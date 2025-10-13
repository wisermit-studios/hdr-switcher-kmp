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

    val buildPlatform: BuildPlatform
        get() = getBuildProperty(
            BUILD_PLATFORM,
            BuildPlatform.entries,
            defaultProperty = {
                when (OperatingSystem.current()) {
                    OperatingSystem.MAC_OS -> BuildPlatform.Macos
                    else -> BuildPlatform.Windows
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
            properties[propertyName] = it
            file.appendText("\n$propertyName=$it")
        }
    }

    private companion object {
        const val BUILD_TYPE = "buildType"
        const val BUILD_PLATFORM = "buildPlatform"
    }
}

val Project.localProperties get() = LocalProperties(rootProject)

