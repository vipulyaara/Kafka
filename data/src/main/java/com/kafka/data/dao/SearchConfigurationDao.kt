package com.kafka.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.kafka.data.entities.SearchConfiguration
import kotlinx.coroutines.flow.Flow

@Dao
abstract class SearchConfigurationDao : EntityDao<SearchConfiguration> {

    @Query("select * from searchconfiguration")
    abstract fun observeSearchConfiguration(): Flow<SearchConfiguration?>

    @Query("UPDATE searchconfiguration SET recentSearches = :recentSearch")
    abstract fun setRecentSearch(recentSearch: List<String>)

    @Query("select * from searchconfiguration")
    abstract suspend fun getSearchConfiguration(): SearchConfiguration?
}
