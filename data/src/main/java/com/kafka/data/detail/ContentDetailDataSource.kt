package com.kafka.data.detail

import com.kafka.data.data.api.ArchiveService
import com.kafka.data.entities.ItemDetail
import com.kafka.data.entities.toItemDetail
import com.kafka.data.extensions.executeWithRetry
import com.kafka.data.extensions.toResult
import com.kafka.data.model.data.Result
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
class ContentDetailDataSource @Inject constructor(
    private val archiveService: ArchiveService
) {

    suspend fun fetchItemDetail(contentId: String): Result<ItemDetail> {
        return archiveService.getItemDetail(contentId)
            .executeWithRetry()
            .toResult { it.toItemDetail() }
    }
}
