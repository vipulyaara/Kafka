package com.kafka.player.domain

import com.kafka.data.dao.ItemDetailDao
import com.kafka.data.entities.File
import com.kafka.data.entities.ItemDetail
import com.kafka.data.entities.Song
import com.kafka.data.entities.mp3Files
import com.kafka.player.playback.player.Player
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class AddPlaylistItems @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val itemDetailDao: ItemDetailDao,
    private val player: Player
) : Interactor<AddPlaylistItems.Param>() {

    override suspend fun doWork(params: Param) {
        withContext(dispatchers.io) {
            val itemDetail = itemDetailDao.itemDetail(params.itemId)
            val playlist = itemDetail.mp3Files()?.map { it.asSong(itemDetail) }
                ?.filter { it.title.isNotEmpty() }?.distinctBy { it.title } ?: emptyList()
            player.setQueue(playlist)
            delay(1000)
            player.play(0)
        }
    }

    data class Param(val itemId: String)
}

fun File.asSong(itemDetail: ItemDetail): Song = Song(
    id = playbackUrl!!,
    itemId = itemDetail.itemId,
    title = title.orEmpty(),
    creator = creator.orEmpty(),
    playbackUrl = playbackUrl!!,
    coverImage = itemDetail.coverImage!!,
    duration = time.toInt()
)

