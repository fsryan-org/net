package com.fsryan.util.net.graphql

/**
 * [A Graph QL Fragment](https://spec.graphql.org/draft/#sec-Language.Fragments)
 *
 * This is one of the building blocks of a [FSGraphqlExecutableDocument], but it
 * does not get formal recognition as a variable because the serialization of
 * the fragments are as an appendage to the query.
 *
 * Your [FSGraphqlExecutableDocument] must have accounting for any
 * [FSGraphqlFragmentSpread] that is used in your query.
 */
sealed interface FSGraphqlFragment: FSGraphqlItem,
    FSGraphqlDirectiveContainer,
    FSGraphqlSelectionSetContainer {

    val type: FSGraphqlNamedType
    override val selectionSet: List<FSGraphqlSelection>

    override fun serialize(): String {
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
        return "fragment $name on ${type.name}$directivesStr$selectionSetStr"
    }

    data class Data internal constructor(
        override val directives: List<FSGraphqlDirective>? = null,
        override val name: String,
        override val selectionSet: List<FSGraphqlSelection>,
        override val type: FSGraphqlNamedType
    ): FSGraphqlFragment

    companion object {
        /**
         * @throws IllegalArgumentException if the name is an illegal fragment
         * name
         */
        fun checkValidFragmentName(name: String) {
            FSGraphqlNamed.checkName(name)
            if (name == "on") {
                throw IllegalArgumentException("Cannot name a fragment 'on'")
            }
        }

        fun create(
            name: String,
            selectionSet: List<FSGraphqlSelection>,
            type: FSGraphqlNamedType
        ): FSGraphqlFragment {
            checkValidFragmentName(name)
            return Data(name = name, selectionSet = selectionSet, type = type)
        }
    }
}