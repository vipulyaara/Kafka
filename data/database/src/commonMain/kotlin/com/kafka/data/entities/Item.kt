package com.kafka.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kafka.data.model.MediaType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author Vipul Kumar; dated 13/02/19.
 */
@Entity
@Serializable
data class Item(
    @SerialName("item_id") @PrimaryKey val itemId: String = "",
    @SerialName("title") val title: String = "",
    @SerialName("media_type") val mediaType: MediaType,
    @SerialName("creators") val creators: List<String> = emptyList(),
    @SerialName("languages") val languages: List<String>? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("cover_image") val coverImage: String? = null,
    @SerialName("collections") val collections: List<String>? = null,
    @SerialName("subjects") val subjects: List<String> = emptyList(),
) : BaseEntity {
    val creator: String
        get() = creators.take(5).joinToString()

    val isAudio: Boolean
        get() = mediaType == MediaType.Audio

    val isInappropriate: Boolean
        get() = collections?.contains("no-preview") ?: false
}
