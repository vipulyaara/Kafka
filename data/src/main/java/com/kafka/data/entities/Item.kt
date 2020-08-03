package com.kafka.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.data.base.model.MediaType

/**
 * @author Vipul Kumar; dated 13/02/19.
 */
@Entity
data class Item(
    @PrimaryKey val itemId: String = "",
    @Embedded(prefix = "creator_") val creator: Creator? = null,
    val language: List<String>? = null,
    val title: String? = null,
    val description: String? = null,
    val mediaType: String? = null,
    val coverImage: String? = null,
    var coverImageResource: Int = 0,
    val collection: List<String>? = null,
    val genre: List<String>? = null
) : BaseEntity

data class Creator(val id: String, val name: String)

fun Item.asRecentlyVisited(visitedTime: Long) =
    RecentItem(itemId, creator?.id, visitedTime)

fun Item.mediaType() = when (mediaType) {
    "texts" -> MediaType.Text
    "audio" -> MediaType.Audio
    "video" -> MediaType.Video
    else -> MediaType.Text
}
