package com.kafka.data.db

import androidx.room.Room
import androidx.room.RoomDatabase
import com.kafka.base.ApplicationInfo
import com.kafka.base.ApplicationScope
import me.tatarka.inject.annotations.Provides

actual interface DatabaseBuilderComponent {
    @ApplicationScope
    @Provides
    fun provideDatabaseBuilder(applicationInfo: ApplicationInfo): RoomDatabase.Builder<KafkaRoomDatabase> {
        val dbFilePath = applicationInfo.cachePath() + "/databaseName"
        return Room.databaseBuilder<KafkaRoomDatabase>(name = dbFilePath)
    }
}
