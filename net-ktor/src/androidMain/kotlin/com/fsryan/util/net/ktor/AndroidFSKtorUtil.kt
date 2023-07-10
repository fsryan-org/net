package com.fsryan.util.net.ktor

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*

actual fun createAndConfigureDebugHttpClient(
    configureContentNegotiation: ContentNegotiation.Config.() -> Unit
): HttpClient = createAndConfigureHttpClient(
    debugLogger = object: Logger {
        override fun log(message: String) {
            Log.i("FS_NETWORK", message)
        }
    },
    configureContentNegotiation = configureContentNegotiation
)