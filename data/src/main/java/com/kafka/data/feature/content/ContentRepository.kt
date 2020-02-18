package com.kafka.data.feature.content

import com.kafka.data.entities.Content
import com.kafka.data.model.data.Result
import com.kafka.data.model.data.Success
import com.kafka.data.query.ArchiveQuery
import com.kafka.data.query.buildLocalQuery
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class ContentRepository @Inject constructor(
    private val localDataSource: ContentLocalDataSource,
    private val remoteDataSource: ContentRemoteDataSource
) {
    fun observeQueryItems(archiveQuery: ArchiveQuery) =
        localDataSource.observeQueryItems(archiveQuery.buildLocalQuery())

    suspend fun updateQuery(archiveQuery: ArchiveQuery): Result<List<Content>> {
        val result = remoteDataSource.fetchItemsByCreator(archiveQuery)
        if (result is Success) {
            result.data.let { localDataSource.saveItems(it) }
        }
        return result
    }
}
