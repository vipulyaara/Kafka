package com.airtel.data.feature.item

import com.airtel.data.data.db.DatabaseTransactionRunner
import com.airtel.data.data.db.dao.SearchDao
import com.airtel.data.entities.Item
import com.airtel.data.query.ArchiveQuery
import com.airtel.data.query._collection
import com.airtel.data.query._creator
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
