package com.kafka.data.feature.detail

import com.kafka.data.data.db.dao.ItemDetailDao
import com.kafka.data.entities.ContentDetail

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class LocalContentDetailStore constructor(
    private val contentDetailDao: ItemDetailDao
) {
    fun itemDetailFlow(contentId: String) = contentDetailDao.itemDetailFlow(contentId)

    fun itemDetail(contentId: String) = contentDetailDao.itemDetail(contentId)

    fun saveItemDetail(contentDetail: ContentDetail) = contentDetailDao.insert(contentDetail)
}
