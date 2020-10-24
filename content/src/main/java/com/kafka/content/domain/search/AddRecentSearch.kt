package com.kafka.content.domain.search

import com.kafka.data.model.AppCoroutineDispatchers
import com.kafka.data.model.Interactor
import com.kafka.content.data.item.ItemRepository
import com.kafka.data.injection.ProcessLifetime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Inject

class AddRecentSearch @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    @ProcessLifetime processScope: CoroutineScope,
    private val itemRepository: ItemRepository
) : Interactor<String>() {
    override val scope: CoroutineScope = processScope + dispatchers.io

    override suspend fun doWork(params: String) {
        itemRepository.addRecentSearch(params)
    }

}
