package com.kafka.player.domain

import com.kafka.data.dao.QueueDao
import com.kafka.data.injection.ProcessLifetime
import com.kafka.player.playback.player.Player
import kotlinx.coroutines.CoroutineScope
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.debug
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class CommandPlayer @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val queueDao: QueueDao,
    private val player: Player
) : Interactor<PlayerCommand>() {

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
