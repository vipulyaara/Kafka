package com.kafka.domain.interactors

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.Service
import com.kafka.base.appService
import com.kafka.base.domain.Interactor
import com.kafka.data.entities.Item
import com.kafka.data.feature.SupabaseDb
import com.kafka.data.feature.item.ItemRepository
import com.kafka.data.model.MediaType
import com.kafka.data.model.SearchFilter
import com.kafka.domain.interactors.query.BuildSearchQuery
import io.github.jan.supabase.postgrest.query.filter.TextSearchType
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchQueryItems @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val buildSearchQuery: BuildSearchQuery,
    private val itemRepository: ItemRepository,
    private val supabaseDb: SupabaseDb
) : Interactor<SearchQueryItems.Params, List<Item>>() {

    override suspend fun doWork(params: Params): List<Item> = withContext(dispatchers.io) {
        if (params.keyword.isEmpty()) return@withContext listOf()

        if (appService == Service.Archive) {
            val query = buildSearchQuery(params.keyword, params.searchFilter, params.mediaTypes)
            itemRepository.updateQuery(query).also {
                itemRepository.saveItems(it)
            }
        } else {
            val query = params.keyword.split(" ")
                .joinToString(separator = "&", prefix = "'", postfix = "'")

            supabaseDb.books.select {
                filter {
                    textSearch("fts", query, TextSearchType.NONE)
                    if (params.mediaTypes.size == 1 && params.mediaTypes[0] == MediaType.Text) {
                        eq("media_type", "texts")
                    }
                    if (params.mediaTypes.size == 1 && params.mediaTypes[0] == MediaType.Audio) {
                        eq("media_type", "audio")
                    }
                }
            }.decodeList<Item>()
                .also {
                    itemRepository.saveItems(it)
                }
        }
    }

    data class Params(
        val keyword: String,
        val searchFilter: List<SearchFilter>,
        val mediaTypes: List<MediaType>,
    )
}
