package com.kafka.player.domain

import com.data.base.AppCoroutineDispatchers
import com.data.base.Interactor
import com.kafka.data.dao.ItemDetailDao
import com.kafka.data.entities.File
import com.kafka.data.entities.ItemDetail
import com.kafka.data.entities.Song
import com.kafka.data.entities.mp3Files
import com.kafka.data.injection.ProcessLifetime
import com.kafka.player.playback.player.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.plus
import javax.inject.Inject

class AddPlaylistItems @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    @ProcessLifetime private val processScope: CoroutineScope,
    private val itemDetailDao: ItemDetailDao,
    private val player: Player
) : Interactor<AddPlaylistItems.Param>() {
    override val scope: CoroutineScope = processScope + dispatchers.io

    override suspend fun doWork(params: Param) {
        val itemDetail = itemDetailDao.itemDetail(params.itemId)
        val playlist = itemDetail.mp3Files()?.map { it.asSong(itemDetail) }
            ?.filter { it.title.isNotEmpty() }?.distinctBy { it.title } ?: emptyList()
        player.setQueue(playlist)
        delay(1000)
        player.play(0)
    }

    data class Param(val itemId: String)
}

fun File.asSong(itemDetail: ItemDetail): Song = Song(
    id = playbackUrl!!,
    itemId = itemDetail.itemId,
    title = title ?: "",
    subtitle = arrayOf(itemDetail.title, creator).joinToString(" $bulletSymbol "),
    playbackUrl = playbackUrl!!,
    coverImage = itemDetail.coverImage!!,
    duration = time.toInt()
)

const val bulletSymbol = " • "
