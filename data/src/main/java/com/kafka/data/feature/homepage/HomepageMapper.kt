package com.kafka.data.feature.homepage

import com.kafka.data.entities.HomepageCollection
import com.kafka.data.feature.item.ItemRepository
import com.kafka.data.model.homepage.HomepageCollectionResponse
import javax.inject.Inject

class HomepageMapper @Inject constructor(private val itemRepository: ItemRepository) {
    suspend fun map(collection: List<HomepageCollectionResponse>) = collection.map {
        when (it) {
            is HomepageCollectionResponse.Row -> HomepageCollection.Row(
                label = it.label,
                items = it.items.map { it.split(", ") }.flatten()
                    .mapNotNull { itemRepository.getItem(it) }, // TODO: observe items
                labelClickable = it.labelClickable
            )

            is HomepageCollectionResponse.Column -> HomepageCollection.Column(
                label = it.label,
                items = it.items.map { it.split(", ") }.flatten()
                    .mapNotNull { itemRepository.getItem(it) },
                labelClickable = it.labelClickable
            )
        }
    }.filter { it.enabled }
}
