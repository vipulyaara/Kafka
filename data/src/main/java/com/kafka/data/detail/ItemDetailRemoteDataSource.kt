package com.kafka.data.detail

import com.data.base.extensions.executeWithRetry
import com.data.base.extensions.toResult
import com.kafka.data.data.api.ArchiveService
import com.kafka.data.entities.ItemDetail
import com.kafka.data.entities.toItemDetail
import com.data.base.model.Result
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
class ItemDetailRemoteDataSource @Inject constructor(
    private val archiveService: ArchiveService
) {

    suspend fun fetchItemDetail(contentId: String): Result<ItemDetail> {
        return archiveService.getItemDetail(contentId)
            .executeWithRetry()
            .toResult { it.toItemDetail() }
    }
}
