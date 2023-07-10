package com.fsryan.util.net.graphql

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class FSGraphqlVariableTest {

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("variableInput")
    fun variable(config: FSGraphqlVariableSerializationTestConfig) = config.runTest()

    class FSGraphqlVariableSerializationTestConfig(
        input: FSGraphqlVariable,
        expected: String
    ): FSGraphqlSerializationTestConfig<FSGraphqlVariable>(input = input, expected = expected)
    
    companion object {
        @JvmStatic
        fun variableInput() = namedTypeVariableInput()
            .toList()
        
        private fun namedTypeVariableInput() = sequenceOf(true, false).flatMap { nullable ->
            sequenceOf("a", "_a", "bc123").flatMap { name ->
                val append = if (nullable) "" else "!"
                sequenceOf(
                    FSGraphqlVariableSerializationTestConfig(
                        input = FSGraphqlVariable.integer(name = name, nullable = nullable),
                        expected = "\$$name: Int$append"
                    ),
                    FSGraphqlVariableSerializationTestConfig(
                        input = FSGraphqlVariable.float(name = name, nullable = nullable),
                        expected = "\$$name: Float$append"
                    ),
                    FSGraphqlVariableSerializationTestConfig(
                        input = FSGraphqlVariable.string(name = name, nullable = nullable),
                        expected = "\$$name: String$append"
                    ),
                    FSGraphqlVariableSerializationTestConfig(
                        input = FSGraphqlVariable.boolean(name = name, nullable = nullable),
                        expected = "\$$name: Boolean$append"
                    ),
                    FSGraphqlVariableSerializationTestConfig(
                        input = FSGraphqlVariable.id(name = name, nullable = nullable),
                        expected = "\$$name: ID$append"
                    )
                )
            }
        }
    }
}