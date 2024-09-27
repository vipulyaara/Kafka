package com.kafka.data.injection

import android.app.Application
import android.os.Debug
import androidx.room.Room
import com.kafka.data.db.KafkaDatabase
import com.kafka.data.db.KafkaRoomDatabase
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import org.kafka.base.ApplicationScope

const val databaseName = "kafka.db"

@Component
@ApplicationScope
interface DatabaseModule {
    @Provides
    @ApplicationScope
    fun provideDatabase(context: Application): KafkaRoomDatabase {
        val builder = Room.databaseBuilder(
            context,
            KafkaRoomDatabase::class.java,
            databaseName,
        ).fallbackToDestructiveMigration()
        if (Debug.isDebuggerConnected()) {
            builder.allowMainThreadQueries()
        }
        return builder.build()
    }

    @Provides
    @ApplicationScope
    fun provideRoomDatabase(bind: KafkaRoomDatabase): KafkaDatabase = bind

    @Provides
    fun provideItemDao(db: KafkaRoomDatabase) = db.itemDao()

    @Provides
    fun provideItemDetailDao(db: KafkaRoomDatabase) = db.itemDetailDao()

    @Provides
    fun provideFileDao(db: KafkaRoomDatabase) = db.fileDao()

    @Provides
    fun provideSearchConfigurationDao(db: KafkaRoomDatabase) = db.recentSearchDao()

    @Provides
    fun provideRecentTextDao(db: KafkaRoomDatabase) = db.recentTextDao()

    @Provides
    fun provideRecentAudioDao(db: KafkaRoomDatabase) = db.recentAudioDao()

    @Provides
    fun provideDownloadRequestsDao(db: KafkaRoomDatabase) = db.downloadRequestsDao()
}
