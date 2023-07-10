pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    plugins {
        kotlin("jvm").version(extra["kotlin.version"] as String)
        kotlin("multiplatform").version(extra["kotlin.version"] as String)
        kotlin("android").version(extra["kotlin.version"] as String)
        id("com.android.application").version(extra["agp.version"] as String)
        id("com.android.library").version(extra["agp.version"] as String)
        id("org.jetbrains.compose").version(extra["compose.version"] as String)
        id("org.jetbrains.kotlinx.kover").version(extra["kover.version"] as String)
        id("com.github.johnrengelman.shadow").version(extra["shadow.version"] as String)
        id("com.jakewharton.mosaic").version(extra["mosaic.version"] as String)
        id("org.jetbrains.dokka").version(extra["kotlin.version"] as String)
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "net"

include(
    ":net"
)