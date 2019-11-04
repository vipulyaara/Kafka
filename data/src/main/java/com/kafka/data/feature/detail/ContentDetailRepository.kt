package com.kafka.data.feature.detail

import com.kafka.data.entities.ContentDetail
import com.kafka.data.feature.Repository
import com.kafka.data.model.data.ErrorResult
import com.kafka.data.model.data.Success
import com.kafka.data.util.AppCoroutineDispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class ContentDetailRepository constructor(
    private val localStore: LocalContentDetailStore,
    private val dataSource: ItemDetailDataSource,
    private val dispatchers: AppCoroutineDispatchers
) : Repository {

    fun observeItemDetail(contentId: String) = localStore.itemDetailFlow(contentId)

    suspend fun updateContentDetail(contentId: String) {
        val local = localStore.itemDetail(contentId)

        val apiResult = coroutineScope {
            async(dispatchers.io) {
                dataSource.fetchItemDetail(contentId)
            }
        }.await()

        if (apiResult is ErrorResult) throw RuntimeException("Error Result")

        localStore.saveItemDetail((apiResult as? Success)?.data ?: local ?: ContentDetail())
    }
}
