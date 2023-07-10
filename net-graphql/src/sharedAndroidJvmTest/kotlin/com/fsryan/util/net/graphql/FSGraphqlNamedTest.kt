package com.fsryan.util.net.graphql

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource

class FSGraphqlNamedTest {

    @ParameterizedTest(name = "[{index}] {0} -> expects exception with message: {1}")
    @MethodSource("exceptionInput")
    fun checkNameException(input: String, expectedExceptionMessage: String) {
        val exception = assertThrows<IllegalArgumentException> {
            FSGraphqlNamed.checkName(input)
        }
        assertEquals(expectedExceptionMessage, exception.message)
    }

    @ParameterizedTest(name = "[{index}] expected to be valid name: {0}")
    @MethodSource("validNameInput")
    fun checkValidName(input: String) {
        FSGraphqlNamed.checkName(input)
    }

    companion object {

        private const val validStartChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_"
        private const val validContinueChars = validStartChars + "0123456789"

        @JvmStatic
        fun exceptionInput() = sequenceOf(
            arguments("", "name must not be empty"),
            arguments("-name", "Invalid name: '-name'; must start with [a-zA-Z_] and continue with [a-zA-Z_0-9] or be a valid introspection name"),
            arguments("0name", "Invalid name: '0name'; must start with [a-zA-Z_] and continue with [a-zA-Z_0-9] or be a valid introspection name"),
            arguments("n!", "Invalid name: 'n!'; must start with [a-zA-Z_] and continue with [a-zA-Z_0-9] or be a valid introspection name"),
        ).toList()

        @JvmStatic
        fun validNameInput() = validStartChars.asSequence().flatMap { startChar ->
            validContinueChars.asSequence().map { continueChar ->
                "$startChar$continueChar"
            }
        }.filter { it != "__" }
        .plus(sequenceOf(
            "__Directive",
            "__EnumValue",
            "__Field",
            "__InputValue",
            "__Schema",
            "__schema",
            "__Type",
            "__type",
            "__typename"
        )).toList()
    }
}