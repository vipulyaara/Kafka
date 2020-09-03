package com.kafka.content.data.detail

import com.data.base.api.ArchiveService
import com.data.base.extensions.executeWithRetry
import com.data.base.extensions.toResult
import com.data.base.model.Result
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
