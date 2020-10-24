package com.kafka.content.data.item

import com.kafka.data.entities.Creator
import com.kafka.data.entities.Item
import com.kafka.data.model.mapper.Mapper
import com.kafka.data.model.model.item.Doc
import com.kafka.data.model.model.item.SearchResponse
import com.kafka.ui_common.extensions.randomCoverResource
import java.util.*
import javax.inject.Inject

class ItemMapper @Inject constructor() : Mapper<SearchResponse, List<Item>> {
    override fun map(from: SearchResponse): List<Item> {
        return from.response.docs.mapIndexed { index, doc -> doc.toItem(index) }
    }

    private fun Doc.toItem(index: Int) = Item(
        itemId = this.identifier,
        language = this.language,
        title = this.title.joinToString().dismissUpperCase(),
        description = this.description?.get(0)?.trim(),
        creator = this.creator?.get(0)?.let { Creator(it, it) },
        mediaType = this.mediatype,
        coverImage = "https://archive.org/services/img/$identifier",
        coverImageResource = randomCoverResource,
        collection = this.collection,
        genre = this.subject,
        position = this.downloads
    )
}

fun String.dismissUpperCase() = if (this.isUpperCase()) {
    this.toLowerCase(Locale.ROOT).capitalize(Locale.ROOT)
} else {
    this
}


fun String.isUpperCase() = toCharArray().all { it.isUpperCase() }
