package com.kafka.data.item

import com.kafka.data.entities.Item
import com.kafka.data.entities.asRecentlyVisited
import com.data.base.model.getOrThrow
import com.kafka.data.query.ArchiveQuery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class ItemRepository @Inject constructor(
    private val itemItemLocalDataSource: ItemLocalDataSource,
    private val remoteDataSource: ItemRemoteDataSource
) {

    fun observeQueryItems() = itemItemLocalDataSource.observeQueryItems()

    fun observeRecentlyVisitedItems(): Flow<List<Item>> {
        return itemItemLocalDataSource.observeRecentlyVisitedItems()
            .map { list ->
                list.sortedByDescending { it.timeStamp }
                    .map { getItemByItemId(it.itemId) }
            }
    }

    private fun getItemByItemId(itemId: String): Item {
        return itemItemLocalDataSource.getItemByItemId(itemId)
    }

    suspend fun addRecentlyVisitedItem(itemId: String) {
        val item = getItemByItemId(itemId)
        itemItemLocalDataSource.saveRecentlyVisitedItem(
            item.asRecentlyVisited(System.currentTimeMillis())
        )
    }

    suspend fun updateQuery(archiveQuery: ArchiveQuery) {
        remoteDataSource.fetchItemsByCreator(archiveQuery)
            .getOrThrow()
            .let { itemItemLocalDataSource.saveItems(it) }
    }
}
