package com.kafka.data.feature.query

import com.kafka.data.data.mapper.Mapper
import com.kafka.data.entities.Item
import com.kafka.data.entities.toArchiveItem
import com.kafka.data.extensions.executeWithRetry
import com.kafka.data.feature.common.DataSource
import com.kafka.data.model.data.Result
import com.kafka.data.model.item.SearchResponse
import com.kafka.data.query.ArchiveQuery
import com.kafka.data.query.buildSearchTerm

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
class QueryRemoteSource : DataSource() {
    private val mapper = object : Mapper<SearchResponse, List<Item>> {
        override fun map(from: SearchResponse): List<Item> {
            return from.response.docs.map { it.toArchiveItem() }
        }
    }

    suspend fun fetchItemsByCreator(archiveQuery: ArchiveQuery): Result<List<Item>> {
        return retrofitRunner.executeForResponse(mapper) {
            archiveService
                .search(archiveQuery.buildSearchTerm())
                .executeWithRetry()
        }
    }
}
