package com.kafka.data.feature.query

import com.kafka.data.data.db.DatabaseTransactionRunner
import com.kafka.data.data.db.dao.QueryDao
import com.kafka.data.entities.Item
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
