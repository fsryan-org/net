package com.fsryan.util.net.graphql

/**
 * [Graph QL Document Spec](https://spec.graphql.org/draft/#sec-Document)
 *
 * This library cannot create a serializer for how the document is transported
 * to your backend, as this library is focused upon the graphql document format
 * only. Your serializer should understand how to transport this document to
 * your Graph QL backend.
 */
interface FSGraphqlExecutableDocument: FSGraphqlNamed {
    /**
     * The values of any variables. Note that we cannot be specific about the
     * types or how they are serialized. Your serializer must take all possible
     * supported Graph QL types into consideration in order for the document to
     * be serialized.
     */
    val variables: Map<String, Any?>?

    /**
     * The graph QL query string. You should probably use [assembleQuery] to
     * create this string.
     */
    val query: String
    /**
     * For creation of extension functions that make a static factory for
     * creating specific [FSGraphqlExecutableDocument] instances
     */
    companion object {
        fun assembleQuery(
            operation: FSGraphqlOperation,
            fragments: List<FSGraphqlFragment> = emptyList()
        ): String {
            val operationStr = operation.serialize()
            val fragmentsStr = fragments.serialize(delimiter = " ", groupStart = "", groupEnd = "")
            return "$operationStr$fragmentsStr"
        }
    }
}
