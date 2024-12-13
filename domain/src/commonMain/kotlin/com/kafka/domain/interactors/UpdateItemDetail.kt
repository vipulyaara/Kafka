package com.kafka.domain.interactors

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.dao.FileDao
import com.kafka.data.dao.ItemDetailDao
import com.kafka.data.feature.item.ItemDetailDataSource
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateItemDetail(
    private val dispatchers: CoroutineDispatchers,
    private val repository: ItemDetailDataSource,
    private val itemDetailDao: ItemDetailDao,
    private val fileDao: FileDao
) : Interactor<UpdateItemDetail.Param, Unit>() {

    override suspend fun doWork(params: Param) {
        withContext(dispatchers.io) {
            val itemDetail = repository.updateItemDetail(params.contentId)
            val files = repository.updateFiles(params.contentId)

            itemDetailDao.insert(itemDetail)
            fileDao.insertAll(files)
        }
    }

    data class Param(val contentId: String)
}
