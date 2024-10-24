package com.kafka.domain.interactors

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.entities.Item
import com.kafka.data.feature.Supabase
import com.kafka.data.feature.item.ItemRepository
import com.kafka.data.model.MediaType
import io.github.jan.supabase.postgrest.query.filter.TextSearchType
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchQueryItems @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val itemRepository: ItemRepository,
    private val supabase: Supabase
) : Interactor<SearchQueryItems.Params, List<Item>>() {

    override suspend fun doWork(params: Params): List<Item> = withContext(dispatchers.io) {
        if (params.keyword.isEmpty()) return@withContext listOf()

        val query = params.keyword.split(" ")
            .joinToString(separator = "&", prefix = "'", postfix = "'")

        supabase.items.select {
            filter {
                textSearch("fts", query, TextSearchType.NONE)
                if (params.mediaType != null) {
                    Item::mediaType eq params.mediaType.value
                }
            }
        }.decodeList<Item>()
            .also { itemRepository.saveItems(it) }
    }

    data class Params(
        val keyword: String,
        val mediaTypes: List<MediaType>,
    ) {
        val mediaType = if (mediaTypes.size == 1) mediaTypes.first() else null
    }
}
