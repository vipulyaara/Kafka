package com.kafka.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.fleeksoft.ksoup.Ksoup
import com.kafka.data.model.MediaType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author Vipul Kumar; dated 13/02/19.
 */
@Entity(indices = [Index(value = ["itemId"], unique = true)])
@Serializable
data class ItemDetail(
    @SerialName("item_id") @PrimaryKey val itemId: String,
    @SerialName("title") val title: String,
    @SerialName("media_type") val mediaType: MediaType,
    @SerialName("long_description") val description: String?,
    @SerialName("creators") val creators: List<String>?,
    @SerialName("translators") val translators: List<String>?,
    @SerialName("collections") val collections: List<String>?,
    @SerialName("languages") val languages: List<String>?,
    @SerialName("cover_images") val coverImages: List<String>?,
    @SerialName("subjects") val subjects: List<String>?,
    @SerialName("rating") val rating: Double? = null,
    @SerialName("publishers") val publishers: List<String>,
    @SerialName("copyright") val copyright: Boolean?,
    @SerialName("copyright_text") val copyrightText: String?,
) : BaseEntity {
    val creator: String?
        get() = creators?.take(5)?.joinToString()

    val coverImage: String?
    get() = coverImages?.randomOrNull()

    val language: String
        get() = languages?.take(5)?.joinToString() ?: "en"

    val uiRating: Int
        get() = (rating ?: 0.0).toInt()

    val isAudio
        get() = this.mediaType.isAudio

    val immutableSubjects: ImmutableList<String>
        get() = subjects.orEmpty().toPersistentList()

    private val trimmedDescription: String
        get() = description?.replaceFirst("<p>", "")
            ?.replaceFirst("</p>", "")
            ?.trim()
            .orEmpty()

    val formattedDescription: String
        get() = Ksoup.parse(trimmedDescription).text()
}
