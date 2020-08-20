package com.kafka.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kafka.data.dao.*
import com.kafka.data.entities.*

interface KafkaDatabase {
    fun itemDetailDao(): ItemDetailDao
    fun recentItemDao(): RecentItemDao
    fun followedItemDao(): FollowedItemDao
    fun searchDao(): SearchDao
    fun itemDao(): ItemDao
    fun queueDao(): QueueDao
    fun languageDao(): LanguageDao
    fun searchConfigurationDao(): SearchConfigurationDao
}

@Database(
    entities = [
        ItemDetail::class,
        Item::class,
        Language::class,
        RecentItem::class,
        FollowedItem::class,
        QueueEntity::class,
        SongEntity::class,
        SearchConfiguration::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(AppTypeConverters::class)
abstract class KafkaRoomDatabase : RoomDatabase(), KafkaDatabase
