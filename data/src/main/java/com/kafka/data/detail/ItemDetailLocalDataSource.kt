package com.kafka.data.detail

import com.kafka.data.data.db.dao.ItemDetailDao
import com.kafka.data.entities.ItemDetail
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
class ItemDetailLocalDataSource @Inject constructor(
    private val contentDetailDao: ItemDetailDao
) {
    fun itemDetailFlow(contentId: String) = contentDetailDao.itemDetailFlow(contentId)

    suspend fun saveItemDetail(itemDetail: ItemDetail) = contentDetailDao.insert(itemDetail)
}
