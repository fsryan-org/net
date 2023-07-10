package com.fsryan.util.net.kxs

import com.fsryan.util.net.FSApiError
import com.fsryan.util.net.FSApiErrorsResponse
import kotlinx.serialization.Serializable

@Serializable
data class KxsFSApiErrorsResponse internal constructor(
    override val errors: List<KxsFSApiError> = emptyList()
): FSApiErrorsResponse

@Serializable
data class KxsFSApiError internal constructor(
    override val value: String,
    override val message: String,
    override val param: String? = null,
    override val location: String? = null
): FSApiError