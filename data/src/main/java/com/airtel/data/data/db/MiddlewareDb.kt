package com.airtel.data.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.airtel.data.data.db.dao.BookDao
import com.airtel.data.data.db.entities.Book

/**
 * Database description.
 */
@Database(
    entities = [Book::class],
    version = 1
)
//@TypeConverters(MiddlewareTypeConverters::class)
internal abstract class MiddlewareDb : RoomDatabase() {
    abstract fun contentDao(): BookDao
}
