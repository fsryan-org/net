package com.fsryan.util.net.graphql

import org.junit.jupiter.api.Test

class FSQueryExamples {

    @Test
    fun createMerchandizedCategoryQuery() {
        val query = FSGraphqlQuery.create(
            name = "MerchandizedCategory",
            variables = listOf(
                FSGraphqlVariable.string("categoryId"),
                FSGraphqlVariable.create("itemListingProductMatch", FSGraphqlType.named("ProductMatchInput")),
                FSGraphqlVariable.create("imageDensities", FSGraphqlType.list(FSGraphqlType.named("FSImageDensity"))),
                FSGraphqlVariable.create("networkId", FSGraphqlType.string()),
                FSGraphqlVariable.create("siteId", FSGraphqlType.string()),
                FSGraphqlVariable.create("storeId", FSGraphqlType.string(nullable = true)),
                FSGraphqlVariable.create("positions", FSGraphqlType.stringList(nullable = true)),
                FSGraphqlVariable.create("includePreviewCategories", FSGraphqlType.boolean()),
                FSGraphqlVariable.create("includeNavOptionsCategories", FSGraphqlType.boolean()),
                FSGraphqlVariable.create("includeBrowseSubcategories", FSGraphqlType.boolean()),
                FSGraphqlVariable.create("includeProducts", FSGraphqlType.boolean()),
                FSGraphqlVariable.create("productLimit", FSGraphqlType.integer()),
            ),
            selectionSet = listOf(
                FSGraphqlField.create(
                    alias = "heroTopAd",
                    name = "singleAd",
                    arguments = listOf(
                        FSGraphqlArgument.variableRef(name = "networkId", variableName = "networkId"),
                        FSGraphqlArgument.variableRef(name = "siteId", variableName = "siteId"),
                        FSGraphqlArgument.literal(name = "placementId", value = "HeroTop"),
                        FSGraphqlArgument.enumLiteral(name = "adCapabilityPackId", value = "AD_PANTHER_1"),
                        FSGraphqlArgument.custom(name = "adParams") {
                            "adParams:[{key:\"position\",value:\"HeroTop\"},{key:\"asn\",value:\"DealsHome\"},{ key:\"ca\",value:\"Mobile/P+C/RootCategoryId\"},{key:\"sz\",value:\"375x*\"}]"
                        },
                    ),
                    selectionSet = listOf(
                        FSGraphqlField.typename(),
                        FSGraphqlField.create(
                            name = "adList",
                            selectionSet = listOf(FSGraphqlFragmentSpread.create("adListFragment"))
                        ),
                        FSGraphqlField.create(name = "adRef")
                    )
                ),
                FSGraphqlField.create(
                    alias = "heroBottomAd",
                    name = "singleAd",
                    arguments = listOf(
                        FSGraphqlArgument.variableRef(name = "networkId", variableName = "networkId"),
                        FSGraphqlArgument.variableRef(name = "siteId", variableName = "siteId"),
                        FSGraphqlArgument.literal(name = "placementId", value = "HeroBottom"),
                        FSGraphqlArgument.enumLiteral(name = "adCapabilityPackId", value = "AD_PANTHER_1"),
                        FSGraphqlArgument.custom(name = "adParams") {
                            "adParams:[{key:\"position\",value:\"HeroBottom\"},{key:\"asn\",value:\"DealsHome\"},{ key:\"ca\",value:\"Mobile/P+C/RootCategoryId\"},{key:\"sz\",value:\"375x*\"}]"
                        },
                    ),
                    selectionSet = listOf(
                        FSGraphqlField.typename(),
                        FSGraphqlField.create(
                            name = "adList",
                            selectionSet = listOf(FSGraphqlFragmentSpread.create("adListFragment"))
                        ),
                        FSGraphqlField.create(name = "adRef")
                    )
                ),
                FSGraphqlField.create(
                    name = "previewCategories",
                    arguments = listOf(
                        FSGraphqlArgument.variableRef(name = "id", variableName = "categoryId"),
                        FSGraphqlArgument.variableRef(name = "storeId", variableName = "storeId"),
                        FSGraphqlArgument.variableRef(name = "positions", variableName = "positions"),
                    ),
                    directives = listOf(
                        FSGraphqlDirective.include("includePreviewCategories")
                    ),
                    selectionSet = listOf(
                        FSGraphqlField.typename(),
                        FSGraphqlField.create(name = "categoryId"),
                        FSGraphqlField.create(
                            name = "ads",
                            selectionSet = listOf(
                                FSGraphqlField.typename(),
                                FSGraphqlFragmentSpread.create(name = "adListFragment")
                            )
                        ),
                        FSGraphqlInlineFragment.createOnType(
                            typeCondition = FSGraphqlType.named("ProductCategoryPreview"),
                            selectionSet = listOf(
                                FSGraphqlField.create(
                                    name = "items",
                                    selectionSet = listOf(
                                        FSGraphqlField.typename(),
                                        FSGraphqlFragmentSpread.create("productSummaryFragment"),
                                    )
                                ),
                                FSGraphqlField.create(
                                    name = "prefixItems",
                                    selectionSet = listOf(
                                        FSGraphqlField.typename(),
                                        FSGraphqlField.create(
                                            name = "product",
                                            selectionSet = listOf(
                                                FSGraphqlField.typename(),
                                                FSGraphqlFragmentSpread.create(name = "productSummaryFragment")
                                            )
                                        )
                                    )
                                )
                            )
                        ),
                        FSGraphqlInlineFragment.createOnType(
                            typeCondition = FSGraphqlType.named("CouponCategoryPreview"),
                            selectionSet = listOf(
                                FSGraphqlField.create(
                                    name = "deals",
                                    selectionSet = listOf(
                                        FSGraphqlField.typename(),
                                        FSGraphqlFragmentSpread.create("offerFragment")
                                    )
                                )
                            )
                        )
                    )
                ),
                FSGraphqlField.create(
                    name = "browseSubcategories",
                    arguments = listOf(FSGraphqlArgument.variableRef("id", "categoryId")),
                    directives = listOf(FSGraphqlDirective.include("includeBrowseSubcategories"))
                ),
                FSGraphqlField.create(
                    name = "navOptionsCategories",
                    arguments = listOf(FSGraphqlArgument.variableRef("id", "categoryId")),
                    directives = listOf(FSGraphqlDirective.include("includeNavOptionsCategories"))
                ),
                FSGraphqlField.create(
                    name = "products",
                    arguments = listOf(
                        FSGraphqlArgument.literal(name = "offset", intValue = 0),
                        FSGraphqlArgument.variableRef(name = "limit", variableName = "productLimit"),
                        FSGraphqlArgument.variableRef(name = "storeId", variableName = "storeId"),
                        FSGraphqlArgument.variableRef(name = "match", variableName = "itemListingProductMatch")
                    ),
                    directives = listOf(
                        FSGraphqlDirective.include("includeProducts")
                    ),
                    selectionSet = listOf(
                        FSGraphqlField.typename(),
                        FSGraphqlField.create(
                            name = "products",
                            selectionSet = listOf(
                                FSGraphqlField.typename(),
                                FSGraphqlFragmentSpread.create("productSummaryFragment")
                            )
                        )
                    )
                )
            )
        )
    }

    @Test
    fun unlikelyDogSledTeam() {
        println(FSGraphqlExecutableDocument.unlikelyDogSledTeam().query)
    }

    @Test
    fun featureToggleGroups() {
        println(FSGraphqlExecutableDocument.featureToggleGroups(chainId = null, storeId = null).query)
    }
}

/*
 * interface IDog {
 *   breed: String!
 * }
 *
 * type DogSledTeam {
 *   dogs: [IDog!]!
 * }
 *
 * type Husky implements IDog {
 *   breed: String!
 *   specialHuskyValue: String!
 * }
 *
 * type Poodle implements IDog {
 *   breed: String!
 *   specialPoodleValue: String!
 * }
 *
 * type Query {
 *   testSchema: UnlikelyDogSledTeamResponse!
 * }
 *
 * type UnlikelyDogSledTeamResponse {
 *   theTeam: DogSledTeam!
 * }
 */

private fun FSGraphqlFragment.Companion.iDogFragment(): FSGraphqlFragment = create(
    name = "iDogFragment",
    type = FSGraphqlType.named("IDog"),
    selectionSet = listOf(
        FSGraphqlField.named("breed"),
        FSGraphqlInlineFragment.createOnType(
            typeCondition = FSGraphqlType.named("Husky"),
            selectionSet = listOf(
                FSGraphqlField.named("specialHuskyValue")
            )
        ),
        FSGraphqlInlineFragment.createOnType(
            typeCondition = FSGraphqlType.named("Poodle"),
            selectionSet = listOf(
                FSGraphqlField.named("specialPoodleValue")
            )
        )
    )
)

private fun FSGraphqlField.Companion.unlikelyDogSledTeam(): FSGraphqlField = create(
    name = "theTeam",
    selectionSet = listOf(
        create(
            name = "dogs",
            selectionSet = listOf(
                FSGraphqlFragmentSpread.named("iDogFragment")
            )
        )
    )
)

private fun FSGraphqlQuery.Companion.unlikelyDogSledTeam(): FSGraphqlQuery = create(
    name = "TestSchema",
    selectionSet = listOf(
        FSGraphqlField.create(
            name = "testSchema",
            selectionSet = listOf(
                FSGraphqlField.unlikelyDogSledTeam()
            )
        )
    )
)

private fun unlikelyDogSledTeamFragments(): List<FSGraphqlFragment> = listOf(FSGraphqlFragment.iDogFragment())

private fun FSGraphqlExecutableDocument.Companion.unlikelyDogSledTeam(): FSGraphqlExecutableDocument {
    return object: FSGraphqlExecutableDocument {
        override val variables: Map<String, Any>? = null
        override val query: String = assembleQuery(
            operation = FSGraphqlQuery.unlikelyDogSledTeam(),
            fragments = unlikelyDogSledTeamFragments()
        )
        override val name: String = "TestSchema"
    }
}

/*
 * type FeatureToggle {
 *   name: String!
 *   type: String!
 *   value: String!
 * }
 *
 * type FeatureToggleGroup {
 *   featureToggles: [FeatureToggle!]!
 *   group: String!
 * }
 *
 * type Query {
 *   featureToggleGroups(chainId: UUID, storeId: UUID): [FeatureToggleGroup!]!
 *   testSchema: UnlikelyDogSledTeamResponse!
 * }
 */
/**
 * The [FSGraphqlQuery] can take variables. If your [FSGraphqlQuery] takes a
 * variable called `storeId` with a type `UUID` (note that this is nullable),
 * then you can reference that variable. You can additionally enforce that the
 * caller supply the literal value of the argument, as with `chainId` below.
 */
private fun FSGraphqlField.Companion.featureToggleGroups(chainId: String?): FSGraphqlField = create(
    name = "featureToggleGroups",
    arguments = listOf(
        FSGraphqlArgument.variableRef(name = "storeId", variableName = "storeId"),
        FSGraphqlArgument.literal(name = "chainId", value = chainId)
    ),
    selectionSet = listOf(
        named("group"),
        create(
            name = "featureToggles",
            selectionSet = listOf(
                named("name"),
                named("type"),
                named("value")
            )
        )
    )
)

/**
 * Because the [FSGraphqlField.Companion.featureToggleGroups] [FSGraphqlField]
 * relies upon a [FSGraphqlArgument.Companion.variableRef], you have to create
 * that variable here. In doing so, you have to be sure that the name of the
 * variable and the type of the variable match exactly. Because the other
 * argument ot the [FSGraphqlField.Companion.featureToggleGroups] [FSGraphqlField]
 * is a literal value for the query, you must force the caller to supply it.
 */
private fun FSGraphqlQuery.Companion.featureToggleGroups(chainId: String?): FSGraphqlQuery = create(
    name = "FeatureToggleGroups",
    variables = listOf(
        FSGraphqlVariable.create(
            name = "storeId",
            FSGraphqlType.named("UUID", nullable = true)
        )
    ),
    selectionSet = listOf(
        FSGraphqlField.featureToggleGroups(chainId = chainId)
    )
)

/**
 * Because you must be able to supply variables, both the `chainId` and
 * `storeId` values must be passed into this function by the caller.
 */
private fun FSGraphqlExecutableDocument.Companion.featureToggleGroups(
    chainId: String? = null,
    storeId: String? = null
): FSGraphqlExecutableDocument {
    return object: FSGraphqlExecutableDocument {
        override val variables: Map<String, Any?> = mapOf("storeId" to storeId)
        override val query: String = assembleQuery(
            operation = FSGraphqlQuery.featureToggleGroups(chainId = chainId)
        )
        override val name: String = "FeatureToggleGroups"
    }
}