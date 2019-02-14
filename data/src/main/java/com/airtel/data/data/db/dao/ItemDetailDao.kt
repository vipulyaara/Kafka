package com.airtel.data.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.airtel.data.entities.Item
import com.airtel.data.entities.ItemDetail
import io.reactivex.Flowable

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
@Dao
abstract class ItemDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertItemDetail(items: ItemDetail)

    @Query("select * from ItemDetail where itemId = :itemId")
    abstract fun itemDetailFlowable(itemId: String): Flowable<ItemDetail>
}
