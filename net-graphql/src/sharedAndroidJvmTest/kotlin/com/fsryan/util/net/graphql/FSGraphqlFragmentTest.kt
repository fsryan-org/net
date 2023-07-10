package com.fsryan.util.net.graphql

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class FSGraphqlFragmentTest {

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("fragmentInput")
    fun fragment(config: FSGraphqlFragmentSerializationTestConfig) = config.runTest()

    class FSGraphqlFragmentSerializationTestConfig(
        input: FSGraphqlFragment,
        expected: String
    ): FSGraphqlSerializationTestConfig<FSGraphqlFragment>(input = input, expected = expected)

    companion object {

        @JvmStatic
        fun fragmentInput() = sequenceOf(
            FSGraphqlFragmentSerializationTestConfig(
                input = FSGraphqlFragment.create(
                    name = "f",
                    type = FSGraphqlType.named("T"),
                    selectionSet = listOf(
                        FSGraphqlField.typename()
                    )
                ),
                expected = "fragment f on T{__typename}"
            )
        ).plus(fsExampleCases()).toList()

        private fun fsExampleCases() = sequenceOf(
            FSGraphqlFragmentSerializationTestConfig(
                input = FSGraphqlFragment.create(
                    name = "adListFragment",
                    type = FSGraphqlType.named(name = "AdWithPlacement"),
                    selectionSet = listOf(
                        FSGraphqlField.typename(),
                        FSGraphqlField.named("position"),
                        FSGraphqlField.create(
                            name = "ad",
                            selectionSet = listOf(
                                FSGraphqlField.typename(),
                                FSGraphqlFragmentSpread.named("adFragment")
                            )
                        ),
                        FSGraphqlField.named("placementId")
                    )
                ),
                expected = "fragment adListFragment on AdWithPlacement{__typename position ad{__typename ...adFragment} placementId}"
            ),
            FSGraphqlFragmentSerializationTestConfig(
                input = FSGraphqlFragment.create(
                    name = "adFragment",
                    type = FSGraphqlType.named(name = "AdDTO"),
                    selectionSet = listOf(
                        FSGraphqlField.typename(),
                        FSGraphqlField.named("aid"),
                        FSGraphqlField.named("aiid"),
                        FSGraphqlField.named("height"),
                        FSGraphqlField.named("width"),
                        FSGraphqlField.named("type"),
                        FSGraphqlField.named("version"),
                        FSGraphqlInlineFragment.createOnType(
                            typeCondition = FSGraphqlType.named("ImageAdDTO"),
                            selectionSet = listOf(
                                FSGraphqlField.create(
                                    name = "image",
                                    selectionSet = listOf(
                                        FSGraphqlField.typename(),
                                        FSGraphqlFragmentSpread.named("imageAssetFragment")
                                    )
                                )
                            )
                        ),
                        FSGraphqlInlineFragment.createOnType(
                            typeCondition = FSGraphqlType.named("ImageCarouselAdDTO"),
                            selectionSet = listOf(
                                FSGraphqlField.create(
                                    name = "carouselPages",
                                    selectionSet = listOf(
                                        FSGraphqlField.typename(),
                                        FSGraphqlFragmentSpread.named("carouselPageFragment")
                                    )
                                )
                            )
                        ),
                        FSGraphqlInlineFragment.createOnType(
                            typeCondition = FSGraphqlType.named("NamedControlAdDTO"),
                            selectionSet = listOf(FSGraphqlField.named("control"))
                        ),
                        FSGraphqlInlineFragment.createOnType(
                            typeCondition = FSGraphqlType.named("OfferAdDTO"),
                            selectionSet = listOf(FSGraphqlField.named("offerId"))
                        ),
                        FSGraphqlInlineFragment.createOnType(
                            typeCondition = FSGraphqlType.named("LayoutAdDTO"),
                            selectionSet = listOf(
                                FSGraphqlField.create(
                                    name = "rows",
                                    selectionSet = listOf(
                                        FSGraphqlField.typename(),
                                        FSGraphqlField.named("height"),
                                        FSGraphqlField.create(
                                            name = "columns",
                                            selectionSet = listOf(
                                                FSGraphqlField.typename(),
                                                FSGraphqlField.named("width"),
                                                FSGraphqlField.named("adRef")
                                            )
                                        )
                                    )
                                )
                            )
                        ),
                        FSGraphqlInlineFragment.createOnType(
                            typeCondition = FSGraphqlType.named("RoundedImageAdDTO"),
                            selectionSet = listOf(
                                FSGraphqlField.create(
                                    name = "image",
                                    selectionSet = listOf(
                                        FSGraphqlField.typename(),
                                        FSGraphqlFragmentSpread.named("imageAssetFragment")
                                    )
                                )
                            )
                        ),
                        FSGraphqlInlineFragment.createOnType(
                            typeCondition = FSGraphqlType.named("RoundedImageCarouselAdDTO"),
                            selectionSet = listOf(
                                FSGraphqlField.create(
                                    name = "carousel",
                                    selectionSet = listOf(
                                        FSGraphqlField.typename(),
                                        FSGraphqlFragmentSpread.named("imageAssetFragment")
                                    )
                                ),
                                FSGraphqlField.create(
                                    name = "carouselPages",
                                    selectionSet = listOf(
                                        FSGraphqlField.typename(),
                                        FSGraphqlFragmentSpread.named("carouselPageFragment")
                                    )
                                ),
                                FSGraphqlField.named("displayDurationMs")
                            )
                        ),
                        FSGraphqlInlineFragment.createOnType(
                            typeCondition = FSGraphqlType.named("ListCollectionDTO"),
                            selectionSet = listOf(
                                FSGraphqlField.create(
                                    name = "placements",
                                    selectionSet = listOf(
                                        FSGraphqlField.typename(),
                                        FSGraphqlField.named("adRef"),
                                        FSGraphqlField.named("height"),
                                        FSGraphqlField.named("id")
                                    )
                                )
                            )
                        )
                    )
                ),
                expected = "fragment adFragment on AdDTO{__typename aid aiid height width type version ...on ImageAdDTO{image{__typename ...imageAssetFragment}} ...on ImageCarouselAdDTO{carouselPages{__typename ...carouselPageFragment}} ...on NamedControlAdDTO{control} ...on OfferAdDTO{offerId} ...on LayoutAdDTO{rows{__typename height columns{__typename width adRef}}} ...on RoundedImageAdDTO{image{__typename ...imageAssetFragment}} ...on RoundedImageCarouselAdDTO{carousel{__typename ...imageAssetFragment} carouselPages{__typename ...carouselPageFragment} displayDurationMs} ...on ListCollectionDTO{placements{__typename adRef height id}}}"
            ),
            FSGraphqlFragmentSerializationTestConfig(
                input = FSGraphqlFragment.create(
                    name = "imageAssetFragment",
                    type = FSGraphqlType.named("ImageAsset"),
                    selectionSet = listOf(
                        FSGraphqlField.typename(),
                        FSGraphqlField.named("id"),
                        FSGraphqlField.create(
                            name = "densities",
                            selectionSet = listOf(
                                FSGraphqlField.typename(),
                                FSGraphqlField.named("url"),
                                FSGraphqlField.named("density")
                            )
                        ),
                        FSGraphqlField.named("altText"),
                        FSGraphqlField.named("revision"),
                        FSGraphqlField.create(
                            name = "action",
                            selectionSet = listOf(
                                FSGraphqlField.typename(),
                                FSGraphqlField.named("action"),
                                FSGraphqlField.named("target")
                            )
                        )
                    )
                ),
                expected = "fragment imageAssetFragment on ImageAsset{__typename id densities{__typename url density} altText revision action{__typename action target}}"
            ),
            FSGraphqlFragmentSerializationTestConfig(
                input = FSGraphqlFragment.create(
                    name = "carouselPageFragment",
                    type = FSGraphqlType.named("CarouselPage"),
                    selectionSet = listOf(
                        FSGraphqlField.typename(),
                        FSGraphqlField.named("durationMs"),
                        FSGraphqlField.create(
                            name = "imageAsset",
                            selectionSet = listOf(
                                FSGraphqlField.typename(),
                                FSGraphqlFragmentSpread.named("imageAssetFragment")
                            )
                        )
                    )
                ),
                expected = "fragment carouselPageFragment on CarouselPage{__typename durationMs imageAsset{__typename ...imageAssetFragment}}"
            ),
            FSGraphqlFragmentSerializationTestConfig(
                input = FSGraphqlFragment.create(
                    name = "productSummaryFragment",
                    type = FSGraphqlType.named("ProductSummary"),
                    selectionSet = listOf(
                        FSGraphqlField.typename(),
                        FSGraphqlField.named("brand"),
                        FSGraphqlField.named("categories"),
                        FSGraphqlField.named("shortDescription"),
                        FSGraphqlField.create(
                            name = "images",
                            selectionSet = listOf(
                                FSGraphqlField.typename(),
                                FSGraphqlFragmentSpread.named("imageRefFragment")
                            )
                        ),
                        FSGraphqlField.create(
                            name = "fdPriceTag",
                            selectionSet = listOf(
                                FSGraphqlField.typename(),
                                FSGraphqlFragmentSpread.named("fdPriceTagFragment")
                            )
                        ),
                        FSGraphqlField.named("id"),
                        FSGraphqlField.named("title"),
                        FSGraphqlField.named("type"),
                        FSGraphqlField.create(
                            name = "tags",
                            selectionSet = listOf(
                                FSGraphqlField.typename(),
                                FSGraphqlFragmentSpread.named("tagFragment")
                            )
                        ),
                    )
                ),
                expected = "fragment productSummaryFragment on ProductSummary{__typename brand categories shortDescription images{__typename ...imageRefFragment} fdPriceTag{__typename ...fdPriceTagFragment} id title type tags{__typename ...tagFragment}}"
            ),
            FSGraphqlFragmentSerializationTestConfig(
                input = FSGraphqlFragment.create(
                    name = "imageRefFragment",
                    type = FSGraphqlType.named("ImageRef"),
                    selectionSet = listOf(
                        FSGraphqlField.typename(),
                        FSGraphqlField.named("altText"),
                        FSGraphqlField.named("caption"),
                        FSGraphqlField.create(
                            name = "images",
                            arguments = listOf(
                                FSGraphqlArgument.variableRef(
                                    name = "desiredDensities",
                                    variableName = "imageDensities"
                                )
                            ),
                            selectionSet = listOf(
                                FSGraphqlField.typename(),
                                FSGraphqlFragmentSpread.named("imageFileFragment")
                            )
                        ),
                        FSGraphqlField.create(
                            name = "type",
                            selectionSet = listOf(
                                FSGraphqlField.typename(),
                                FSGraphqlField.named("description"),
                                FSGraphqlField.named("height"),
                                FSGraphqlField.named("type"),
                                FSGraphqlField.named("width")
                            )
                        )
                    )
                ),
                expected = "fragment imageRefFragment on ImageRef{__typename altText caption images(desiredDensities: \$imageDensities){__typename ...imageFileFragment} type{__typename description height type width}}"
            ),
            FSGraphqlFragmentSerializationTestConfig(
                input = FSGraphqlFragment.create(
                    name = "imageFileFragment",
                    type = FSGraphqlType.named("ImageFile"),
                    selectionSet = listOf(
                        FSGraphqlField.typename(),
                        FSGraphqlField.named("density"),
                        FSGraphqlField.named("device"),
                        FSGraphqlField.named("uri")
                    )
                ),
                expected = "fragment imageFileFragment on ImageFile{__typename density device uri}"
            ),
            FSGraphqlFragmentSerializationTestConfig(
                input = FSGraphqlFragment.create(
                    name = "fdPriceTagFragment",
                    type = FSGraphqlType.named("FamilyDollarPriceTag"),
                    selectionSet = listOf(
                        FSGraphqlField.typename(),
                        FSGraphqlField.named("discountType"),
                        FSGraphqlField.create(
                            name = "finalPrice",
                            selectionSet = listOf(
                                FSGraphqlField.typename(),
                                FSGraphqlFragmentSpread.named("priceDetailFragment")
                            )
                        ),
                        FSGraphqlField.create(
                            name = "finalSavings",
                            selectionSet = listOf(
                                FSGraphqlField.typename(),
                                FSGraphqlField.named("savingsDisplayAmount"),
                                FSGraphqlField.named("savingsDisplayPercent"),
                                FSGraphqlField.named("savingsQuantity"),
                                FSGraphqlField.named("mustBuyAtLeast")
                            )
                        ),
                        FSGraphqlField.create(
                            name = "regularPrice",
                            selectionSet = listOf(
                                FSGraphqlField.typename(),
                                FSGraphqlFragmentSpread.named("priceDetailFragment")
                            )
                        ),
                        FSGraphqlField.named("scenario"),
                        FSGraphqlField.create(
                            name = "tags",
                            selectionSet = listOf(
                                FSGraphqlField.typename(),
                                FSGraphqlFragmentSpread.named("tagFragment")
                            )
                        ),
                        FSGraphqlField.named("termsAndConditions")
                    )
                ),
                expected = "fragment fdPriceTagFragment on FamilyDollarPriceTag{__typename discountType finalPrice{__typename ...priceDetailFragment} finalSavings{__typename savingsDisplayAmount savingsDisplayPercent savingsQuantity mustBuyAtLeast} regularPrice{__typename ...priceDetailFragment} scenario tags{__typename ...tagFragment} termsAndConditions}"
            ),
            FSGraphqlFragmentSerializationTestConfig(
                input = FSGraphqlFragment.create(
                    name = "priceDetailFragment",
                    type = FSGraphqlType.named("PriceDetail"),
                    selectionSet = listOf(
                        FSGraphqlField.typename(),
                        FSGraphqlField.named("displayPrice"),
                        FSGraphqlField.named("buyQuantity"),
                        FSGraphqlField.named("effectiveDate"),
                        FSGraphqlField.named("expirationDate"),
                        FSGraphqlField.named("getQuantity"),
                        FSGraphqlField.named("mixAndMatchId"),
                        FSGraphqlField.named("model"),
                        FSGraphqlField.named("quantityLimit"),
                        FSGraphqlField.named("quantityMinimum"),
                        FSGraphqlField.named("soldBy"),
                        FSGraphqlField.named("type")
                    )
                ),
                expected = "fragment priceDetailFragment on PriceDetail{__typename displayPrice buyQuantity effectiveDate expirationDate getQuantity mixAndMatchId model quantityLimit quantityMinimum soldBy type}"
            ),
            FSGraphqlFragmentSerializationTestConfig(
                input = FSGraphqlFragment.create(
                    name = "tagFragment",
                    type = FSGraphqlType.named("Tag"),
                    selectionSet = listOf(
                        FSGraphqlField.typename(),
                        FSGraphqlField.named("code"),
                        FSGraphqlField.named("description"),
                        FSGraphqlField.named("id"),
                        FSGraphqlField.create(
                            name = "images",
                            selectionSet = listOf(
                                FSGraphqlField.typename(),
                                FSGraphqlFragmentSpread.named("imageRefFragment")
                            )
                        ),
                        FSGraphqlField.named("title"),
                        FSGraphqlField.named("type"),
                        FSGraphqlField.named("value")
                    )
                ),
                expected = "fragment tagFragment on Tag{__typename code description id images{__typename ...imageRefFragment} title type value}"
            ),
            FSGraphqlFragmentSerializationTestConfig(
                input = FSGraphqlFragment.create(
                    name = "offerFragment",
                    type = FSGraphqlType.named("Offer"),
                    selectionSet = listOf(
                        FSGraphqlField.typename(),
                        FSGraphqlField.create(
                            name = "sortGroups",
                            selectionSet = listOf(
                                FSGraphqlField.typename(),
                                FSGraphqlField.named("groupId"),
                                FSGraphqlField.create(
                                    name = "dimensions",
                                    selectionSet = listOf(
                                        FSGraphqlField.typename(),
                                        FSGraphqlField.named("dimensionId"),
                                        FSGraphqlField.named("rank")
                                    )
                                )
                            )
                        ),
                        FSGraphqlField.named("id"),
                        FSGraphqlField.named("temporalStatus"),
                        FSGraphqlField.named("savingsSortValue"),
                        FSGraphqlField.named("rankSortValue"),
                        FSGraphqlField.named("retailerDisplayCategoryIds"),
                        FSGraphqlField.named("storeId"),
                        FSGraphqlField.named("multipleUse"),
                        FSGraphqlField.named("maxRedeemCount"),
                        FSGraphqlField.named("popularity"),
                        FSGraphqlField.named("offerDescription"),
                        FSGraphqlField.named("shortDescription"),
                        FSGraphqlField.named("terms"),
                        FSGraphqlField.named("activeDate"),
                        FSGraphqlField.named("valueText"),
                        FSGraphqlField.named("expirationDate"),
                        FSGraphqlField.named("clipStartDate"),
                        FSGraphqlField.named("clipEndDate"),
                        FSGraphqlField.named("brand"),
                        FSGraphqlField.named("claimedCount"),
                        FSGraphqlField.named("maxTransactionRedeemCount"),
                        FSGraphqlField.named("isExclusive"),
                        FSGraphqlField.create(
                            name = "decorators",
                            selectionSet = listOf(
                                FSGraphqlField.typename(),
                                FSGraphqlField.named("priority"),
                                FSGraphqlField.named("type"),
                                FSGraphqlInlineFragment.createOnType(
                                    typeCondition = FSGraphqlType.named("ExpiringOfferDecorator"),
                                    selectionSet = listOf(FSGraphqlField.named("expireDate"))
                                ),
                            )
                        ),
                        FSGraphqlField.create(
                            name = "shopperOffer",
                            selectionSet = listOf(
                                FSGraphqlField.typename(),
                                FSGraphqlField.named("claimed"),
                                FSGraphqlField.named("loyaltyReward"),
                                FSGraphqlField.named("redeemed"),
                                FSGraphqlField.create(
                                    name = "actions",
                                    selectionSet = listOf(
                                        FSGraphqlField.typename(),
                                        FSGraphqlField.named("action"),
                                        FSGraphqlField.named("date"),
                                        FSGraphqlField.named("source"),
                                        FSGraphqlField.named("sourceId")
                                    )
                                ),
                                FSGraphqlField.named("source"),
                                FSGraphqlField.named("sourceId")
                            )
                        ),
                        FSGraphqlField.named("featured"),
                        FSGraphqlField.named("offerType"),
                        FSGraphqlField.named("offerValueType"),
                        FSGraphqlField.named("fundingType"),
                        FSGraphqlField.create(
                            name = "offerParameters",
                            selectionSet = listOf(
                                FSGraphqlField.typename(),
                                FSGraphqlInlineFragment.createOnType(
                                    typeCondition = FSGraphqlType.named("BoGoParameters"),
                                    selectionSet = listOf(
                                        FSGraphqlField.named("availableDays"),
                                        FSGraphqlField.named("buyQuantity"),
                                        FSGraphqlField.named("getQuantity"),
                                        FSGraphqlField.named("offerValue")
                                    )
                                ),
                                FSGraphqlInlineFragment.createOnType(
                                    typeCondition = FSGraphqlType.named("CentsOffParameters"),
                                    selectionSet = listOf(
                                        FSGraphqlField.named("availableDays"),
                                        FSGraphqlField.named("buyQuantity"),
                                        FSGraphqlField.named("offerValue")
                                    )
                                ),
                                FSGraphqlInlineFragment.createOnType(
                                    typeCondition = FSGraphqlType.named("CompoundCentsOffParameters"),
                                    selectionSet = listOf(
                                        FSGraphqlField.named("availableDays"),
                                        FSGraphqlField.named("buyQuantity"),
                                        FSGraphqlField.named("offerValue")
                                    )
                                ),
                                FSGraphqlInlineFragment.createOnType(
                                    typeCondition = FSGraphqlType.named("CompoundFixedPriceParameters"),
                                    selectionSet = listOf(
                                        FSGraphqlField.named("availableDays"),
                                        FSGraphqlField.named("buyQuantity"),
                                        FSGraphqlField.named("offerValue")
                                    )
                                ),
                                FSGraphqlInlineFragment.createOnType(
                                    typeCondition = FSGraphqlType.named("FixedPriceParameters"),
                                    selectionSet = listOf(
                                        FSGraphqlField.named("availableDays"),
                                        FSGraphqlField.named("buyQuantity"),
                                        FSGraphqlField.named("offerValue")
                                    )
                                ),
                                FSGraphqlInlineFragment.createOnType(
                                    typeCondition = FSGraphqlType.named("FreeOfferParameters"),
                                    selectionSet = listOf(
                                        FSGraphqlField.named("availableDays"),
                                        FSGraphqlField.named("buyQuantity"),
                                        FSGraphqlField.named("offerValue")
                                    )
                                ),
                                FSGraphqlInlineFragment.createOnType(
                                    typeCondition = FSGraphqlType.named("OrderTotalParameters"),
                                    selectionSet = listOf(
                                        FSGraphqlField.named("availableDays"),
                                        FSGraphqlField.named("maxPurchase"),
                                        FSGraphqlField.named("maxRewardQuantity"),
                                        FSGraphqlField.named("minPurchase"),
                                        FSGraphqlField.named("offerValue")
                                    )
                                ),
                                FSGraphqlInlineFragment.createOnType(
                                    typeCondition = FSGraphqlType.named("PercentOffParameters"),
                                    selectionSet = listOf(
                                        FSGraphqlField.named("availableDays"),
                                        FSGraphqlField.named("buyQuantity"),
                                        FSGraphqlField.named("offerValue")
                                    )
                                )
                            )
                        ),
                        FSGraphqlField.create(
                            name = "images",
                            arguments = listOf(
                                FSGraphqlArgument.enumListLiteral(name = "use", "Detail")
                            ),
                            selectionSet = listOf(
                                FSGraphqlField.typename(),
                                FSGraphqlField.named("altText"),
                                FSGraphqlField.named("default"),
                                FSGraphqlField.create(
                                    name = "urls",
                                    selectionSet = listOf(
                                        FSGraphqlField.typename(),
                                        FSGraphqlField.named("url"),
                                        FSGraphqlField.named("density")
                                    )
                                ),
                                FSGraphqlField.named("use")
                            )
                        )
                    )
                ),
                expected = "fragment offerFragment on Offer{__typename sortGroups{__typename groupId dimensions{__typename dimensionId rank}} id temporalStatus savingsSortValue rankSortValue retailerDisplayCategoryIds storeId multipleUse maxRedeemCount popularity offerDescription shortDescription terms activeDate valueText expirationDate clipStartDate clipEndDate brand claimedCount maxTransactionRedeemCount isExclusive decorators{__typename priority type ...on ExpiringOfferDecorator{expireDate}} shopperOffer{__typename claimed loyaltyReward redeemed actions{__typename action date source sourceId} source sourceId} featured offerType offerValueType fundingType offerParameters{__typename ...on BoGoParameters{availableDays buyQuantity getQuantity offerValue} ...on CentsOffParameters{availableDays buyQuantity offerValue} ...on CompoundCentsOffParameters{availableDays buyQuantity offerValue} ...on CompoundFixedPriceParameters{availableDays buyQuantity offerValue} ...on FixedPriceParameters{availableDays buyQuantity offerValue} ...on FreeOfferParameters{availableDays buyQuantity offerValue} ...on OrderTotalParameters{availableDays maxPurchase maxRewardQuantity minPurchase offerValue} ...on PercentOffParameters{availableDays buyQuantity offerValue}} images(use: [Detail]){__typename altText default urls{__typename url density} use}}"
            )
        )
    }
}