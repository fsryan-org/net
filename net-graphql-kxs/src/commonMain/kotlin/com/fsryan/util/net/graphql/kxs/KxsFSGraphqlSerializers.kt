package com.fsryan.util.net.graphql.kxs

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ArraySerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import kotlinx.serialization.serializer
import kotlin.jvm.JvmInline
import kotlin.reflect.KClass

/**
 * A serializer (not currently deserializer) for [Any] that will be used for
 * serializing Graphql Variables.
 *
 * > **Note**
 * > In the one known implementation of this interface (the one that gets built
 * > for you if you use the [Builder.build] function or the
 * > [fsGraphqlVariableValueSerializer] function), primitive value, array,
 * > set, and list serialization are all handled for you. _SERIALIZATION OF
 * > OTHER COLLECTIONS IS NOT HANDLED FOR YOU_.
 *
 * > **Note**
 * > In the one known implementation of this interface (the one that gets built
 * > for you if you use the [Builder.build] function or the
 * > [fsGraphqlVariableValueSerializer] function),
 */
interface FSGraphqlVariableValueSerializer: KSerializer<Any> {

    /**
     * A builder for a [FSGraphqlVariableValueSerializer]. You have
     */
    class Builder internal constructor() {
        private val serializerMap = mutableMapOf<KClass<*>, KSerializer<Any>>()

        /**
         * Adds a serializer to the serialization context for [Any] that
         * serializes type [T] only.
         */
        fun <T:Any> add(klass: KClass<T>, serializer: KSerializer<T>): Builder {
            serializerMap[klass] = serializer as KSerializer<Any>
            return this
        }

        // TODO: this is causing the following exception for native only:
        /*
         * Compilation failed: Backend Internal error: Exception during IR lowering
         * File being compiled: /Users/ryan/repos/ryan/fsryan/net/net-graphql-kxs/src/commonMain/kotlin/com/fsryan/util/net/graphql/kxs/KxsFSGraphqlSerializers.kt
         * The root cause java.lang.IllegalArgumentException was thrown at: org.jetbrains.kotlin.ir.types.IrTypeSystemContext$DefaultImpls.captureFromArguments(IrTypeSystemContext.kt:248)
         */
//        /**
//         * Adds a serializer to the serialization context for [Any] that is
//         * capable of doing an object mapping prior to serialization. This is
//         * useful in case alternative implementations of interface [I] are
//         * acceptable, but alternative serializations are not (they usually
//         * aren't). For example:
//         *
//         * ```
//         * interface MyInterface {
//         *     val mySpecialValue: String
//         * }
//         *
//         * @Serializable
//         * data class MyInterfaceImplementation(): MyInterface {
//         *     override val mySpecialValue = "Isn't this special?"
//         * }
//         * ```
//         *
//         * In this situation, you'd get a generated
//         * [KSerializer]<`MyInterfaceImplementation`> instead of
//         * [KSerializer]<`MyInterface`>. If it is important to serialize
//         * `MyInterface`, then use this function, providing the [mapper], which
//         * maps from type [I] (`MyInterface`) to type [S]
//         * (`MyInterfaceImplementation`), an implementation of [I] that has a
//         * serializer.
//         */
//        fun <I:Any, S:I> add(klass: KClass<I>, serializer: KSerializer<S>, mapper: (I) -> S): Builder {
//            serializerMap[klass] = object: KSerializer<I> {
//                override val descriptor: SerialDescriptor
//                    get() = serializer.descriptor
//
//                override fun deserialize(decoder: Decoder): I {
//                    return serializer.deserialize(decoder)
//                }
//
//                override fun serialize(encoder: Encoder, value: I) {
//                    val mapped: S = mapper(value)
//                    serializer.serialize(encoder, mapped)
//                }
//
//            } as KSerializer<Any>
//            return this
//        }

        fun build(): FSGraphqlVariableValueSerializer {
            return FSGraphqlVariableValueSerializerValue(serializerMap.toMap())
        }
    }
}

/**
 * At whatever level you're setting up the serializers for
 * kotlinx.serialization to use when serializing/deserializing, you should call
 * this function. The serializer you're configuring specifically applies to the
 * [KxsFSGraphqlExecutableDocument.variables] map values, which can be of type
 * [Any] and the serializer for types unknown at this level cannot be
 * determined when this library is built.
 *
 * If you're developing a library that needs to serialize graphql variable
 * values, then you need to create an extension function on
 * [FSGraphqlVariableValueSerializer.Builder] that the consumer may call. In this
 * function, you need to add whatever serializers to the context that are
 * required for the library's graphql calls.
 *
 * > **Note**
 * > It is safe to add serializers for the same class multiple times. However,
 * > you should be careful of the order you're adding the serializers. the last
 * > one wins.
 */
fun JsonBuilder.fsGraphqlVariableValueSerializer(enrich: FSGraphqlVariableValueSerializer.Builder.() -> Unit) {
    val builder = FSGraphqlVariableValueSerializer.Builder()
    builder.enrich()
    val serializer = builder.build()
    serializersModule += SerializersModule {
        contextual(Any::class) {
            serializer
        }
    }
}


@JvmInline
private value class FSGraphqlVariableValueSerializerValue(
    val registry: Map<KClass<*>, KSerializer<Any>>
): FSGraphqlVariableValueSerializer {
    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("GraphqlVariableValueSerializer")

    // TODO: support deserializing . . . for completeness? It's not required
    //  for the single purpose of serializing graphql variables, but throwing
    //  the UnsupportedOperationException is a bit suspect.
    override fun deserialize(decoder: Decoder): Any {
        throw UnsupportedOperationException("Impossible to deserialize Any")
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: Any) {
        when (value) {
            is Boolean -> encoder.encodeSerializableValue(serializer(), value)
            is Byte -> encoder.encodeSerializableValue(serializer(), value)
            is Char -> encoder.encodeSerializableValue(serializer(), value)
            is Double -> encoder.encodeSerializableValue(serializer(), value)
            is Float -> encoder.encodeSerializableValue(serializer(), value)
            is Int -> encoder.encodeSerializableValue(serializer(), value)
            is Long -> encoder.encodeSerializableValue(serializer(), value)
            is Short -> encoder.encodeSerializableValue(serializer(), value)
            is String -> encoder.encodeSerializableValue(serializer(), value)
            is Array<*> -> encoder.encodeSerializableValue(ArraySerializer(this), value as Array<Any>)
            is List<*> -> encoder.encodeSerializableValue(ListSerializer(this), value as List<Any>)
            is Set<*> -> encoder.encodeSerializableValue(SetSerializer(this), value as Set<Any>)
            else -> findPolymorphicSerializer(value)?.let { resolved ->
                encoder.encodeSerializableValue(resolved, value)
            } ?: throw IllegalStateException("Could not find serializer for class ${value::class}")
        }
    }

    private fun findPolymorphicSerializer(value: Any): KSerializer<Any>? {
        return registry[value::class] ?: registry.keys.firstOrNull {
            it.isInstance(value)
        }?.let { registry[it] }
    }
}