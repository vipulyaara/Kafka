package com.kafka.data.entities

import android.text.Html
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kafka.data.model.item.File
import com.kafka.data.model.item.ItemDetailResponse

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
    val mediaType: String? = null,
    val coverImage: String? = null,
    val coverImageResource: Int = 0,
    val files: List<File>? = null
) : BaseEntity

fun List<File>.filterMp3() = filter { it.format.contains("mp3", true) }

fun ItemDetail?.formattedDescription() = this?.description?.let { Html.fromHtml(it)?.toString() } ?: ""
