package com.kafka.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Download(
    @PrimaryKey val fileId: String,
    val url: String,
    val status: Status,
    val progress: Int,
    val filePath: String
) {
    enum class Status { Downloading, Completed, Failed }
}
