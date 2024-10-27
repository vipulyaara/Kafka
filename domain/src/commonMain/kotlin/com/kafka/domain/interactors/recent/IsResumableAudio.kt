package com.kafka.domain.interactors.recent

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.dao.RecentAudioDao
import com.sarahang.playback.core.apis.AudioDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

/**
 * Checks if the item has been played before.
 * It does not explicitly check if the item is an audio item but if it exists in recent audios then it must be an audio.
 * */
@Inject
class IsResumableAudio(
    private val dispatchers: CoroutineDispatchers,
    private val recentAudioDao: RecentAudioDao,
    private val audioDataSource: AudioDataSource
) : SubjectInteractor<IsResumableAudio.Params, Boolean>() {

    override fun createObservable(params: Params): Flow<Boolean> {
        return recentAudioDao.observeByAlbumId(params.itemId).map { recentAudio ->
            val files = audioDataSource.findAudiosByItemId(params.itemId)

            files.map { it.id }.indexOf(recentAudio?.fileId) > 0
                    || (recentAudio?.currentTimestamp ?: 0) > 0
        }.flowOn(dispatchers.io)
    }

    data class Params(val itemId: String)

}
