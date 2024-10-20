package com.kafka.data.feature.item

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.Service
import com.kafka.base.appService
import com.kafka.data.api.ArchiveService
import com.kafka.data.dao.FileDao
import com.kafka.data.entities.File
import com.kafka.data.entities.ItemDetail
import com.kafka.data.feature.SupabaseDb
import com.kafka.data.feature.decodeFiles
import com.kafka.data.model.item.Publishers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ItemDetailDataSource @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val fileDao: FileDao,
    private val itemDetailMapper: ItemDetailMapper,
    private val librivoxFileMapper: LibrivoxFileMapper,
    private val archiveService: ArchiveService,
    private val supabaseDb: SupabaseDb
) {
    suspend fun updateItemDetail(contentId: String): ItemDetail = withContext(dispatchers.io) {
        if (appService == Service.Archive) {
            itemDetailMapper.map(archiveService.getItemDetail(contentId))
        } else {
            try {
                supabaseDb.bookDetail.select {
                    filter { eq("book_id", contentId) }
                }.decodeSingle<ItemDetail>()
            } catch (e: Exception) {
                error("Item does not exist")
            }
        }
    }

    suspend fun updateFiles(itemDetail: ItemDetail, contentId: String): List<File> = withContext(dispatchers.io) {
        if (appService == Service.Supabase) {
            if (itemDetail.publishers.contains(Publishers.librivox) && contentId.startsWith("librivox_")) {
                val librivoxId = contentId.removePrefix("librivox_")
                val fileResponse = archiveService.getLibrivoxAudioTracks(librivoxId)
                librivoxFileMapper.map(fileResponse, itemDetail)
            } else {
                supabaseDb.files.select {
                    filter { eq("book_id", contentId) }
                }.decodeFiles()
                    .map { it.copy(localUri = fileDao.getOrNull(it.fileId)?.localUri) }
                    .filterNot { it.format == "image" }
            }
        } else {
            emptyList()
        }
    }
}
