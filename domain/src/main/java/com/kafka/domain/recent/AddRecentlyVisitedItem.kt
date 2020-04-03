package com.kafka.domain.recent

import com.kafka.data.di.ProcessLifetime
import com.kafka.data.item.ItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Inject

class AddRecentlyVisitedItem @Inject constructor(
    dispatchers: com.data.base.AppCoroutineDispatchers,
    @ProcessLifetime processScope: CoroutineScope,
    private val itemRepository: ItemRepository
) : com.kafka.domain.Interactor<AddRecentlyVisitedItem.Params>() {
    override val scope: CoroutineScope = processScope + dispatchers.io

    override suspend fun doWork(params: Params) {
        itemRepository.addRecentlyVisitedItem(params.itemId)
    }

    data class Params(val itemId: String)
}
