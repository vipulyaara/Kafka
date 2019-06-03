package com.kafka.data.feature.detail

import com.kafka.data.data.mapper.Mapper
import com.kafka.data.entities.ItemDetail
import com.kafka.data.entities.toItemDetail
import com.kafka.data.extensions.executeWithRetry
import com.kafka.data.feature.common.DataSource
import com.kafka.data.model.data.Result
import com.kafka.data.model.item.ItemDetailResponse

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
