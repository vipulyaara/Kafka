package com.kafka.data.feature.detail

import com.kafka.data.data.api.ArchiveService
import com.kafka.data.data.api.RetrofitRunner
import com.kafka.data.data.mapper.Mapper
import com.kafka.data.entities.ContentDetail
import com.kafka.data.entities.contentDetail
import com.kafka.data.extensions.executeWithRetry
import com.kafka.data.model.data.Result
import com.kafka.data.model.item.ItemDetailResponse
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
class ContentDetailDataSource @Inject constructor(
    private val archiveService: ArchiveService,
    private val retrofitRunner: RetrofitRunner
) {
    private val mapper = object : Mapper<ItemDetailResponse, ContentDetail> {
        override fun map(from: ItemDetailResponse): ContentDetail {
            return from.contentDetail()
        }
    }

    suspend fun fetchItemDetail(contentId: String): Result<ContentDetail> {
        return retrofitRunner.executeForResponse(mapper) {
            archiveService.getItemDetail(contentId)
                .executeWithRetry()
        }
    }
}
