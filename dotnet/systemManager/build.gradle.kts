import BuildConfig.SystemManager

object Group {
    const val BUILD = "build"
}

val projectSrcDir = layout.projectDirectory.dir("src")
val outputsDir = layout.buildDirectory.dir("outputs")

val cleanDotnet by tasks.registering(Exec::class) {
    group = Group.BUILD
    setWorkingDir(projectSrcDir)
    commandLine("dotnet", "clean")
}

tasks.register<Delete>("clean") {
    group = Group.BUILD
    delete(layout.buildDirectory)
    dependsOn(cleanDotnet)
}

listOf(
    BuildType.Debug,
    BuildType.Release,
).map { buildType ->

    val outputFile = File("${outputsDir.get()}/$buildType", SystemManager.EXE_FILE_NAME)

    val publishTask = tasks.register<Exec>("publish${buildType.name}Exe") {
        group = Group.BUILD
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
            "-c:$buildType,AssemblyName=${SystemManager.EXE_FILE_NAME.substringBefore(".")}",
            "-r:win-x64",
            "-p:PublishSingleFile=true",
            "--self-contained=false",
            "-o:${outputFile.parent}",
        )

        val startTasks = startTasks

        doLast {
            if (startTasks.contains(name)) {
                logger.lifecycle("The EXE is written to $outputFile.")
            }
        }
    }

    configurations.register("${buildType}Binary") {
        isCanBeResolved = false

        attributes {
            attribute(ArtifactAttribute.TYPE, ArtifactAttribute.TYPE_BINARY)
            attribute(ArtifactAttribute.VARIANT, "$buildType")
        }

        artifacts {
            add(name, publishTask)
        }
    }
}

