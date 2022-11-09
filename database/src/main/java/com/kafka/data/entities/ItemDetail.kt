package com.kafka.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * @author Vipul Kumar; dated 13/02/19.
 */
@Entity(indices = [Index(value = ["itemId"], unique = true)])
data class ItemDetail(
    @PrimaryKey val itemId: String = "",
    val language: String? = null,
    val title: String? = null,
    val description: String? = null,
    val creator: String? = null,
    val collection: String? = null,
    val mediaType: String? = null,
    val coverImage: String? = null,
    val coverImageResource: Int = 0,
    val files: List<String>? = null,
    val metadata: List<String>? = null
) : BaseEntity

fun ItemDetail?.readerUrl()  = "https://archive.org/details/${this?.itemId}/mode/1up?view=theater"
