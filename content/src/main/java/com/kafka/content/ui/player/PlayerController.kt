package com.kafka.content.ui.player

import androidx.databinding.BindingAdapter
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.lottie.LottieAnimationView
import com.data.base.extensions.debug
import com.kafka.content.*
import com.kafka.player.domain.PlayerAction
import com.kafka.player.domain.PlayerCommand
import com.kafka.player.domain.PlayerViewState
import com.kafka.player.domain.PlayingState
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
            debug { "state ${data?.currentSong?.playingState}" }
            id("player_controls ${data?.currentSong?.playingState}")
            song(data?.currentSong?.song)
            isPlaying(data?.currentSong?.playingState)
            isFollowed(data?.isFavorite)
            playerCommandListener(object : PlayerCommandListener {
                override fun favoriteClick() {
                    playerActioner.sendAction(PlayerAction.FavoriteClick)
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
                isPlaying(data.currentSong?.playingState == PlayingState.Play)
                isCurrent(it.id == data.currentSong?.currentSongId)
                song(it)
                clickListener { _ ->
                    playerActioner.sendAction(
                        PlayerAction.Command(PlayerCommand.Play(it.playbackUrl))
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

@BindingAdapter("playIcon")
fun LottieAnimationView.playIcon(state: PlayingState?) {
    debug { "state is $state" }
    when (state) {
        PlayingState.Play -> setAnimation(R.raw.loader_2)
        PlayingState.Pause -> setAnimation(R.raw.loader_2)
        else -> setAnimation(R.raw.loader_3)
    }
    playAnimation()
}
