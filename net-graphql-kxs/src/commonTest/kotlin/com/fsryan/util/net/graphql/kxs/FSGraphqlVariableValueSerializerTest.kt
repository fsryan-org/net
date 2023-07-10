package com.fsryan.util.net.graphql.kxs

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.random.Random
import kotlin.test.Test

class FSGraphqlVariableValueSerializerTest {

    @Serializable
    data class TestClass1(
        val valueBoolean: Boolean = Random.nextBoolean(),
        val valueByte: Byte = Random.nextInt().toByte(),
        val valueChar: Char = Random.nextInt().toChar(),
        val valueDouble: Double = Random.nextDouble(),
        val valueFloat: Float = Random.nextFloat(),
        val valueInt: Int = Random.nextInt(),
        val valueLong: Long = Random.nextLong(),
        val valueShort: Short = Random.nextInt().toShort(),
        val valueString: String = CharArray(Random.nextInt(until = 33)) {
            Random.nextInt(from = 33, until = 126).toChar()
        }.concatToString()
    )

    @Serializable
    data class TestClass2(val testClass1: TestClass1 = TestClass1())

    @Suppress("JSON_FORMAT_REDUNDANT")
    @Test
    fun shouldCorrectlySerializeGraphqlVariablesFromContextualSerializer() {
        val output: String = Json {
            fsGraphqlVariableValueSerializer {
                add(TestClass1::class, TestClass1.serializer()) // <-- This is a class that contains only primitives+strings
                add(TestClass2::class, TestClass2.serializer()) // <-- This is a class that contains another
//                add(TestInterface::class, TestInterfaceImplementation.serializer()) { value ->
//                    when (value) {
//                        is TestInterfaceImplementation -> value
//                        else -> TestInterfaceImplementation(value.testClass2)
//                    }
//                }
            }
        }.encodeToString(
            KxsFSGraphqlExecutableDocument(
                name = "name",
                query = "query",
                variables = mapOf(
                    "test_class_1" to TestClass1(),
                    "test_class_2" to TestClass2(),
                    "valueBoolean" to Random.nextBoolean(),
                    "valueByte" to Random.nextInt().toByte(),
                    "valueChar" to Random.nextInt().toChar(),
                    "valueDouble" to Random.nextDouble(),
                    "valueFloat" to Random.nextFloat(),
                    "valueInt" to Random.nextInt(),
                    "valueLong" to Random.nextLong(),
                    "valueShort" to Random.nextInt().toShort(),
                    "valueArray" to Array(Random.nextInt(from = 1, until = 17)) {
                        TestClass2()
                    },
                    "valueList" to Array(Random.nextInt(from = 1, until = 17)) {
                        TestClass2()
                    }.toList(),
                    "valueSet" to Array(Random.nextInt(from = 1, until = 17)) { TestClass2() }.toSet()
                )
            )
        )
        println(output)
    }
}