package com.kafka.data.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

import com.google.firebase.firestore.DocumentId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Keep
@Serializable
data class RecentItem(
    @DocumentId
    @Transient val fileId: String = "",
    @SerialName("itemId") val itemId: String,
    @SerialName("title") val title: String,
    @SerialName("coverUrl") val coverUrl: String,
    @SerialName("creator") val creator: String,
    @SerialName("mediaType") val mediaType: String,
    @SerialName("createdAt") val createdAt: Long,
) {
    constructor() : this(
        fileId = "",
        itemId = "",
        title = "",
        coverUrl = "",
        creator = "",
        mediaType = "",
        createdAt = 0,
    )

    companion object {
        fun fromItem(item: ItemDetail, fileId: String = item.files!!.first()): RecentItem {
            return RecentItem(
                fileId = fileId,
                itemId = item.itemId,
                title = item.title.orEmpty(),
                coverUrl = item.coverImage.orEmpty(),
                creator = item.creator.orEmpty(),
                mediaType = item.mediaType.orEmpty(),
                createdAt = System.currentTimeMillis(),
            )
        }
    }
}

@Entity(tableName = "recent_text")
data class RecentTextItem(
    @PrimaryKey val fileId: String,
    val currentPage: Int, // starts at 1
    val localUri: String,
    val type: Type = Type.PDF,
    val pages: List<Page> = emptyList(),
) : BaseEntity {
    enum class Type { PDF }

    @Serializable
    data class Page(val index: Int, val text: String)
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
