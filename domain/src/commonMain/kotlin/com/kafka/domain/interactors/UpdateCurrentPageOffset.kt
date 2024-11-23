package com.kafka.domain.interactors

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.debug
import com.kafka.base.domain.Interactor
import com.kafka.data.dao.RecentTextDao
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateCurrentPageOffset(
    private val dispatchers: CoroutineDispatchers,
    private val recentTextDao: RecentTextDao,
) : Interactor<UpdateCurrentPageOffset.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            debug { "UpdateCurrentPageOffset: $params" }
            recentTextDao.insertOrUpdateCurrentPageOffset(params.fileId, params.currentPageOffset)
        }
    }

    data class Params(val fileId: String, val currentPageOffset: Int)
}
