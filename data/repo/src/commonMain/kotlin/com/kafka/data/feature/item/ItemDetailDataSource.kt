package com.kafka.data.feature.item

import com.kafka.base.CoroutineDispatchers
import com.kafka.data.api.ArchiveService
import com.kafka.data.entities.File
import com.kafka.data.entities.ItemDetail
import com.kafka.data.feature.Supabase
import com.kafka.data.model.item.Publishers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class ItemDetailDataSource(
    private val dispatchers: CoroutineDispatchers,
    private val librivoxFileMapper: LibrivoxFileMapper,
    private val archiveService: ArchiveService,
    private val supabase: Supabase
) {
    suspend fun updateItemDetail(contentId: String): ItemDetail = withContext(dispatchers.io) {
        supabase.itemDetail.select {
            filter { ItemDetail::itemId eq contentId }
        }.decodeSingle<ItemDetail>()
    }

    suspend fun updateFiles(itemDetail: ItemDetail, contentId: String): List<File> =
        withContext(dispatchers.io) {
            if (itemDetail.publishers.contains(Publishers.librivox)
                && contentId.startsWith("librivox_")
            ) {
                val librivoxId = contentId.removePrefix("librivox_")
                val fileResponse = archiveService.getLibrivoxAudioTracks(librivoxId)
                librivoxFileMapper.map(fileResponse, itemDetail)
            } else {
                supabase.files.select {
                    filter { File::itemId eq contentId }
                }.decodeList()
            }
        }
}
