package com.fsryan.util.net

import com.fsryan.util.types.FSValueOrError

/**
 * An interface that allows for maintaining/updating the request context.
 * > *Note:*
 * > This is _CRITICAL_: The design of this interface is made to allow for
 * > composition of other implementations of [FSContextualRequestWrapper]
 * > objects. For example, one possible [FSContextualRequestWrapper]
 * > implementation could populate the [FSRequestContext] with the user ID and
 * > base URL, This [FSContextualRequestWrapper] could itself be wrapped by
 * > a [FSContextualRequestWrapper] that adds the access token to the
 * > [FSRequestContext]. Additionally, this may be useful for providing common
 * > data like headers.
 *
 * > *Note:*
 * > If you want to create an implementation of this interface that requires
 * > the userId and baseUrl to be populated, then you should change the
 * > nullability of the `currentContext` type to non-null. Then, your
 * > implementation is forced to be wrapped by another implementation that
 * > provides the [FSRequestContext].
 */
interface FSContextualRequestWrapper {
    /**
     * Wraps an operation that requires context. Note that the return type
     * _MUST_ be non-null. If you need this to be nullable, then you should
     * use the [FSValueOrError] type and return [FSValueOrError.error] if
     * the actual value is null.
     *
     * @param currentContext the [FSRequestContext] for the operation, if it
     * exists. If it does not exist, then the [FSContextualRequestWrapper]
     * implementation is forced to create one, meaning, at least the user ID
     * and base URL must be populated.
     * @param operation the operation, given the [FSRequestContext]
     * @see FSRequestContext
     * @return the result of the operation after it has been provided the
     * [FSRequestContext]
     */
    suspend fun <T: Any> wrapContextualOperation(
        currentContext: FSRequestContext? = null,
        operation: suspend (currentContext: FSRequestContext) -> T
    ): T

    companion object {
        /**
         * Creates a [FSContextualRequestWrapper] that provides the user ID and
         * base URL from already-known values. This is especially useful in
         * testing scenarios or when you need to plug in an external value.
         */
        fun createStatic(userId: String, authBaseUrl: String): FSContextualRequestWrapper {
            return FSContextualRequestWrapper { userId to authBaseUrl }
        }
    }
}

/**
 * Fake constructor for [FSContextualRequestWrapper], this enables delegation
 * of the operations that fetch the user ID and base URL as a pair (your
 * implementation may split this into two operations if necessary).
 */
fun FSContextualRequestWrapper(
    userIdBaseUrlDelegate: suspend () -> Pair<String, String>,
): FSContextualRequestWrapper = object: FSContextualRequestWrapper {
    override suspend fun <T : Any> wrapContextualOperation(
        currentContext: FSRequestContext?,
        operation: suspend (currentContext: FSRequestContext) -> T
    ): T {
        val (userId, baseUrl) = userIdBaseUrlDelegate()
        val context = FSRequestContext(userId, baseUrl)
        return operation(context)
    }
}
