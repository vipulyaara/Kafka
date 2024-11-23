package com.kafka.domain.interactors

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.debug
import com.kafka.base.domain.Interactor
import com.kafka.data.dao.RecentTextDao
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class GetLastPageOffset(
    private val dispatchers: CoroutineDispatchers,
    private val recentTextDao: RecentTextDao,
) : Interactor<GetLastPageOffset.Params, Int>() {

    override suspend fun doWork(params: Params): Int = withContext(dispatchers.io) {
        debug { "GetLastPageOffset: $params" }
        return@withContext recentTextDao.getOrNull(params.fileId)?.currentPageOffset ?: 0
    }

    data class Params(val fileId: String)
}
