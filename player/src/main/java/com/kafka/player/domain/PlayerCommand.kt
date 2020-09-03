package com.kafka.player.domain

sealed class PlayerCommand {
    data class Play(val itemId: String, val mediaId: String? = null) : PlayerCommand()
    object Previous : PlayerCommand()
    object Next : PlayerCommand()
    object ToggleCurrent : PlayerCommand()
}
