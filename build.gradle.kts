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
        // I noticed that uploads to AWS were very slow, and that I got this
        // log message:
        // JAXB is unavailable. Will fallback to SDK implementation which may be less performant.If you are using Java 9+, you will need to include javax.xml.bind:jaxb-api as a dependency.
        // See: https://github.com/seek-oss/gradle-aws-plugin/issues/15
        // It appears this is necessary to speed up the aws uploads. This is
        // because Java 9+ does not include this library. See:
        // https://stackoverflow.com/questions/43574426/jaxb-api-jar-not-on-classpath
         classpath("javax.xml.bind:jaxb-api:2.3.1")
        // If this doesn't help, try:
//        classpath("org.glassfish.jaxb:jakarta.xml.bind-api:2.3.2")
//        classpath("jakarta.xml.bind:jaxb-runtime:2.3.2")
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
        val awsAccessKey = props.prop(this@allprojects, propName = "com.fsryan.aws_access_key", envVarName = "AWS_ACCESS_KEY")
        val awsSecretKey = props.prop(this@allprojects, propName = "com.fsryan.aws_secret_key", envVarName = "AWS_SECRET_KEY")
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
}