package com.kafka.data.item

import com.data.base.extensions.executeWithRetry
import com.data.base.extensions.toResult
import com.kafka.data.entities.Item
import com.data.base.model.Result
import com.kafka.data.data.api.ArchiveService
import com.kafka.data.entities.toItem
import com.kafka.data.query.ArchiveQuery
import com.kafka.data.query.buildRemoteQuery
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
