package com.kafka.data.data.config

import android.content.Context
import android.os.Debug
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kafka.data.data.db.DatabaseTransactionRunner
import com.kafka.data.data.db.KafkaRoomDatabase
import com.kafka.data.data.db.RoomTransactionRunner
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

const val databaseName = "kafka.db"

@Module(
    includes = [
        RoomDatabaseModule::class,
        DatabaseDaoModule::class,
        DatabaseModuleBinds::class
    ]
)
class DatabaseModule

@Module
class RoomDatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(context: Context): KafkaRoomDatabase {
        val builder = Room.databaseBuilder(
            context, KafkaRoomDatabase::class.java, databaseName
        ).fallbackToDestructiveMigration()
        if (Debug.isDebuggerConnected()) {
            builder.allowMainThreadQueries()
        }
        return builder.build()
    }
}


@Module
abstract class DatabaseModuleBinds {
    @Singleton
    @Binds
    abstract fun provideDatabaseTransactionRunner(bind: RoomTransactionRunner): DatabaseTransactionRunner

    @Singleton
    @Binds
    abstract fun provideRoomDatabase(bind: KafkaRoomDatabase): RoomDatabase
}

@Module
class DatabaseDaoModule {
    @Provides
    fun providePoetsDao(db: KafkaRoomDatabase) = db.queryDao()

    @Provides
    fun providePoetEntryDao(db: KafkaRoomDatabase) = db.itemDetailDao()

    @Provides
    fun provideContentDao(db: KafkaRoomDatabase) = db.languageDao()

    @Provides
    fun provideContentEntryDao(db: KafkaRoomDatabase) = db.searchDao()

    @Provides
    fun provideRecentItemDao(db: KafkaRoomDatabase) = db.recentItemDao()
}
