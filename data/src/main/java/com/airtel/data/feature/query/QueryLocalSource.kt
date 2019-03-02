package com.airtel.data.feature.query

import com.airtel.data.data.db.DatabaseTransactionRunner
import com.airtel.data.data.db.dao.QueryDao
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
class QueryLocalSource constructor(
    private val transactionRunner: DatabaseTransactionRunner,
    private val dao: QueryDao
) {
    fun observeQueryByCreator(creator: String): Flowable<List<Item>> {
        return dao.observeQueryByCreator(creator)
    }

    fun observeQueryByCollection(collection: String): Flowable<List<Item>> {
        return dao.observeQueryByCollection("%$collection%")
    }

    fun observeQueryByGenre(genre: String): Flowable<List<Item>> {
        return dao.observeQueryByGenre("%$genre%")
    }

    fun saveItems(items: List<Item>) = transactionRunner {
        dao.insertItems(items)
    }
}
