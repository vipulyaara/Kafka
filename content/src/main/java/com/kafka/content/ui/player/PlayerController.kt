package com.kafka.content.ui.player

import com.airbnb.epoxy.TypedEpoxyController
import com.kafka.content.*
import com.kafka.player.domain.PlayerAction
import com.kafka.player.domain.PlayerCommand
import com.kafka.player.domain.PlayerViewState
import com.kafka.player.playback.player.Player
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

class PlayerController @Inject constructor(
    private val player: Player
) : TypedEpoxyController<PlayerViewState>() {
    lateinit var playerActioner: Channel<PlayerAction>

    override fun buildModels(data: PlayerViewState?) {

        playerInfo {
            id("player_info")
            song(data?.currentSong?.song)
        }

        playerControls {
            id("player_controls")
            song(data?.currentSong?.song)
            isPlaying(data?.currentSong?.isPlaying)
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
        data?.queueSongs?.forEach {
            song {
                id(it.id)
                isPlaying(it.id == data.currentSong?.currentSongId)
                song(it)
                clickListener { _ ->
                    playerActioner.sendAction(
                        PlayerAction.Command(PlayerCommand.Play(data.itemDetail?.itemId!!, it.playbackUrl))
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
