package com.kafka.data.db

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kafka.base.ApplicationScope
import com.kafka.data.injection.databaseName
import me.tatarka.inject.annotations.Provides

actual interface DatabaseBuilderComponent {
    @ApplicationScope
    @Provides
    fun provideDatabaseBuilder(application: Application): RoomDatabase.Builder<KafkaRoomDatabase> {
        val dbFile = application.getDatabasePath(databaseName)
        return Room.databaseBuilder<KafkaRoomDatabase>(application, dbFile.absolutePath)
    }
}
