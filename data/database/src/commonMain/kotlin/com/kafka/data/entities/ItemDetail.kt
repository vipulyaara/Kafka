package com.kafka.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kafka.data.model.MediaType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jsoup.Jsoup

/**
 * @author Vipul Kumar; dated 13/02/19.
 */
@Entity(indices = [Index(value = ["itemId"], unique = true)])
@Serializable
data class ItemDetail(
    @SerialName("item_id") @PrimaryKey val itemId: String,
    @SerialName("title") val title: String,
    @SerialName("media_type") val mediaType: MediaType,
    @SerialName("long_description") val description: String? = null,
    @SerialName("creators") val creators: List<String>? = null,
    @SerialName("collections") val collections: List<String>? = null,
    @SerialName("languages") val languages: List<String>? = null,
    @SerialName("cover_image") val coverImage: String? = null,
    @SerialName("subjects") val subject: List<String>? = null,
    @SerialName("rating") val rating: Double? = null,
    @SerialName("publishers") val publishers: List<String> = emptyList(),
) : BaseEntity {
    val creator: String?
        get() = creators?.take(5)?.joinToString()

    val language: String?
        get() = languages?.take(5)?.joinToString()

    val uiRating: Int
        get() = (rating ?: 0.0).toInt()

    val isAudio
        get() = this.mediaType.isAudio

    val immutableSubjects: ImmutableList<String>
        get() = subject.orEmpty().toPersistentList()

    private val trimmedDescription: String
        get() = description?.replaceFirst("<p>", "")
            ?.replaceFirst("</p>", "")
            ?.trim()
            .orEmpty()

    val formattedDescription: String
        get() = Jsoup.parse(trimmedDescription).text()
}
