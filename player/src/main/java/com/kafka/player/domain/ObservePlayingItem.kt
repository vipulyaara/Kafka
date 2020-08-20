package com.kafka.player.domain

import com.data.base.AppCoroutineDispatchers
import com.data.base.SubjectInteractor
import com.data.base.extensions.debug
import com.kafka.player.playback.Player
import com.kafka.player.playback.model.MediaItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObservePlayingItem @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val player: Player
) : SubjectInteractor<Unit, MediaItem?>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override fun createObservable(params: Unit): Flow<MediaItem?> {
        return player.callbacks.currentItemFlow.onEach {
            debug { "media item changed $it" }
        }
    }
}
