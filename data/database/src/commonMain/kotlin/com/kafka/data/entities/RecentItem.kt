package com.kafka.data.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kafka.data.model.MediaType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class CurrentlyReading(
    @SerialName("file_id") val fileId: String,
    @SerialName("book_id") val itemId: String,
    @SerialName("uid") val uid: String,
)

@Keep
@Serializable
data class RecentItem(
    @SerialName("file_id") val fileId: String,
    @SerialName("item_id") val itemId: String,
    @SerialName("title") val title: String,
    @SerialName("cover_url") val coverUrl: String,
    @SerialName("creator") val creator: String,
    @SerialName("media_type") val mediaType: MediaType = MediaType.Default,
    @SerialName("createdAt") val updatedAt: Long,
    @SerialName("uid") val uid: String,
) {
    constructor() : this(
        fileId = "",
        itemId = "",
        title = "",
        coverUrl = "",
        creator = "",
        mediaType = MediaType.Default,
        updatedAt = 0,
        uid = ""
    )

    companion object {
        fun fromItem(file: File, uid: String): RecentItem {
            return RecentItem(
                fileId = file.fileId,
                itemId = file.itemId,
                title = file.itemTitle.orEmpty(),
                coverUrl = file.coverImage.orEmpty(),
                creator = file.creator,
                updatedAt = System.currentTimeMillis(),
                mediaType = file.mediaType,
                uid = uid
            )
        }
    }
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
