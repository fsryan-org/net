package com.fsryan.util.net.graphql

/**
 * [a GraphqlDirective](https://spec.graphql.org/draft/#sec-Language.Directives)
 *
 * Directives allow you to put conditions into your query. There are two
 * built-in directives:
 * - [FSGraphqlDirective.skip]
 * - [FSGraphqlDirective.include]
 */
sealed interface FSGraphqlDirective: FSGraphqlItem, FSGraphqlArgumentContainer {
    
    override fun serialize(): String = "@$name${arguments?.serialize(delimiter = ",").orEmpty()}"

    data class Data internal constructor(
        override val name: String,
        override val arguments: List<FSGraphqlArgument>?
    ): FSGraphqlDirective

    companion object {
        /**
         * [The skip directive](https://spec.graphql.org/draft/#sec-Type-System.Directives.Built-in-Directives)
         * References the variable in this document's context that must be of
         * non null [FSGraphqlType.boolean].
         * @param booleanVariableName the name of the non-null boolean type
         * variable that you want to reference
         */
        fun skip(booleanVariableName: String): FSGraphqlDirective {
            return builtIn("skip", booleanVariableName)
        }
        /**
         * [The skip directive](https://spec.graphql.org/draft/#sec-Type-System.Directives.Built-in-Directives)
         * This function allows you to specify the value of the argument used.
         * @param literal the name of the non-null boolean type
         * variable that you want to reference
         */
        fun skip(literal: Boolean): FSGraphqlDirective {
            return builtInLiteral("skip", literal)
        }

        /**
         * [The include directive](https://spec.graphql.org/draft/#sec-Type-System.Directives.Built-in-Directives)
         * References the variable in this document's context that must be of
         * non null [FSGraphqlType.boolean].
         * @param booleanVariableName the name of the non-null boolean type
         * variable that you want to reference
         */
        fun include(booleanVariableName: String): FSGraphqlDirective {
            return builtIn("include", booleanVariableName)
        }
        /**
         * [The include directive](https://spec.graphql.org/draft/#sec-Type-System.Directives.Built-in-Directives)
         * This function allows you to specify the value of the argument used.
         * @param literal the name of the non-null boolean type
         * variable that you want to reference
         */
        fun include(literal: Boolean): FSGraphqlDirective {
            return builtInLiteral("include", literal)
        }

        /**
         * [Directive](https://spec.graphql.org/draft/#sec-Type-System.Directives)
         * Creates a directive with the specified name and arguments.
         * @param name The name of the directive
         * @param arguments all of the arguments that this directive takes.
         * Note that if you use [FSGraphqlArgument.variableRef], then you must be
         * sure such a variable exists and that the variable has the matching
         * type.
         *
         * @throws IllegalArgumentException when the [name] is invalid
         */
        fun create(name: String, arguments: List<FSGraphqlArgument>? = null): FSGraphqlDirective {
            FSGraphqlNamed.checkName(name)
            return Data(name = name, arguments = arguments)
        }

        private fun builtIn(name: String, booleanVariableName: String): FSGraphqlDirective {
            val arguments = listOf(FSGraphqlArgument.variableRef("if", booleanVariableName))
            return Data(name = name, arguments = arguments)
        }
        private fun builtInLiteral(name: String, value: Boolean): FSGraphqlDirective {
            val arguments = listOf(FSGraphqlArgument.literal("if", value))
            return Data(name = name, arguments = arguments)
        }
    }
}