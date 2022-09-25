package com.kafka.data.feature.item

import android.text.Html
import com.kafka.data.entities.File
import com.kafka.data.entities.ItemDetail
import com.kafka.data.entities.isMp3
import com.kafka.data.entities.isPdf
import com.kafka.data.model.item.ItemDetailResponse
import org.kafka.base.debug
import java.net.URL
import javax.inject.Inject

private val supportedFiles = listOf("pdf", "mp3", "epub", "wav", "txt")

class ItemDetailMapper @Inject constructor() {
    fun map(from: ItemDetailResponse): ItemDetail {
        val metadata = from.metadata
        debug { "${from.files}" }
        val files = from.files.map { it.asFile(from.dirPrefix()) }
            .filter { supportedFiles.contains(it.extension) }
        return ItemDetail(
            itemId = metadata.identifier,
            language = metadata.licenseurl,
            title = metadata.title?.dismissUpperCase(),
            description = (metadata.description?.joinToString()?.format() ?: ""),
            creator = metadata.creator?.joinToString(),
            collection = metadata.collection?.joinToString(),
            mediaType = metadata.mediatype,
            files = files,
            coverImageResource = 0,
            coverImage = "https://archive.org/services/img/${metadata.identifier}",
            metadata = metadata.collection
        )
    }
}

fun String?.format() = Html.fromHtml(this)?.toString()

fun ItemDetailResponse.dirPrefix() = "https://$server$dir"

fun com.kafka.data.model.item.File.asFile(prefix: String) = File(
    id = name.orEmpty(),
    size = size?.toIntOrNull()?.run { this/1000_000 }.toString() + " MB",
    title = title ?: "---",
    extension = name?.split(".")?.last().orEmpty(),
    creator = creator,
    time = (mtime ?: "0").toLong(),
    format = format,
    playbackUrl = if (format.isMp3()) URL("$prefix/$name").toString() else null,
    readerUrl = if (format.isPdf()) URL("$prefix/$name").toString() else null
)
