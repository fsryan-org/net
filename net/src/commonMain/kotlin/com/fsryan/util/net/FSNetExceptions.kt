package com.fsryan.util.net


/**
 * Base class for exceptions caused by a network operation
 */
open class FSNetException(message: String? = null, cause: Throwable? = null): Exception(message, cause)

/**
 * Base class for exceptions that occur at the networking layer that are not
 * due to 4XX, 5XX error codes--these will not have error codes. You should
 * check the cause of this exception to see whether the cause is related to
 * the status of the network.
 */
open class FSNetConnectionException(message: String? = null, cause: Throwable? = null): FSNetException(message, cause)

/**
 * Base class for describing an exception that occurred on an FS Api. In this
 * case, the [statusCode] will be somewhere between 400 (inclusive) and 600
 * (exclusive). The [response] will be the parsed response body, if it exists.
 * In some cases, such as a 502 response, the response body cannot be
 * populated.
 */
open class FSNetApiException(
    val statusCode: Int,
    val response: FSApiErrorsResponse? = null,
    message: String? = null,
    cause: Throwable? = null
): FSNetException(message, cause)


/**
 * The specific exception returned when the service claims the caller is not
 * logged in. This could happen for a variety of reasons, perhaps the
 * `Authorization` header is not populated, incorrectly formatted, or the
 * token has a mismatched signature or is expired.
 */
class FSApiUnauthorizedException(
    response: FSApiErrorsResponse? = null,
    message: String? = null,
    cause: Throwable? = null
): FSNetApiException(statusCode = 401, response = response, message = message, cause = cause)

/**
 * The specific exception returned with the service claims the caller is not
 * allowed to access a particular resource, but _IS_ logged in and _IS_ allowed
 * to know of the existence of the resource.
 */
class FSApiForbiddenException(
    response: FSApiErrorsResponse? = null,
    message: String? = null,
    cause: Throwable? = null
): FSNetApiException(statusCode = 403, response = response, message = message, cause = cause)

/**
 * The specific exception returned with the service claims that the requested
 * resource was not found. This could occur for many reasons, however, the most
 * common reason is that the resource just doesn't exist. It is also possible
 * that the logged-in user is not authorized to know of the existence of the
 * resource.
 */
class FSApiNotFoundException(
    response: FSApiErrorsResponse? = null,
    message: String? = null,
    cause: Throwable? = null
): FSNetApiException(statusCode = 403, response = response, message = message, cause = cause)

/**
 * Sometimes the service will send back a response that says that the app needs
 * to slow down and not send so many requests. When this happens, the app is
 * intended to wait for a period of time before retrying the request.
 */
open class FSTooManyRequestsException(
    response: FSApiErrorsResponse? = null,
    message: String? = null,
    cause: Throwable? = null
): FSNetApiException(statusCode = 429, response = response, message = message, cause = cause)