package com.kafka.data.content

import com.kafka.data.extensions.executeWithRetry
import com.kafka.data.extensions.toResult
import com.kafka.data.data.api.ArchiveService
import com.kafka.data.data.mapper.Mapper
import com.kafka.data.entities.Item
import com.kafka.data.entities.toItem
import com.kafka.data.model.data.Result
import com.kafka.data.model.item.SearchResponse
import com.kafka.data.query.ArchiveQuery
import com.kafka.data.query.buildRemoteQuery
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
class ContentRemoteDataSource @Inject constructor(
    private val archiveService: ArchiveService
) {

    suspend fun fetchItemsByCreator(archiveQuery: ArchiveQuery): Result<List<Item>> {
        return archiveService
            .search(archiveQuery.buildRemoteQuery())
            .executeWithRetry()
            .toResult { it.response.docs.map { it.toItem() } }
    }
}
