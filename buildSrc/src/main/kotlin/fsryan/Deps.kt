package fsryan

object Deps {

    object Versions {
        object Global {
            object Android {
                const val compileSdk = 33
                const val minSdk = 24
                const val targetSdk = 33
            }
            object FSRyan {
                const val logging = "0.4.2"
            }
            object Goncalossilva {
                const val resources = "0.4.0"
            }
            object JakeWharton {
                const val mosaic = "0.9.1"
            }
            object JetBrains {
                const val coroutines = "1.7.1"
                const val compose = "1.5.10"
                const val dokka = "1.9.10"
                const val kotlin = "1.9.20"
            }
            object Koin {
                const val core = "3.4.0"
            }
        }
        object Main {
            object Ajalt {
                const val clikt = "3.5.2"
            }
            object Android {
                const val desugarJDKLibs = "2.0.3"
            }
            object Androidx {
                const val annotation = "1.6.0"
                const val core = "1.9.0"
                const val appCompat = "1.6.1"
            }
            object BenAsher44 {
                const val uuid = "0.8.1"
            }
            object FSRyan {
                const val types = "0.0.1-build.5"
            }
            object IonSpin {
                const val bigNum = "0.3.8"
            }
            object JetBrains {
                const val datetime = "0.4.1"
                const val kxSerialization = "1.6.0"
            }
            object Ktor {
                const val ktor = "2.3.5"
            }
            object Square {
                const val okHttp3 = "4.11.0"
            }
        }
        object Plugin {
            object Android {
                const val plugin = "7.4.2"
            }
            object FSRyan {
                const val cicd = "0.0.2-build.4"
                const val module = "0.0.1-build.3"
            }
            object Google {
                const val ksp = "${Global.JetBrains.kotlin}-1.0.14"
            }
            object JetBrains {
                const val kover = "0.7.4"
                const val kxs = "1.9.0"
            }
            object JohnRengelman {
                const val shadow = "8.1.1"
            }
            object Kotest {}
        }

        object Test {
            object Androidx {
                const val core = "1.4.0"
                const val junit = "1.1.3"
            }
            object CashApp {
                const val turbine = "1.0.0"
            }
            object FSRyan {
                const val testtools = "0.0.4"
            }
            object JUnit4 {
                const val lib = "4.13"
            }
            // TODO: remove? Better to use kotlin-test
            object JUnit {
                const val jupiter = "5.8.1"
                const val platform = "1.8.1"
            }
            object Mockative {
                const val core = "1.4.1"
            }
        }
    }

    object Main {
        object Ajalt {
            private val version = Versions.Main.Ajalt
            const val clikt = "com.github.ajalt.clikt:clikt:${version.clikt}"
        }
        object Android {
            private val version = Versions.Main.Android
            const val desugarJDKLibs = "com.android.tools:desugar_jdk_libs:${version.desugarJDKLibs}"
        }
        object Androidx {
            private val version = Versions.Main.Androidx
            const val coreKTX = "androidx.core:core-ktx:${version.core}"
            const val annotation = "androidx.annotation:annotation:${version.annotation}"
            const val appCompat = "androidx.appcompat:appcompat:${version.appCompat}"
        }
        object BenAsher44 {
            private val version = Versions.Main.BenAsher44
            const val uuid = "com.benasher44:uuid:${version.uuid}"
        }
        object FSRyan {
            private val globalVersion = Versions.Global.FSRyan
            private val version = Versions.Main.FSRyan
            const val logging = "com.fsryan.tools:logging:${globalVersion.logging}"
            const val loggingAndroid = "com.fsryan.tools:logging-android:${globalVersion.logging}"
            const val loggingAndroidDebug = "com.fsryan.tools:logging-android-debug:${globalVersion.logging}"
        }
        object IonSpin {
            private val version = Versions.Main.IonSpin
            const val bigNum = "com.ionspin.kotlin:bignum:${version.bigNum}"
        }
        object JetBrains {
            private val version = Versions.Main.JetBrains
            private val globalVersion = Versions.Global.JetBrains
            const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${globalVersion.coroutines}"
            const val datetime = "org.jetbrains.kotlinx:kotlinx-datetime:${version.datetime}"
            const val kxSerializationJson = "org.jetbrains.kotlinx:kotlinx-serialization-json:${version.kxSerialization}"
        }
        object Koin {
            private val version = Versions.Global.Koin
            const val android = "io.insert-koin:koin-android:${version.core}"
            const val core = "io.insert-koin:koin-core:${version.core}"
        }
        object Ktor {
            private val version = Versions.Main.Ktor
            const val clientCore = "io.ktor:ktor-client-core:${version.ktor}"
            const val clientContentNegotiation = "io.ktor:ktor-client-content-negotiation:${version.ktor}"
            const val clientDarwin = "io.ktor:ktor-client-darwin:${version.ktor}"
            const val clientLogging = "io.ktor:ktor-client-logging:${version.ktor}"
            const val clientOkHttp = "io.ktor:ktor-client-okhttp:${version.ktor}"
            const val clientJS = "io.ktor:ktor-client-js:${version.ktor}"
            const val serializationKotlinxJson = "io.ktor:ktor-serialization-kotlinx-json:${version.ktor}"
        }
    }
    object Plugin {
        object Android {
            private val version = Versions.Plugin.Android
            const val plugin = "com.android.tools.build:gradle:${version.plugin}"
        }
        object FSRyan {
            private val version = Versions.Plugin.FSRyan
            const val cicd = "com.fsryan.gradle:fsryan-cicd:${version.cicd}"
            const val module = "com.fsryan.gradle:fsryan-module:${version.module}"
        }
        object Google {
            private val version = Versions.Plugin.Google
            const val ksp = "com.google.devtools.ksp:symbol-processing-api:${version.ksp}"
        }
        object JakeWharton {
            private val version = Versions.Global.JakeWharton
            const val mosaic = "com.jakewharton.mosaic:mosaic-gradle-plugin:${version.mosaic}"
        }
        object JetBrains {
            private val version = Versions.Global.JetBrains
            private val pluginVersion = Versions.Plugin.JetBrains
            const val compose = "org.jetbrains.compose:compose-gradle-plugin:${version.compose}"
            const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${version.kotlin}"
            const val kxSerialization = "org.jetbrains.kotlin:kotlin-serialization:${version.kotlin}"
            const val dokka = "org.jetbrains.dokka:org.jetbrains.dokka.gradle.plugin:${version.kotlin}"
            const val kover = "org.jetbrains.kotlinx:kover-gradle-plugin:${pluginVersion.kover}"
        }
        object JohnRengelman {
            private val version = Versions.Plugin.JohnRengelman
            const val shadow = "gradle.plugin.com.github.johnrengelman:shadow:${version.shadow}"
        }
    }
    object Test {
        object Androidx {
            private val version = Versions.Test.Androidx
            const val core = "androidx.test:core:${version.core}"
            const val coreKTX = "androidx.test:core-ktx:${version.core}"
            const val junit = "androidx.test.ext:junit:${version.junit}"
            const val junitKtx = "androidx.test.ext:junit-ktx:${version.junit}"
            const val rules =  "androidx.test:rules:${version.core}"
            const val runner = "androidx.test:runner:${version.core}"
        }
        object CashApp {
            private val version = Versions.Test.CashApp
            const val turbine = "app.cash.turbine:turbine:${version.turbine}"
        }
        object FSRyan {
            private val version = Versions.Test.FSRyan
            /**
             * This is a silly tool I wrote because I wanted better assertions
             * on collections in JUnit. It's not bad. Perhaps I should make it
             * multiplatform and work on kotlin-test as well.
             */
            const val junit4JvmTools = "com.fsryan.testtools.jvm:junit4jvmtools:${version.testtools}"
        }
        object Goncalossilva {
            private val globalVersion = Versions.Global.Goncalossilva
            const val resources = "com.goncalossilva:resources:${globalVersion.resources}"
        }
        object JetBrains {
            private val version = Versions.Global.JetBrains
            const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${version.coroutines}"
            const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${version.kotlin}"
        }
        object JUnit4 {
            private val version = Versions.Test.JUnit4
            const val lib = "junit:junit:${version.lib}"
        }
        // TODO: remove? Do we still need JUnit5 now that we're using kotlin-test?
        object JUnit {
            private val version = Versions.Test.JUnit
            const val api = "org.junit.jupiter:junit-jupiter-api:${version.jupiter}"
            const val engine = "org.junit.jupiter:junit-jupiter-engine:${version.jupiter}"
            const val params = "org.junit.jupiter:junit-jupiter-params:${version.jupiter}"
            const val platform = "org.junit.platform:junit-platform-launcher:${version.platform}"
        }
        object Koin {
            private val version = Versions.Global.Koin
            const val test =  "io.insert-koin:koin-test:${version.core}"
        }
        /**
         * A mocking framework for Kotlin Multiplatform that leverages Kotlin's
         * Symbol Processing capability to generate on-the-fly mocks.
         */
        object Mockative {
            private val version = Versions.Test.Mockative
            /**
             * The runtime portion of Mockative
             * @see [Mockative]
             */
            const val core = "io.mockative:mockative:${version.core}"
            const val mockativeProcessor = "io.mockative:mockative-processor:${version.core}"
        }
    }
}
