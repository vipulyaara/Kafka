package com.kafka.data.feature.item

import com.kafka.data.api.ArchiveService
import com.kafka.data.dao.ItemDetailDao
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.network.resultApiCall
import javax.inject.Inject

class ItemDetailRepository @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val itemDetailDao: ItemDetailDao,
    private val itemDetailMapper: ItemDetailMapper,
    private val archiveService: ArchiveService
) {
    fun observeItemDetail(itemId: String) = itemDetailDao.itemDetailFlow(itemId)

    suspend fun updateItemDetail(contentId: String) = resultApiCall(dispatchers.io) {
        itemDetailMapper.map(archiveService.getItemDetail(contentId))
            .let { itemDetailDao.insert(it) }
    }
}
