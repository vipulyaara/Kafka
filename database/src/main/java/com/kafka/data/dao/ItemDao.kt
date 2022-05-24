package com.kafka.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import com.kafka.data.entities.FollowedItem
import com.kafka.data.entities.Item
import kotlinx.coroutines.flow.Flow

/**
 * @author Vipul Kumar; dated 29/11/18.
 */

typealias ItemLocalDataSource = ItemDao

@Dao
abstract class ItemDao : EntityDao<Item> {

    @RawQuery(observedEntities = [Item::class])
    abstract fun observeQueryItems(buildLocalQuery: SimpleSQLiteQuery): Flow<List<Item>>

    @Query("select * from item where itemId = :itemId")
    abstract suspend fun getItemByItemId(itemId: String): Item

    @Query("delete from item")
    abstract suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM followed_item WHERE itemId = :itemId")
    abstract fun observeItemFollowed(itemId: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM followed_item WHERE itemId = :itemId")
    abstract suspend fun isItemFollowed(itemId: String): Int

    @Query("DELETE FROM followed_item WHERE itemId = :itemId")
    abstract suspend fun removeFromFollowedItems(itemId: String): Int

    @Insert(entity = FollowedItem::class)
    abstract suspend fun insertFollowedItem(followedItem: FollowedItem)
}
