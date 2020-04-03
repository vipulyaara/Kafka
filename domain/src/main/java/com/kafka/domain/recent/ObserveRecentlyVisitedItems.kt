package com.kafka.domain.recent

import com.kafka.data.item.ItemRepository
import com.kafka.data.entities.Item
import com.kafka.data.query.ArchiveQuery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Interactor for updating the homepage.
 * */
class ObserveRecentlyVisitedItems @Inject constructor(
    dispatchers: com.data.base.AppCoroutineDispatchers,
    private val itemRepository: ItemRepository
) : com.kafka.domain.SubjectInteractor<ObserveRecentlyVisitedItems.Params, List<Item>>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override fun createObservable(params: Params): Flow<List<Item>> {
        return itemRepository.observeRecentlyVisitedItems()
    }

    data class Params(val archiveQuery: ArchiveQuery)
}
