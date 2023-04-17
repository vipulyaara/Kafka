package com.kafka.data.feature.item

import android.text.Html
import com.kafka.data.dao.FileDao
import com.kafka.data.dao.ItemDao
import com.kafka.data.entities.File.Companion.supportedExtensions
import com.kafka.data.entities.ItemDetail
import com.kafka.data.model.item.File
import com.kafka.data.model.item.ItemDetailResponse
import org.kafka.base.debug
import javax.inject.Inject

class ItemDetailMapper @Inject constructor(
    private val fileDao: FileDao,
    private val itemDao: ItemDao,
    private val fileMapper: FileMapper
) {
    suspend fun map(from: ItemDetailResponse): ItemDetail {
        debug { "mapping item detail: ${from.files.joinToString { it.name }}" }
        return ItemDetail(
            itemId = from.metadata.identifier,
            language = from.metadata.licenseurl,
            title = from.metadata.title?.dismissUpperCase(),
            description = (from.metadata.description?.joinToString()?.format() ?: ""),
            creator = from.metadata.creator?.joinToString()?.sanitizeForRoom(),
            collection = from.metadata.collection?.joinToString(),
            mediaType = from.metadata.mediatype,
            files = from.files.filter { it.fileId.isNotEmpty() }.map { it.fileId },
            coverImage = from.findCoverImage(),
            metadata = from.metadata.collection,
            primaryFile = from.files.primaryFile(from.metadata.mediatype)?.fileId,
            subject = from.metadata.subject
                ?.subList(0, from.metadata.subject!!.size.coerceAtMost(12))
                ?.filter { it.isNotEmpty() },
            rating = itemDao.getOrNull(from.metadata.identifier)?.rating
        ).also {
            insertFiles(from, it)
        }
    }

    private fun List<File>.primaryFile(mediaType: String?) =
        if (mediaType == "texts") getTextFile() else firstOrNull()

    private fun List<File>.getTextFile() =
        firstOrNull { it.name.extension() == "pdf" }
            ?: firstOrNull { it.name.extension() == "epub" }
            ?: firstOrNull { it.name.extension() == "txt" }

    private fun String.extension() = split(".").last()

    private fun ItemDetailResponse.findCoverImage() = files.firstOrNull {
        it.name.startsWith("cover_")
                && (it.name.endsWith("jpg") || it.name.endsWith("png"))
    }?.let { "https://archive.org/download/${metadata.identifier}/${it.name}" }
        ?: "https://archive.org/services/img/${metadata.identifier}"

    private suspend fun insertFiles(from: ItemDetailResponse, item: ItemDetail) {
        val files = from.files.map {
            fileMapper.map(
                file = it,
                item = item,
                prefix = from.dirPrefix(),
                localUri = fileDao.getOrNull(it.name)?.localUri
            )
        }.filter { supportedExtensions.contains(it.extension) }

        fileDao.insertAll(files)
    }
}

fun String?.format() = Html.fromHtml(this, 0)?.toString()

fun ItemDetailResponse.dirPrefix() = "https://$server$dir"

