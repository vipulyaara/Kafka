package com.kafka.data.feature.item

import com.kafka.data.api.ArchiveService
import com.kafka.data.dao.ItemDetailDao
import com.kafka.data.resultApiCall
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.debug
import javax.inject.Inject

class ItemDetailDataSource @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val itemDetailDao: ItemDetailDao,
    private val itemDetailMapper: ItemDetailMapper,
    private val archiveService: ArchiveService,
) {
    suspend fun updateItemDetail(contentId: String) = resultApiCall(dispatchers.io) {
        itemDetailMapper.map(archiveService.getItemDetail(contentId)).let {
            debug { "Item detail is $it" }
            itemDetailDao.insert(it)
        }
    }
}
