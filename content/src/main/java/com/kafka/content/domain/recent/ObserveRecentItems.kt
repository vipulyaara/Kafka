package com.kafka.content.domain.recent

import com.kafka.data.model.SubjectInteractor
import com.kafka.content.data.item.ItemRepository
import com.kafka.data.entities.ItemWithRecentItem
import com.kafka.data.model.AppCoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Interactor for updating the homepage.
 * */
class ObserveRecentItems @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val itemRepository: ItemRepository
) : SubjectInteractor<Unit, List<ItemWithRecentItem>>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override fun createObservable(params: Unit): Flow<List<ItemWithRecentItem>> {
        return itemRepository.observeItemsWithRecentlyVisitedInfo()
    }
}
