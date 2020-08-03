package com.kafka.content.ui.player

import com.airbnb.epoxy.TypedEpoxyController
import com.kafka.content.*
import com.kafka.data.entities.mp3Files
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

        verticalSpacingLarge { id("spacing") }
        verticalSpacingLarge { id("spacing2") }
        bottomsheetHandle {id("handle") }
        data?.itemDetail?.mp3Files()?.forEach {
            song {
                id(it.title)
                song(it)
            }
        }
    }
}
