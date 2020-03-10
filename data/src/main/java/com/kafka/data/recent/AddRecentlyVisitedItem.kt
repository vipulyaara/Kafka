package com.kafka.data.recent

import com.kafka.data.content.ContentRepository
import com.kafka.data.data.config.ProcessLifetime
import com.kafka.data.data.interactor.Interactor
import com.kafka.data.util.AppCoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Inject

class AddRecentlyVisitedItem @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    @ProcessLifetime processScope: CoroutineScope,
    private val contentRepository: ContentRepository
) : Interactor<AddRecentlyVisitedItem.Params>() {
    override val scope: CoroutineScope = processScope + dispatchers.io

    override suspend fun doWork(params: Params) {
        contentRepository.addRecentlyVisitedItem(params.itemId)
    }

    data class Params(val itemId: String)
}
