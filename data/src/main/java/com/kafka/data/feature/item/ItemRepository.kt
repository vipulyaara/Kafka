package com.kafka.data.feature.item

import androidx.sqlite.db.SimpleSQLiteQuery
import com.kafka.data.dao.ItemLocalDataSource
import com.kafka.data.dao.RecentItemLocalDataSource
import com.kafka.data.dao.SearchConfigurationDao
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemWithRecentItem
import com.kafka.data.entities.SearchConfiguration
import com.kafka.data.entities.asRecentlyVisited
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class ItemRepository @Inject constructor(
    private val itemLocalDataSource: ItemLocalDataSource,
    private val searchConfigurationDao: SearchConfigurationDao,
    private val recentItemLocalDataSource: RecentItemLocalDataSource,
    private val remoteDataSource: ItemRemoteDataSource
) {

    fun observeQueryItems(simpleSQLiteQuery: SimpleSQLiteQuery) =
        itemLocalDataSource.observeQueryItems(simpleSQLiteQuery)

    private fun observeRecentlyVisitedItems() =
        recentItemLocalDataSource.observeRecentlyVisitedItems()

    fun observeItemsWithRecentlyVisitedInfo(): Flow<List<ItemWithRecentItem>> {
        return observeRecentlyVisitedItems().map { list ->
            list.sortedByDescending { it.timeStamp }
                .map { ItemWithRecentItem(getItemByItemId(it.itemId), it) }
        }
    }

    private fun getItemByItemId(itemId: String): Item {
        return itemLocalDataSource.getItemByItemId(itemId)
    }

    suspend fun addRecentlyVisitedItem(itemId: String) {
        val item = getItemByItemId(itemId)
        recentItemLocalDataSource.insert(
            item.asRecentlyVisited(System.currentTimeMillis())
        )
    }

    suspend fun updateQuery(query: String) {
        remoteDataSource.fetchItemsByQuery(query).getOrThrow().let {
            itemLocalDataSource.insertAll(it)
        }
    }

    suspend fun addRecentSearch(keyword: String) = searchConfigurationDao.apply {
        val searchConfiguration = getSearchConfiguration()
        val config =
            searchConfiguration?.copy(recentSearches = searchConfiguration.recentSearches?.toMutableList()
                ?.also { it.add(keyword) } ?: listOf(
                keyword
            )) ?: SearchConfiguration(recentSearches = listOf(keyword))
        insert(config)
    }

    suspend fun removeRecentSearch(keyword: String) = searchConfigurationDao.apply {
        val searchConfiguration = getSearchConfiguration()!!
        insert(
            searchConfiguration.copy(recentSearches = searchConfiguration.recentSearches?.toMutableList()
                ?.also { it.removeAll { it == keyword } })
        )
    }

    fun observeRecentSearch() =
        searchConfigurationDao.observeSearchConfiguration().mapNotNull { it?.recentSearches }
}
