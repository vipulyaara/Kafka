package com.kafka.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audio")
data class Audio(
    @PrimaryKey var id: String = "",
    var itemId: String = "",
    var artistId: String? = null,
    var title: String = "",
    var creator: String = "",
    var duration: Long = 0,
    var trackNumber: Int = 0,
    var playbackUrl: String = "",
    val coverImage: String? = null
) : BaseEntity {
    companion object {
        val unknown
            get() = Audio("unknown", "unknown")
    }
}

val Audio.formattedDuration
    get() = duration.formatMilliseconds()

val Audio.subtitle
    get() = arrayOf(title, creator).joinToString(" $bulletSymbol ")

val Audio.fileSubtitle
    get() = arrayOf(creator, formattedDuration).joinToString(" $bulletSymbol ")

fun Long.formatMilliseconds(): String {
    val minutes = this / 1000 / 1000 / 60
    val seconds = this / 1000 / 1000 % 60

    return listOf(minutes, seconds).joinToString(" : ")
}

const val bulletSymbol = " • "
