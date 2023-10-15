package org.kafka.domain.interactors

import com.kafka.data.dao.RecentTextDao
import kotlinx.coroutines.withContext
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class UpdateCurrentPage @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val recentTextDao: RecentTextDao,
) : Interactor<UpdateCurrentPage.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            recentTextDao.updateCurrentPage(params.fileId, params.currentPage)
        }
    }

    data class Params(val fileId: String, val currentPage: Int)
}
