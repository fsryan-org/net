package com.fsryan.util.net

import com.fsryan.util.types.FSPropertyContainer
import com.fsryan.util.types.requireTypedValue

private const val CONTEXT_KEY_USER_ID: String = "com.fsryan.util.net.USER_ID"
private const val CONTEXT_KEY_AUTH_BASE_URL: String = "com.fsryan.util.net.AUTH_BASE_URL"
private const val CONTEXT_KEY_ACCESS_TOKEN: String = "com.fsryan.util.net.ACCESS_TOKEN"

/**
 * An interface that allows for maintaining/updating the request context. This
 * especially useful in situations like a chain of requests in which you need
 * to maintain the same contextual data. For example, if the currently active
 * user happens to change between requests 1 and 2 on the chain, the second
 * request would become invalid if we were to look up the user ID again.
 *
 * There are other bits of contextual information, such as the access token,
 * that may be needed.
 *
 * > *Note:*
 * > You should not serialize this, as the data for the request may be stale
 * > upon deserialization.
 *
 * > *Note:*
 * > You cannot overwrite userId or baseUrl with the [enrichedWith] function.
 */
sealed interface FSRequestContext: FSPropertyContainer<FSRequestContext> {
    /**
     * The contextual user ID.
     */
    val userId: String
    /**
     * The contextual base URL of whatever auth service you're using
     */
    val authBaseUrl: String
}

/**
 * Fake constructor for [FSRequestContext], this requires the minimal set of
 * values to add to the context. If you add [properties], then you should also
 * create an extension property/function that provides strongly typed access to
 * the property.
 */
fun FSRequestContext(userId: String, baseUrl: String, properties: Map<String, Any> = emptyMap()): FSRequestContext {
    return FSRequestContextData(
        properties.toMutableMap().apply {
            put(CONTEXT_KEY_USER_ID, userId)
            put(CONTEXT_KEY_AUTH_BASE_URL, baseUrl)
        }
    )
}

/**
 * Adds the access token to the [FSRequestContext]. This is for maintaining the
 * access token across a chain of requests.
 */
fun FSRequestContext.withAccessToken(accessToken: String): FSRequestContext {
    return enrichedWith(mapOf(CONTEXT_KEY_ACCESS_TOKEN to accessToken))
}

/**
 * Provides access to the access token in the [FSRequestContext]. This is for
 * maintaining the access token across a chain of requests.
 */
val FSRequestContext.accessToken: String? get() = value(CONTEXT_KEY_ACCESS_TOKEN)

private data class FSRequestContextData(override val extensibleProperties: Map<String, Any>): FSRequestContext {
    override val userId: String get() = requireTypedValue(CONTEXT_KEY_USER_ID)
    override val authBaseUrl: String get() = requireTypedValue(CONTEXT_KEY_AUTH_BASE_URL)
    override fun enrichedWith(extendedProperties: Map<String, Any>): FSRequestContext {
        return FSRequestContext(userId, authBaseUrl, extensibleProperties + extendedProperties)
    }
}