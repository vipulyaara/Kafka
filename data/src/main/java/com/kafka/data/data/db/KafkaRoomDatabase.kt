package com.kafka.data.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kafka.data.data.db.dao.ItemDao
import com.kafka.data.data.db.dao.ItemDetailDao
import com.kafka.data.data.db.dao.LanguageDao
import com.kafka.data.data.db.dao.SearchDao
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import com.kafka.data.model.LanguageModel
import com.kafka.data.recent.RecentItemDao
import com.kafka.data.recent.RecentItem

/**
 * Database description.
 */
@Database(
    entities = [ItemDetail::class, Item::class, LanguageModel::class, RecentItem::class],
    version = 1
)
@TypeConverters(MiddlewareTypeConverters::class)
abstract class KafkaRoomDatabase : RoomDatabase() {
    abstract fun itemDetailDao(): ItemDetailDao
    abstract fun recentItemDao(): RecentItemDao
    abstract fun searchDao(): SearchDao
    abstract fun queryDao(): ItemDao
    abstract fun languageDao(): LanguageDao
}
