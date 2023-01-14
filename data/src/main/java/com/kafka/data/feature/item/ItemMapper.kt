package com.kafka.data.feature.item

import com.kafka.data.entities.Creator
import com.kafka.data.entities.Item
import com.kafka.data.model.item.Doc
import com.kafka.data.model.item.SearchResponse
import javax.inject.Inject

class ItemMapper @Inject constructor() {
     fun map(from: SearchResponse): List<Item> {
        return from.response.docs.mapIndexed { index, doc -> doc.toItem(index) }
    }

    private fun Doc.toItem(index: Int) = Item(
        itemId = this.identifier,
        language = this.language,
        title = this.title.dismissUpperCase(),
        description = this.description?.get(0)?.trim(),
        creator = this.creator?.get(0)?.let { Creator(it, it) },
        mediaType = this.mediatype,
        coverImage = "https://archive.org/services/img/$identifier",
        coverImageResource = 0,
        collection = this.collection,
        genre = this.subject,
        position = this.downloads,
        uploader = this.uploader
    )
}

fun String.dismissUpperCase() = if (this.isUpperCase()) {
    this.lowercase().replaceFirstChar { it.uppercase() }
} else {
    this
}

fun String.isUpperCase() = toCharArray().all { it.isUpperCase() }
