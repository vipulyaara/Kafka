package com.kafka.user.initializer

import com.kafka.data.dao.RecentAudioDao
import com.kafka.data.entities.RecentAudioItem
import com.sarahang.playback.core.PlaybackConnection
import com.sarahang.playback.core.albumId
import com.sarahang.playback.core.fileId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kafka.base.AppInitializer
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.ProcessLifetime
import org.kafka.base.debug
import javax.inject.Inject

/**
 * An [AppInitializer] that updates the the currently playing audio.
 *
 * It listens to the [PlaybackConnection.nowPlaying] and updates the recent audio item
 * so that it can be played from last item when the user plays the album again.
 */
class AudioProgressInitializer @Inject constructor(
    private val playbackConnection: PlaybackConnection,
    private val dispatchers: CoroutineDispatchers,
    private val recentAudioDao: RecentAudioDao,
    @ProcessLifetime private val coroutineScope: CoroutineScope,
) : AppInitializer {

    override fun init() {
        coroutineScope.launch(dispatchers.io) {
            playbackConnection.nowPlaying
                .collectLatest { timestamp ->
                    debug { "Updating progress for $timestamp" }

                    playbackConnection.nowPlaying.value.albumId?.let { albumId ->
                        playbackConnection.nowPlaying.value.fileId.let { fileId ->
                            val audioItem = recentAudioDao.getByAlbumId(albumId)
                            if (audioItem == null) {
                                val audio = RecentAudioItem(fileId = fileId, albumId = albumId)
                                recentAudioDao.insert(audio)
                            } else {
                                recentAudioDao.updateNowPlaying(albumId = albumId, fileId = fileId)
                            }
                        }
                    }
                }
        }
    }
}
