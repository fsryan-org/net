package com.fsryan.util.net.ktor

import com.fsryan.util.net.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder
import kotlin.time.Duration.Companion.seconds

/**
 * Platform specific HTTP Client provided with the correct engine. The idea is
 * to use this function to create the client with the engine only and then
 * attach the configuration after-the-fact.
 *
 * @return [HttpClient]
 */
expect fun createHttpClient(): HttpClient

/**
 * Creates the http client and configures the logging and client negotiation
 * based upon the inputs.
 * > *Note:*
 * > When the [debugLogger] is non-null, the [LogLevel] is set to
 * > [LogLevel.ALL]
 */
fun createAndConfigureHttpClient(
    debugLogger: Logger? = null,
    configureContentNegotiation: ContentNegotiation.Config.() -> Unit = {}
): HttpClient = createHttpClient().config {
    debugLogger?.let {
        install(Logging) {
            logger = it
            level = LogLevel.ALL
        }
    }
    install(ContentNegotiation) {
        configureContentNegotiation()
    }
}

/**
 * Creates the http client and configures the logging and JSON client
 * negotiation based upon the inputs.
 * @see createAndConfigureHttpClient
 */
fun createAndConfigureJsonHttpClient(
    debugLogger: Logger? = null,
    modifyJsonBuilder: JsonBuilder.() -> Unit = {}
): HttpClient {
    return createAndConfigureHttpClient(debugLogger) {
        json(Json {
            modifyJsonBuilder()
        })
    }
}

/**
 * Creates the http client and configures the client negotiation based upon the
 * input. This will configure a logger to output a log to the standard output
 * of each platform (e.g. console.log for js, System.out for jvm, etc.).
 */
expect fun createAndConfigureDebugHttpClient(
    configureContentNegotiation: ContentNegotiation.Config.() -> Unit = {}
): HttpClient

/**
 * An abstraction for validating HTTP Responses from an FS Service. This is not
 * used by default, but you can use it when you configure your [HttpClient]. If
 * you use it, then the business logic will be prepared to handle each of the
 * built-in exceptions.
 */
fun HttpClientConfig<*>.installHttpResponseValidator() {
    HttpResponseValidator {
        validateResponse { response ->
            if (response.status.value < 400 || response.status.value >= 600) {
                return@validateResponse
            }
            val errorResponse: FSApiErrorsResponse? = try {
                response.body()
            } catch (e: Exception) {
                null
            }
            throw when (response.status) {
                HttpStatusCode.Unauthorized -> FSApiUnauthorizedException(errorResponse, null, null)
                HttpStatusCode.Forbidden -> FSApiForbiddenException(errorResponse, null, null)
                HttpStatusCode.NotFound -> FSApiNotFoundException(errorResponse, null, null)
                HttpStatusCode.TooManyRequests -> FSTooManyRequestsException(errorResponse, null, null)
                else -> FSNetApiException(response.status.value, errorResponse, null, null)
            }
        }
    }
}

/**
 * Configures the timeouts for the [HttpClient] to the given values. The
 * default values are 15 seconds for each of the timeouts. If you want all
 * values to be equal, just set the [requestTimeoutMillis] value, and the other
 * values will be set to that.
 */
fun HttpClientConfig<*>.configureTimeouts(
    requestTimeoutMillis: Long = 15.seconds.inWholeMilliseconds,
    socketTimeoutMillis: Long = requestTimeoutMillis,
    connectTimeoutMillis: Long = requestTimeoutMillis
) {
    install(HttpTimeout) {
        this.requestTimeoutMillis = requestTimeoutMillis
        this.socketTimeoutMillis = socketTimeoutMillis
        this.connectTimeoutMillis = connectTimeoutMillis
    }
}