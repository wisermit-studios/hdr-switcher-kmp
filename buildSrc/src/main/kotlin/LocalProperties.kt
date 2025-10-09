import org.gradle.api.Project
import java.util.Properties

class LocalProperties(rootProject: Project) {

    private val file = rootProject.file("local.properties")
    private val properties = Properties().apply {
        file.inputStream().use { load(it) }
    }

    val buildType: BuildType
        get() {
            val value = properties[BUILD_TYPE] as String?

            return if (value == null) {
                BuildType.Debug.also {
                    addProperty(BUILD_TYPE, it.value)
                }
            } else {
                BuildType.entries
                    .find { it.value == value }
                    ?: throw Exception(
                        "Invalid $BUILD_TYPE '$value'. Expected values: ${BuildType.entries}."
                    )
            }
        }

    val buildTarget: BuildTarget
        get() {
            val value = properties[BUILD_TARGET] as String?

            return if (value == null) {
                when (System.getProperty("os.name").startsWith("Mac")) {
                    true -> BuildTarget.Macos
                    else -> BuildTarget.Windows
                }.also {
                    addProperty(BUILD_TARGET, it.value)
                }
            } else {
                BuildTarget.entries.find { it.value == value }
                    ?: throw Exception(
                        "Invalid $BUILD_TARGET '$value'. Expected values: ${BuildTarget.entries}."
                    )
            }
        }

    private fun addProperty(property: String, value: String) {
        properties[property] = value
        file.appendText("\n$property=$value")
    }

    private companion object {
        const val BUILD_TYPE = "buildType"
        const val BUILD_TARGET = "buildTarget"
    }
}

val Project.localProperties get() = LocalProperties(rootProject)

