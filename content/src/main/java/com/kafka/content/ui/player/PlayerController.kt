package com.kafka.content.ui.player

import com.airbnb.epoxy.TypedEpoxyController
import com.kafka.content.*
import com.kafka.data.entities.mp3Files
import com.kafka.player.domain.CommandPlayer
import com.kafka.player.domain.PlayerAction
import com.kafka.player.domain.PlayerCommand
import com.kafka.player.domain.PlayerViewState
import com.kafka.player.playback.Player
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

class PlayerController @Inject constructor(
    private val player: Player,
    private val commandPlayer: CommandPlayer
) : TypedEpoxyController<PlayerViewState>() {

    lateinit var playerActioner: Channel<PlayerAction>

    override fun buildModels(data: PlayerViewState?) {

        playerInfo {
            id("player_info")
            mediaItem(data?.mediaItem)
        }

        playerControls {
            id("player_controls")
            mediaItem(data?.mediaItem)
            isFollowed(data?.isFavorite)
            playerCommandListener(object : PlayerCommandListener {
                override fun favoriteClick() {
                    playerActioner.sendAction(PlayerAction.FavoriteClick)
                }

                override fun playClick() {
                    player.togglePlayPause()
                    playerActioner.sendAction(PlayerAction.Command(PlayerCommand.ToggleCurrent))
                }

                override fun previousClick() {
                    player.previous()
                    playerActioner.sendAction(PlayerAction.Command(PlayerCommand.Previous))
                }

                override fun nextClick() {
                    player.next()
                    playerActioner.sendAction(PlayerAction.Command(PlayerCommand.Next))
                }

                override fun seekTo(position: Int) {
                    player.seekTo(position.toLong())
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
                        PlayerAction.Command(PlayerCommand.Play(data.itemDetail?.itemId!!, it.playbackUrl!!))
                    )
                }
            }
        }
    }
}

interface PlayerCommandListener {
    fun favoriteClick()
    fun playClick()
    fun previousClick()
    fun nextClick()
    fun seekTo(position: Int)
}
