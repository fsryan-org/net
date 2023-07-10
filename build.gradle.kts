import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin
import org.jetbrains.dokka.DokkaConfiguration.Visibility
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("com.android.application").version(fsryan.Deps.Versions.Plugin.Android.plugin).apply(false)
    id("com.android.library").version(fsryan.Deps.Versions.Plugin.Android.plugin).apply(false)
    kotlin("android").version(fsryan.Deps.Versions.Global.JetBrains.kotlin).apply(false)
    kotlin("multiplatform").version(fsryan.Deps.Versions.Global.JetBrains.kotlin).apply(false)
    id("org.jetbrains.dokka").version(fsryan.Deps.Versions.Global.JetBrains.kotlin).apply(false)
}

buildscript {
    val props = fsryan.BuildProperties
    props.initializeWith(rootProject)
    val awsAccessKey = props.prop(rootProject, propName = "com.fsryan.aws_access_key", envVarName = "AWS_ACCESS_KEY")
    val awsSecretKey = props.prop(rootProject, propName = "com.fsryan.aws_secret_key", envVarName = "AWS_SECRET_KEY")
    repositories {
        if (hasProperty("fsryan.includeMavenLocal")) {
            mavenLocal()
        }
        maven {
            url = uri("s3://fsryan-maven-repo/release")
            credentials(AwsCredentials::class) {
                accessKey = awsAccessKey
                secretKey = awsSecretKey
            }
        }
        maven {
            url = uri("s3://fsryan-maven-repo/snapshot")
            credentials(AwsCredentials::class) {
                accessKey = awsAccessKey
                secretKey = awsSecretKey
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
}