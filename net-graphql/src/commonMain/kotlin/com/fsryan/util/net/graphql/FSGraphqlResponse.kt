package com.fsryan.util.net.graphql

interface FSGraphqlResponse<T:Any> {
    val data: T? get() = null
    val error: String? get() = null
    val errors: List<FSGraphqlErrorMessage>? get() = null
}

interface FSGraphqlMapResponse: FSGraphqlResponse<Map<String, Any>>

interface FSGraphqlErrorMessage {
    /**
     * [GraphQL Spec](https://spec.graphql.org/June2018/#sec-Errors)
     * > Every error must contain an entry with the key message with a string
     * > description of the error intended for the developer as a guide to
     * > understand and correct the error.
     */
    val message: String

    /**
     * @sse GraphqlQueryLocation
     */
    val locations: List<FSGraphqlQueryLocation>

    /**
     * [GraphQL Spec](https://spec.graphql.org/June2018/#sec-Errors)
     * > If an error can be associated to a particular field in the GraphQL
     * > result, it must contain an entry with the key path that details the
     * > path of the response field which experienced the error. This allows
     * > clients to identify whether a null result is intentional or caused by
     * > a runtime error.
     */
    val path: List<String>?
    /**
     * [GraphQL Spec](https://spec.graphql.org/June2018/#sec-Errors)
     * > GraphQL services may provide an additional entry to errors with key
     * > extensions. This entry, if set, must have a map as its value. This
     * > entry is reserved for implementors to add additional information to
     * > errors however they see fit, and there are no additional restrictions
     * > on its contents.
     *
     * It's difficult to be specific about what this means in terms of Kotlin
     * code when providing a general framework. Thus, at the present time, we
     * are forcing all value types to be [String]. At present, this is a
     * limiting decision that forces consumers to deal with this situation at
     * the serialization level.
     */
    val extensions: Map<String, String>?

    companion object
}

/**
 * [GraphQL Spec](https://spec.graphql.org/June2018/#sec-Errors)
 * > If an error can be associated to a particular point in the requested
 * > GraphQL document, it should contain an entry with the key locations
 * > with a list of locations, where each location is a map with the keys
 * > line and column, both positive numbers starting from 1 which describe
 * > the beginning of an associated syntax element.
 */
interface FSGraphqlQueryLocation {
    val line: Int
    val column: Int

    companion object
}