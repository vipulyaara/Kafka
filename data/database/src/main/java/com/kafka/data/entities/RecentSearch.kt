package com.kafka.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import javax.annotation.concurrent.Immutable

@Entity(
    tableName = "recent_search",
    indices = [Index(value = ["search_term"], unique = true)],
)
@Immutable
data class RecentSearch(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long = 0,
    @ColumnInfo(name = "search_term") var searchTerm: String = "",
) : BaseEntity
