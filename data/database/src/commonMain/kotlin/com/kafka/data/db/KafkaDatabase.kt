package com.kafka.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.kafka.data.dao.DownloadDao
import com.kafka.data.dao.FileDao
import com.kafka.data.dao.ItemDao
import com.kafka.data.dao.ItemDetailDao
import com.kafka.data.dao.RecentAudioDao
import com.kafka.data.dao.RecentSearchDao
import com.kafka.data.dao.RecentTextDao
import com.kafka.data.entities.Download
import com.kafka.data.entities.File
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import com.kafka.data.entities.RecentAudioItem
import com.kafka.data.entities.RecentSearch
import com.kafka.data.entities.RecentTextItem

interface KafkaDatabase {
    fun itemDetailDao(): ItemDetailDao
    fun fileDao(): FileDao
    fun itemDao(): ItemDao
    fun recentSearchDao(): RecentSearchDao
    fun recentTextDao(): RecentTextDao
    fun recentAudioDao(): RecentAudioDao
    fun downloadDao(): DownloadDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object KafkaDatabaseConstructor : RoomDatabaseConstructor<KafkaRoomDatabase> {
    override fun initialize(): KafkaRoomDatabase
}

@Database(
    entities = [
        ItemDetail::class,
        File::class,
        Item::class,
        Download::class,
        RecentSearch::class,
        RecentTextItem::class,
        RecentAudioItem::class,
    ],
    version = 1,
    exportSchema = true,
)
@ConstructedBy(KafkaDatabaseConstructor::class)
@TypeConverters(AppTypeConverters::class)
abstract class KafkaRoomDatabase : RoomDatabase(), KafkaDatabase
