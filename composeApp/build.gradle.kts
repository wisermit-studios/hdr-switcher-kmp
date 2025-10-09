import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
//    alias(libs.plugins.composeHotReload)
    kotlin("plugin.serialization") version libs.versions.kotlin
}

val appResourcesDir = project.layout.projectDirectory.dir("resources")
val windowsAppResourcesBinDir = appResourcesDir.dir("windows/bin")

logger.lifecycle("Running with buildDesktopTarget '$buildDesktopTarget' and buildType '$buildType'.")

kotlin {
    jvmToolchain(21)
    jvm()

    sourceSets {
        all {
            languageSettings {
                optIn("androidx.compose.material3.ExperimentalMaterial3Api")
                optIn("androidx.compose.ui.ExperimentalComposeUiApi")
            }
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.kotlinx.serialization.json)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        jvmMain {
            val jvmPlatformTarget = "jvm${buildDesktopTarget.name}"
            val jvmTargetDir = layout.projectDirectory.dir("src/$jvmPlatformTarget")

            kotlin.srcDir(jvmTargetDir.dir("kotlin"))
            resources.srcDir(jvmTargetDir.dir("resources"))

            dependencies {
                implementation(libs.kotlinx.coroutines.swing)
                implementation(compose.desktop.currentOs)
                implementation(libs.net.java.jna)
                implementation(libs.net.java.jna.platform)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.wisermit.hdrswitcher.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Msi, TargetFormat.Dmg)
            packageName = BuildConfig.AppCompose.PACKAGE_NAME
            packageVersion = BuildConfig.AppCompose.PACKAGE_VERSION
            appResourcesRootDir.set(appResourcesDir)

            windows {
                menu = true
                shortcut = false
                dirChooser = true
            }
        }
    }
}

val systemManagerExe: Configuration by configurations.creating {
    isCanBeConsumed = false
    attributes {
        attribute(buildTypeAttr, "$buildType")
    }
}

dependencies {
    systemManagerExe(projects.dotnet.systemManager)
}

val importSystemManagerExeTask = tasks.register<Copy>("importSystemManagerForWindowsResources") {
    description = "Copy the SystemManger EXE to jvmWindows resource folder."

    from(systemManagerExe)
    into(windowsAppResourcesBinDir)
}

afterEvaluate {
    tasks.named<Sync>("prepareAppResources") {
        if (buildDesktopTarget == BuildDesktopTarget.Windows) {
            dependsOn(importSystemManagerExeTask)
        }
    }
}