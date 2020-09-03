package com.kafka.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.kafka.data.entities.RecentItem
import kotlinx.coroutines.flow.Flow

typealias RecentItemLocalDataSource = RecentItemDao

@Dao
abstract class RecentItemDao : EntityDao<RecentItem> {

    @Query("select * from recentitem")
    abstract fun observeRecentlyVisitedItems(): Flow<List<RecentItem>>

    @Query("select * from recentitem order by timeStamp desc limit :number")
    abstract fun getTopVisitedAuthors(number: Int): Flow<List<RecentItem>>
}
