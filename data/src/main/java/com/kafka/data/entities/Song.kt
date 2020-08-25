package com.kafka.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "song")
data class Song(
    @PrimaryKey var id: String = "",
    var itemId: String = "",
    var artistId: String? = null,
    var title: String = "",
    var subtitle: String = "",
    var duration: Int = 0,
    var trackNumber: Int = 0,
    var playbackUrl: String = "",
    val coverImage: String? = null
): BaseEntity
