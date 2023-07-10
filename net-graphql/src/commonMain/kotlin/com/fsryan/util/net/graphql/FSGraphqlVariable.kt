package com.fsryan.util.net.graphql

/**
 * [The Graph QL Variable](https://spec.graphql.org/draft/#sec-Language.Variables)
 *
 * Variables are the means of supplying variable input to graphql operations.
 * The values of these variables are given to the graphql document.
 */
interface FSGraphqlVariable: FSGraphqlItem {
    val type: FSGraphqlType

    override fun serialize(): String = "\$$name: ${type.serialize()}"

    data class Data internal constructor(
        override val name: String,
        override val type: FSGraphqlType
    ): FSGraphqlVariable

    /**
     * For creation of extension functions that make a static factory for
     * creating specific [FSGraphqlVariable] instances. All of the built-in
     * scalar types are already supported.
     */
    companion object {
        fun integer(name: String, nullable: Boolean = false): FSGraphqlVariable {
            return create(name = name, type = FSGraphqlType.integer(nullable))
        }
        fun float(name: String, nullable: Boolean = false): FSGraphqlVariable {
            return create(name = name, type = FSGraphqlType.float(nullable))
        }
        fun string(name: String, nullable: Boolean = false): FSGraphqlVariable {
            return create(name = name, type = FSGraphqlType.string(nullable))
        }
        fun boolean(name: String, nullable: Boolean = false): FSGraphqlVariable {
            return create(name = name, type = FSGraphqlType.boolean(nullable))
        }
        fun id(name: String, nullable: Boolean = false): FSGraphqlVariable {
            return create(name = name, type = FSGraphqlType.id(nullable))
        }
        fun create(name: String, type: FSGraphqlType): FSGraphqlVariable {
            FSGraphqlNamed.checkName(name)
            return Data(name = name, type = type)
        }
    }
}