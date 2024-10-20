package com.kafka.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.RoomRawQuery
import com.kafka.data.entities.Item
import kotlinx.coroutines.flow.Flow

/**
 * @author Vipul Kumar; dated 29/11/18.
 */

@Dao
abstract class ItemDao : EntityDao<Item> {

    @RawQuery(observedEntities = [Item::class])
    abstract fun observeQueryItems(buildLocalQuery: RoomRawQuery): Flow<List<Item>>

    @Query("select * from item where itemId = :itemId")
    abstract fun observe(itemId: String): Flow<Item?>

    @Query("select * from item where itemId = :itemId")
    abstract suspend fun getOrNull(itemId: String): Item?

    @Query("select * from item where itemId IN (:itemIds)")
    abstract fun observe(itemIds: List<String>): Flow<List<Item>>

    @Query("SELECT EXISTS(SELECT * FROM item where itemId = :itemId)")
    abstract suspend fun exists(itemId: String): Boolean
}
