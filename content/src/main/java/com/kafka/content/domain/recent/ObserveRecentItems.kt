package com.kafka.content.domain.recent

import com.data.base.SubjectInteractor
import com.kafka.content.data.item.ItemRepository
import com.kafka.data.entities.ItemWithRecentItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Interactor for updating the homepage.
 * */
class ObserveRecentItems @Inject constructor(
    dispatchers: com.data.base.AppCoroutineDispatchers,
    private val itemRepository: ItemRepository
) : SubjectInteractor<Unit, List<ItemWithRecentItem>>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override fun createObservable(params: Unit): Flow<List<ItemWithRecentItem>> {
        return itemRepository.observeItemsWithRecentlyVisitedInfo()
    }
}
