package com.airtel.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.airtel.data.model.MediaType
import com.airtel.data.model.item.Doc

/**
 * @author Vipul Kumar; dated 13/02/19.
 */
@Entity
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val contentId: String? = null,
    val language: String? = null,
    val title: String? = null,
    val description: String? = null,
    val creator: String? = null,
    val mediaType: String? = null
) {

    fun generateStableId(): Long {
        return id
    }
}

fun Doc.toArchiveItem() = Item(
    contentId = this.identifier,
    language = "",
    title = this.title,
    description = "",
    creator = this.creator,
    mediaType = this.mediatype
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
