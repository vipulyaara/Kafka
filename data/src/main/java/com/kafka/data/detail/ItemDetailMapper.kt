package com.kafka.data.detail

import com.data.base.mapper.Mapper
import com.kafka.data.entities.File
import com.kafka.data.entities.ItemDetail
import com.kafka.data.model.item.ItemDetailResponse
import javax.inject.Inject

class ItemDetailMapper @Inject constructor() : Mapper<ItemDetailResponse, ItemDetail> {
    override fun map(from: ItemDetailResponse): ItemDetail {
        return ItemDetail(
            itemId = from.metadata.identifier,
            language = from.metadata.licenseurl,
            title = from.metadata.title,
            description = "✪✪✪✪✪  " + from.metadata.description,
            creator = from.metadata.creator,
            mediaType = from.metadata.mediatype,
            files = from.files.filter { it.original != null }.map { it.asFile(from.buildPlaybackUrlPrefix()) }
//    coverImage = "https:/" + this.server + this.dir + "/" + this.files.firstOrNull {
//        it.format == "JPEG" || it.format.contains(
//            "Tile"
//        )
//    }?.name
        )
    }
}

fun ItemDetailResponse.buildPlaybackUrlPrefix() = "https://$server$dir"

fun com.kafka.data.model.item.File.asFile(prefix: String) = File(
    title = title,
    creator = creator,
    time = (mtime ?: "0").toLong(),
    format = format,
    playbackUrl = "$prefix/$original"
)
