package com.kafka.content.data.detail

import com.data.base.model.getOrThrow
import com.kafka.data.dao.ItemDetailLocalDataSource
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class ItemDetailRepository @Inject constructor(
    private val itemDetailLocalDataSource: ItemDetailLocalDataSource,
    private val itemDetailRemoteDataSource: ItemDetailRemoteDataSource
) {

    fun observeItemDetail(itemId: String) = itemDetailLocalDataSource.itemDetailFlow(itemId)

    suspend fun updateItemDetail(contentId: String) {
        itemDetailRemoteDataSource.fetchItemDetail(contentId)
            .getOrThrow()
            .let { itemDetailLocalDataSource.insert(it) }
    }
}
