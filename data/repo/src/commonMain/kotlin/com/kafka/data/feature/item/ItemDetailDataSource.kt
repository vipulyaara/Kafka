package com.kafka.data.feature.item

import com.kafka.data.api.ArchiveService
import com.kafka.data.dao.ItemDetailDao
import kotlinx.coroutines.withContext
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.debug
import javax.inject.Inject

class ItemDetailDataSource @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val itemDetailDao: ItemDetailDao,
    private val itemDetailMapper: ItemDetailMapper,
    private val archiveService: ArchiveService,
) {
    suspend fun updateItemDetail(contentId: String) = withContext(dispatchers.io) {
        itemDetailMapper.map(archiveService.getItemDetail(contentId)).let {
            debug { "Item detail is $it" }
            itemDetailDao.insert(it)
        }
    }
}
