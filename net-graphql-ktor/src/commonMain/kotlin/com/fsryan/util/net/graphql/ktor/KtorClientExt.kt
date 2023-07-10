package com.fsryan.util.net.graphql.ktor

import com.fsryan.util.net.graphql.FSGraphqlExecutableDocument
import com.fsryan.util.net.graphql.FSGraphqlResponse
import com.fsryan.util.net.ktor.createAndConfigureJsonHttpClient
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.JsonBuilder

/**
 * Send a graphql request to get a result. Your HttpClient must be configured
 * with a serializer that can serialize the implementation of
 * [FSGraphqlExecutableDocument] that you're using or else the request will not
 * be made.
 *
 * @param baseUrl endpoint graphql request will be sent to
 * @param executableDocument query or mutation document
 * @param graphqlEndpoint the endpoint to which the GraphQL request will be
 * sent
 * @param contentType describes format of the data being transmitted
 * @param modifyHeaders a function that headers to applied to request, for
 * example, a function that adds an authorization header
 * [ContentNegotiation] to determine de/serialization
 */
suspend inline fun <reified T: Any, reified R: FSGraphqlResponse<T>> HttpClient.sendGraphqlRequest(
    baseUrl: String,
    executableDocument: FSGraphqlExecutableDocument,
    graphqlEndpoint: String = "graphql",
    contentType: ContentType = ContentType.Application.Json,
    noinline modifyHeaders: HeadersBuilder.() -> Unit = {}
): R {
    val response: R = post(urlString = "$baseUrl/$graphqlEndpoint") {
        headers(modifyHeaders)
        contentType(contentType)
        setBody(executableDocument)
    }.body()
    return response
}

/**
 * Configures the appropriate class discriminator for use with a GraphQL
 * endpoint (`__typename`) and then adds the content negotiation for Json
 * given this class disciriminator.
 */
fun createAndConfigureKtorClientWithGraphqlJson(
    debugLogger: Logger? = null,
    modifyJsonBuilder: JsonBuilder.() -> Unit = {}
): HttpClient {
    return createAndConfigureJsonHttpClient(debugLogger = debugLogger) {
        classDiscriminator = "__typename"
        modifyJsonBuilder()
    }
}