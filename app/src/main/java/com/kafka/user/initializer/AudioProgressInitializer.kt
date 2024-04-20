package com.kafka.user.initializer

import android.app.Application
import com.kafka.data.dao.RecentAudioDao
import com.kafka.data.entities.RecentAudioItem
import com.sarahang.playback.core.PlaybackConnection
import com.sarahang.playback.core.album
import com.sarahang.playback.core.fileId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kafka.base.AppInitializer
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.ProcessLifetime
import org.kafka.base.debug
import javax.inject.Inject

class AudioProgressInitializer @Inject constructor(
    private val playbackConnection: PlaybackConnection,
    private val dispatchers: CoroutineDispatchers,
    private val recentAudioDao: RecentAudioDao,
    @ProcessLifetime private val coroutineScope: CoroutineScope,
) : AppInitializer {

    override fun init(application: Application) {
        coroutineScope.launch(dispatchers.io) {
            playbackConnection.nowPlaying
//                .filter { it.position % 5L == 0L && it.position != 0L }
                .collectLatest { timestamp ->
                    debug { "Updating progress for $timestamp" }

                    val albumId = playbackConnection.nowPlaying.value.album

                    playbackConnection.nowPlaying.value.fileId?.let { fileId ->
                        val audioItem = recentAudioDao.get(fileId)
                        if (audioItem == null) {
                            val audio = RecentAudioItem(
                                fileId = fileId,
                                albumId = albumId,
                                currentTimestamp = 0,
                                duration = 0
                            )
                            recentAudioDao.insert(audio)
                        } else {
                            recentAudioDao.updateTimestamp(fileId, 0)
                        }
                    }
                }
        }
    }
}
