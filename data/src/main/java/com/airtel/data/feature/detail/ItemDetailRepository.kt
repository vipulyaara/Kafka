package com.airtel.data.feature.detail

import com.airtel.data.entities.Book
import com.airtel.data.entities.ItemDetail
import com.airtel.data.feature.Repository
import com.airtel.data.model.data.Result
import com.airtel.data.model.data.Success

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
