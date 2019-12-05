package com.kafka.data.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kafka.data.data.db.dao.ContentDao
import com.kafka.data.data.db.dao.ItemDetailDao
import com.kafka.data.data.db.dao.LanguageDao
import com.kafka.data.data.db.dao.SearchDao
import com.kafka.data.entities.Book
import com.kafka.data.entities.Content
import com.kafka.data.entities.ContentDetail
import com.kafka.data.model.LanguageModel

/**
 * Database description.
 */
@Database(
    entities = [Book::class, ContentDetail::class, Content::class, LanguageModel::class],
    version = 1
)
@TypeConverters(MiddlewareTypeConverters::class)
internal abstract class MiddlewareDb : RoomDatabase() {
    abstract fun itemDetailDao(): ItemDetailDao
    abstract fun searchDao(): SearchDao
    abstract fun queryDao(): ContentDao
    abstract fun languageDao(): LanguageDao
}
