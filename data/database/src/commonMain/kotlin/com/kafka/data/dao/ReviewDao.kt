package com.kafka.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.kafka.data.entities.Review
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ReviewDao : EntityDao<Review> {
    @Transaction
    @Query("SELECT * FROM review WHERE item_id = :itemId")
    abstract fun observeReviews(itemId: String): Flow<List<Review>>

    @Transaction
    @Query("SELECT * FROM review WHERE item_id = :itemId LIMIT :limit")
    abstract fun observeReviews(itemId: String, limit: Int): Flow<List<Review>>

    @Query("DELETE FROM review WHERE item_id = :itemId")
    abstract suspend fun delete(itemId: String)
}
