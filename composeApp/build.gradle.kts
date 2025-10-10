import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
//    alias(libs.plugins.composeHotReload)
    kotlin("plugin.serialization") version libs.versions.kotlin
}

logger.lifecycle("Running with buildPlatform '$buildPlatform' and buildType '$buildType'.")

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
            val jvmPlatformTarget = "jvm${buildPlatform.name}"
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
        mainClass = "${BuildConfig.AppCompose.PACKAGE}.MainKt"

        buildTypes.release {
            proguard {
                configurationFiles.from(project.file("proguard-rules.pro"))
            }
        }

        nativeDistributions {
            targetFormats(TargetFormat.Msi, TargetFormat.Dmg)
            packageName = BuildConfig.AppCompose.PACKAGE_NAME
            packageVersion = BuildConfig.AppCompose.PACKAGE_VERSION
            appResourcesRootDir.set(layout.projectDirectory.dir("resources"))

            windows {
                menu = true
                shortcut = false
                dirChooser = true
            }
        }
    }
}

compose.resources {
    publicResClass = false
    packageOfResClass = "${BuildConfig.AppCompose.PACKAGE}.resources"
    generateResClass = auto
}

val binary: Configuration by configurations.creating {
    isCanBeConsumed = false
    attributes {
        attribute(ArtifactAttribute.BUILD_TYPE, buildType)
    }
}

dependencies {
    binary(projects.dotnet.systemManager)
}

afterEvaluate {
    tasks.named<Sync>("prepareAppResources") {
        if (buildPlatform == BuildPlatform.Windows) {
            from(binary) { into("bin") }
        }
    }
}