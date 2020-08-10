package com.kafka.content.data.item

import com.data.base.mapper.Mapper
import com.data.base.model.item.Doc
import com.data.base.model.item.SearchResponse
import com.kafka.data.entities.Creator
import com.kafka.data.entities.Item
import javax.inject.Inject

class ItemMapper @Inject constructor(): Mapper<SearchResponse, List<Item>> {
    override fun map(from: SearchResponse): List<Item> {
        return from.response.docs.mapIndexed { index, doc -> doc.toItem(index) }
    }

    private fun Doc.toItem(index: Int) = Item(
        itemId = this.identifier,
        language = this.language,
        title = this.title,
        description = this.description?.get(0)?.trim(),
        creator = this.creator?.get(0)?.let { Creator(it, it) },
        mediaType = this.mediatype,
        coverImage = "https://archive.org/services/img/$identifier",
        coverImageResource = 0,
        collection = this.collection,
        genre = this.subject,
        position = this.downloads
    )
}
