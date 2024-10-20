package com.kafka.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.DeleteTable
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import com.kafka.data.dao.DownloadRequestsDao
import com.kafka.data.dao.FileDao
import com.kafka.data.dao.ItemDao
import com.kafka.data.dao.ItemDetailDao
import com.kafka.data.dao.RecentAudioDao
import com.kafka.data.dao.RecentSearchDao
import com.kafka.data.dao.RecentTextDao
import com.kafka.data.entities.DownloadRequest
import com.kafka.data.entities.File
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import com.kafka.data.entities.QueueEntity
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
    fun downloadRequestsDao(): DownloadRequestsDao
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
        QueueEntity::class,
        RecentSearch::class,
        RecentTextItem::class,
        RecentAudioItem::class,
        DownloadRequest::class,
    ],
    version = 1,
    exportSchema = true,
)
@ConstructedBy(KafkaDatabaseConstructor::class)
@TypeConverters(AppTypeConverters::class)
abstract class KafkaRoomDatabase : RoomDatabase(), KafkaDatabase {
    @DeleteTable(tableName = "user")
    class UserRemovalMigration : AutoMigrationSpec
    @RenameColumn(tableName = "recent_audio", fromColumnName = "fileId", toColumnName = "albumId")
    class RecentAudioMigration : AutoMigrationSpec
    @DeleteColumn(tableName = "download_requests", columnName = "created_at")
    @DeleteColumn(tableName = "Item", columnName = "genre")
    class DownloadRequestsMigration : AutoMigrationSpec
}
