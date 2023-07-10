package com.fsryan.util.net.graphql

import org.junit.jupiter.api.Assertions.assertEquals

abstract class FSGraphqlSerializationTestConfig<T:FSGraphqlSerializable>(
    val input: T,
    val expected: String
) {
    fun runTest() {
        val actual = input.serialize()
        assertEquals(expected, actual)
    }

    override fun toString(): String {
        return "$input -> $expected"
    }
}