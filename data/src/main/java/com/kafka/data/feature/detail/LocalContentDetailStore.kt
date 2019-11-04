package com.kafka.data.feature.detail

import com.kafka.data.data.db.DatabaseTransactionRunner
import com.kafka.data.data.db.dao.ItemDetailDao
import com.kafka.data.entities.ContentDetail
import kotlinx.coroutines.flow.Flow

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class LocalContentDetailStore constructor(
    private val transactionRunner: DatabaseTransactionRunner,
    private val contentDetailDao: ItemDetailDao
) {
    fun itemDetailFlow(contentId: String): Flow<ContentDetail> {
        return contentDetailDao.itemDetailFlow(contentId)
    }

    fun itemDetail(contentId: String) = contentDetailDao.itemDetail(contentId)

    fun saveItemDetail(contentDetail: ContentDetail) = transactionRunner {
        contentDetailDao.insertItemDetail(contentDetail)
    }
}
