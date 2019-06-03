package com.kafka.data.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kafka.data.data.db.dao.BookDao
import com.kafka.data.data.db.dao.ItemDetailDao
import com.kafka.data.data.db.dao.QueryDao
import com.kafka.data.data.db.dao.SearchDao
import com.kafka.data.entities.Book
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail

/**
 * Database description.
 */
@Database(
    entities = [Book::class, ItemDetail::class, Item::class],
    version = 1
)
@TypeConverters(MiddlewareTypeConverters::class)
internal abstract class MiddlewareDb : RoomDatabase() {
    abstract fun contentDao(): BookDao
    abstract fun itemDetailDao(): ItemDetailDao
    abstract fun searchDao(): SearchDao
    abstract fun queryDao(): QueryDao
}
