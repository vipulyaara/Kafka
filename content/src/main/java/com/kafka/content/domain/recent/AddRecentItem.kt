package com.kafka.content.domain.recent

import com.data.base.Interactor
import com.kafka.content.data.item.ItemRepository
import com.kafka.data.injection.ProcessLifetime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Inject

class AddRecentItem @Inject constructor(
    dispatchers: com.data.base.AppCoroutineDispatchers,
    @ProcessLifetime processScope: CoroutineScope,
    private val itemRepository: ItemRepository
) : Interactor<AddRecentItem.Params>() {
    override val scope: CoroutineScope = processScope + dispatchers.io

    override suspend fun doWork(params: Params) {
        itemRepository.addRecentlyVisitedItem(params.itemId)
    }

    data class Params(val itemId: String)
}
