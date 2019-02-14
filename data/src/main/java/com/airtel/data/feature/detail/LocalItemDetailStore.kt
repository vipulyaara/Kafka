package com.airtel.data.feature.detail

import com.airtel.data.data.db.DatabaseTransactionRunner
import com.airtel.data.data.db.dao.ItemDetailDao
import com.airtel.data.entities.ItemDetail
import io.reactivex.Flowable

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class LocalItemDetailStore constructor(
    private val transactionRunner: DatabaseTransactionRunner,
    private val itemDetailDao: ItemDetailDao
) {
    fun itemDetailFlowable(itemId: String): Flowable<ItemDetail> {
        return itemDetailDao.itemDetailFlowable(itemId)
    }

    fun saveItemDetail(itemDetail: ItemDetail) = transactionRunner {
        itemDetailDao.insertItemDetail(itemDetail)
    }
}
