import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin
import org.jetbrains.dokka.DokkaConfiguration.Visibility
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("com.android.application").version(fsryan.Deps.Versions.Plugin.Android.plugin).apply(false)
    id("com.android.library").version(fsryan.Deps.Versions.Plugin.Android.plugin).apply(false)
    kotlin("android").version(fsryan.Deps.Versions.Global.JetBrains.kotlin).apply(false)
    kotlin("multiplatform").version(fsryan.Deps.Versions.Global.JetBrains.kotlin).apply(false)
    id("org.jetbrains.dokka").version(fsryan.Deps.Versions.Global.JetBrains.dokka).apply(false)
}

buildscript {
    val props = fsryan.BuildProperties
    props.initializeWith(rootProject)
    repositories {
        if (hasProperty("fsryan.includeMavenLocal")) {
            mavenLocal()
        }
        maven {
            name = "fsryan-release"
            url = uri("https://maven.fsryan.com/fsryan-release")
            credentials(PasswordCredentials::class) {
                username = props.prop(
                    rootProject,
                    propName = "com.fsryan.fsryan_maven_repo_user",
                    envVarName = "FSRYAN_MAVEN_REPO_USER"
                )
                password = props.prop(
                    rootProject,
                    propName = "com.fsryan.fsryan_release_password",
                    envVarName = "FSRYAN_MAVEN_RELEASE_REPO_TOKEN"
                )
            }
        }
        maven {
            name = "fsryan-snapshot"
            url = uri("https://maven.fsryan.com/fsryan-snapshot")
            credentials(PasswordCredentials::class) {
                username = props.prop(
                    rootProject,
                    propName = "com.fsryan.fsryan_maven_repo_user",
                    envVarName = "FSRYAN_MAVEN_REPO_USER"
                )
                password = props.prop(
                    rootProject,
                    propName = "com.fsryan.fsryan_snapshot_password",
                    envVarName = "FSRYAN_MAVEN_SNAPSHOT_REPO_TOKEN"
                )
            }
        }
        mavenCentral()
        google()
    }
    dependencies {
        with(fsryan.Deps.Plugin.FSRyan) {
            classpath(cicd)
            classpath(module)
        }
    }
}

allprojects {
    group = "com.fsryan.util"
    // Works around a bug where the Arm version of NodeJS cannot be found on Apple
    // Silicon (sets the node version to the lowest supported)
    rootProject.plugins.withType<NodeJsRootPlugin> {
        rootProject.the<NodeJsRootExtension>().nodeVersion = "16.0.0"
    }

    tasks.withType<DokkaTask>().configureEach {
        dokkaSourceSets.configureEach {
            reportUndocumented.set(true)
            documentedVisibilities.set(listOf(Visibility.PUBLIC, Visibility.INTERNAL))
        }
    }
    repositories {
        val props = fsryan.BuildProperties
        if (hasProperty("fsryan.includeMavenLocal")) {
            mavenLocal()
        }
        maven {
            url = uri("https://maven.fsryan.com/fsryan-release")
            credentials(PasswordCredentials::class) {
                username = props.prop(
                    rootProject,
                    propName = "com.fsryan.fsryan_maven_repo_user",
                    envVarName = "FSRYAN_MAVEN_REPO_USER"
                )
                password = props.prop(
                    rootProject,
                    propName = "com.fsryan.fsryan_release_password",
                    envVarName = "FSRYAN_MAVEN_RELEASE_REPO_TOKEN"
                )
            }
        }
        maven {
            url = uri("https://maven.fsryan.com/fsryan-snapshot")
            credentials(PasswordCredentials::class) {
                username = props.prop(
                    rootProject,
                    propName = "com.fsryan.fsryan_maven_repo_user",
                    envVarName = "FSRYAN_MAVEN_REPO_USER"
                )
                password = props.prop(
                    rootProject,
                    propName = "com.fsryan.fsryan_snapshot_password",
                    envVarName = "FSRYAN_MAVEN_SNAPSHOT_REPO_TOKEN"
                )
            }
        }
        mavenCentral()
        google()
    }
}