package com.kafka.content.data.item

import com.data.base.model.ArchiveQuery
import com.data.base.model.asLocalQuery
import com.data.base.model.getOrThrow
import com.kafka.data.dao.ItemLocalDataSource
import com.kafka.data.dao.RecentItemLocalDataSource
import com.kafka.data.entities.Item
import com.kafka.data.entities.RecentItem
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
    private val remoteDataSource: ItemRemoteDataSource
) {

    fun observeQueryItems(archiveQuery: ArchiveQuery) =
        itemLocalDataSource.observeQueryItems(archiveQuery.asLocalQuery())

    fun observeRecentlyVisitedItems(): Flow<List<RecentItem>> {
        return recentItemLocalDataSource.observeRecentlyVisitedItems()
    }

    fun observeRecentlyVisitedItemsAsItems(): Flow<List<Item>> {
        return observeRecentlyVisitedItems()
            .map { list ->
                list.sortedByDescending { it.timeStamp }
                    .map { getItemByItemId(it.itemId) }
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

    suspend fun updateQuery(archiveQuery: ArchiveQuery) {
        remoteDataSource.fetchItemsByCreator(archiveQuery)
            .getOrThrow()
            .let {
                itemLocalDataSource.insertAll(it)
            }
    }
}
