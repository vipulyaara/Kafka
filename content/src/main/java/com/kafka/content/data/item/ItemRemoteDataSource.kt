package com.kafka.content.data.item

import com.data.base.api.ArchiveService
import com.data.base.extensions.executeWithRetry
import com.data.base.extensions.toResult
import com.data.base.model.Result
import com.kafka.data.entities.Item
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
class ItemRemoteDataSource @Inject constructor(
    private val archiveService: ArchiveService,
    private val itemMapper: ItemMapper
) {

    suspend fun fetchItemsByQuery(query: String): Result<List<Item>> {
        return archiveService
            .search(query)
            .executeWithRetry()
            .toResult(itemMapper)
    }
}
