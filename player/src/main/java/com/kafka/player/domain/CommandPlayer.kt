package com.kafka.player.domain

import com.data.base.AppCoroutineDispatchers
import com.data.base.Interactor
import com.data.base.extensions.debug
import com.kafka.data.dao.ItemDetailDao
import com.kafka.data.injection.ProcessLifetime
import com.kafka.player.exo.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommandPlayer @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    @ProcessLifetime private val processScope: CoroutineScope,
    private val itemDetailDao: ItemDetailDao,
    private val player: Player
) : Interactor<PlayerCommand>() {
    override val scope: CoroutineScope = processScope + dispatchers.io

    override suspend fun doWork(params: PlayerCommand) {
        debug { "player command invoked for $params" }
        when (params) {
            is PlayerCommand.Play -> {
                player.playCurrent()
            }
        }
    }
}
