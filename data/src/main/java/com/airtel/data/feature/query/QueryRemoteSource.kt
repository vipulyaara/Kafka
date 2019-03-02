package com.airtel.data.feature.query

import com.airtel.data.data.mapper.Mapper
import com.airtel.data.entities.Item
import com.airtel.data.entities.toArchiveItem
import com.airtel.data.extensions.executeWithRetry
import com.airtel.data.feature.common.DataSource
import com.airtel.data.model.data.Result
import com.airtel.data.model.item.SearchResponse
import com.airtel.data.query.ArchiveQuery
import com.airtel.data.query.buildSearchTerm

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
