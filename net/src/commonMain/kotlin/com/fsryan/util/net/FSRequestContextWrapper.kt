package com.swiftly.net

/**
 * An interface that allows for maintaining/updating the request context
 */
interface FSRequestContextWrapper {
    /**
     * Wraps an Authorized operation and will execute the error path without
     * calling the operation when the access token cannot be fetched.
     * Implementations should be careful to wrap any [Throwable] emitted by the
     * token fetch operation inside an [AccessTokenFetchFailedException] so the
     * consumer can decide how to proceed.
     */
    suspend fun <T: Any> wrapAuthorizedOperation(
        /**
         * This allows the caller to set the user whose authorization token is
         * used in the request. This is important because a chain of requests
         * may get started such that you cannot allow thw user to change during
         * the course of the chain.
         */
        contextualUserId: String? = null,
        /**
         * This allows the caller to set the base URL to make calls against for
         * the request. This is important because a request chain may get
         * started such that you cannot allow the base URL to change during the
         * course of the chain.
         */
        contextualBaseUrl: String? = null,
        /**
         * The operation that must be authorized. The user whose authorization
         * token is being used is passed in as well as the accessToken
         */
        operation: suspend (tokenUserId: String, baseUrl: String, accessToken: String) -> T
    ): T

    companion object {
        fun jwtAuthHeader(token: String): String = "Bearer $token"

        fun createUnauthorized(unauthorizedUser: String, currentBaseUrl: suspend () -> String): AuthorizedRequester {
            return object: AuthorizedRequester {
                override suspend fun <T : Any> wrapAuthorizedOperation(
                    contextualUserId: String?,
                    contextualBaseUrl: String?,
                    operation: suspend (tokenUserId: String, baseUrl: String, accessToken: String) -> T
                ): T {
                    throw com.swiftly.net.AccessTokenFetchFailedException(
                        isUserKnown = false,
                        contextualUserId = contextualUserId ?: unauthorizedUser,
                        contextualBaseUrl = contextualBaseUrl ?: currentBaseUrl(),
                        cause = null
                    )
                }

            }
        }
    }
}
