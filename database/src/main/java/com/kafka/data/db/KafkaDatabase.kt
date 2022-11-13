package com.kafka.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kafka.data.dao.AudioDao
import com.kafka.data.dao.FileDao
import com.kafka.data.dao.FollowedItemDao
import com.kafka.data.dao.ItemDao
import com.kafka.data.dao.ItemDetailDao
import com.kafka.data.dao.LanguageDao
import com.kafka.data.dao.RecentItemDao
import com.kafka.data.dao.RecentSearchDao
import com.kafka.data.dao.SearchDao
import com.kafka.data.entities.Audio
import com.kafka.data.entities.File
import com.kafka.data.entities.FollowedItem
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import com.kafka.data.entities.Language
import com.kafka.data.entities.QueueEntity
import com.kafka.data.entities.RecentItem
import com.kafka.data.entities.RecentSearch

interface KafkaDatabase {
    fun itemDetailDao(): ItemDetailDao
    fun fileDao(): FileDao
    fun recentItemDao(): RecentItemDao
    fun followedItemDao(): FollowedItemDao
    fun searchDao(): SearchDao
    fun itemDao(): ItemDao
    fun queueDao(): AudioDao
    fun languageDao(): LanguageDao
    fun searchConfigurationDao(): RecentSearchDao
}

@Database(
    entities = [
        ItemDetail::class,
        File::class,
        Item::class,
        Language::class,
        RecentItem::class,
        FollowedItem::class,
        QueueEntity::class,
        Audio::class,
        RecentSearch::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(AppTypeConverters::class)
abstract class KafkaRoomDatabase : RoomDatabase(), KafkaDatabase
