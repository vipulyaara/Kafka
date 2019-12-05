package com.kafka.data.feature.search

import com.kafka.data.entities.Content
import com.kafka.data.feature.Repository
import com.kafka.data.model.data.Result
import com.kafka.data.model.data.Success
import com.kafka.data.query.ArchiveQuery

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class SearchRepository constructor(
    private val localSource: SearchLocalSource,
    private val remoteSource: SearchRemoteSource
) : Repository {

    fun observeSearch(archiveQuery: ArchiveQuery) = localSource.observeSearch(archiveQuery)

    suspend fun updateItemsByCreator(archiveQuery: ArchiveQuery): Result<List<Content>> {
        val result = remoteSource.fetchItemsByCreator(archiveQuery)
        when (result) {
            is Success -> {
                result.data.let {
                    localSource.saveItems(it)
                }
            }
        }
        return result
    }
}
