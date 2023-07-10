package com.fsryan.util.net.graphql.kxs

import com.fsryan.util.net.graphql.FSGraphqlErrorMessage
import com.fsryan.util.net.graphql.FSGraphqlQueryLocation
import kotlinx.serialization.*

internal fun FSGraphqlErrorMessage.Companion.toKxs(
    message: String,
    locations: List<FSGraphqlQueryLocation>,
    path: List<String>? = null,
    extensions: Map<String, String>? = null
): FSGraphqlErrorMessage {
    return KxsFSGraphqlErrorMessage(
        message = message,
        locations = locations.map { it.toKxs() },
        path = path,
        extensions = extensions
    )
}

@Serializable
internal data class KxsFSGraphqlErrorMessage(
    override val message: String,
    override val locations: List<KxsFSGraphqlQueryLocation>,
    override val path: List<String>? = null,
    override val extensions: Map<String, String>? = null
): FSGraphqlErrorMessage

@Serializable
internal data class KxsFSGraphqlQueryLocation(override val line: Int, override val column: Int): FSGraphqlQueryLocation

internal fun FSGraphqlErrorMessage.toKxs(): KxsFSGraphqlErrorMessage = when (this) {
    is KxsFSGraphqlErrorMessage -> this
    else -> KxsFSGraphqlErrorMessage(
        message = message,
        locations = locations.map { it.toKxs() },
        path = path,
        extensions = extensions
    )
}

internal fun FSGraphqlQueryLocation.toKxs(): KxsFSGraphqlQueryLocation = when (this) {
    is KxsFSGraphqlQueryLocation -> this
    else -> KxsFSGraphqlQueryLocation(line = line, column = column)
}