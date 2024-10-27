package com.kafka.domain.observers

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.dao.FileDao
import com.kafka.data.dao.ItemDetailDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@Inject
class ShouldAutoDownload(
    private val dispatchers: CoroutineDispatchers,
    private val itemDetailDao: ItemDetailDao,
    private val fileDao: FileDao,
) : SubjectInteractor<ShouldAutoDownload.Param, Boolean>() {

    override fun createObservable(params: Param): Flow<Boolean> {
        return itemDetailDao.observeItemDetail(params.itemId)
            .map {
                val file = fileDao.getOrNull(params.fileId)
                shouldAutoDownload && (file?.size ?: 0) < 10000000L
            }.flowOn(dispatchers.io)
    }

    data class Param(val itemId: String, val fileId: String)
}

const val shouldAutoDownload = false
