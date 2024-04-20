package org.kafka.domain.interactors.recent

import com.kafka.data.dao.ItemDetailDao
import com.kafka.data.dao.RecentAudioDao
import com.sarahang.playback.core.PlaybackConnection
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class PlayAudio @Inject constructor(
    private val playbackConnection: PlaybackConnection,
    private val recentAudioDao: RecentAudioDao,
    private val itemDetailDao: ItemDetailDao
) : Interactor<String>() {

    override suspend fun doWork(params: String) {
        val item = itemDetailDao.get(params)
//        val audio = recentAudioDao.getByAlbumId(params)

        val index = item.files?.indexOf("audio?.fileId")?.coerceAtLeast(0) ?: 0

        playbackConnection.playAlbum(params, index)
    }
}
