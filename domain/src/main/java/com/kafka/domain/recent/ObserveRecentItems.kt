package com.kafka.domain.recent

import com.data.base.SubjectInteractor
import com.kafka.data.item.ItemRepository
import com.kafka.data.entities.Item
import com.kafka.data.entities.RecentItem
import com.kafka.data.query.ArchiveQuery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Interactor for updating the homepage.
 * */
class ObserveRecentItems @Inject constructor(
    dispatchers: com.data.base.AppCoroutineDispatchers,
    private val itemRepository: ItemRepository
) : SubjectInteractor<Unit, List<RecentItem>>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override fun createObservable(params: Unit): Flow<List<RecentItem>> {
        return itemRepository.observeRecentlyVisitedItems()
    }
}
