package com.kafka.data.feature.search

import com.kafka.data.data.db.DatabaseTransactionRunner
import com.kafka.data.data.db.dao.SearchDao
import com.kafka.data.entities.Item
import com.kafka.data.query.ArchiveQuery
import com.kafka.data.query._creator
import io.reactivex.Flowable

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class SearchLocalSource constructor(
    private val transactionRunner: DatabaseTransactionRunner,
    private val searchDao: SearchDao
) {
    fun observeSearch(archiveQuery: ArchiveQuery): Flowable<List<Item>> {
        return searchDao.searchItemsFlowable(archiveQuery.queries[_creator] ?: "")
    }

    fun saveItems(items: List<Item>) = transactionRunner {
        searchDao.insertItems(items)
    }
}
