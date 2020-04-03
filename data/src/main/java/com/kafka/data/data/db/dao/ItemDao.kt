package com.kafka.data.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.kafka.data.entities.Item
import kotlinx.coroutines.flow.Flow

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
@Dao
abstract class ItemDao : EntityDao<Item> {

    @Query("select * from item")
    abstract fun observeQueryItems(): Flow<List<Item>>

    @Query("select * from item where contentId = :itemId")
    abstract fun getItemByItemId(itemId: String): Item
}
