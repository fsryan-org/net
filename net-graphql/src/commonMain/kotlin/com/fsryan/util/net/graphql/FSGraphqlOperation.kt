package com.fsryan.util.net.graphql

/**
 * [A Graph QL Operation](https://spec.graphql.org/draft/#sec-Language.Operations)
 *
 * This represents a full Graph QL operation (query, mutation, or subscription)
 *
 * Currently, only [FSGraphqlQuery] and [FSGraphqlMutation] are supported. We may
 * support subscription someday.
 */
sealed interface FSGraphqlOperation: FSGraphqlItem,
    FSGraphqlVariableContainer,
    FSGraphqlDirectiveContainer,
    FSGraphqlSelectionSetContainer {

    val type: String
    override val selectionSet: List<FSGraphqlSelection>

    override fun serialize(): String {
        val variablesStr = variables?.serialize(delimiter = ",").orEmpty()
        val directivesStr = directives?.serialize(
            delimiter = " ",
            groupStart = " ",
            groupEnd = ""
        ).orEmpty()
        val selectionSetStr = selectionSet.serialize(
            delimiter = " ",
            groupStart = "{",
            groupEnd = "}"
        )
        return "$type $name$variablesStr$directivesStr$selectionSetStr"
    }

    companion object {
        internal fun checkSelectionSet(selectionSet: List<FSGraphqlSelection>) {
            if (selectionSet.isEmpty()) {
                throw IllegalArgumentException("selection set must not be empty")
            }
        }
    }
}

sealed interface FSGraphqlQuery: FSGraphqlOperation {

    override val type: String
        get() = "query"

    data class Data internal constructor(
        override val directives: List<FSGraphqlDirective>?,
        override val name: String,
        override val selectionSet: List<FSGraphqlSelection>,
        override val variables: List<FSGraphqlVariable>?
    ): FSGraphqlQuery

    companion object {
        fun create(
            directives: List<FSGraphqlDirective>? = null,
            name: String,
            selectionSet: List<FSGraphqlSelection>,
            variables: List<FSGraphqlVariable>? = null
        ): FSGraphqlQuery {
            FSGraphqlNamed.checkName(name)
            FSGraphqlOperation.checkSelectionSet(selectionSet)
            return Data(
                directives = directives,
                name = name,
                selectionSet = selectionSet,
                variables = variables
            )
        }
    }
}

sealed interface FSGraphqlMutation: FSGraphqlOperation {

    override val type: String
        get() = "mutation"

    data class Data internal constructor(
        override val directives: List<FSGraphqlDirective>?,
        override val name: String,
        override val selectionSet: List<FSGraphqlSelection>,
        override val variables: List<FSGraphqlVariable>?
    ): FSGraphqlMutation

    companion object {
        fun create(
            directives: List<FSGraphqlDirective>? = null,
            name: String,
            selectionSet: List<FSGraphqlSelection>,
            variables: List<FSGraphqlVariable>? = null
        ): FSGraphqlMutation {
            FSGraphqlNamed.checkName(name)
            FSGraphqlOperation.checkSelectionSet(selectionSet)
            return Data(
                directives = directives,
                name = name,
                selectionSet = selectionSet,
                variables = variables
            )
        }
    }
}