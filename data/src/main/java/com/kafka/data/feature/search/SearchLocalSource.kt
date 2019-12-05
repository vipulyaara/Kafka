package com.kafka.data.feature.search

import com.kafka.data.data.db.DatabaseTransactionRunner
import com.kafka.data.data.db.dao.SearchDao
import com.kafka.data.entities.Content
import com.kafka.data.query.ArchiveQuery
import com.kafka.data.query._searchTerm
import io.reactivex.Flowable

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class SearchLocalSource constructor(
    private val transactionRunner: DatabaseTransactionRunner,
    private val searchDao: SearchDao
) {
    fun observeSearch(archiveQuery: ArchiveQuery): Flowable<List<Content>> {
        return searchDao.searchItemsFlow(archiveQuery.queries.get(_searchTerm)?.toTypedArray()?.get(0) ?: "")
    }

    fun saveItems(contents: List<Content>) = transactionRunner {
        searchDao.insertItems(contents)
    }
}
