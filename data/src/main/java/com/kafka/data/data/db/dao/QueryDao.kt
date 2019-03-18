package com.kafka.data.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kafka.data.entities.Item
import io.reactivex.Flowable

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
@Dao
abstract class QueryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertItems(items: List<Item>)

    @Query("select * from Item where creator = :creator order by title")
    abstract fun observeQueryByCreator(creator: String): Flowable<List<Item>>

    @Query("select * from Item where collection like :collection order by title")
    abstract fun observeQueryByCollection(collection: String): Flowable<List<Item>>

    @Query("select * from Item where genre like :genre order by title")
    abstract fun observeQueryByGenre(genre: String): Flowable<List<Item>>
}
