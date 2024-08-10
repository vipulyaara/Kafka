package org.kafka.domain.interactors

import com.kafka.data.entities.Item
import com.kafka.data.feature.item.ItemRepository
import com.kafka.data.model.MediaType
import com.kafka.data.model.SearchFilter
import kotlinx.coroutines.withContext
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.domain.ResultInteractor
import org.kafka.domain.interactors.query.BuildSearchQuery
import javax.inject.Inject

class SearchQueryItems @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val buildSearchQuery: BuildSearchQuery,
    private val itemRepository: ItemRepository,
) : ResultInteractor<SearchQueryItems.Params, List<Item>>() {

    override suspend fun doWork(params: Params): List<Item> = withContext(dispatchers.io) {
        if (params.keyword.isEmpty()) return@withContext listOf()

        val query = buildSearchQuery(params.keyword, params.searchFilter, params.mediaTypes)
        itemRepository.updateQuery(query).also {
            itemRepository.saveItems(it)
        }
    }

    data class Params(
        val keyword: String,
        val searchFilter: List<SearchFilter>,
        val mediaTypes: List<MediaType>,
    )
}
