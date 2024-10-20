package com.kafka.data.feature.item

import com.kafka.data.entities.Item
import com.kafka.data.model.MediaType
import com.kafka.data.model.item.Doc
import com.kafka.data.model.item.SearchResponse
import javax.inject.Inject

class ItemMapper @Inject constructor() {
    fun map(from: SearchResponse): List<Item> {
        return from.response?.docs
            ?.map { doc -> doc.toItem() }
            .orEmpty()
    }

    private fun Doc.toItem() = Item(
        itemId = this.identifier,
        languages = this.language,
        title = this.title.joinToString().dismissUpperCase(),
        description = this.description?.joinToString()?.trim().orEmpty(),
        creators = this.creator.orEmpty(),
        mediaType = MediaType.from(this.mediatype),
        coverImage = "https://archive.org/services/img/$identifier",
        collections = this.collection.orEmpty(),
        subjects = subject.orEmpty(),
    )
}

fun String.dismissUpperCase() = if (this.isUpperCase()) {
    this.lowercase().replaceFirstChar { it.uppercase() }
} else {
    this
}

// Room query breaks when there is a ' in the string todo: improve this
fun String.sanitizeForRoom() = this.replace("'", "")

fun String.isUpperCase() = toCharArray().all { it.isUpperCase() }
