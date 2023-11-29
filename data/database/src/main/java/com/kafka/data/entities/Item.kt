package com.kafka.data.entities

import androidx.compose.runtime.Immutable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Vipul Kumar; dated 13/02/19.
 */
@Immutable
@Entity
data class Item(
    @PrimaryKey val itemId: String = "",
    @Embedded(prefix = "creator_") val creator: Creator? = null,
    val language: List<String>? = null,
    val title: String? = null,
    val description: String? = null,
    val mediaType: String? = null,
    val coverImage: String? = null,
    val collection: List<String>? = null,
    val genre: List<String>? = null,
    val subject: String? = null,
    val uploader: String? = null,
    val position: Int = 0,
    val rating: Double? = null,
) : BaseEntity {
    val isAudio: Boolean
        get() = mediaType == "audio"

    val isInappropriate: Boolean
        get() = collection?.contains("no-preview") == true
}

@Immutable
data class Creator(val id: String, val name: String)
