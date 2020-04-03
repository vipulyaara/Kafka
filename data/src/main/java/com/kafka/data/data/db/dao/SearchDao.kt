package com.kafka.data.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.kafka.data.entities.Item
import kotlinx.coroutines.flow.Flow

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
@Dao
abstract class SearchDao : EntityDao<Item> {

    @Query("select * from Item where genre = :creator order by title")
    abstract fun searchItemsFlow(creator: String): Flow<List<Item>>
}
