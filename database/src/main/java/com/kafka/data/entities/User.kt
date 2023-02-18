package com.kafka.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "display_name") val displayName: String,
    @ColumnInfo(name = "image_url") val imageUrl: String? = null,
) : BaseEntity
