package com.kafka.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecentItem(
    @PrimaryKey val itemId: String,
    val timeStamp: Long
) : BaseEntity

//sealed class RecentItem {
//    abstract val itemId: String
//    abstract val fileId: String
//
//    data class Readable(
//        override val itemId: String,
//        override val fileId: String,
//        val currentPage: Int
//    ) : RecentItem()
//
//    data class Listenable(
//        override val itemId: String,
//        override val fileId: String,
//        val timeStamp: Long
//    ) : RecentItem()
//}

data class ItemWithRecentItem(val item: Item, val recentItem: RecentItem)
