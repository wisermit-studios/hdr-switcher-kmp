import org.gradle.api.Project
import org.gradle.api.attributes.Attribute

object ArtifactAttribute {
    val BUILD_TYPE =
        Attribute.of("${BuildConfig.AppCompose.PACKAGE}.buildType", BuildType::class.java)
}

val Project.startTasks: List<String> get() = gradle.startParameter.taskNames

val Project.buildType: BuildType
    get() {
        return when {
            startTasks.any { it.contains("Release") } -> BuildType.Release
            startTasks.any { it.contains("Debug") } -> BuildType.Debug
            else -> localProperties.buildType
        }
    }

val Project.buildPlatform: BuildPlatform
    get() {
        return when {
            startTasks.any { it.endsWith("Msi") || it.endsWith("Exe") } -> BuildPlatform.Windows
            startTasks.any { it.endsWith("Dmg") || it.endsWith("Pkg") } -> BuildPlatform.Macos
            else -> localProperties.buildPlatform
        }
    }

enum class BuildType {
    Debug, Release;

    val value get() = name.lowercase()

    override fun toString() = value
}

enum class BuildPlatform {
    Windows, Macos;

    val value: String get() = name.lowercase()

    override fun toString() = value
}