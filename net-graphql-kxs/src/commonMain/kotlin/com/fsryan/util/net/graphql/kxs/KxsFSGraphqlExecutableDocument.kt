package com.fsryan.util.net.graphql.kxs

import com.fsryan.util.net.graphql.FSGraphqlExecutableDocument
import com.fsryan.util.net.graphql.FSGraphqlFragment
import com.fsryan.util.net.graphql.FSGraphqlOperation
import kotlinx.serialization.*

fun FSGraphqlExecutableDocument.Companion.kxs(
    name: String,
    query: String,
    variables: Map<String, Any?>? = null
): FSGraphqlExecutableDocument = KxsFSGraphqlExecutableDocument(
    name = name,
    query = query,
    variables = variables
)

fun FSGraphqlOperation.asSerializableJsonDocument(
    fragments: List<FSGraphqlFragment> = emptyList(),
    variables: Map<String, Any?>? = null
): FSGraphqlExecutableDocument = FSGraphqlExecutableDocument.kxs(
    name = name,
    query = FSGraphqlExecutableDocument.assembleQuery(this, fragments),
    variables = variables
)

@Serializable
data class KxsFSGraphqlExecutableDocument internal constructor(
    @SerialName("operationName") override val name: String,
    override val query: String,
    override val variables: Map<String, @Contextual Any?>?
): FSGraphqlExecutableDocument

internal fun FSGraphqlExecutableDocument.toKXSData(): KxsFSGraphqlExecutableDocument = when (this) {
    is KxsFSGraphqlExecutableDocument -> this
    else -> KxsFSGraphqlExecutableDocument(name = name, query = query, variables = variables)
}