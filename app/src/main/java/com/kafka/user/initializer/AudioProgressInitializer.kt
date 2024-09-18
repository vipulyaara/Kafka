package com.kafka.user.initializer

import com.kafka.data.dao.RecentAudioDao
import com.kafka.data.entities.RecentAudioItem
import com.sarahang.playback.core.PlaybackConnection
import com.sarahang.playback.core.albumId
import com.sarahang.playback.core.fileId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.kafka.base.AppInitializer
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.ProcessLifetime
import org.kafka.base.debug
import javax.inject.Inject

/**
 * An [AppInitializer] that updates the the currently playing audio.
 *
 * It listens to the [PlaybackConnection.playbackProgress] and updates the recent audio item
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
            playbackConnection.playbackProgress
                .filter { it.isPlaying }
                .buffer(1)
                .map { it.position + it.elapsed }
                .filter { it % 5000 < 1000 }
                .onEach { debug { "Updating recent audio position: $it" } }
                .distinctUntilChanged()
                .collectLatest { timestamp ->
                    val nowPlaying = playbackConnection.nowPlaying.value
                    nowPlaying.albumId?.let { albumId ->
                        updateRecentAudio(
                            albumId = albumId,
                            fileId = nowPlaying.fileId,
                            timestamp = timestamp
                        )
                    }
                }
        }
    }

    private suspend fun updateRecentAudio(albumId: String, fileId: String, timestamp: Long) {
        val audioItem = recentAudioDao.getByAlbumId(albumId)
        if (audioItem == null) {
            val audio = RecentAudioItem(
                fileId = fileId,
                albumId = albumId,
                currentTimestamp = timestamp,
                duration = 0 // duration is not available
            )
            recentAudioDao.insert(audio)
        } else {
            recentAudioDao.updateNowPlaying(
                albumId = albumId,
                fileId = fileId,
                currentTimestamp = timestamp
            )
        }
    }
}
