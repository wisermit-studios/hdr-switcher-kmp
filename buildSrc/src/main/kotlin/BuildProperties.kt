import org.gradle.api.Project
import org.gradle.api.attributes.Attribute

val buildTypeAttr = Attribute.of("${BuildConfig.AppCompose.PACKAGE}.buildType", String::class.java)

val Project.startTasks: List<String> get() = gradle.startParameter.taskNames

val Project.buildType: BuildType
    get() {
        return when {
            startTasks.any { it.contains("Release") } -> BuildType.Release
            startTasks.any { it.contains("Debug") } -> BuildType.Debug
            else -> localProperties.buildType
        }
    }

val Project.buildDesktopTarget: BuildDesktopTarget
    get() {
        return when {
            startTasks.any { it.endsWith("Msi") || it.endsWith("Exe") } -> BuildDesktopTarget.Windows
            startTasks.any { it.endsWith("Dmg") || it.endsWith("Pkg") } -> BuildDesktopTarget.Macos
            else -> localProperties.buildDesktopTarget
        }
    }

enum class BuildType() {
    Debug, Release;

    val value get() = name.lowercase()

    override fun toString() = value
}

enum class BuildDesktopTarget {
    Windows, Macos;

    val value: String get() = name.lowercase()

    override fun toString() = value
}