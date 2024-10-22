package com.kafka.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kafka.data.model.MediaType

@Entity(
    tableName = "recent_search",
    indices = [Index(value = ["search_term"], unique = true)],
)
data class RecentSearch(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long = 0,
    @ColumnInfo(name = "search_term") var searchTerm: String = "",
    @ColumnInfo(name = "media_types") var mediaTypes: List<MediaType>, //todo
) : BaseEntity
