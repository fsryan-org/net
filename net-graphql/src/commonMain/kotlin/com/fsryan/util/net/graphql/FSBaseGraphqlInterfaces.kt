package com.fsryan.util.net.graphql

/**
 * Any unit that can be serialized as a constituent part of or all of a graphql
 * query, mutation, or subscription. The [serialize] function actually
 * serializes graphql query/mutation/subscription in accordance with the
 * [Graph QL spec](https://spec.graphql.org/June2018)
 */
interface FSGraphqlSerializable {
    fun serialize(): String
}

internal fun List<FSGraphqlSerializable>.serialize(
    delimiter: String,
    groupStart: String = "(",
    groupEnd: String = ")",
    ungroupForSize1: Boolean = false
): String = when (size) {
    0 -> ""
    1 -> when (ungroupForSize1) {
        true -> get(0).serialize()
        false -> "$groupStart${joinToString(separator = delimiter) { it.serialize() }}$groupEnd"
    }
    else -> "$groupStart${joinToString(separator = delimiter) { it.serialize() }}$groupEnd"
}

interface FSGraphqlNamed {
    val name: String

    companion object {
        /**
         * Checks whether the name passed in is a valid Graph QL name.
         */
        fun checkName(name: String) {
            if (name.isEmpty()) {
                throw IllegalArgumentException("name must not be empty")
            }
            if (name.startsWith("__")) {
                when (name) {
                    "__Directive",
                    "__EnumValue",
                    "__Field",
                    "__InputValue",
                    "__Schema",
                    "__schema",
                    "__Type",
                    "__type",
                    "__typename" -> return
                    else -> throw IllegalArgumentException("Invalid introspection: $name")
                }
            }
            if (!Regex("[a-zA-Z_][a-zA-Z_0-9]*").matches(name)) {
                throw IllegalArgumentException("Invalid name: '$name'; must start with [a-zA-Z_] and continue with [a-zA-Z_0-9] or be a valid introspection name")
            }
        }
    }
}

interface FSGraphqlItem: FSGraphqlNamed, FSGraphqlSerializable

interface FSGraphqlArgumentContainer {
    val arguments: List<FSGraphqlArgument>?
}

interface FSGraphqlVariableContainer {
    val variables: List<FSGraphqlVariable>?
}

interface FSGraphqlDirectiveContainer {
    val directives: List<FSGraphqlDirective>?
}

interface FSGraphqlSelectionSetContainer {
    val selectionSet: List<FSGraphqlSelection>?
}
