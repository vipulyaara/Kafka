package com.kafka.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kafka.data.model.common.BaseEntity
import com.kafka.data.model.item.File
import com.kafka.data.model.item.ItemDetailResponse

/**
 * @author Vipul Kumar; dated 13/02/19.
 */
@Entity(
    indices = [
        Index(value = ["contentId"], unique = true)
    ]
)
data class ContentDetail(
    override val id: Long = 0L,
    @PrimaryKey val contentId: String = "",
    val language: String? = null,
    val title: String? = null,
    val description: String? = null,
    val creator: String? = null,
    val mediaType: String? = null,
    val coverImage: String? = null,
    val files: List<File>? = null
) : BaseEntity {

    fun generateStableId(): String {
        return contentId
    }
}

fun ItemDetailResponse.contentDetail() = ContentDetail(
    contentId = this.metadata.identifier,
    language = this.metadata.licenseurl,
    title = this.metadata.title,
    description = this.metadata.description,
    creator = this.metadata.creator,
    mediaType = this.metadata.mediatype
//    coverImage = "https:/" + this.server + this.dir + "/" + this.files.firstOrNull {
//        it.format == "JPEG" || it.format.contains(
//            "Tile"
//        )
//    }?.name
)

fun ContentDetail.mediaType() = mediaType(mediaType)
