package com.kafka.data.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kafka.data.data.db.dao.*
import com.kafka.data.entities.*
import com.kafka.data.recent.RecentItemDao

interface KafkaDatabase {
    fun itemDetailDao(): ItemDetailDao
    fun recentItemDao(): RecentItemDao
    fun followedItemDao(): FollowedItemDao
    fun searchDao(): SearchDao
    fun queryDao(): ItemDao
    fun languageDao(): LanguageDao
}
@Database(
    entities = [
        ItemDetail::class,
        Item::class,
        Language::class,
        RecentItem::class,
        FollowedItem::class
    ],
    version = 1
)
@TypeConverters(AppTypeConverters::class)
abstract class KafkaRoomDatabase : RoomDatabase(), KafkaDatabase
