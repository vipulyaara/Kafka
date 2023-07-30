package org.kafka.domain.observers

import com.kafka.data.entities.Item
import com.kafka.data.feature.item.ItemRepository
import com.kafka.data.model.SearchFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import org.kafka.domain.interactors.buildQuery
import org.kafka.domain.interactors.query.BuildLocalQuery
import javax.inject.Inject

class ObserveSearchItems @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val buildLocalQuery: BuildLocalQuery,
    private val itemRepository: ItemRepository,
) : SubjectInteractor<ObserveSearchItems.Params, List<Item>>() {

    override fun createObservable(params: Params): Flow<List<Item>> {
        return if (params.keyword.isEmpty()) {
            flowOf(emptyList())
        } else {
            itemRepository.observeQueryItems(
                buildLocalQuery(buildQuery(params.keyword, params.searchFilter))
            ).flowOn(dispatchers.io)
        }
    }

    data class Params(val keyword: String, val searchFilter: List<SearchFilter>)
}
