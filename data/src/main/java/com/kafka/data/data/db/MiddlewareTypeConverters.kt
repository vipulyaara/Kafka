package com.kafka.data.data.db

import androidx.room.TypeConverter
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import kotlinx.serialization.serializer

/**
 * @author Vipul Kumar; dated 21/01/19.
 */
class MiddlewareTypeConverters {
    @TypeConverter
    @ImplicitReflectionSerializer fun stringToList(input: String?): List<String>? {
        return if (input == null) null else Json.parse(
            String.serializer().list,
            input
        )
    }

    @TypeConverter
    @ImplicitReflectionSerializer fun listToString(input: List<String>?): String? {
        return if (input == null) null else Json.stringify(
            String.serializer().list,
            input
        )
    }
}
