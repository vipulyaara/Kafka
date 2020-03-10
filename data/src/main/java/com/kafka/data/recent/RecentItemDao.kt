package com.kafka.data.recent

import androidx.room.Dao
import androidx.room.Query
import com.kafka.data.data.db.dao.EntityDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RecentItemDao : EntityDao<RecentItem>() {

    @Query("select * from recentitem")
    abstract fun observeRecentlyVisitedItems(): Flow<List<RecentItem>>

    @Query("select * from recentitem order by timeStamp desc limit :number")
    abstract fun getTopVisitedAuthors(number: Int): Flow<List<RecentItem>>
}
