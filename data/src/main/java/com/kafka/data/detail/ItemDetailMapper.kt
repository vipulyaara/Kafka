package com.kafka.data.detail

import com.data.base.mapper.Mapper
import com.kafka.data.entities.*
import com.kafka.data.model.item.ItemDetailResponse
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
            description = "✪✪✪✪✪  " + (metadata.description?.joinToString() ?: ""),
            creator = metadata.creator?.joinToString(),
            mediaType = metadata.mediatype,
            files = files.filter { it.title != null },
            coverImage = "https://archive.org/services/img/${metadata.identifier}"
        )
    }
}

fun ItemDetailResponse.dirPrefix() = "https://$server$dir"

fun com.kafka.data.model.item.File.asFile(prefix: String) = File(
    title = name,
    creator = creator,
    time = (mtime ?: "0").toLong(),
    format = format,
    playbackUrl = if (format.isMp3()) URL("$prefix/$name").toString() else null,
    readerUrl = if (format.isPdf()) URL("$prefix/$name").toString() else null
)
