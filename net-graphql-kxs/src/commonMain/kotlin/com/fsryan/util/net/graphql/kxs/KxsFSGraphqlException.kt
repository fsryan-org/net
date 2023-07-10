package com.fsryan.util.net.graphql.kxs

import com.fsryan.util.net.graphql.FSGraphqlErrorMessage
import com.fsryan.util.net.graphql.FSGraphqlException
import com.fsryan.util.net.graphql.FSGraphqlResponse

fun FSGraphqlResponse<*>.createException(): FSGraphqlException {
    var errorList = errors ?: emptyList()
    errorList = error?.let {
        errorList + FSGraphqlErrorMessage.toKxs(it, emptyList())
    } ?: errorList

    return when (errorList.isEmpty()) {
        true -> FSGraphqlException.createUnknown()
        false -> FSGraphqlException.create(errorList)
    }
}