package com.kafka.data.db

import androidx.room.Room
import androidx.room.RoomDatabase
import com.kafka.base.ApplicationScope
import com.kafka.data.injection.databaseName
import me.tatarka.inject.annotations.Provides
import java.io.File

actual interface DatabaseBuilderComponent {
    @ApplicationScope
    @Provides
    fun provideDatabaseBuilder(): RoomDatabase.Builder<KafkaRoomDatabase> {
        // todo: kmp select appropriate directory
        val dbFile = File(
            System.getProperty("java.io.tmpDir"),
            databaseName
        )

        return Room.databaseBuilder<KafkaRoomDatabase>(dbFile.absolutePath)
    }
}
