package com.kafka.data.feature.item

import android.text.Html
import com.kafka.data.dao.FileDao
import com.kafka.data.entities.File
import com.kafka.data.entities.File.Companion.supportedFiles
import com.kafka.data.entities.ItemDetail
import com.kafka.data.entities.isMp3
import com.kafka.data.entities.isPdf
import com.kafka.data.entities.isText
import com.kafka.data.model.item.ItemDetailResponse
import java.net.URL
import javax.inject.Inject
import com.kafka.data.model.item.File as FileResponse

class ItemDetailMapper @Inject constructor(private val fileDao: FileDao) {
    suspend fun map(from: ItemDetailResponse): ItemDetail {
        return ItemDetail(
            itemId = from.metadata.identifier,
            language = from.metadata.licenseurl,
            title = from.metadata.title?.dismissUpperCase(),
            description = (from.metadata.description?.joinToString()?.format() ?: ""),
            creator = from.metadata.creator?.joinToString(),
            collection = from.metadata.collection?.joinToString(),
            mediaType = from.metadata.mediatype,
            files = from.files.filter { it.name.isNotEmpty() }.map { it.name },
            coverImageResource = 0,
            coverImage = "https://archive.org/services/img/${from.metadata.identifier}",
            metadata = from.metadata.collection
        ).also {
            insertFiles(from, it)
        }
    }

    private suspend fun insertFiles(from: ItemDetailResponse, item: ItemDetail) {
        val files = from.files.map {
            it.asFile(
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

fun FileResponse.asFile(
    itemId: String,
    itemTitle: String?,
    coverImage: String?,
    prefix: String,
    localUri: String?
) = File(
    fileId = name,
    itemId = itemId,
    itemTitle = itemTitle,
    size = size?.toIntOrNull()?.run { this / 1000_000 }.toString() + " MB",
    title = title ?: "---",
    extension = name.split(".").last(),
    creator = creator,
    time = (mtime ?: "0").toLong(),
    format = format,
    playbackUrl = if (format.isMp3()) URL("$prefix/$name").toString() else null,
    readerUrl = if (format.isPdf() || format.isText()) URL("$prefix/$name").toString() else null,
    coverImage = coverImage,
    localUri = localUri
)
