package com.kafka.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.kafka.data.entities.Review
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ReviewDao : EntityDao<Review> {
    @Query("SELECT * FROM review WHERE item_id = :itemId ORDER BY created_at DESC")
    abstract fun observeReviews(itemId: String): Flow<List<Review>>

    @Query("SELECT * FROM review WHERE item_id = :itemId ORDER BY created_at DESC LIMIT :limit")
    abstract fun observeReviews(itemId: String, limit: Int): Flow<List<Review>>
}
