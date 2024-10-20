package com.kafka.domain.interactors

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.Service
import com.kafka.base.appService
import com.kafka.base.domain.Interactor
import com.kafka.data.entities.Item
import com.kafka.data.feature.SupabaseDb
import com.kafka.data.feature.item.ItemRepository
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.booksByAuthor
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateCreatorItems @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val itemRepository: ItemRepository,
    private val updateItems: UpdateItems,
    private val supabaseDb: SupabaseDb
) : Interactor<UpdateCreatorItems.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (appService == Service.Archive) {
                val query = ArchiveQuery().booksByAuthor(params.creator)
                updateItems(UpdateItems.Params(query))
            } else {
                val items = supabaseDb.books.select {
                    filter { contains("creators", listOf(params.creator)) }
                }.decodeList<Item>()
                itemRepository.saveItems(items)
            }
        }
    }

    data class Params(val creator: String)
}
