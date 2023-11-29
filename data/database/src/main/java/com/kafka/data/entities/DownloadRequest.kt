package com.kafka.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime

@Entity(tableName = "download_requests")
data class DownloadRequest(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "entity_type")
    val entityType: Type,

    @ColumnInfo(name = "request_id")
    val requestId: Int = REQUEST_NOT_SET,

    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
) : BaseEntity {

    companion object {
        const val REQUEST_NOT_SET = 0

        fun fromAudio(file: File) = DownloadRequest(
            id = file.fileId,
            entityType = Type.Audio,
        )
    }

    enum class Type {
        Audio, Playlist;

        override fun toString() = name

        companion object {
            private val map = entries.associateBy { it.name }

            fun from(value: String) = map[value] ?: Audio
        }
    }
}
