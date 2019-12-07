package com.kafka.data.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.kafka.data.entities.Content
import kotlinx.coroutines.flow.Flow

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
@Dao
abstract class ContentDao : EntityDao<Content> {

    @Query("select * from Content")
    abstract fun observeQueryByCreator(): Flow<List<Content>>

    @Query("select * from Content where collection like :collection order by title")
    abstract fun observeQueryByCollection(collection: String): Flow<List<Content>>

    @Query("select * from Content where genre like :genre order by title")
    abstract fun observeQueryByGenre(genre: String): Flow<List<Content>>
}
