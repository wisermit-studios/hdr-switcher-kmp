import BuildConfig.SystemManager

object Group {
    const val BUILD = "build"
    const val PUBLISHER = "publisher"
}

val projectSrcDir = layout.projectDirectory.dir("src")
val outputsDir = layout.buildDirectory.dir("outputs")

tasks.register<Exec>("clean") {
    group = Group.BUILD
    setWorkingDir(projectSrcDir)
    commandLine("dotnet", "clean")
}

listOf(
    BuildType.Debug,
    BuildType.Release,
).map { buildType ->

    val outputFile = File("${outputsDir.get()}/$buildType", SystemManager.EXE_FILE_NAME)

    val publishTask = tasks.register<Exec>("publish${buildType.name}Exe") {
        group = Group.PUBLISHER
        setWorkingDir(projectSrcDir)

        inputs.files(
            fileTree(projectSrcDir) {
                exclude("**/bin/**")
                exclude("**/obj/**")
            },
        )

        outputs.file(outputFile)

        commandLine(
            "dotnet", "publish",
            "-c", "$buildType,AssemblyName=${SystemManager.EXE_FILE_NAME.substringBefore(".")}",
            "-r", "win-x64",
            "-p:PublishSingleFile=true",
            "--self-contained", "false",
            "-o", outputFile.parent,
        )

        val startTasks = startTasks

        doLast {
            if (startTasks.contains(name)) {
                logger.lifecycle("The EXE is written to $outputFile.")
            }
        }
    }

    configurations.register("systemManager${buildType.name}Exe") {
        isCanBeResolved = false
        attributes {
            attribute(buildTypeAttr, "$buildType")
        }
        outgoing.artifact(outputFile) {
            builtBy(publishTask)
        }
    }
}

