package com.kafka.data.entities

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "user")
data class User(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "display_name") val displayName: String,
    @ColumnInfo(name = "image_url") val imageUrl: String? = null,
    @ColumnInfo(name = "anonymous") val anonymous: Boolean
) : BaseEntity {
    constructor() : this("", "", null, false)
}
