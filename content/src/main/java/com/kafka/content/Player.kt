package com.kafka.content

import com.data.base.AppCoroutineDispatchers
import com.kafka.player.domain.CommandPlayer
import com.kafka.player.domain.PlayerCommand
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealPlayer @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val commandPlayer: CommandPlayer
): Player {
    override fun command(playerCommand: PlayerCommand) {
        commandPlayer(playerCommand)
    }
}

interface Player {
    fun command(playerCommand: PlayerCommand)
}
