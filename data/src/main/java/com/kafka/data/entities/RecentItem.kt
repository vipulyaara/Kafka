package com.kafka.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecentItem(
    @PrimaryKey val itemId: String,
    val timeStamp: Long
) : BaseEntity


data class ItemWithRecentItem(val item: Item, val recentItem: RecentItem)
