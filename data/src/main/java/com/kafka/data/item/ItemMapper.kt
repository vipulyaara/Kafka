package com.kafka.data.item

import com.data.base.mapper.Mapper
import com.kafka.data.entities.Item
import com.kafka.data.entities.toItem
import com.kafka.data.model.item.SearchResponse
import javax.inject.Inject

class ItemMapper @Inject constructor(): Mapper<SearchResponse, List<Item>> {
    override fun map(from: SearchResponse): List<Item> {
        return from.response.docs.map { it.toItem() }
    }
}
