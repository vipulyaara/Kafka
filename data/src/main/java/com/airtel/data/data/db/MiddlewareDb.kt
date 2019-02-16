package com.airtel.data.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.airtel.data.data.db.dao.BookDao
import com.airtel.data.data.db.dao.ItemDetailDao
import com.airtel.data.data.db.dao.SearchDao
import com.airtel.data.entities.Book
import com.airtel.data.entities.Item
import com.airtel.data.entities.ItemDetail

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
}
