package com.fsryan.util.net.graphql

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class FSGraphqlArgumentTest {

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("argumentInput")
    fun variable(config: FSGraphqlArgumentSerializationTestConfig) = config.runTest()

    class FSGraphqlArgumentSerializationTestConfig(
        input: FSGraphqlArgument,
        expected: String
    ): FSGraphqlSerializationTestConfig<FSGraphqlArgument>(input = input, expected = expected)
    
    companion object {

        @JvmStatic
        fun argumentInput() = sequenceOf("a", "_a", "bc123").flatMap { name ->
            sequenceOf(
                FSGraphqlArgumentSerializationTestConfig(
                    input = FSGraphqlArgument.literal(name = name, booleanValue = true),
                    expected = "$name: true"
                ),
                FSGraphqlArgumentSerializationTestConfig(
                    input = FSGraphqlArgument.literal(name = name, booleanValue = null),
                    expected = "$name: null"
                ),
                FSGraphqlArgumentSerializationTestConfig(
                    input = FSGraphqlArgument.literal(name = name, doubleValue = 0.5),
                    expected = "$name: 0.5"
                ),
                FSGraphqlArgumentSerializationTestConfig(
                    input = FSGraphqlArgument.literal(name = name, doubleValue = null),
                    expected = "$name: null"
                ),
                FSGraphqlArgumentSerializationTestConfig(
                    input = FSGraphqlArgument.literal(name = name, floatValue = 0.25F),
                    expected = "$name: 0.25"
                ),
                FSGraphqlArgumentSerializationTestConfig(
                    input = FSGraphqlArgument.literal(name = name, floatValue = null),
                    expected = "$name: null"
                ),
                FSGraphqlArgumentSerializationTestConfig(
                    input = FSGraphqlArgument.literal(name = name, intValue = 100),
                    expected = "$name: 100"
                ),
                FSGraphqlArgumentSerializationTestConfig(
                    input = FSGraphqlArgument.literal(name = name, intValue = null),
                    expected = "$name: null"
                ),
                FSGraphqlArgumentSerializationTestConfig(
                    input = FSGraphqlArgument.literal(name = name, longValue = 101L),
                    expected = "$name: 101"
                ),
                FSGraphqlArgumentSerializationTestConfig(
                    input = FSGraphqlArgument.literal(name = name, longValue = null),
                    expected = "$name: null"
                ),
                FSGraphqlArgumentSerializationTestConfig(
                    input = FSGraphqlArgument.literal(name = name, value = "String"),
                    expected = "$name: \"String\""
                ),
                FSGraphqlArgumentSerializationTestConfig(
                    input = FSGraphqlArgument.literal(name = name, value = null),
                    expected = "$name: null"
                ),
                FSGraphqlArgumentSerializationTestConfig(
                    input = FSGraphqlArgument.enumLiteral(name = name, value = "SOMETHING"),
                    expected = "$name: SOMETHING"
                ),
                FSGraphqlArgumentSerializationTestConfig(
                    input = FSGraphqlArgument.enumLiteral(name = name, value = null),
                    expected = "$name: null"
                ),
                FSGraphqlArgumentSerializationTestConfig(
                    input = FSGraphqlArgument.variableRef(name = name, variableName = "_it_is_a_variable"),
                    expected = "$name: \$_it_is_a_variable"
                )
            )
        }.toList()
    }
}