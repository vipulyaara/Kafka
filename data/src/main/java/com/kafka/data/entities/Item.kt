package com.kafka.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kafka.data.extensions.getRandomAuthorResource
import com.data.base.model.MediaType
import com.data.base.model.item.Doc

/**
 * @author Vipul Kumar; dated 13/02/19.
 */
@Entity
data class Item(
    @PrimaryKey val itemId: String = "",
    val language: List<String>? = null,
    val title: String? = null,
    val description: String? = null,
    val creator: String? = null,
    val mediaType: String? = null,
    val coverImage: String? = null,
    var coverImageResource: Int = 0,
    val collection: List<String>? = null,
    val genre: List<String>? = null
) : BaseEntity

fun Doc.toItem() = Item(
    itemId = this.identifier,
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
    RecentItem(itemId, creator, visitedTime)

fun Item.mediaType() = mediaType(mediaType)

fun mediaType(mediaType: String?): MediaType {
    return when (mediaType) {
        "texts" -> MediaType.Text
        "audio" -> MediaType.Audio
        "video" -> MediaType.Video
        else -> MediaType.Text
    }
}
