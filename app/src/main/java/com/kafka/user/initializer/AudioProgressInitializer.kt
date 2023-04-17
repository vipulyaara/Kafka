package com.kafka.user.initializer

import android.app.Application
import com.kafka.data.AppInitializer
import com.kafka.data.dao.RecentAudioDao
import com.kafka.data.entities.RecentAudioItem
import com.kafka.data.injection.ProcessLifetime
import com.sarahang.playback.core.PlaybackConnection
import com.sarahang.playback.core.fileId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.debug
import javax.inject.Inject

class AudioProgressInitializer @Inject constructor(
    private val playbackConnection: PlaybackConnection,
    private val dispatchers: AppCoroutineDispatchers,
    private val recentAudioDao: RecentAudioDao,
    @ProcessLifetime private val coroutineScope: CoroutineScope,
) : AppInitializer {
    override fun init(application: Application) {
        coroutineScope.launch(dispatchers.io) {
            playbackConnection.playbackProgress
                .filter { it.position % 5L == 0L && it.position != 0L }
                .collectLatest { timestamp ->
                    debug { "Updating progress for $timestamp" }
                    playbackConnection.nowPlaying.value.fileId?.let { fileId ->
                        val audioItem = recentAudioDao.get(fileId)
                        if (audioItem == null) {
                            val audio = RecentAudioItem(fileId, timestamp.position, timestamp.total)
                            recentAudioDao.insert(audio)
                        } else {
                            recentAudioDao.updateTimestamp(fileId, timestamp.position)
                        }
                    }
                }
        }
    }
}
