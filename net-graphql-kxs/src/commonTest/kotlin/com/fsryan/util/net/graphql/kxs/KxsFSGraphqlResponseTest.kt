package com.fsryan.util.net.graphql.kxs

import com.goncalossilva.resources.Resource
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class KxsFSGraphqlResponseTest {

    private val json = Json {
        isLenient = true
//        fsGraphqlVariableValueSerializer {}
    }


    @Test
    fun deserializeProductNotFound() {
        val resource = Resource("src/commonTest/resources/error_with_extensions.json")
        val sourceJson = resource.readText()
        val deserialized = json.decodeFromString(deserializer = KxsFSGraphqlErrorMessage.serializer(), sourceJson)
        assertNotNull(deserialized, "deserialized null")
        val errorCode = checkNotNull(deserialized.extensions?.get("errorCode"))
        assertEquals(2100, errorCode.toInt())
        val serialized = json.encodeToString(deserialized)
        val reDeserialized = json.decodeFromString(deserializer = KxsFSGraphqlErrorMessage.serializer(), serialized)
        assertEquals(deserialized, reDeserialized)
    }

    @Test
    fun deserializeErrorWithExtensionsOfMultipleTypes() {
        val resource = Resource("src/commonTest/resources/error_with_mixed_path_types.json")
        val sourceJson = resource.readText()
        val deserialized = json.decodeFromString(deserializer = KxsFSGraphqlErrorMessage.serializer(), sourceJson)
        assertNotNull(deserialized, "deserialized null")
        val serialized = json.encodeToString(deserialized)
        val reDeserialized = json.decodeFromString(deserializer = KxsFSGraphqlErrorMessage.serializer(), serialized)
        assertEquals(deserialized, reDeserialized)
    }
}