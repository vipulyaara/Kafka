package com.kafka.user.initializer

import android.app.Application
import com.sarahang.playback.core.PlaybackConnection
import com.sarahang.playback.core.fileId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kafka.base.AppInitializer
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.ProcessLifetime
import org.kafka.domain.interactors.UpdateRecentItem
import org.kafka.domain.observers.ObserveRecentItems
import javax.inject.Inject

class AudioItemInitializer @Inject constructor(
    private val playbackConnection: PlaybackConnection,
    private val dispatchers: CoroutineDispatchers,
    private val updateRecentItem: UpdateRecentItem,
    private val observeRecentItems: ObserveRecentItems,
    @ProcessLifetime private val coroutineScope: CoroutineScope,
) : AppInitializer {

    override fun init(application: Application) {
        coroutineScope.launch(dispatchers.io) {
            playbackConnection.nowPlaying
                .collectLatest { nowPlaying ->
                    nowPlaying.fileId?.let { fileId ->

                    }
                }
        }
    }
}
