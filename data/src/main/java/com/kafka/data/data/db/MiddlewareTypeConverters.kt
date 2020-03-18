package com.kafka.data.data.db

import androidx.room.TypeConverter
import com.kafka.data.model.item.File
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type

/**
 * @author Vipul Kumar; dated 21/01/19.
 */
class MiddlewareTypeConverters {
    @TypeConverter
    fun stringToList(input: String?): List<String>? {
        return if (input == null) null else Moshi.Builder().build().adapter<List<String>>(
            Types.newParameterizedType(List::class.java, String::class.java)
        ).fromJson(input)
    }

    @TypeConverter
    fun listToString(input: List<String>?): String? {
        return if (input == null) null else Moshi.Builder().build().adapter<List<String>>(
            Types.newParameterizedType(List::class.java, String::class.java)
        ).toJson(input)
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
