package com.kafka.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kafka.data.extensions.getRandomAuthorResource
import com.kafka.data.model.MediaType
import com.kafka.data.model.item.Doc

/**
 * @author Vipul Kumar; dated 13/02/19.
 */
@Entity
data class Item(
    val id: Long = 0L,
    @PrimaryKey val contentId: String = "",
    val language: List<String>? = null,
    val title: String? = null,
    val description: String? = null,
    val creator: String? = null,
    @ColumnInfo(name = "media_type")
    val mediaType: String? = null,
    val coverImage: String? = null,
    var coverImageResource: Int = 0,
    val collection: List<String>? = null,
    val genre: List<String>? = null
) : BaseEntity

fun Doc.toItem() = Item(
    contentId = this.identifier,
    language = this.language,
    title = this.title,
    description = this.description?.get(0)?.trim(),
    creator = this.creator?.get(0),
    mediaType = this.mediatype,
    coverImage = "https://archive.org/services/img/$identifier",
    coverImageResource = getRandomAuthorResource(),
    collection = this.collection,
    genre = this.subject
)

fun Item.asRecentlyVisited(visitedTime: Long) =
    RecentItem(contentId, creator, visitedTime)

fun Item.mediaType() = mediaType(mediaType)

fun mediaType(mediaType: String?): MediaType {
    return when (mediaType) {
        "texts" -> MediaType.Text
        "audio" -> MediaType.Audio
        "video" -> MediaType.Video
        else -> MediaType.Text
    }
}
