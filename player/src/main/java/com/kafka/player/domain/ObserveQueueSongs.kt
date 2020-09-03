package com.kafka.player.domain

import com.data.base.AppCoroutineDispatchers
import com.data.base.SubjectInteractor
import com.kafka.data.dao.QueueDao
import com.kafka.data.entities.Song
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveQueueSongs @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val queueDao: QueueDao
) : SubjectInteractor<Unit, List<Song>>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override fun createObservable(params: Unit): Flow<List<Song>> {
        return queueDao.observeQueueSongs()
    }
}
