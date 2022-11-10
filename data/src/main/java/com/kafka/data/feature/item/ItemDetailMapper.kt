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
import org.kafka.base.debug
import java.net.URL
import javax.inject.Inject
import com.kafka.data.model.item.File as FileResponse

class ItemDetailMapper @Inject constructor(private val fileDao: FileDao ) {
    suspend fun map(from: ItemDetailResponse): ItemDetail {
        val metadata = from.metadata
        debug { "${from.files}" }

        val files = from.files.map {
            it.asFile(metadata.identifier, from.dirPrefix(), fileDao.fileOrNull(it.name)?.localUri)
        }.filter { supportedFiles.contains(it.extension) }

        fileDao.insertAll(files)

        return ItemDetail(
            itemId = metadata.identifier,
            language = metadata.licenseurl,
            title = metadata.title?.dismissUpperCase(),
            description = (metadata.description?.joinToString()?.format() ?: ""),
            creator = metadata.creator?.joinToString(),
            collection = metadata.collection?.joinToString(),
            mediaType = metadata.mediatype,
            files = from.files.filter { it.name.isNotEmpty() }.map { it.name },
            coverImageResource = 0,
            coverImage = "https://archive.org/services/img/${metadata.identifier}",
            metadata = metadata.collection
        )
    }
}

fun String?.format() = Html.fromHtml(this)?.toString()

fun ItemDetailResponse.dirPrefix() = "https://$server$dir"

fun FileResponse.asFile(itemId: String, prefix: String, localUri: String?) = File(
    fileId = name,
    itemId = itemId,
    size = size?.toIntOrNull()?.run { this / 1000_000 }.toString() + " MB",
    title = title ?: "---",
    extension = name.split(".").last(),
    creator = creator,
    time = (mtime ?: "0").toLong(),
    format = format,
    playbackUrl = if (format.isMp3()) URL("$prefix/$name").toString() else null,
    readerUrl = if (format.isPdf() || format.isText()) URL("$prefix/$name").toString() else null,
    localUri = localUri
)

//[{"id":"ALFAKHAR.pdf","size":"3 MB","title":"ALFAKHAR","extension":"pdf","creator":null,"time":1229825349,"format":"Image Container PDF","playbackUrl":null,"readerUrl":"https://ia802807.us.archive.org/24/items/ALFAKHAR/ALFAKHAR.pdf"},
//{"id":"ALFAKHAR_djvu.txt","size":"0 MB","title":"---","extension":"txt","creator":null,"time":1251205988,"format":"DjVuTXT","playbackUrl":null,"readerUrl":"https://ia802807.us.archive.org/24/items/ALFAKHAR/ALFAKHAR_djvu.txt"},
//{"id":"ALFAKHAR_text.pdf","size":"6 MB","title":"---","extension":"pdf","creator":null,"time":1251205969,"format":"Additional Text PDF","playbackUrl":null,"readerUrl":"https://ia802807.us.archive.org/24/items/ALFAKHAR/ALFAKHAR_text.pdf"}]
