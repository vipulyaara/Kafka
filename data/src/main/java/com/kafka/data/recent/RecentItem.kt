package com.kafka.data.recent

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kafka.data.model.common.BaseEntity

@Entity
data class RecentItem(
    @PrimaryKey val itemId: String,
    val creator: String?,
    val timeStamp: Long
) : BaseEntity
