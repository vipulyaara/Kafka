package com.kafka.data.db

import androidx.room.Room
import androidx.room.RoomDatabase
import com.kafka.base.ApplicationScope
import com.kafka.data.injection.databaseName
import com.kafka.data.platform.appDirectory
import me.tatarka.inject.annotations.Provides
import java.io.File

actual interface DatabaseBuilderComponent {
    @ApplicationScope
    @Provides
    fun provideDatabaseBuilder(): RoomDatabase.Builder<KafkaRoomDatabase> {
        val dbFile = File(System.getProperty(appDirectory), databaseName)
        return Room.databaseBuilder<KafkaRoomDatabase>(dbFile.absolutePath)
    }
}
