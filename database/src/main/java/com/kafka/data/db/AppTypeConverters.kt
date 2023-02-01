package com.kafka.data.db

import androidx.room.TypeConverter
import com.kafka.data.entities.File
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

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

    private val localDateFormat = DateTimeFormatter.ISO_LOCAL_DATE_TIME

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

    @TypeConverter
    fun toLocalDateTime(value: String): LocalDateTime = when (value.isBlank()) {
        true -> LocalDateTime.now()
        else -> LocalDateTime.parse(value, localDateFormat)
    }

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime): String = localDateFormat.format(value)
}
