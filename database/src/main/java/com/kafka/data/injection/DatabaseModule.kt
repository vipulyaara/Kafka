package com.kafka.data.injection

import android.content.Context
import android.os.Debug
import androidx.room.Room
import com.kafka.data.db.KafkaDatabase
import com.kafka.data.db.KafkaRoomDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton
import kotlin.coroutines.EmptyCoroutineContext

const val databaseName = "kafka.db"

@InstallIn(SingletonComponent::class)
@Module(
    includes = [
        RoomDatabaseModule::class,
        DatabaseDaoModule::class,
        DatabaseModuleBinds::class
    ]
)
class DatabaseModule

@InstallIn(SingletonComponent::class)
@Module
class RoomDatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): KafkaRoomDatabase {
        val builder = Room.databaseBuilder(
            context, KafkaRoomDatabase::class.java, databaseName
        ).fallbackToDestructiveMigration()
        if (Debug.isDebuggerConnected()) {
            builder.allowMainThreadQueries()
        }
        return builder.build()
    }

    @Singleton
    @Provides
    fun providesStoreDispatcher(): CoroutineScope = CoroutineScope(EmptyCoroutineContext)
}

@InstallIn(SingletonComponent::class)
@Module
abstract class DatabaseModuleBinds {

    @Singleton
    @Binds
    abstract fun provideRoomDatabase(bind: KafkaRoomDatabase): KafkaDatabase
}

@InstallIn(SingletonComponent::class)
@Module
class DatabaseDaoModule {
    @Provides
    fun provideItemDao(db: KafkaRoomDatabase) = db.itemDao()

    @Provides
    fun provideItemDetailDao(db: KafkaRoomDatabase) = db.itemDetailDao()

    @Provides
    fun provideFileDao(db: KafkaRoomDatabase) = db.fileDao()

    @Provides
    fun provideTextFileDao(db: KafkaRoomDatabase) = db.textFileDao()

    @Provides
    fun provideContentEntryDao(db: KafkaRoomDatabase) = db.searchDao()

    @Provides
    fun provideFollowedItemDao(db: KafkaRoomDatabase) = db.followedItemDao()

    @Provides
    fun provideSearchConfigurationDao(db: KafkaRoomDatabase) = db.recentSearchDao()

    @Provides
    fun provideDownloadRequestsDao(db: KafkaRoomDatabase) = db.downloadRequestsDao()

    @Provides
    fun provideAuthDao(db: KafkaRoomDatabase) = db.authDao()
}
