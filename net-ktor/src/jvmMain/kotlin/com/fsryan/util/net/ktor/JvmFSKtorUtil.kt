package com.fsryan.util.net.ktor

import com.fsryan.util.net.ktor.createAndConfigureHttpClient
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*

// TODO: do we want to place a dependency upon Apache logging, kermit?

actual fun createAndConfigureDebugHttpClient(
    configureContentNegotiation: ContentNegotiation.Config.() -> Unit
): HttpClient = createAndConfigureHttpClient(
    debugLogger = object: Logger {
        override fun log(message: String) {
            println("[FS_NETWORK] $message")
        }
    },
    configureContentNegotiation = configureContentNegotiation
)