package com.kafka.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kafka.data.extensions.formatMilliseconds

@Entity(tableName = "song")
data class Song(
    @PrimaryKey var id: String = "",
    var itemId: String = "",
    var artistId: String? = null,
    var title: String = "",
    var creator: String = "",
    var duration: Int = 0,
    var trackNumber: Int = 0,
    var playbackUrl: String = "",
    val coverImage: String? = null
) : BaseEntity

val Song.formattedDuration
    get() = duration.formatMilliseconds()

val Song.subtitle
    get() = arrayOf(title, creator).joinToString(" $bulletSymbol ")

val Song.fileSubtitle
    get() = arrayOf(creator, formattedDuration).joinToString(" $bulletSymbol ")


const val bulletSymbol = " • "
