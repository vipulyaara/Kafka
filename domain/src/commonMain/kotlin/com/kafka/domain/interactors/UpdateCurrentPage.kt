package com.kafka.domain.interactors

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.debug
import com.kafka.base.domain.Interactor
import com.kafka.data.dao.RecentTextDao
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateCurrentPage(
    private val dispatchers: CoroutineDispatchers,
    private val recentTextDao: RecentTextDao,
) : Interactor<UpdateCurrentPage.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            debug { "UpdateCurrentPage: $params" }
            recentTextDao.insertOrUpdateCurrentPage(params.fileId, params.currentPage)
        }
    }

    data class Params(val fileId: String, val currentPage: Int)
}
