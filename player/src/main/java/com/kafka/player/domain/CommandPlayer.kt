package com.kafka.player.domain

import com.data.base.AppCoroutineDispatchers
import com.data.base.Interactor
import com.data.base.extensions.debug
import com.kafka.data.detail.ItemDetailRepository
import com.kafka.data.entities.firstAudio
import com.kafka.data.injection.ProcessLifetime
import com.kafka.player.timber.MusicUtils
import com.kafka.player.timber.models.Song
import com.kafka.player.timber.playback.players.SongPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommandPlayer @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    @ProcessLifetime private val processScope: CoroutineScope,
    private val itemDetailRepository: ItemDetailRepository,
    private val songPlayer: SongPlayer
) : Interactor<CommandPlayer.Command>() {
    override val scope: CoroutineScope = processScope + dispatchers.io

    override suspend fun doWork(params: Command) {
        debug { "player command invoked for $params" }
        when (params) {
            is Command.Play -> {
                getItemDetail(params.itemId).apply {
                    MusicUtils.songUri = firstAudio() ?: ""
                    songPlayer.playSong(
                        Song(
                            id = itemId.hashCode().toLong(),
                            title = title ?: "",
                            artist = creator ?: ""
                        )
                    )
                }
            }
            Command.Toggle -> {
                songPlayer.onPlayingState { isPlaying ->
                    if (isPlaying) songPlayer.pause() else songPlayer.playSong()
                }
            }
        }
    }

    private fun getItemDetail(itemId: String) = itemDetailRepository.getItemDetail(itemId)

    sealed class Command {
        data class Play(val itemId: String) : Command()
        object Toggle : Command()
    }
}
