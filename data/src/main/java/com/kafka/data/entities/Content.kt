package com.kafka.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kafka.data.model.MediaType
import com.kafka.data.model.item.Doc

/**
 * @author Vipul Kumar; dated 13/02/19.
 */
@Entity
data class Item(
    @PrimaryKey val contentId: String = "",
    val language: String? = null,
    val title: String? = null,
    val description: String? = null,
    val creator: String? = null,
    val mediaType: String? = null,
    val coverImage: String? = null,
    val collection: List<String>? = null,
    val genre: List<String>? = null
) {

    fun generateStableId(): String {
        return contentId
    }
}

fun Doc.toArchiveItem() = Item(
    contentId = this.identifier,
    language = "",
    title = this.title,
    description = this.description?.get(0)?.trim(),
    creator = this.creator?.get(0),
    mediaType = this.mediatype,
    coverImage = "https://archive.org/services/img/$identifier",
    collection = this.collection,
    genre = this.subject
)

fun Item.mediaType() = mediaType(mediaType)

fun mediaType(mediaType: String?): MediaType {
    return when (mediaType) {
        "texts" -> MediaType.Text
        "audio" -> MediaType.Audio
        "video" -> MediaType.Video
        else -> MediaType.Text
    }
}
