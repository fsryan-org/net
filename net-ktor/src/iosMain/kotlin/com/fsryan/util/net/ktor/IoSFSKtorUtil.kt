package com.fsryan.util.net.ktor

import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import platform.Foundation.NSLog

actual fun createHttpClient(): HttpClient = HttpClient(Darwin)

actual fun createAndConfigureDebugHttpClient(
    configureContentNegotiation: ContentNegotiation.Config.() -> Unit
): HttpClient = createAndConfigureHttpClient(
    debugLogger = object: Logger {
        override fun log(message: String) {
            NSLog("[FS_NETWORK] %@", message)
        }
    },
    configureContentNegotiation = configureContentNegotiation
)