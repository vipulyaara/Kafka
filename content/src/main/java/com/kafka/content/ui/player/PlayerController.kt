package com.kafka.content.ui.player

import com.airbnb.epoxy.TypedEpoxyController
import com.kafka.content.playerControls
import com.kafka.content.playerInfo
import com.kafka.player.domain.PlayerViewState

class PlayerController : TypedEpoxyController<PlayerViewState>() {
    override fun buildModels(data: PlayerViewState?) {

        playerInfo {
            id("player_info")
            playerData(data?.playerData)
        }

        playerControls {
            id("player_controls")
        }
    }
}
