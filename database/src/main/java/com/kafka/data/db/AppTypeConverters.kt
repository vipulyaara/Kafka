package com.kafka.data.db

import androidx.room.TypeConverter
import com.kafka.data.entities.File
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * @author Vipul Kumar; dated 21/01/19.
 */
class AppTypeConverters {
    private val json: Json by lazy {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    @TypeConverter
    fun stringToList(data: String) = json.decodeFromString<List<String>?>(data)

    @TypeConverter
    fun listToString(data: List<String>?) = json.encodeToString(data)

    @TypeConverter
    fun stringToFileList(data: String) = json.decodeFromString<List<File>?>(data)

    @TypeConverter
    fun fileListToString(data: List<File>?) = json.encodeToString(data)

    @TypeConverter
    fun stringToFile(data: String) = json.decodeFromString<File>(data)

    @TypeConverter
    fun fileToString(data: File) = json.encodeToString(data)
}
