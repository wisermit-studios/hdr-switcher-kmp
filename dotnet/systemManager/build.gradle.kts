import BuildConfig.SystemManager

object Group {
    const val BUILD = "build"
}

val projectSrcDir = layout.projectDirectory.dir("src")
val outputsDir = layout.buildDirectory.dir("outputs")

interface Injected {
    @get:Inject
    val fs: FileSystemOperations
}

tasks.register<Exec>("clean") {
    group = Group.BUILD
    setWorkingDir(projectSrcDir)

    commandLine("dotnet", "clean")

    val injected = project.objects.newInstance<Injected>()
    val buildDirectory = layout.buildDirectory.get()
    doLast {
        injected.fs.delete {
            delete(buildDirectory)
        }
    }
}

listOf(
    BuildType.Debug,
    BuildType.Release,
).map { buildType ->

    val publishTask = tasks.register<Exec>("publish${buildType.name}Exe") {
        group = Group.BUILD
        setWorkingDir(projectSrcDir)

        val outputBinDir = outputsDir.get().dir("$buildType/bin")
        val outputFile = outputBinDir.file(SystemManager.EXE_FILE_NAME)

        inputs.files(
            fileTree(projectSrcDir) {
                exclude("**/bin/**")
                exclude("**/obj/**")
            },
        )

        outputs.file(outputFile)

        commandLine(
            "dotnet", "publish",
            "-c:$buildType,AssemblyName=${outputFile.asFile.name.substringBefore(".")}",
            "-r:win-x64",
            "-p:PublishSingleFile=true",
            "--self-contained=false",
            "-o:${outputBinDir}",
        )

        val isExplicitPublish = startTasks.contains(name)

        doLast {
            if (isExplicitPublish) {
                logger.lifecycle("The EXE is written to $outputFile.")
            }
        }
    }

    configurations.register("${buildType}Binary") {
        isCanBeResolved = false
        attributes {
            attribute(ArtifactAttribute.BUILD_TYPE, buildType)
        }
        artifacts {
            add(name, publishTask)
        }
    }
}

