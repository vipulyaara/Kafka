package com.kafka.data.db

import androidx.room.TypeConverter
import com.kafka.data.entities.Download
import com.kafka.data.model.MediaType
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
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
    fun toLocalDateTime(value: Long): Instant = when (value == 0L) {
        true -> Clock.System.now()
        else -> Instant.fromEpochMilliseconds(value)
    }

    @TypeConverter
    fun fromLocalDateTime(value: Instant): Long = value.toEpochMilliseconds()

    @TypeConverter
    fun stringToMediaTypes(data: String) = json.decodeFromString<List<MediaType>>(data)

    @TypeConverter
    fun mediaTypesToString(data: List<MediaType>) = json.encodeToString(data)

    @TypeConverter
    fun stringToMediaType(data: String) = MediaType.from(data)

    @TypeConverter
    fun mediaTypeToString(data: MediaType) = data.value

    @TypeConverter
    fun stringToDownloadStatus(data: String) = Download.Status.valueOf(data)

    @TypeConverter
    fun downloadStatusString(data: Download.Status) = data.name
}
