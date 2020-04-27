package com.kafka.data.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
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
    abstract fun getItemByItemId(itemId: String): Item
}
