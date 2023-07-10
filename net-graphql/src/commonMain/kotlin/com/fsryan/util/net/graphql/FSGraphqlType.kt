package com.fsryan.util.net.graphql

import kotlin.jvm.JvmInline

/**
 * [A Graph QL Type Reference](https://spec.graphql.org/draft/#sec-Type-References)
 * Describes a Graph QL type. Actual Graph QL types can be either Named,
 * NonNull, or List, however, for simplicity, we're marking nullability in the
 * [nullable] property. Thus, nullability is a property of the type rather than
 * the type itself.
 *
 * A Note on nullability: We tend to prefer non-null types. As such, we default
 * to the non-null type. This is in contrast with
 * [Graph QL nullability default](https://spec.graphql.org/draft/#sec-Non-Null)
 * which says that types are nullable by default.
 */
interface FSGraphqlType: FSGraphqlItem {
    val nullable: Boolean

    override fun serialize(): String = if (nullable) name else "$name!"

    companion object {

        /**
         * [The Graph QL Int type](https://spec.graphql.org/draft/#sec-Int)
         *
         * This type is built in to the graphql framework.
         *
         * @param nullable `false` by default. Set to `true` if the type should
         * allow null values
         */
        fun integer(nullable: Boolean = false): FSGraphqlNamedType = named("Int", nullable)
        /**
         * [The Graph QL Float type](https://spec.graphql.org/draft/#sec-Float)
         *
         * This type is built in to the graphql framework
         *
         * @param nullable `false` by default. Set to `true` if the type should
         * allow null values
         */
        fun float(nullable: Boolean = false): FSGraphqlNamedType = named("Float", nullable)
        /**
         * [The Graph QL String type](https://spec.graphql.org/draft/#sec-String)
         *
         * This type is built in to the graphql framework
         *
         * @param nullable `false` by default. Set to `true` if the type should
         * allow null values
         */
        fun string(nullable: Boolean = false): FSGraphqlNamedType = named("String", nullable)
        /**
         * [The Graph QL Boolean type](https://spec.graphql.org/draft/#sec-Boolean)
         *
         * This type is built in to the graphql framework
         *
         * @param nullable `false` by default. Set to `true` if the type should
         * allow null values
         */
        fun boolean(nullable: Boolean = false): FSGraphqlNamedType = named("Boolean", nullable)
        /**
         * [The Graph QL ID type](https://spec.graphql.org/draft/#sec-ID)
         *
         * This type is built in to the graphql framework
         *
         * @param nullable `false` by default. Set to `true` if the type should
         * allow null values
         */
        fun id(nullable: Boolean = false): FSGraphqlNamedType = named("ID", nullable)
        /**
         * [The Graph QL Boolean type](https://spec.graphql.org/draft/#sec-Scalars.Built-in-Scalars)
         *
         * This type is built in to the graphql framework
         *
         * @param nullable `false` by default. Set to `true` if the type should
         * allow null values
         */
        fun named(name: String, nullable: Boolean = false): FSGraphqlNamedType {
            return if (nullable) NullableNamedType.create(name) else NonNullNamedType.create(name)
        }
        fun stringList(itemsNullable: Boolean = false, nullable: Boolean = false): FSGraphqlType {
            return when (nullable) {
                true -> NullableListType.create(string(itemsNullable))
                false -> NonNullListType.create(string(itemsNullable))
            }
        }

        /**
         * [The Graph QL List type](https://spec.graphql.org/draft/#sec-List)
         *
         * This type is built in to the graphql framework
         *
         * @param nullable `false` by default. Set to `true` if the type should
         * allow null values
         */
        fun list(parameterizedType: FSGraphqlType, nullable: Boolean = false): FSGraphqlType {
            return when (nullable) {
                true -> NullableListType.create(parameterizedType)
                false -> NonNullListType.create(parameterizedType)
            }
        }
    }
}

/**
 * We sometimes need to guarantee that a type is a named type as opposed to a
 * list type. This interface allows us to make that specification publicly.
 */
interface FSGraphqlNamedType: FSGraphqlType

interface NullableNamedType: FSGraphqlNamedType {
    companion object {
        fun create(value: String): FSGraphqlNamedType = NullableNamedTypeValue(value)
    }
}

interface NonNullNamedType: FSGraphqlNamedType {
    companion object {
        fun create(value: String): FSGraphqlNamedType = NonNullNamedTypeValue(value)
    }
}

interface NullableListType: FSGraphqlType {
    companion object {
        fun create(parameterizedType: FSGraphqlType): FSGraphqlType = NullableListTypeValue(parameterizedType)
    }
}

interface NonNullListType: FSGraphqlType {
    companion object {
        fun create(parameterizedType: FSGraphqlType): FSGraphqlType = NonNullListTypeValue(parameterizedType)
    }
}


@JvmInline
internal value class NullableNamedTypeValue(private val value: String): FSGraphqlNamedType {
    override val name: String get() = value
    override val nullable: Boolean get() = true
}

@JvmInline
internal value class NonNullNamedTypeValue(private val value: String): FSGraphqlNamedType {
    override val name: String get() = value
    override val nullable: Boolean get() = false
}

@JvmInline
internal value class NullableListTypeValue(private val parameterizedType: FSGraphqlType): FSGraphqlType {
    override val name: String get() = "[${parameterizedType.serialize()}]"
    override val nullable: Boolean get() = true
}

@JvmInline
internal value class NonNullListTypeValue(private val parameterizedType: FSGraphqlType): FSGraphqlType {
    override val name: String get() = "[${parameterizedType.serialize()}]"
    override val nullable: Boolean get() = false
}