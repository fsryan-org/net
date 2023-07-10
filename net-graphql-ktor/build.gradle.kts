import com.fsryan.cicd.CICDCommitInspector
import com.fsryan.cicd.FSRyanCICDPlugin
import com.fsryan.cicd.GitApi
import com.fsryan.cicd.SemanticVersion
import com.fsryan.module.*
import fsryan.Deps
import fsryan.Deps.Versions

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.kotlinx.kover")
    id("fsryan-module")
    id("fsryan-cicd")
}
// TODO: fix the cicd plugin such that it knows what version to use
val versionString = CICDCommitInspector(GitApi(project)).findLastVersion(projectQualifier = project.name)
val semanticVersion = SemanticVersion.parse(versionString)
version = plugins.findPlugin(FSRyanCICDPlugin::class)?.branchSpecificVersionName(semanticVersion)
    ?: throw IllegalStateException("no version for project: ${project.name}")

kotlin {

    sourceSets {
        maybeCreate("commonMain").apply {
            dependencies {
                api(project(":net-ktor"))
                api(project(":net-graphql"))
            }
        }
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.fsryan.util.net.graphql.ktor"

    defaultConfig {
        minSdk = Versions.Global.Android.minSdk
        targetSdk = Versions.Global.Android.targetSdk
    }
}

// TODO: contingent based upon platform.
fsryanCICD {
    cicdReleaseDependentTasks = setOf("koverHtmlReport", "assemble", "publishAllPublicationsToFSRyanReleaseRepository")
    cicdDevelopDependentTasks = setOf("koverHtmlReport", "assemble", "publishAllPublicationsToFSRyanReleaseRepository")
}

dependencies {
    kover(project(":net-ktor"))
    kover(project(":net-graphql"))
}