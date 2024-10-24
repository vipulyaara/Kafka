package com.kafka.domain.interactors

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.entities.Item
import com.kafka.data.feature.Supabase
import com.kafka.data.feature.item.ItemRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateCreatorItems @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val itemRepository: ItemRepository,
    private val supabase: Supabase
) : Interactor<UpdateCreatorItems.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val items = supabase.items.select {
                filter { Item::creators contains listOf(params.creator) }
            }.decodeList<Item>()

            itemRepository.saveItems(items)
        }
    }

    data class Params(val creator: String)
}
