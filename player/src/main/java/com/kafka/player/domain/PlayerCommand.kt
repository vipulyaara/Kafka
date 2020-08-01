package com.kafka.player.domain

sealed class PlayerCommand {
    data class Play(val itemId: String) : PlayerCommand()
    object ToggleCurrent : PlayerCommand()
}
