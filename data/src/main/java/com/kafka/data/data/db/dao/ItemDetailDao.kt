package com.kafka.data.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kafka.data.entities.ContentDetail
import kotlinx.coroutines.flow.Flow

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
@Dao
abstract class ItemDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertItemDetail(items: ContentDetail)

    @Query("select * from ContentDetail where contentId = :contentId")
    abstract fun itemDetailFlow(contentId: String): Flow<ContentDetail>

    @Query("select * from ContentDetail where contentId = :contentId")
    abstract fun itemDetail(contentId: String): ContentDetail
}
