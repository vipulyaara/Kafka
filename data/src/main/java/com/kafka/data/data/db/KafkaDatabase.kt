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
import com.kafka.data.entities.Language
import com.kafka.data.recent.RecentItemDao
import com.kafka.data.entities.RecentItem

interface KafkaDatabase {
    fun itemDetailDao(): ItemDetailDao
    fun recentItemDao(): RecentItemDao
    fun searchDao(): SearchDao
    fun queryDao(): ItemDao
    fun languageDao(): LanguageDao
}
@Database(
    entities = [
        ItemDetail::class,
        Item::class,
        Language::class,
        RecentItem::class
    ],
    version = 1
)
@TypeConverters(MiddlewareTypeConverters::class)
abstract class KafkaRoomDatabase : RoomDatabase(), KafkaDatabase
