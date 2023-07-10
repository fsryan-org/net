package com.fsryan.util.net.graphql

sealed class FSGraphqlException(message: String): Exception(message) {
    companion object {
        fun create(errorList: List<FSGraphqlErrorMessage>): FSGraphqlException {
            return when (errorList.isNotEmpty()) {
                true -> FSGraphqlErrorException(errorList)
                false -> FSGraphqlUnknownErrorException()
            }
        }
        fun createUnknown(): FSGraphqlException = create(emptyList())
    }
}

data class FSGraphqlErrorException internal constructor(
    val errors: List<FSGraphqlErrorMessage>
): FSGraphqlException(errors.joinToString("\n"))

class FSGraphqlUnknownErrorException internal constructor(): FSGraphqlException("No error message received") {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FSGraphqlUnknownErrorException) return false
        return true
    }

    override fun hashCode(): Int {
        return this::class.hashCode()
    }
}