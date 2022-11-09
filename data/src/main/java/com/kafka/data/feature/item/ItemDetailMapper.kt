package com.kafka.data.feature.item

import android.text.Html
import com.kafka.data.dao.ItemDetailDao
import com.kafka.data.entities.ItemDetail
import com.kafka.data.model.item.ItemDetailResponse
import org.kafka.base.debug
import javax.inject.Inject

private val supportedFiles = listOf("pdf", "mp3", "epub", "wav", "txt")

class ItemDetailMapper @Inject constructor(private val itemDetailDao: ItemDetailDao) {
    suspend fun map(from: ItemDetailResponse): ItemDetail {
        val metadata = from.metadata
        debug { "${from.files}" }

//        val files = from.files.map { file ->
//            val localUri = itemDetailDao.itemDetailOrNull(metadata.identifier)?.files
//                ?.firstOrNull { it.id == metadata.identifier }?.localUri
//            file.asFile(from.dirPrefix(), localUri)
//        }.filter { supportedFiles.contains(it.extension) }

        return ItemDetail(
            itemId = metadata.identifier,
            language = metadata.licenseurl,
            title = metadata.title?.dismissUpperCase(),
            description = (metadata.description?.joinToString()?.format() ?: ""),
            creator = metadata.creator?.joinToString(),
            collection = metadata.collection?.joinToString(),
            mediaType = metadata.mediatype,
            files = from.files.map { it.name.orEmpty() },
            coverImageResource = 0,
            coverImage = "https://archive.org/services/img/${metadata.identifier}",
            metadata = metadata.collection
        )
    }
}

fun String?.format() = Html.fromHtml(this)?.toString()

fun ItemDetailResponse.dirPrefix() = "https://$server$dir"

//fun com.kafka.data.model.item.File.asFile(prefix: String, localUri: String?) = File(
//    fileId = name.orEmpty(),
//    size = size?.toIntOrNull()?.run { this / 1000_000 }.toString() + " MB",
//    title = title ?: "---",
//    extension = name?.split(".")?.last().orEmpty(),
//    creator = creator,
//    time = (mtime ?: "0").toLong(),
//    format = format,
//    playbackUrl = if (format.isMp3()) URL("$prefix/$name").toString() else null,
//    readerUrl = if (format.isPdf() || format.isText()) URL("$prefix/$name").toString() else null,
//    localUri = localUri
//)

//[{"id":"ALFAKHAR.pdf","size":"3 MB","title":"ALFAKHAR","extension":"pdf","creator":null,"time":1229825349,"format":"Image Container PDF","playbackUrl":null,"readerUrl":"https://ia802807.us.archive.org/24/items/ALFAKHAR/ALFAKHAR.pdf"},
//{"id":"ALFAKHAR_djvu.txt","size":"0 MB","title":"---","extension":"txt","creator":null,"time":1251205988,"format":"DjVuTXT","playbackUrl":null,"readerUrl":"https://ia802807.us.archive.org/24/items/ALFAKHAR/ALFAKHAR_djvu.txt"},
//{"id":"ALFAKHAR_text.pdf","size":"6 MB","title":"---","extension":"pdf","creator":null,"time":1251205969,"format":"Additional Text PDF","playbackUrl":null,"readerUrl":"https://ia802807.us.archive.org/24/items/ALFAKHAR/ALFAKHAR_text.pdf"}]
