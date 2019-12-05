package com.kafka.data.feature.content

import com.kafka.data.entities.Content
import com.kafka.data.feature.Repository
import com.kafka.data.model.data.Result
import com.kafka.data.model.data.Success
import com.kafka.data.query.ArchiveQuery

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class ContentRepository constructor(
    private val localSource: ContentLocalSource,
    private val remoteSource: ContentRemoteSource
) : Repository {

    fun observeQueryByCreator(creator: String) = localSource.observeQueryByCreator(creator)

    fun observeQueryByCollection(collection: String) = localSource.observeQueryByCollection(collection)

    fun observeQueryByGenre(genre: String) = localSource.observeQueryByGenre(genre)

    suspend fun updateQuery(archiveQuery: ArchiveQuery): Result<List<Content>> {
        val result = remoteSource.fetchItemsByCreator(archiveQuery)
        if (result is Success) {
            result.data.let { localSource.saveItems(it) }
        }
        return result
    }
}
