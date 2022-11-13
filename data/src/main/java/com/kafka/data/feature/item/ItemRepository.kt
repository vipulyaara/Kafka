package com.kafka.data.feature.item

import androidx.sqlite.db.SimpleSQLiteQuery
import com.kafka.data.dao.ItemLocalDataSource
import com.kafka.data.dao.RecentItemLocalDataSource
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemWithRecentItem
import com.kafka.data.entities.asRecentlyVisited
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class ItemRepository @Inject constructor(
    private val itemLocalDataSource: ItemLocalDataSource,
    private val recentItemLocalDataSource: RecentItemLocalDataSource,
    private val remoteDataSource: ItemDataSource
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

    private suspend fun getItemByItemId(itemId: String): Item {
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
}
