package com.fsryan.util.net.graphql

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class FSGraphqlSelectionTest {

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("fragmentSpreadInput")
    fun fragmentSpread(config: FSGraphqlSelectionSerializationTestConfig) = config.runTest()

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("inlineFragmentInput")
    fun inlineFragment(config: FSGraphqlSelectionSerializationTestConfig) = config.runTest()

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("fieldInput")
    fun field(config: FSGraphqlSelectionSerializationTestConfig) = config.runTest()

    class FSGraphqlSelectionSerializationTestConfig(
        input: FSGraphqlSelection,
        expected: String
    ): FSGraphqlSerializationTestConfig<FSGraphqlSelection>(input = input, expected = expected)
    
    companion object {

        @JvmStatic
        fun fragmentSpreadInput() = sequenceOf(
            FSGraphqlSelectionSerializationTestConfig(
                input = FSGraphqlFragmentSpread.create(name = "name"),
                expected = "...name"
            ),
            FSGraphqlSelectionSerializationTestConfig(
                input = FSGraphqlFragmentSpread.create(
                    name = "name2",
                    directives = listOf(FSGraphqlDirective.Companion.include(true))
                ),
                expected = "...name2 @include(if: true)"
            )
        ).toList()

        @JvmStatic
        fun inlineFragmentInput() = sequenceOf(
            FSGraphqlSelectionSerializationTestConfig(
                input = FSGraphqlInlineFragment.createForEnclosingType(
                    directives = listOf(FSGraphqlDirective.Companion.include("shouldIncludeField2")),
                    selectionSet = listOf(FSGraphqlField.create(name = "field2"))
                ),
                expected = "... @include(if: \$shouldIncludeField2){field2}"
            ),
            FSGraphqlSelectionSerializationTestConfig(
                input = FSGraphqlInlineFragment.createForEnclosingType(
                    directives = listOf(
                        FSGraphqlDirective.include("shouldIncludeField3"),
                        FSGraphqlDirective.skip("shouldNotIncludeField3")
                    ),
                    selectionSet = listOf(FSGraphqlField.create(name = "field3"))
                ),
                expected = "... @include(if: \$shouldIncludeField3) @skip(if: \$shouldNotIncludeField3){field3}"
            ),
            FSGraphqlSelectionSerializationTestConfig(
                input = FSGraphqlInlineFragment.createOnType(
                    selectionSet = listOf(FSGraphqlField.create(name = "field3")),
                    typeCondition = FSGraphqlType.named(name = "MyType")
                ),
                expected = "...on MyType{field3}"
            ),
            FSGraphqlSelectionSerializationTestConfig(
                input = FSGraphqlInlineFragment.createOnType(
                    selectionSet = listOf(
                        FSGraphqlField.create(name = "field1"),
                        FSGraphqlField.create(name = "field2"),
                        FSGraphqlField.create(name = "field3")
                    ),
                    typeCondition = FSGraphqlType.named(name = "MyType")
                ),
                expected = "...on MyType{field1 field2 field3}"
            )
        ).toList()

        @JvmStatic
        fun fieldInput() = sequenceOf(
            FSGraphqlSelectionSerializationTestConfig(
                input = FSGraphqlField.create(
                    name = "field1"
                ),
                expected = "field1"
            ),
            FSGraphqlSelectionSerializationTestConfig(
                input = FSGraphqlField.create(
                    alias = "field1Alias",
                    name = "field1"
                ),
                expected = "field1Alias: field1"
            ),
            FSGraphqlSelectionSerializationTestConfig(
                input = FSGraphqlField.create(
                    name = "field1",
                    directives = listOf(
                        FSGraphqlDirective.include("shouldIncludeField1")
                    )
                ),
                expected = "field1 @include(if: \$shouldIncludeField1)"
            ),
            FSGraphqlSelectionSerializationTestConfig(
                input = FSGraphqlField.create(
                    name = "field1",
                    directives = listOf(
                        FSGraphqlDirective.include("shouldIncludeField1"),
                        FSGraphqlDirective.skip("shouldSkipField1")
                    )
                ),
                expected = "field1 @include(if: \$shouldIncludeField1) @skip(if: \$shouldSkipField1)"
            ),
            FSGraphqlSelectionSerializationTestConfig(
                input = FSGraphqlField.create(
                    name = "field1",
                    arguments = listOf(
                        FSGraphqlArgument.literal(name = "width", intValue = 100),
                        FSGraphqlArgument.variableRef(name = "height", variableName = "expectedHeight")
                    )
                ),
                expected = "field1(width: 100,height: \$expectedHeight)"
            ),
            FSGraphqlSelectionSerializationTestConfig(
                input = FSGraphqlField.create(
                    name = "field1",
                    selectionSet = listOf(
                        FSGraphqlField.create(name = "field1Member1"),
                        FSGraphqlField.create( name = "field1Member2")
                    )
                ),
                expected = "field1{field1Member1 field1Member2}"
            ),
            FSGraphqlSelectionSerializationTestConfig(
                input = FSGraphqlField.create(
                    alias = "field1Alias",
                    arguments = listOf(
                        FSGraphqlArgument.variableRef(name = "width", variableName = "expectedWidth"),
                        FSGraphqlArgument.literal(name = "height", intValue = 100)
                    ),
                    name = "field1",
                    directives = listOf(
                        FSGraphqlDirective.skip("shouldSkipField1")
                    ),
                    selectionSet = listOf(
                        FSGraphqlField.create(name = "field1Member1"),
                        FSGraphqlField.create( name = "field1Member2")
                    )
                ),
                expected = "field1Alias: field1(width: \$expectedWidth,height: 100) @skip(if: \$shouldSkipField1){field1Member1 field1Member2}"
            )
        ).toList()
    }
}