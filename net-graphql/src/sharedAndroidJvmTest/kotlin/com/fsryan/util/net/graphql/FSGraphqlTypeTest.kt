package com.fsryan.util.net.graphql

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class FSGraphqlTypeTest {

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("namedTypeInput")
    fun namedType(config: FSGraphqlTypeSerializationTestConfig) = config.runTest()

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("listTypeInput")
    fun listType(config: FSGraphqlTypeSerializationTestConfig) = config.runTest()

    class FSGraphqlTypeSerializationTestConfig(
        input: FSGraphqlType,
        expected: String
    ): FSGraphqlSerializationTestConfig<FSGraphqlType>(input = input, expected = expected)

    companion object {

        @JvmStatic
        fun namedTypeInput() = namedTypeScalars().toList()

        @JvmStatic
        fun listTypeInput() = listTypeScalars().toList()

        private fun namedTypeScalars() = sequenceOf(true, false).flatMap { nullable ->
            val append = if (nullable) "" else "!"
            sequenceOf(
                FSGraphqlTypeSerializationTestConfig(
                    input = FSGraphqlType.integer(nullable = nullable),
                    expected = "Int$append"
                ),
                FSGraphqlTypeSerializationTestConfig(
                    input = FSGraphqlType.float(nullable = nullable),
                    expected = "Float$append"
                ),
                FSGraphqlTypeSerializationTestConfig(
                    input = FSGraphqlType.string(nullable = nullable),
                    expected = "String$append"
                ),
                FSGraphqlTypeSerializationTestConfig(
                    input = FSGraphqlType.boolean(nullable = nullable),
                    expected = "Boolean$append"
                ),
                FSGraphqlTypeSerializationTestConfig(
                    input = FSGraphqlType.id(nullable = nullable),
                    expected = "ID$append"
                )
            )
        }

        private fun listTypeScalars() = sequenceOf(true, false).flatMap { nullable ->
            val append = if (nullable) "" else "!"
            namedTypeScalars().map { namedTypeConfig ->
                FSGraphqlTypeSerializationTestConfig(
                    input = FSGraphqlType.list(namedTypeConfig.input, nullable = nullable),
                    expected = "[${namedTypeConfig.expected}]$append"
                )
            }
        }
    }
}