package com.fsryan.util.net

/**
 * Encapsulates a response from an FS service.
 * [P] is the type encapsulating the parameters of the request
 * [R] is the type encapsulating the response to the request
 */
sealed interface FSResponse<out P: Any, out R: Any> {
    val request: FSRequest<P>
    val data: R?
}

/**
 * Encapsulates a request that generated data sent to an FS service.
 * [P] is the type encapsulating the parameters of the request
 */
sealed interface FSRequest<out P: Any> {
    val id: String
    val parameters: P
}

/**
 * fake constructor for a [FSResponse]
 *
 * @see FSResponse
 */
fun <P:Any, R:Any> FSResponse(request: FSRequest<P>, response: R?): FSResponse<P, R> {
    return FSResponseData(request, response)
}

/**
 * fake constructor for a [FSRequest]
 *
 * @see FSRequest
 */
fun <P:Any> FSRequest(id: String, parameters: P): FSRequest<P> {
    return FSRequestData(id, parameters)
}

private data class FSResponseData<out P: Any, out R: Any>(
    override val request: FSRequest<P>,
    override val data: R?
): FSResponse<P, R>

private data class FSRequestData<out P: Any>(override val id: String, override val parameters: P): FSRequest<P>