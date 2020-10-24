package com.kafka.content.data.detail

import com.kafka.data.api.ArchiveService
import com.kafka.data.extensions.executeWithRetry
import com.kafka.data.extensions.toResult
import com.kafka.data.model.model.Result
import com.kafka.data.entities.ItemDetail
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
class ItemDetailRemoteDataSource @Inject constructor(
    private val archiveService: ArchiveService,
    private val itemDetailMapper: ItemDetailMapper
) {

    suspend fun fetchItemDetail(contentId: String): Result<ItemDetail> {
        return archiveService.getItemDetail(contentId)
            .executeWithRetry()
            .toResult(itemDetailMapper)
    }
}
