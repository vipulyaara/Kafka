package com.kafka.data.db

import androidx.room.TypeConverter
import com.kafka.data.entities.RecentTextItem
import com.kafka.data.model.MediaType
import com.kafka.data.model.SearchFilter
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
    fun stringToPageList(data: String) = json.decodeFromString<List<RecentTextItem.Page>>(data)

    @TypeConverter
    fun pageListToString(data: List<RecentTextItem.Page>) = json.encodeToString(data)

    @TypeConverter
    fun stringToMediaType(data: String) = json.decodeFromString<List<MediaType>>(data)

    @TypeConverter
    fun mediaTypeToString(data: List<MediaType>) = json.encodeToString(data)

    @TypeConverter
    fun stringToSearchFilter(data: String) = json.decodeFromString<List<SearchFilter>>(data)

    @TypeConverter
    fun searchFilterToString(data: List<SearchFilter>) = json.encodeToString(data)
}
