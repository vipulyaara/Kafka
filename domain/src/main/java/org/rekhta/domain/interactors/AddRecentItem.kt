package org.rekhta.domain.interactors

import com.kafka.data.feature.item.ItemRepository
import kotlinx.coroutines.withContext
import org.rekhta.base.AppCoroutineDispatchers
import org.rekhta.base.domain.Interactor
import javax.inject.Inject

class AddRecentItem @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val itemRepository: ItemRepository
) : Interactor<AddRecentItem.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            itemRepository.addRecentlyVisitedItem(params.itemId)
        }
    }

    data class Params(val itemId: String)
}
