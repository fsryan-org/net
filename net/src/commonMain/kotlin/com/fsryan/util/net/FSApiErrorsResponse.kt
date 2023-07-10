package com.fsryan.util.net

/**
 * A response from an FS API that contains a list of errors
 */
interface FSApiErrorsResponse {
    /**
     * A list of errors that came back from an FS API. Except when the FS API
     * is not working properly, there should always be at least one error.
     */
    val errors: List<FSApiError>
}

/**
 * An error that came back from an FS API
 */
interface FSApiError {
    /**
     * The value that caused the error
     */
    val value: String
    /**
     * A (hopefully) helpful message that describes the error that occurred.
     */
    val message: String
    /**
     * The parameter where the [value] was read
     */
    val param: String?
    /**
     * The location on the request where the [value] was read
     */
    val location: String?
}