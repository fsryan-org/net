package com.fsryan.util.net.graphql

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class FSGraphqlDirectiveTest {

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("directiveInput")
    fun directive(config: FSGraphqlDirectiveSerializationTestConfig) = config.runTest()

    class FSGraphqlDirectiveSerializationTestConfig(
        input: FSGraphqlDirective,
        expected: String
    ): FSGraphqlSerializationTestConfig<FSGraphqlDirective>(input = input, expected = expected)
    
    companion object {

        @JvmStatic
        fun directiveInput() = literalInput()
            .plus(referenceInput())
            .toList()

        private fun literalInput() = sequenceOf(true, false).flatMap { literal ->
            sequenceOf(
                FSGraphqlDirectiveSerializationTestConfig(
                    input = FSGraphqlDirective.include(literal = literal),
                    expected = "@include(if: $literal)"
                ),
                FSGraphqlDirectiveSerializationTestConfig(
                    input = FSGraphqlDirective.skip(literal = literal),
                    expected = "@skip(if: $literal)"
                )
            )
        }

        private fun referenceInput() = sequenceOf("ref1", "ref2").flatMap { booleanVariableName ->
            sequenceOf(
                FSGraphqlDirectiveSerializationTestConfig(
                    input = FSGraphqlDirective.include(booleanVariableName = booleanVariableName),
                    expected = "@include(if: \$$booleanVariableName)"
                ),
                FSGraphqlDirectiveSerializationTestConfig(
                    input = FSGraphqlDirective.skip(booleanVariableName = booleanVariableName),
                    expected = "@skip(if: \$$booleanVariableName)"
                )
            )
        }
    }
}