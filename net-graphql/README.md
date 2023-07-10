 #client-net-graphql

FS Ryan's graphql framework that aids in the creation of graphql queries


#Where to go for help
You should check out the [graphql spec](https://spec.graphql.org/draft/)

#Example
You should be able to query your schema like this:
```
curl -v -X POST -d '<insert graphql JSON document>' -H "Content-Type: application/json" -H "Accept: application/json" https://yourhost/graphql
```

Suppose you have the following schema:
```
schema {
  query: Query
  mutation: Mutation
}

"Directs the executor to include this field or fragment only when the `if` argument is true"
directive @include(
    "Included when true."
    if: Boolean!
  ) on FIELD | FRAGMENT_SPREAD | INLINE_FRAGMENT

"Directs the executor to skip this field or fragment when the `if`'argument is true."
directive @skip(
    "Skipped when true."
    if: Boolean!
  ) on FIELD | FRAGMENT_SPREAD | INLINE_FRAGMENT

"Marks the field or enum value as deprecated"
directive @deprecated(
    "The reason for the deprecation"
    reason: String = "No longer supported"
  ) on FIELD_DEFINITION | ENUM_VALUE

"Exposes a URL that specifies the behaviour of this scalar."
directive @specifiedBy(
    "The URL that specifies the behaviour of this scalar."
    url: String!
  ) on SCALAR

interface IDog {
  breed: String!
}

type DogSledTeam {
  dogs: [IDog!]!
}

type FeatureToggle {
  name: String!
  type: String!
  value: String!
}

type FeatureToggleGroup {
  featureToggles: [FeatureToggle!]!
  group: String!
}

type Husky implements IDog {
  breed: String!
  specialHuskyValue: String!
}

type Mutation {
}

type Poodle implements IDog {
  breed: String!
  specialPoodleValue: String!
}

type Query {
  featureToggleGroups(chainId: UUID, storeId: UUID): [FeatureToggleGroup!]!
  testSchema: UnlikelyDogSledTeamResponse!
}

type UnlikelyDogSledTeamResponse {
  theTeam: DogSledTeam!
}

"A type representing a java.util.UUID"
scalar UUID
```

##How to run the `testSchema` query.

Here is the thought process in defining it:
1. Determine what are the smallest chunks that you can share (usually this will be a [GraphqlField](src/main/java/com.fsryan.util.net/graphql/GraphqlSelection.kt) or [GraphqlFragment](src/main/java/com.fsryan.util.net/graphql/GraphqlFragment.kt) ).
    ```kotlin
    // Making extension functions on the companion object will aid in readability
    fun GraphqlFragment.Companion.iDogFragment(): GraphqlFragment = create(
        name = "iDogFragment",
        type = GraphqlType.named("IDog"),           // <-- When you need to specify a type, you can do it like this
        selectionSet = listOf(
            GraphqlField.named("breed"),            // <-- Both classes that implement IDog have a breed field, so you don't need to put that behind an inline fragment
            GraphqlInlineFragment.createOnType(     // <-- This inline fragment will get used if the runtime type is Husky 
                typeCondition = GraphqlType.named("Husky"),
                selectionSet = listOf(
                    GraphqlField.named("specialHuskyValue")
                )
            ),
            GraphqlInlineFragment.createOnType(     // <-- This inline fragment will get used if the runtime type is Poodle
                typeCondition = GraphqlType.named("Poodle"),
                selectionSet = listOf(
                    GraphqlField.named("specialPoodleValue")
                )
            )
        )
    )
    ```
2. Create the smaller building blocks and assemble them
    ```kotlin
    // The following GraphqlField can be shared in other queries
    fun GraphqlField.Companion.unlikelyDogSledTeam(): GraphqlField = create(
        name = "theTeam",
        selectionSet = listOf(
            create(
                name = "dogs",
                selectionSet = listOf(
                    GraphqlFragmentSpread.named("iDogFragment") // <-- references the iDogFragment created above
                )
            )
        )
    )
    // It helps consumers if you have a function like this that can return the fragments necessary to run the query.
    fun unlikelyDogSledTeamFragments(): List<GraphqlFragment> = listOf(GraphqlFragment.iDogFragment())
    ```
3. Create A [GraphqlExecutableDocument](src/main/java/com.fsryan.util.net/graphql/GraphqlExecutableDocument.kt)
    ```kotlin
    fun GraphqlExecutableDocument.Companion.unlikelyDogSledTeam(): GraphqlExecutableDocument {
        return object: GraphqlExecutableDocument {
            override val variables: Map<String, Any>? = null
            override val query: String = assembleQuery(
                operation = GraphqlQuery.unlikelyDogSledTeam(),
                fragments = unlikelyDogSledTeamFragments()
            )
            override val name: String = "TestSchema"
        }
    }
    ```
   
You can run the `QueryExamples.unlikelyDogSledTeam` test in order to see this.

## How to run the featureToggleGroups query

The `featureToggleGroups` query contains graphql variables.

You can run the [QueryExamples.featureToggleGroups](src/jvmTest/kotlin/com.fsryan.util.net/graphql/QueryExamples.kt) test in order to see this.