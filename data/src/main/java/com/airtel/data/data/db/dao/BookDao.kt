package com.airtel.data.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.airtel.data.entities.Book
import io.reactivex.Flowable

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
@Dao
abstract class BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertBooks(books: List<Book>)

    @Query("select * from Book")
    abstract fun booksFlowable(): Flowable<List<Book>>
}
