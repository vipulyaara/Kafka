package com.kafka.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecentItem(
    @PrimaryKey val itemId: String,
    val creatorId: String?,
    val timeStamp: Long
) : BaseEntity
