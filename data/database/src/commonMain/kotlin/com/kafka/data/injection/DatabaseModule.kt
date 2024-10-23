package com.kafka.data.injection

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.kafka.base.ApplicationScope
import com.kafka.data.db.KafkaDatabase
import com.kafka.data.db.KafkaRoomDatabase
import kotlinx.coroutines.Dispatchers
import me.tatarka.inject.annotations.Provides

const val databaseName = "kafka_1.db"

interface DatabaseModule {
    @Provides
    @ApplicationScope
    fun provideDatabase(builder: RoomDatabase.Builder<KafkaRoomDatabase>): KafkaRoomDatabase {
        return builder
            .fallbackToDestructiveMigrationOnDowngrade(true)
            .fallbackToDestructiveMigration(true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    @Provides
    @ApplicationScope
    fun provideRoomDatabase(bind: KafkaRoomDatabase): KafkaDatabase = bind

    @Provides
    @ApplicationScope
    fun provideItemDao(db: KafkaRoomDatabase) = db.itemDao()

    @Provides
    @ApplicationScope
    fun provideItemDetailDao(db: KafkaRoomDatabase) = db.itemDetailDao()

    @Provides
    @ApplicationScope
    fun provideFileDao(db: KafkaRoomDatabase) = db.fileDao()

    @Provides
    @ApplicationScope
    fun provideSearchConfigurationDao(db: KafkaRoomDatabase) = db.recentSearchDao()

    @Provides
    @ApplicationScope
    fun provideRecentTextDao(db: KafkaRoomDatabase) = db.recentTextDao()

    @Provides
    @ApplicationScope
    fun provideRecentAudioDao(db: KafkaRoomDatabase) = db.recentAudioDao()

    @Provides
    @ApplicationScope
    fun provideDownloadDao(db: KafkaRoomDatabase) = db.downloadDao()
}
