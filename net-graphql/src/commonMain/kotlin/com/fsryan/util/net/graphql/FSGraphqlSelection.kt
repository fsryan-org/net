package com.fsryan.util.net.graphql

import kotlin.jvm.JvmInline

/**
 * [A Graph QL Selection](https://spec.graphql.org/draft/#sec-Selection-Sets)
 *
 * This interface comprises anything that may be included in a selection set:
 * - [FSGraphqlField]
 * - [FSGraphqlFragmentSpread]
 * - [FSGraphqlInlineFragment]
 *
 * This interface allows you to define just what you want to get back in a
 * response. Graph QL's SelectionSet type is really just a collection of
 * Graph QL Selections, which could be
 * - [Fields](https://spec.graphql.org/draft/#sec-Language.Fields)
 * - [Fragment Spreads](https://spec.graphql.org/draft/#FragmentSpread), or
 * - [Inline Fragments](https://spec.graphql.org/draft/#sec-Inline-Fragments)
 */
interface FSGraphqlSelection: FSGraphqlSerializable, FSGraphqlDirectiveContainer

/**
 * [A Graph QL Field](https://spec.graphql.org/draft/#sec-Language.Fields)
 *
 * [The Graph QL Argument section](https://spec.graphql.org/draft/#sec-Language.Arguments)
 * describes fields in an important way:
 *
 * > Fields are conceptually functions which return values, and occasionally
 * accept arguments which alter their behavior
 *
 * As far as our library is concerned, this interface of the greatest
 * importance in creating building blocks of queries that can then get consumed
 * at a higher level. The idea is to put together queries that leverage fields
 * defined at lower levels so that you do not have to redefine that portion of
 * the query, how to serialize the query, or how to deserialize the return.
 */
interface FSGraphqlField: FSGraphqlSelection,
    FSGraphqlNamed,
    FSGraphqlArgumentContainer,
    FSGraphqlSelectionSetContainer {

    /**
     * You can differentiate multiple fields of the same type using an alias.
     */
    val alias: String?

    override fun serialize(): String {
        val aliasStr = alias?.let { "$it: "}.orEmpty()
        val argumentsStr = arguments?.serialize(delimiter = ",").orEmpty()
        val directivesStr = directives?.serialize(
            delimiter = " ",
            groupStart = " ",
            groupEnd = ""
        ).orEmpty()
        val selectionSetStr = selectionSet?.serialize(
            delimiter = " ",
            groupStart = "{",
            groupEnd = "}"
        ).orEmpty()
        return "$aliasStr$name$argumentsStr$directivesStr$selectionSetStr"
    }

    data class Data internal constructor(
        override val alias: String? = null,
        override val arguments: List<FSGraphqlArgument>? = null,
        override val name: String,
        override val directives: List<FSGraphqlDirective>? = null,
        override val selectionSet: List<FSGraphqlSelection>? = null
    ): FSGraphqlField

    companion object {
        fun typename(): FSGraphqlField = FSGraphqlNameOnlyField.create("__typename")
        fun named(name: String): FSGraphqlField {
            FSGraphqlNamed.checkName(name)
            return FSGraphqlNameOnlyField.create(name)
        }
        fun create(
            alias: String? = null,
            arguments: List<FSGraphqlArgument>? = null,
            name: String,
            directives: List<FSGraphqlDirective>? = null,
            selectionSet: List<FSGraphqlSelection>? = null
        ): FSGraphqlField {
            alias?.let { FSGraphqlNamed.checkName(it) }
            FSGraphqlNamed.checkName(name)
            return Data(
                alias = alias,
                arguments = arguments,
                name = name,
                directives = directives,
                selectionSet = selectionSet
            )
        }
    }
}

interface FSGraphqlNameOnlyField: FSGraphqlField {
    companion object {
        fun create(value: String): FSGraphqlField = FSGraphqlNameOnlyFieldValue(value)
    }
}

/**
 * [The Graph QL Fragment Spread](https://spec.graphql.org/draft/#FragmentSpread)
 *
 * This is the means by which you reference a fragment definition that you have
 * created elsewhere. In combination with the [FSGraphqlFragment], which lets you
 * create the
 * [Graph QL Fragment Definition](https://spec.graphql.org/draft/#FragmentDefinition),
 * the fragmetn spread is yet another means by which you can share query
 * building blocks.
 */
interface FSGraphqlFragmentSpread: FSGraphqlSelection, FSGraphqlNamed {

    override fun serialize(): String {
        val directivesStr = directives?.serialize(
            delimiter = " ",
            groupStart = " ",
            groupEnd = ""
        ).orEmpty()
        return "...$name$directivesStr"
    }

    data class Data internal constructor(
        override val name: String,
        override val directives: List<FSGraphqlDirective>? = null
    ): FSGraphqlFragmentSpread

    companion object {
        /**
         * If your fragment spread contains no directives, then use this for
         * an efficiency improvement.
         */
        fun named(name: String): FSGraphqlFragmentSpread {
            FSGraphqlFragment.checkValidFragmentName(name)
            return FSGraphqlNameOnlyFragmentSpread.create(name)
        }
        fun create(name: String, directives: List<FSGraphqlDirective>? = null): FSGraphqlFragmentSpread {
            FSGraphqlFragment.checkValidFragmentName(name)
            return Data(name = name, directives = directives)
        }
    }
}

/**
 * A memory optimization for when creating queries with lots of spreads. If
 * your platform supports the idea of a value class, then you should use it as
 * the underlying implementation of this interface.
 */
interface FSGraphqlNameOnlyFragmentSpread: FSGraphqlFragmentSpread {
    companion object {
        fun create(value: String): FSGraphqlFragmentSpread {
            return FSGraphqlNameOnlyFragmentSpreadValue(value)
        }
    }
}


/**
 * [The Graph QL InlineFragment](https://spec.graphql.org/draft/#sec-Inline-Fragments)
 *
 * Sometimes you will want to optionally receive additional values in a
 * selection set based upon the type of the data returned. Since you can't know
 * what that type is when defining the query, this is how you account for the
 * different possibilites of returned types.
 */
interface FSGraphqlInlineFragment: FSGraphqlSelection, FSGraphqlSelectionSetContainer {
    val typeCondition: FSGraphqlNamedType?

    data class Data internal constructor(
        override val directives: List<FSGraphqlDirective>? = null,
        override val selectionSet: List<FSGraphqlSelection>,
        override val typeCondition: FSGraphqlNamedType? = null
    ): FSGraphqlInlineFragment {
        // TODO: put this in the interface?
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
            return when (typeCondition) {
                null -> "...$directivesStr$selectionSetStr"
                else -> "...on ${typeCondition.name}$directivesStr$selectionSetStr"
            }
        }
    }

    companion object {

        /**
         * [Graph QL Inline Fragment](https://spec.graphql.org/draft/#sec-Inline-Fragments)
         *
         * This kind of inline fragment allows you to apply polymorphism.
         */
        fun createOnType(
            selectionSet: List<FSGraphqlSelection>,
            typeCondition: FSGraphqlNamedType,
        ): FSGraphqlInlineFragment {
            return create(selectionSet = selectionSet, typeCondition = typeCondition)
        }

        /**
         * [Graph QL optional inclusion](https://spec.graphql.org/draft/#sel-GAFbfJABBBqC7wY)
         *
         * This kind of inline fragment allows you to apply a directive such as one that
         * optionally includes fields in the enclosing type.
         */
        fun createForEnclosingType(
            directives: List<FSGraphqlDirective>,
            selectionSet: List<FSGraphqlSelection>
        ): FSGraphqlInlineFragment {
            if (directives.isEmpty()) {
                throw IllegalArgumentException("inline fragment on enclosing type must contain at least one directive")
            }
            return Data(directives = directives, selectionSet = selectionSet)
        }

        private fun create(
            directives: List<FSGraphqlDirective>? = null,
            selectionSet: List<FSGraphqlSelection>,
            /**
             * Inline fragments may also be used to apply a directive to a
             * group of fields. If the TypeCondition is omitted, an inline
             * fragment is considered to be of the same type as the enclosing
             * context.
             */
            typeCondition: FSGraphqlNamedType? = null
        ): FSGraphqlInlineFragment {
            if (selectionSet.isEmpty()) {
                throw IllegalArgumentException("Selection set must contain at least one selection")
            }
            return Data(
                directives = directives,
                selectionSet = selectionSet,
                typeCondition = typeCondition
            )
        }
    }
}

/**
 * A memory optimization for when creating queries with lots of fields. This
 * prevents unnecessary memory allocations when all we're doing is applying
 * significance ot a string.
 */
@JvmInline
internal value class FSGraphqlNameOnlyFieldValue(private val value: String): FSGraphqlField {
    override fun serialize(): String = value
    override val directives: List<FSGraphqlDirective>? get() = null
    override val name: String get() = value
    override val alias: String? get() = null
    override val arguments: List<FSGraphqlArgument>? get() = null
    override val selectionSet: List<FSGraphqlSelection>? get() = null
}

/**
 * A memory optimization for when creating queries with lots of spreads. This
 * prevents unnecessary memory allocations when all we're doing is applying
 * significance ot a string.
 */
@JvmInline
internal value class FSGraphqlNameOnlyFragmentSpreadValue(private val value: String): FSGraphqlFragmentSpread {
    override val directives: List<FSGraphqlDirective>? get() = null
    override val name: String get() = value
}