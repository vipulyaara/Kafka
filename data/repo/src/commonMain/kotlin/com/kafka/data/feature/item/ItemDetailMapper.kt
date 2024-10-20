package com.kafka.data.feature.item

import androidx.core.text.HtmlCompat
import com.kafka.base.debug
import com.kafka.data.dao.FileDao
import com.kafka.data.entities.File.Companion.supportedExtensions
import com.kafka.data.entities.ItemDetail
import com.kafka.data.model.MediaType
import com.kafka.data.model.item.ItemDetailResponse
import javax.inject.Inject

class ItemDetailMapper @Inject constructor(
    private val fileDao: FileDao,
    private val fileMapper: FileMapper,
) {
    suspend fun map(from: ItemDetailResponse): ItemDetail {
        debug { "mapping item detail: ${from.files.joinToString { it.name }}" }

        return ItemDetail(
            itemId = from.metadata.identifier,
            languages = from.metadata.languages,
            title = from.metadata.title?.firstOrNull()?.dismissUpperCase().orEmpty(),
            description = from.metadata.description?.joinToString()?.format() ?: "",
            creators = from.metadata.creator?.map { it.sanitizeForRoom() }?.take(5),
            collections = from.metadata.collection,
            mediaType = MediaType.from(from.metadata.mediatype),
            coverImage = from.findCoverImage(),
            subject = run {
                val subjects = from.metadata.subject?.map { it.split(";") }?.flatten()
                subjects?.subList(0, subjects.size.coerceAtMost(12))
                    ?.map { it.substring(0, it.length.coerceAtMost(50)) }
                    ?.map { it.trim() }
                    ?.filter { it.isNotEmpty() }
            },
            rating = 0.0,
            isAccessRestricted = from.metadata.accessRestrictedItem
        ).also {
            insertFiles(from, it)
        }
    }

    private fun ItemDetailResponse.findCoverImage(): String {
        val coverFile = files.firstOrNull {
            it.name.startsWith("cover_") && (it.name.endsWith("jpg") || it.name.endsWith("png"))
        }

        return if (coverFile != null) {
            "https://archive.org/download/${metadata.identifier}/${coverFile.name}"
        } else {
            "https://archive.org/services/img/${metadata.identifier}"
        }
    }

    private suspend fun insertFiles(from: ItemDetailResponse, item: ItemDetail) {
        val files = from.files.map { file ->
            fileMapper.map(
                file = file,
                item = item,
                prefix = from.dirPrefix(),
                localUri = fileDao.getOrNull(file.fileId)?.localUri,
            )
        }.filter { supportedExtensions.contains(it.extension) }

        fileDao.insertAll(files)
    }
}

fun String?.format() = HtmlCompat.fromHtml(this.orEmpty(), 0).toString()

fun ItemDetailResponse.dirPrefix() = "https://$server$dir"
