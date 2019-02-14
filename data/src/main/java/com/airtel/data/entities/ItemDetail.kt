package com.airtel.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.airtel.data.model.item.ItemDetailResponse

/**
 * @author Vipul Kumar; dated 13/02/19.
 */
@Entity(indices = [
        Index(value = ["itemId"], unique = true)
    ])
data class ItemDetail(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val itemId: String? = "",
    val language: String? = null,
    val title: String? = null,
    val description: String? = null,
    val creator: String? = null,
    val mediaType: String? = null,
    val coverImage: String? = null
) {

    fun generateStableId(): Long {
        return id
    }
}

fun ItemDetailResponse.toItemDetail() = ItemDetail(
    itemId = this.metadata.identifier,
    language = this.metadata.licenseurl,
    title = this.metadata.title,
    description = this.metadata.description,
    creator = this.metadata.creator,
    mediaType = this.metadata.mediatype,
    coverImage = "https:/" + this.server + this.dir + "/" + this.files.firstOrNull { it.format == "JPEG" }?.name
)

fun ItemDetail.mediaType() = mediaType(mediaType)
