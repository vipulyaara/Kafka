package com.kafka.player.domain

import com.data.base.AppCoroutineDispatchers
import com.data.base.Interactor
import com.kafka.data.dao.ItemDetailDao
import com.kafka.data.entities.File
import com.kafka.data.entities.ItemDetail
import com.kafka.data.entities.mp3Files
import com.kafka.data.injection.ProcessLifetime
import com.kafka.player.exo.Player
import com.kafka.player.exo.model.MediaItem
import kotlinx.coroutines.CoroutineScope
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
        with(itemDetailDao.itemDetail(params.itemId)) {
            val playlist = mp3Files()?.map { it.asMediaItem(this) }
            player.addPlaylistItems(playlist!!)
        }
    }

    private fun File.asMediaItem(itemDetail: ItemDetail): MediaItem = MediaItem(
        id = playbackUrl!!,
        title = title!!,
        subtitle = creator ?: "",
        playbackUrl = playbackUrl!!,
        coverImageUrl = itemDetail.coverImage!!,
        duration = time
    )

    data class Param(val itemId: String)
}
