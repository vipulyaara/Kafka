package com.kafka.data.feature.query

import com.kafka.data.entities.Item
import com.kafka.data.feature.Repository
import com.kafka.data.model.data.Result
import com.kafka.data.model.data.Success
import com.kafka.data.query.ArchiveQuery

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class QueryRepository constructor(
    private val localSource: QueryLocalSource,
    private val remoteSource: QueryRemoteSource
) : Repository {

    fun observeQueryByCreator(creator: String) =
        localSource.observeQueryByCreator(creator)

    fun observeQueryByCollection(collection: String) =
        localSource.observeQueryByCollection(collection)

    fun observeQueryByGenre(genre: String) =
        localSource.observeQueryByGenre(genre)

    suspend fun updateQuery(archiveQuery: ArchiveQuery): Result<List<Item>> {
        val result = remoteSource.fetchItemsByCreator(archiveQuery)
        if (result is Success) {
            result.data.let { localSource.saveItems(it) }
        }
        return result
    }
}
