package org.kafka.domain.observers

import com.kafka.data.dao.FileDao
import com.kafka.data.dao.ItemDetailDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

class ShouldAutoDownload @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val itemDetailDao: ItemDetailDao,
    private val fileDao: FileDao,
) : SubjectInteractor<ShouldAutoDownload.Param, Boolean>() {

    override fun createObservable(params: Param): Flow<Boolean> {
        return itemDetailDao.observeItemDetail(params.itemId)
            .map { itemDetail ->
                val file = fileDao.getOrNull(itemDetail?.primaryFile.orEmpty())
                (file?.size ?: 0) < 10000000L
            }.flowOn(dispatchers.io)
    }

    data class Param(val itemId: String)
}
