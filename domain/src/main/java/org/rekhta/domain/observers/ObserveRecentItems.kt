package org.rekhta.domain.observers

import com.kafka.data.entities.ItemWithRecentItem
import com.kafka.data.feature.item.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.rekhta.base.AppCoroutineDispatchers
import org.rekhta.base.domain.SubjectInteractor
import javax.inject.Inject

/**
 * Interactor for updating the homepage.
 * */
class ObserveRecentItems @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val itemRepository: ItemRepository
) : SubjectInteractor<Unit, List<ItemWithRecentItem>>() {

    override suspend fun createObservable(params: Unit): Flow<List<ItemWithRecentItem>> {
        return itemRepository.observeItemsWithRecentlyVisitedInfo().flowOn(dispatchers.io)
    }
}
