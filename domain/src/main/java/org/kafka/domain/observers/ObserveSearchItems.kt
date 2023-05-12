package org.kafka.domain.observers

import com.kafka.data.entities.Item
import com.kafka.data.feature.item.ItemRepository
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.SearchFilter
import com.kafka.data.model.booksByAuthor
import com.kafka.data.model.booksBySubject
import com.kafka.data.model.booksByTitleKeyword
import com.kafka.data.model.joinerOr
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import org.kafka.domain.interactors.query.BuildLocalQuery
import javax.inject.Inject

class ObserveSearchItems @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val buildLocalQuery: BuildLocalQuery,
    private val itemRepository: ItemRepository
) : SubjectInteractor<ObserveSearchItems.Params, List<Item>>() {

    override fun createObservable(params: Params): Flow<List<Item>> {
        return itemRepository.observeQueryItems(
            buildLocalQuery(buildQuery(params.keyword, params.searchFilter))
        ).flowOn(dispatchers.io)
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
