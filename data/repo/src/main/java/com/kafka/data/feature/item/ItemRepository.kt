package com.kafka.data.feature.item

import androidx.sqlite.db.SimpleSQLiteQuery
import com.kafka.data.dao.ItemLocalDataSource
import com.kafka.data.entities.Item
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class ItemRepository @Inject constructor(
    private val itemLocalDataSource: ItemLocalDataSource,
    private val remoteDataSource: ItemDataSource,
) {
    fun observeQueryItems(simpleSQLiteQuery: SimpleSQLiteQuery) =
        itemLocalDataSource.observeQueryItems(simpleSQLiteQuery)

    suspend fun updateQuery(query: String) =
        remoteDataSource.fetchItemsByQuery(query).getOrThrow()

    suspend fun saveItems(items: List<Item>) = itemLocalDataSource.insertAll(items)

    suspend fun exists(id: String) = itemLocalDataSource.exists(id)
}
