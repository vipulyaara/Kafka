package org.kafka.domain.interactors

import com.kafka.data.dao.RecentAudioDao
import com.sarahang.playback.core.PlaybackConnection
import com.sarahang.playback.core.apis.AudioDataSource
import org.kafka.analytics.logger.Analytics
import com.kafka.base.domain.Interactor
import javax.inject.Inject

/**
 * Resumes an album from the last played audio
 * or start from the beginning if last played audio is not found or in case of an error.
 * */
class ResumeAlbum @Inject constructor(
    private val playbackConnection: PlaybackConnection,
    private val recentAudioDao: RecentAudioDao,
    private val audioDataSource: AudioDataSource,
    private val analytics: Analytics
) : Interactor<String>() {

    override suspend fun doWork(params: String) {
        val audio = recentAudioDao.getByAlbumId(params)
        val files = audioDataSource.findAudiosByItemId(params)

        val index = files
            .map { it.id }
            .indexOf(audio?.fileId)
            .coerceAtLeast(0)

        analytics.log { playItem(itemId = params, index = index) }

        playbackConnection.playAlbum(params, index, audio?.currentTimestamp)
    }
}
