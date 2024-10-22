package com.kafka.data.db

import androidx.room.Room
import androidx.room.RoomDatabase
import com.kafka.base.ApplicationInfo
import com.kafka.base.ApplicationScope
import com.kafka.data.injection.databaseName
import me.tatarka.inject.annotations.Provides
import java.io.File

actual interface DatabaseBuilderComponent {
    @ApplicationScope
    @Provides
    fun provideDatabaseBuilder(applicationInfo: ApplicationInfo): RoomDatabase.Builder<KafkaRoomDatabase> {
        val dbFile = File(System.getProperty(applicationInfo.cachePath()), databaseName)
        return Room.databaseBuilder<KafkaRoomDatabase>(dbFile.absolutePath)
    }
}
