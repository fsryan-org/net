import com.fsryan.cicd.CICDCommitInspector
import com.fsryan.cicd.FSRyanCICDPlugin
import com.fsryan.cicd.GitApi
import com.fsryan.cicd.SemanticVersion
import fsryan.Deps
import fsryan.Deps.Versions

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
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
                api(project(":net"))
                with(Deps.Main.JetBrains) {
                    api(kxSerializationJson)
                }
            }
        }
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.fsryan.util.net.kxs"

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
    kover(project(":net"))
}