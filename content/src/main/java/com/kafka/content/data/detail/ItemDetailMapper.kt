package com.kafka.content.data.detail

import android.text.Html
import com.data.base.mapper.Mapper
import com.data.base.model.item.ItemDetailResponse
import com.kafka.data.entities.File
import com.kafka.data.entities.ItemDetail
import com.kafka.data.entities.isMp3
import com.kafka.data.entities.isPdf
import java.net.URL
import javax.inject.Inject

class ItemDetailMapper @Inject constructor() : Mapper<ItemDetailResponse, ItemDetail> {
    override fun map(from: ItemDetailResponse): ItemDetail {
        val metadata = from.metadata
        val files = from.files.map { it.asFile(from.dirPrefix()) }
        return ItemDetail(
            itemId = metadata.identifier,
            language = metadata.licenseurl,
            title = metadata.title,
            description = "✪✪✪✪✪  " + (metadata.description?.joinToString()?.format() ?: ""),
            creator = metadata.creator?.joinToString(),
            collection = metadata.collection?.joinToString(),
            mediaType = metadata.mediatype,
            files = files.filter { it.title != null },
            coverImageResource = 0,
            coverImage = "https://archive.org/services/img/${metadata.identifier}",
            metadata = metadata.collection
        )
    }
}


fun String?.format() = Html.fromHtml(this)?.toString()

fun ItemDetailResponse.dirPrefix() = "https://$server$dir"

fun com.data.base.model.item.File.asFile(prefix: String) = File(
    title = name,
    creator = creator,
    time = (mtime ?: "0").toLong(),
    format = format,
    playbackUrl = if (format.isMp3()) URL("$prefix/$name").toString() else null,
    readerUrl = if (format.isPdf()) URL("$prefix/$name").toString() else null
)
