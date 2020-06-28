package com.kafka.data.injection

import android.content.Context
import android.os.Debug
import androidx.room.Room
import com.kafka.data.data.db.KafkaDatabase
import com.kafka.data.data.db.KafkaRoomDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton
import kotlin.coroutines.EmptyCoroutineContext

const val databaseName = "kafka.db"

@InstallIn(ApplicationComponent::class)
@Module(
    includes = [
        RoomDatabaseModule::class,
        DatabaseDaoModule::class,
        DatabaseModuleBinds::class
    ]
)
class DatabaseModule

@InstallIn(ApplicationComponent::class)
@Module
class RoomDatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(context: Context): KafkaRoomDatabase {
        val builder = Room.databaseBuilder(
            context, KafkaRoomDatabase::class.java, databaseName
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
        if (Debug.isDebuggerConnected()) {
            builder.allowMainThreadQueries()
        }
        return builder.build()
    }

    @ForStore
    @Singleton
    @Provides
    fun providesStoreDispatcher(): CoroutineScope = CoroutineScope(EmptyCoroutineContext)
}

@InstallIn(ApplicationComponent::class)
@Module
abstract class DatabaseModuleBinds {

    @Singleton
    @Binds
    abstract fun provideRoomDatabase(bind: KafkaRoomDatabase): KafkaDatabase
}

@InstallIn(ApplicationComponent::class)
@Module
class DatabaseDaoModule {
    @Provides
    fun provideItemDao(db: KafkaRoomDatabase) = db.itemDao()

    @Provides
    fun provideQueueDao(db: KafkaRoomDatabase) = db.queueDao()

    @Provides
    fun providePoetEntryDao(db: KafkaRoomDatabase) = db.itemDetailDao()

    @Provides
    fun provideContentDao(db: KafkaRoomDatabase) = db.languageDao()

    @Provides
    fun provideContentEntryDao(db: KafkaRoomDatabase) = db.searchDao()

    @Provides
    fun provideRecentItemDao(db: KafkaRoomDatabase) = db.recentItemDao()

    @Provides
    fun provideFollowedItemDao(db: KafkaRoomDatabase) = db.followedItemDao()
}
