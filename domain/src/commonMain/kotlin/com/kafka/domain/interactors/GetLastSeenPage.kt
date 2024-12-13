package com.kafka.domain.interactors

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.debug
import com.kafka.base.domain.Interactor
import com.kafka.data.dao.RecentTextDao
import com.kafka.data.entities.RecentTextItem
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class GetLastSeenPage(
    private val dispatchers: CoroutineDispatchers,
    private val recentTextDao: RecentTextDao,
) : Interactor<GetLastSeenPage.Params, Int>() {

    override suspend fun doWork(params: Params): Int = withContext(dispatchers.io) {
        debug { "GetLastSteenPage: $params" }
        return@withContext recentTextDao.getOrNull(params.fileId)?.currentPage ?: 0
    }

    data class Params(val fileId: String)
}