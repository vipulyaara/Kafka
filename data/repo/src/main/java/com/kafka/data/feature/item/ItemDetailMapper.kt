package com.kafka.data.feature.item

import android.text.Html
import com.kafka.data.dao.FileDao
import com.kafka.data.dao.ItemDao
import com.kafka.data.entities.File.Companion.supportedExtensions
import com.kafka.data.entities.ItemDetail
import com.kafka.data.model._mediaTypeText
import com.kafka.data.model.item.File
import com.kafka.data.model.item.ItemDetailResponse
import org.kafka.base.debug
import javax.inject.Inject

class ItemDetailMapper @Inject constructor(
    private val fileDao: FileDao,
    private val itemDao: ItemDao,
    private val fileMapper: FileMapper,
) {
    suspend fun map(from: ItemDetailResponse): ItemDetail {
        debug { "mapping item detail: ${from.files.joinToString { it.name }}" }

        return ItemDetail(
            itemId = from.metadata.identifier,
            language = from.metadata.languages?.map { it.split(";") }?.flatten()?.joinToString(),
            title = from.metadata.title?.firstOrNull()?.dismissUpperCase(),
            description = from.metadata.description?.joinToString()?.format() ?: "",
            creator = from.metadata.creator?.take(5)?.joinToString()?.sanitizeForRoom(),
            collection = from.metadata.collection?.joinToString(),
            mediaType = from.metadata.mediatype,
            files = from.files.filter { it.fileId.isNotEmpty() }.map { it.fileId },
            coverImage = from.findCoverImage(),
            metadata = from.metadata.collection,
            primaryFile = from.files.primaryFile(from.metadata.mediatype)?.fileId,
            subject = run {
                val subjects = from.metadata.subject?.map { it.split(";") }?.flatten()
                subjects?.subList(0, subjects.size.coerceAtMost(12))
                    ?.map { it.substring(0, it.length.coerceAtMost(50)) }
                    ?.map { it.trim() }
                    ?.filter { it.isNotEmpty() }
            },
            rating = itemDao.getOrNull(from.metadata.identifier)?.rating,
            isAccessRestricted = from.metadata.accessRestrictedItem
        ).also {
            insertFiles(from, it)
        }
    }

    private fun List<File>.primaryFile(mediaType: String?) =
        if (mediaType == _mediaTypeText) getTextFile() else firstOrNull()

    private fun List<File>.getTextFile() = firstOrNull { it.name.extension() == "pdf" }

    private fun String.extension() = split(".").last()

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
                localUri = fileDao.getOrNull(file.name)?.localUri,
            )
        }.filter { supportedExtensions.contains(it.extension) }

        fileDao.insertAll(files)
    }
}

fun String?.format() = Html.fromHtml(this, 0)?.toString()

fun ItemDetailResponse.dirPrefix() = "https://$server$dir"
