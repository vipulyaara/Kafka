package com.kafka.player.domain

import com.data.base.AppCoroutineDispatchers
import com.data.base.Interactor
import com.data.base.extensions.debug
import com.kafka.data.dao.ItemDetailDao
import com.kafka.data.entities.firstAudio
import com.kafka.data.injection.ProcessLifetime
import com.kafka.player.timber.MusicUtils
import com.kafka.player.timber.models.Song
import com.kafka.player.timber.playback.players.SongPlayer
import com.kafka.ui.actions.PlayerCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.plus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommandPlayer @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    @ProcessLifetime private val processScope: CoroutineScope,
    private val itemDetailDao: ItemDetailDao,
    private val songPlayer: SongPlayer
) : Interactor<PlayerCommand>() {
    override val scope: CoroutineScope = processScope + dispatchers.io

    override suspend fun doWork(params: PlayerCommand) {
        debug { "player command invoked for $params" }
        when (params) {
            is PlayerCommand.Play -> {
                getItemDetail(params.itemId).apply {
                    MusicUtils.songUri = firstAudio()?.playbackUrl ?: ""
                    songPlayer.playSong(
                        Song(
                            id = itemId.hashCode().toLong(),
                            title = title ?: "",
                            artist = firstAudio()?.title ?: ""
                        )
                    )
                }
            }
            PlayerCommand.ToggleCurrent -> {
                songPlayer.playingState.collect {
                    if (it) songPlayer.pause() else songPlayer.playSong()
                }
            }
        }
    }

    private fun getItemDetail(itemId: String) = itemDetailDao.itemDetail(itemId)

}
