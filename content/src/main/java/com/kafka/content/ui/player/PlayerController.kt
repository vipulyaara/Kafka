package com.kafka.content.ui.player

import com.airbnb.epoxy.TypedEpoxyController
import com.kafka.content.*
import com.kafka.data.entities.mp3Files
import com.kafka.player.domain.PlayerAction
import com.kafka.player.domain.PlayerCommand
import com.kafka.player.domain.PlayerViewState
import kotlinx.coroutines.channels.Channel

class PlayerController : TypedEpoxyController<PlayerViewState>() {
    lateinit var playerActioner: Channel<PlayerAction>

    override fun buildModels(data: PlayerViewState?) {

        playerInfo {
            id("player_info")
            playerData(data?.playerData)
        }

        playerControls {
            id("player_controls")
            playerData(data?.playerData)
            playerCommandListener(object : PlayerCommandListener {
                override fun favoriteClick() {

                }

                override fun playClick() {
                    playerActioner.sendAction(PlayerAction.Command(PlayerCommand.ToggleCurrent))
                }

                override fun previousClick() {
                    playerActioner.sendAction(PlayerAction.Command(PlayerCommand.Previous))
                }

                override fun nextClick() {
                    playerActioner.sendAction(PlayerAction.Command(PlayerCommand.Next))
                }

            })
        }

        verticalSpacingLarge { id("spacing") }
        bottomsheetHandle { id("handle") }
        data?.itemDetail?.mp3Files()?.forEach {
            song {
                id(it.title)
                song(it)
                clickListener { _ ->
                    playerActioner.sendAction(
                        PlayerAction.Command(
                            PlayerCommand.Play(
                                data.itemDetail?.itemId!!, it.playbackUrl!!))) }
                }
            }
        }
    }

    interface PlayerCommandListener {
        fun favoriteClick()
        fun playClick()
        fun previousClick()
        fun nextClick()
    }
