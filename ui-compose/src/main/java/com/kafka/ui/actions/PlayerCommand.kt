package com.kafka.ui.actions

sealed class PlayerCommand {
    data class Play(val itemId: String) : PlayerCommand()
    object ToggleCurrent : PlayerCommand()
}
