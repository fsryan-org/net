package com.fsryan.util.net.ktor

import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*

private val client: HttpClient by lazy { HttpClient(Js) }

actual fun createHttpClient(): HttpClient = client

actual fun createAndConfigureDebugHttpClient(
    configureContentNegotiation: ContentNegotiation.Config.() -> Unit
): HttpClient = createAndConfigureHttpClient(
    debugLogger = object: Logger {
        override fun log(message: String) {
            console.log("FS_NETWORK", message)
        }
    },
    configureContentNegotiation = configureContentNegotiation
)