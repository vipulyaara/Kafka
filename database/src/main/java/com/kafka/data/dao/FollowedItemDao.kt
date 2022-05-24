package com.kafka.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.kafka.data.entities.FollowedItem
import kotlinx.coroutines.flow.Flow

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
@Dao
abstract class FollowedItemDao : EntityDao<FollowedItem> {
    @Query("SELECT * FROM followed_item")
    abstract fun observeFollowedItems(): Flow<List<FollowedItem>>

    @Query("SELECT COUNT(*) FROM followed_item WHERE itemId = :itemId")
    abstract fun observeItemFollowed(itemId: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM followed_item WHERE itemId = :itemId")
    abstract suspend fun isItemFollowed(itemId: String): Int

    @Query("DELETE FROM followed_item WHERE itemId = :itemId")
    abstract suspend fun removeFromFollowedItems(itemId: String): Int
}
