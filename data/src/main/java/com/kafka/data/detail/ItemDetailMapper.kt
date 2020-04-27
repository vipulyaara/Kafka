package com.kafka.data.detail

import com.data.base.mapper.Mapper
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
            files = from.files
//    coverImage = "https:/" + this.server + this.dir + "/" + this.files.firstOrNull {
//        it.format == "JPEG" || it.format.contains(
//            "Tile"
//        )
//    }?.name
        )
    }

}
