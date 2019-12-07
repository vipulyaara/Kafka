package com.kafka.data.feature.detail

import com.kafka.data.data.db.dao.ItemDetailDao
import com.kafka.data.entities.ContentDetail
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
//33 Madrasa kuhna, Mahal shah bibi, Rampur UP, 244901
//+91 9412541108
class LocalContentDetailStore @Inject constructor(
    private val contentDetailDao: ItemDetailDao
) {
    fun itemDetailFlow(contentId: String) = contentDetailDao.itemDetailFlow(contentId)

    fun itemDetail(contentId: String) = contentDetailDao.itemDetail(contentId)

    fun saveItemDetail(contentDetail: ContentDetail) = contentDetailDao.insert(contentDetail)
}
