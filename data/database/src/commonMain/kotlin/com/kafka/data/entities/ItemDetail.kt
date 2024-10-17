package com.kafka.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kafka.data.model._mediaTypeAudio
import com.kafka.data.model._mediaTypeText
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

/**
 * @author Vipul Kumar; dated 13/02/19.
 */
@Entity(indices = [Index(value = ["itemId"], unique = true)])
data class ItemDetail(
    @PrimaryKey val itemId: String,
    val language: String? = null,
    val title: String? = null,
    val description: String? = null,
    val creator: String? = null,
    val creators: List<String>? = null,
    val collection: String? = null,
    val mediaType: String? = null,
    val coverImage: String? = null,
    val files: List<String>? = null,
    val metadata: List<String>? = null,
    val primaryFile: String? = null,
    val subject: List<String>? = null,
    val rating: Double? = null,
    val isAccessRestricted: Boolean = false,
) : BaseEntity {
    val uiRating: Int
        get() = (rating ?: 0.0).toInt()

    val isAudio
        get() = this.mediaType.isAudioMediaType

    val isText
        get() = this.mediaType == _mediaTypeText

    val immutableSubjects: ImmutableList<String>
        get() = subject.orEmpty().toPersistentList()
}

val String?.isAudioMediaType
    get() = this == _mediaTypeAudio
