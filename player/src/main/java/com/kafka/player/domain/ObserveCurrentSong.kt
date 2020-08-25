package com.kafka.player.domain

import com.data.base.AppCoroutineDispatchers
import com.data.base.SubjectInteractor
import com.data.base.extensions.debug
import com.kafka.data.dao.QueueDao
import com.kafka.data.entities.Song
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveCurrentSong @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val queueDao: QueueDao
) : SubjectInteractor<Unit, CurrentSong>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override fun createObservable(params: Unit): Flow<CurrentSong> {
        return combine(
            queueDao.observeCurrentSongId(),
            queueDao.observePlayingStatus(),
            queueDao.observeSeekPosition()
        ) { currentSongId, isPlaying, seekPosition ->
            debug { "current item $currentSongId $isPlaying $seekPosition " }
            CurrentSong(currentSongId ?: "", isPlaying ?: false, seekPosition ?: 0,
                currentSongId?.let { queueDao.getSongById(it) } ?: Song())
        }
    }
}

data class CurrentSong(val currentSongId: String, val isPlaying: Boolean, val seekPosition: Long, val song: Song)
