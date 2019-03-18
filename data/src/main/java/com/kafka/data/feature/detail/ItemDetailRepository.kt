package com.kafka.data.feature.detail

import com.kafka.data.entities.ItemDetail
import com.kafka.data.feature.Repository
import com.kafka.data.model.data.Result
import com.kafka.data.model.data.Success

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class ItemDetailRepository constructor(
    private val localStore: LocalItemDetailStore,
    private val dataSource: ItemDetailDataSource
) : Repository {

    fun observeForFlowable(itemId: String) = localStore.itemDetailFlowable(itemId)

    suspend fun updateItemDetail(itemDetail: String): Result<ItemDetail> {
        val result = dataSource.fetchItemDetail(itemDetail)
        when (result) {
            is Success -> {
                result.data.let {
                    localStore.saveItemDetail(it)
                }
            }
        }
        return result
    }
}
