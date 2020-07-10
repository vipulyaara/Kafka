package com.kafka.content.data.item

import com.data.base.api.ArchiveService
import com.data.base.extensions.executeWithRetry
import com.data.base.extensions.toResult
import com.data.base.model.ArchiveQuery
import com.data.base.model.Result
import com.data.base.model.buildRemoteQuery
import com.kafka.data.entities.Item
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
class ItemRemoteDataSource @Inject constructor(
    private val archiveService: ArchiveService,
    private val itemMapper: ItemMapper
) {

    suspend fun fetchItemsByCreator(archiveQuery: ArchiveQuery): Result<List<Item>> {
        return archiveService
            .search(archiveQuery.buildRemoteQuery())
            .executeWithRetry()
            .toResult(itemMapper)
    }
}
