package com.kafka.data.content

import com.kafka.data.entities.Item
import com.kafka.data.entities.asRecentlyVisited
import com.kafka.data.model.data.Result
import com.kafka.data.model.data.Success
import com.kafka.data.model.data.dataOrThrowError
import com.kafka.data.query.ArchiveQuery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class ContentRepository @Inject constructor(
    private val localItemItemDataSource: LocalItemDataSource,
    private val remoteDataSource: ContentRemoteDataSource
) {

    fun observeQueryItems() = localItemItemDataSource.observeQueryItems()

    fun observeRecentlyVisitedItems(): Flow<List<Item>> {
        return localItemItemDataSource.observeRecentlyVisitedItems()
            .map { list ->
                list.sortedByDescending { it.timeStamp }
                    .map { getItemByItemId(it.itemId) }
            }
    }

    private fun getItemByItemId(itemId: String): Item {
        return localItemItemDataSource.getItemByItemId(itemId)
    }

    suspend fun addRecentlyVisitedItem(itemId: String) {
        val item = getItemByItemId(itemId)
        localItemItemDataSource.saveRecentlyVisitedItem(
            item.asRecentlyVisited(System.currentTimeMillis())
        )
    }

    suspend fun updateQuery(archiveQuery: ArchiveQuery) {
        remoteDataSource.fetchItemsByCreator(archiveQuery)
            .dataOrThrowError()
            .let { localItemItemDataSource.saveItems(it) }
    }
}
