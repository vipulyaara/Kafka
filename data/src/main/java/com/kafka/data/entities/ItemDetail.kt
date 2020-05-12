package com.kafka.data.entities

import android.text.Html
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kafka.data.extensions.formattedDuration
import org.threeten.bp.Duration

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

fun ItemDetail?.formattedDescription() = this?.description?.let { Html.fromHtml(it)?.toString() } ?: ""

