package com.kafka.data.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kafka.data.model.MediaType
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class CurrentlyReading(
    @SerialName("uid") val uid: String,
    @SerialName("file_id") val fileId: String,
    @SerialName("item_id") val itemId: String,
    @SerialName("current_page") val currentPage: Long,
    @SerialName("current_page_offset") val currentPageOffset: Long,
    @SerialName("updated_at") val updatedAt: Instant = Clock.System.now(),
)

@Keep
@Serializable
@Entity
data class RecentItemSchema(
    @SerialName("file_id") val fileId: String,
    @SerialName("item_id") val itemId: String,
    @SerialName("items") val item: Item,
) {
    companion object {
        const val joinedColumns =
            "file_id, item_id, updated_at, items(title, creators, cover_image, media_type)"
    }
}

@Keep
@Serializable
@Entity
data class RecentItem(
    @SerialName("uid") val uid: String,
    @SerialName("title") val title: String,
    @SerialName("file_id") val fileId: String,
    @SerialName("item_id") val itemId: String,
    @SerialName("cover_image") val coverUrl: String?,
    @SerialName("creators") val creators: List<String>,
    @SerialName("media_type") val mediaType: MediaType = MediaType.Default,
    @SerialName("updated_at") val updatedAt: Instant,
    @SerialName("progress") val progress: Float = 0f,
) {
    val creator = creators.take(5).joinToString()
}

@Entity(tableName = "recent_text")
data class RecentTextItem(
    @PrimaryKey val fileId: String,
    val currentPage: Int, // starts at 1
    val currentPageOffset: Int = 0,
    val localUri: String,
    val type: Type = Type.PDF
) : BaseEntity {
    enum class Type { PDF, EPUB }
}

@Entity(tableName = "recent_audio")
data class RecentAudioItem(
    @PrimaryKey val albumId: String,
    val fileId: String,
    val currentTimestamp: Long = 0,
    val duration: Long = 0,
) : BaseEntity {
    val progress: Int
        get() = (currentTimestamp * 100 / duration).toInt()
}

data class RecentItemWithProgress(
    val recentItem: RecentItem,
    val percentage: Int,
) {
    val progress: Float
        get() = percentage / 100f
}
