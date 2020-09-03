package com.kafka.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "queue_meta_data")
data class QueueEntity constructor(
    @PrimaryKey(autoGenerate = false) var id: Long = 0,
    var currentSeekPos: Long = 0,
    var currentSongId: String? = null,
    var isPlaying: Boolean = false
) : BaseEntity
