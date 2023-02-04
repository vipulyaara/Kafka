package com.kafka.data.feature.item

import android.text.Html
import com.kafka.data.dao.FileDao
import com.kafka.data.entities.File.Companion.supportedFiles
import com.kafka.data.entities.ItemDetail
import com.kafka.data.model.item.ItemDetailResponse
import org.kafka.base.debug
import javax.inject.Inject

class ItemDetailMapper @Inject constructor(
    private val fileDao: FileDao,
    private val fileMapper: FileMapper
) {
    suspend fun map(from: ItemDetailResponse): ItemDetail {
        debug { "mapping item detail: ${from.files.joinToString { it.name }}" }
        return ItemDetail(
            itemId = from.metadata.identifier,
            language = from.metadata.licenseurl,
            title = from.metadata.title?.dismissUpperCase(),
            description = (from.metadata.description?.joinToString()?.format() ?: ""),
            creator = from.metadata.creator?.joinToString(),
            collection = from.metadata.collection?.joinToString(),
            mediaType = from.metadata.mediatype,
            files = from.files.filter { it.name.isNotEmpty() }.map { it.name },
            coverImage = from.findCoverImage(),
            metadata = from.metadata.collection
        ).also {
            insertFiles(from, it)
        }
    }

    private fun ItemDetailResponse.findCoverImage() = files.firstOrNull {
        it.name.startsWith("cover_1")
                && (it.name.endsWith("jpg") || it.name.endsWith("png"))
    }?.let { "https://archive.org/download/${metadata.identifier}/${it.name}" }
        ?: "https://archive.org/services/img/${metadata.identifier}"

    private suspend fun insertFiles(from: ItemDetailResponse, item: ItemDetail) {
        val files = from.files.map {
            fileMapper.map(
                file = it,
                itemId = from.metadata.identifier,
                itemTitle = item.title,
                prefix = from.dirPrefix(),
                localUri = fileDao.fileOrNull(it.name)?.localUri,
                coverImage = item.coverImage
            )
        }.filter { supportedFiles.contains(it.extension) }

        fileDao.insertAll(files)
    }
}

fun String?.format() = Html.fromHtml(this, 0)?.toString()

fun ItemDetailResponse.dirPrefix() = "https://$server$dir"

