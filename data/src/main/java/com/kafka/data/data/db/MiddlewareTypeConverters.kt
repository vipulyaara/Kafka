package com.kafka.data.data.db

import androidx.room.TypeConverter
import com.kafka.data.model.item.File
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import kotlinx.serialization.serializer

/**
 * @author Vipul Kumar; dated 21/01/19.
 */
class MiddlewareTypeConverters {
    @UnstableDefault
    @TypeConverter
    @ImplicitReflectionSerializer fun stringToList(input: String?): List<String>? {
        return if (input == null) null else Json.parse(
            String.serializer().list,
            input
        )
    }

    @UnstableDefault
    @TypeConverter
    @ImplicitReflectionSerializer fun listToString(input: List<String>?): String? {
        return if (input == null) null else Json.stringify(
            String.serializer().list,
            input
        )
    }

    @JvmName("stringToFile")
    @TypeConverter
    fun stringToFile(input: String?): List<File>? {
        return if (input == null) null else Moshi.Builder().build().adapter<List<File>>(
            Types.newParameterizedType(List::class.java, File::class.java)
        ).fromJson(input)
    }

    @JvmName("fileToString")
    @TypeConverter
    fun fileToString(input: List<File>?): String? {
        return if (input == null) null else Moshi.Builder().build().adapter<List<File>>(
            Types.newParameterizedType(List::class.java, File::class.java)
        ).toJson(input)
    }
}
