package com.kafka.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kafka.data.model.MediaType

/**
 * @author Vipul Kumar; dated 13/02/19.
 */
@Entity
data class Item constructor(
    @PrimaryKey val itemId: String = "",
    @Embedded(prefix = "creator_") val creator: Creator? = null,
    val language: List<String>? = null,
    val title: String? = null,
    val description: String? = null,
    val mediaType: String? = null,
    val coverImage: String? = null,
    val collection: List<String>? = null,
    val genre: List<String>? = null,
    val subjects: List<String>? = null,
    val uploader: String? = null,
    val position: Int = 0
) : BaseEntity

data class Creator(val id: String, val name: String)

fun Item.asRecentlyVisited(visitedTime: Long) = RecentItem(itemId, visitedTime)

fun Item.mediaType() = when (mediaType) {
    "texts" -> MediaType.Text
    "audio" -> MediaType.Audio
    "video" -> MediaType.Video
    else -> MediaType.Text
}
