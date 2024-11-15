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
    @SerialName("item_id") @PrimaryKey val itemId: String,
    @SerialName("title") val title: String,
    @SerialName("media_type") val mediaType: MediaType,
    @SerialName("creators") val creators: List<String>,
    @SerialName("languages") val languages: List<String>?,
    @SerialName("description") val description: String?,
    @SerialName("cover_image") val coverImage: String?,
    @SerialName("collections") val collections: List<String>?,
    @SerialName("subjects") val subjects: List<String>,
) : BaseEntity {
    val creator: String
        get() = creators.take(5).joinToString()

    val isAudio: Boolean
        get() = mediaType == MediaType.Audio

    val isInappropriate: Boolean
        get() = collections?.contains("no-preview") ?: false
}
