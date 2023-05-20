package org.kafka.domain.interactors

import com.kafka.data.feature.item.ItemRepository
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.SearchFilter
import com.kafka.data.model.booksByAuthor
import com.kafka.data.model.booksBySubject
import com.kafka.data.model.booksByTitleKeyword
import com.kafka.data.model.joinerOr
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import org.kafka.domain.interactors.query.BuildRemoteQuery
import javax.inject.Inject

class SearchQueryItems @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val buildRemoteQuery: BuildRemoteQuery,
    private val itemRepository: ItemRepository,
) : Interactor<SearchQueryItems.Params>() {

    override suspend fun doWork(params: Params): Unit = withContext(dispatchers.io) {
        val archiveQuery = buildQuery(params.keyword, params.searchFilter)
        itemRepository.updateQuery(buildRemoteQuery(archiveQuery)).let {
            itemRepository.saveItems(it)
        }
    }

    private fun buildQuery(
        keyword: String,
        searchFilters: List<SearchFilter> = SearchFilter.values().toList()
    ): ArchiveQuery {
        val query = ArchiveQuery()
        searchFilters.forEach {
            val joiner = if (it == searchFilters.last()) "" else joinerOr
            when (it) {
                SearchFilter.Creator -> query.booksByAuthor(keyword, joiner)
                SearchFilter.Name -> query.booksByTitleKeyword(keyword, joiner)
                SearchFilter.Subject -> query.booksBySubject(keyword, joiner)
            }
        }

        return query
    }

    data class Params(val keyword: String, val searchFilter: List<SearchFilter>)
}
