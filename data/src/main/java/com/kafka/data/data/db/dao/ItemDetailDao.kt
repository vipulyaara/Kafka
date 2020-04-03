package com.kafka.data.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.kafka.data.entities.ItemDetail
import kotlinx.coroutines.flow.Flow

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
@Dao
abstract class ItemDetailDao : EntityDao<ItemDetail> {

    @Query("select * from ItemDetail where itemId = :itemId")
    abstract fun itemDetailFlow(itemId: String): Flow<ItemDetail>
}
