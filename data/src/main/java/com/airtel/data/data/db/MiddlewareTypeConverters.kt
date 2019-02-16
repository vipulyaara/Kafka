package com.airtel.data.data.db

import androidx.room.TypeConverter
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import kotlinx.serialization.serializer

/**
 * @author Vipul Kumar; dated 21/01/19.
 */
class MiddlewareTypeConverters {
    @TypeConverter
    @ImplicitReflectionSerializer fun stringToList(input: String) = Json.parse<List<String>>(
        String.serializer().list,
        input
    )

    @TypeConverter
    @ImplicitReflectionSerializer fun listToString(input: List<String>) = Json.stringify(
        String.serializer().list,
        input
    )
}
