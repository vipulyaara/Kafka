package org.kafka.domain.interactors.recent

import com.kafka.data.dao.RecentAudioDao
import com.sarahang.playback.core.apis.AudioDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

class IsResumableAudio @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val recentAudioDao: RecentAudioDao,
    private val audioDataSource: AudioDataSource
) : SubjectInteractor<IsResumableAudio.Params, Boolean>() {

    override fun createObservable(params: Params): Flow<Boolean> {
        return recentAudioDao.observeByAlbumId(params.albumId).map { recentAudio ->
            val files = audioDataSource.findAudiosByItemId(params.albumId)

            files.map { it.id }.indexOf(recentAudio?.fileId) > 0
        }.flowOn(dispatchers.io)
    }

    data class Params(val albumId: String)

}
