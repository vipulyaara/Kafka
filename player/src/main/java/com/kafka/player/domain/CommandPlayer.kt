package com.kafka.player.domain

import com.kafka.data.model.AppCoroutineDispatchers
import com.kafka.data.model.Interactor
import com.kafka.data.extensions.debug
import com.kafka.data.dao.QueueDao
import com.kafka.data.injection.ProcessLifetime
import com.kafka.player.playback.player.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Inject

class CommandPlayer @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    @ProcessLifetime private val processScope: CoroutineScope,
    private val queueDao: QueueDao,
    private val player: Player
) : Interactor<PlayerCommand>() {
    override val scope: CoroutineScope = processScope + dispatchers.io

    override suspend fun doWork(params: PlayerCommand) {
        debug { "player command invoked for $params" }
        when (params) {
            is PlayerCommand.Play -> {
                if (params.mediaId != null) {
                    queueDao.getQueueSongs().apply {
                        val position = indexOfFirst { it?.id == params.mediaId }
                        player.play(position)
                    }
                } else {
                    player.play()
                }
            }
            is PlayerCommand.ToggleCurrent -> {
                player.togglePlayPause()
            }
            is PlayerCommand.Next -> {
                player.next()
            }
            is PlayerCommand.Previous -> {
                player.previous()
            }
        }
    }
}
