package com.kafka.player.domain

import com.data.base.AppCoroutineDispatchers
import com.data.base.Interactor
import com.data.base.extensions.debug
import com.kafka.data.detail.ItemDetailRepository
import com.kafka.data.entities.ItemDetail
import com.kafka.data.entities.filterMp3
import com.kafka.data.injection.ProcessLifetime
import com.kafka.player.playback.MediaResource
import com.kafka.player.playback.Playback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Inject

class CommandPlayer @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    @ProcessLifetime private val processScope: CoroutineScope,
    private val itemDetailRepository: ItemDetailRepository,
    private val playback: Playback
) : Interactor<CommandPlayer.Command>() {

    override val scope: CoroutineScope = processScope + dispatchers.io

    override suspend fun doWork(params: Command) {
        debug { "player command invoked for $params" }
        when (params) {
            is Command.Play -> {
                playback.play(getItemDetail(params.itemId).asMediaResource(), true)
            }
        }
    }

    private fun getItemDetail(itemId: String) = itemDetailRepository.getItemDetail(itemId)

    private fun ItemDetail.asMediaResource() = MediaResource(
        mediaId = "new",
        mediaUrl = files?.filterMp3()?.firstOrNull()?.playbackUrl,
        queueId = 0,
        headData = null
    )

    sealed class Command {
        data class Play(val itemId: String) : Command()
    }
}
