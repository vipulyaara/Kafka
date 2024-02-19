package org.kafka.domain.interactors

import com.kafka.data.dao.RecentAudioDao
import kotlinx.coroutines.withContext
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class UpdateCurrentTimestamp @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val recentAudioDao: RecentAudioDao,
) : Interactor<UpdateCurrentTimestamp.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            recentAudioDao.updateTimestamp(params.fileId, params.timestamp)
        }
    }

    data class Params(val fileId: String, val timestamp: Long)
}
