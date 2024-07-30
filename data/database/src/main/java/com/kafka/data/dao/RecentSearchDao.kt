package com.kafka.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.kafka.data.entities.RecentSearch
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RecentSearchDao : EntityDao<RecentSearch> {
    @Transaction
    @Query("SELECT * FROM recent_search ORDER BY id DESC")
    abstract fun observeRecentSearch(): Flow<List<RecentSearch>>

    @Query("DELETE FROM recent_search WHERE search_term = :searchTerm")
    abstract suspend fun delete(searchTerm: String)
}
