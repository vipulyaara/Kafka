package org.kafka.domain.interactors

import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class UpdateCurrentPage @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<UpdateCurrentPage.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
//            textFileDao.updateCurrentPage(params.fileId, params.currentPage)
        }
    }

    data class Params(val fileId: String, val currentPage: Int)
}
