package com.kafka.data.detail

import com.data.base.model.getOrThrow
import com.kafka.data.extensions.asyncOrAwait
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class ItemDetailRepository @Inject constructor(
    private val itemDetailLocalDataSource: ItemDetailLocalDataSource,
    private val itemDetailRemoteDataSource: ItemDetailRemoteDataSource
) {

    fun observeItemDetail(contentId: String) = itemDetailLocalDataSource.itemDetailFlow(contentId)

    suspend fun updateItemDetail(contentId: String) {
        asyncOrAwait(key = "updateContentDetail") {
            itemDetailRemoteDataSource.fetchItemDetail(contentId)
                .getOrThrow()
                .let { itemDetailLocalDataSource.saveItemDetail(it) }
        }
    }
}
