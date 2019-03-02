package com.airtel.data.feature.detail

import com.airtel.data.data.mapper.Mapper
import com.airtel.data.entities.ItemDetail
import com.airtel.data.entities.toItemDetail
import com.airtel.data.extensions.executeWithRetry
import com.airtel.data.feature.common.DataSource
import com.airtel.data.model.data.Result
import com.airtel.data.model.item.ItemDetailResponse

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
class ItemDetailDataSource : DataSource() {
    private val mapper = object : Mapper<ItemDetailResponse, ItemDetail> {
        override fun map(from: ItemDetailResponse): ItemDetail {
            return from.toItemDetail()
        }
    }

    suspend fun fetchItemDetail(itemId: String): Result<ItemDetail> {
        return retrofitRunner.executeForResponse(mapper) {
            archiveService
                .getItemDetail(itemId)
                .executeWithRetry()
        }
    }
}
