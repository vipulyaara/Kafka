package com.kafka.data.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.kafka.data.entities.Content
import kotlinx.coroutines.flow.Flow

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
@Dao
abstract class SearchDao {

    @Query("select * from Content where genre = :creator order by title")
    abstract fun searchItemsFlow(creator: String): Flow<List<Content>>
}
